import { PropertiesService } from "./properties.service";

describe("PropertiesService", () => {
  it("parses the CAIXA listing text into property form fields", () => {
    const text = `##### RESIDENCIAL PRAIA DE JANGADA

Valor de avaliação: R$ 151.000,00
**Valor mínimo de venda: R$ 88.404,91** ( desconto de 41,45%)

Tipo de imóvel: **Apartamento**
Quartos: **2**
Número do imóvel: **855553502536-2**
Matrícula(s): **68663**
Comarca: **JABOATAO DOS GUARARAPES-PE**
Ofício: **01**
Inscrição imobiliária: **1309685103077406349**
Averbação dos leilões negativos: **Averbado**

Área total = **68,38m2**
Área privativa = **41,56m2**

**Compra Direta**

**Endereço:**
RUA DA PAZ,N. 244 APTO. 302 BL 40, BARRA DE JANGADA - CEP: 54460-312, JABOATAO DOS GUARARAPES - PERNAMBUCO

[Baixar matrícula do imóvel](https://venda-imoveis.caixa.gov.br/sistema/detalhe-imovel.asp?hdnimovel=8555535025362#)**Descrição:**
2 Quartos, Área de Serviço, Wc, Sala, Cozinha. .

FORMAS DE PAGAMENTO ACEITAS:
** Recursos próprios.
** Permite utilização de FGTS. Consulte condições e enquadramento.

REGRAS PARA PAGAMENTO DAS DESPESAS (caso existam):
** Condomínio: Sob responsabilidade do comprador, até o limite de 10% em relação ao valor de avaliação do imóvel.
** Tributos: Sob responsabilidade do comprador, quando o débito for inferior a 10% do valor de avaliação.`;
    const service = new PropertiesService({} as any, {} as any);

    const result = service.importFromCaixaText(text);

    expect(result).toEqual(
      expect.objectContaining({
        title: "RESIDENCIAL PRAIA DE JANGADA - APTO. 302 BL 40",
        type: "APARTAMENTO",
        negotiationType: "COMPRA_DIRETA",
        address: "RUA DA PAZ",
        number: "244",
        complement: "APTO. 302 BL 40",
        neighborhood: "BARRA DE JANGADA",
        city: "JABOATAO DOS GUARARAPES",
        state: "PE",
        zipCode: "54460-312",
        totalArea: "68.38",
        builtArea: "41.56",
        registryNumber: "68663",
        status: "EM_ANALISE",
      }),
    );
    expect(result.notes).toContain("Código CAIXA: 855553502536-2");
    expect(result.notes).toContain("R$ 88.404,91 (desconto de 41,45%)");
    expect(result.notes).toContain("Inscrição imobiliária: 1309685103077406349");
    expect(result.notes).toContain("REGRAS PARA PAGAMENTO DAS DESPESAS");
  });

  it("rejects incomplete CAIXA text without an address", () => {
    const service = new PropertiesService({} as any, {} as any);

    expect(() => service.importFromCaixaText("RESIDENCIAL PRAIA DE JANGADA")).toThrow(
      "Não foi possível identificar o título e o endereço.",
    );
  });

  it("generates a sequential code for a new property", async () => {
    const prisma = {
      property: {
        findMany: jest
          .fn()
          .mockResolvedValue([{ code: "CF-0002" }, { code: "CF-0001" }]),
        findUnique: jest.fn().mockResolvedValue(null),
        create: jest.fn().mockResolvedValue({
          id: "p-1",
          code: "CF-0003",
          title: "Casa nova",
          status: "EM_ANALISE",
        }),
      },
      propertyStatusHistory: {
        create: jest.fn().mockResolvedValue({}),
      },
    } as any;
    prisma.$transaction = jest.fn((callback: any) =>
      callback({
        property: prisma.property,
        propertyStatusHistory: prisma.propertyStatusHistory,
      }),
    );

    const checklists = {
      ensure: jest.fn().mockResolvedValue(undefined),
    } as any;

    const service = new PropertiesService(prisma, checklists);
    const result = await service.create({
      title: "Casa nova",
      type: "APARTAMENTO",
      negotiationType: "LEILAO_SFI",
      negotiationDeadline: new Date("2026-10-15T00:00:00.000Z"),
      address: "Rua das Flores",
      neighborhood: "Centro",
      city: "São Paulo",
      state: "SP",
    } as any);

    expect(result.code).toBe("CF-0003");
    expect(prisma.property.findMany).toHaveBeenCalled();
    expect(prisma.property.create).toHaveBeenCalledWith(
      expect.objectContaining({
        data: expect.objectContaining({
          code: "CF-0003",
          title: "Casa nova",
          negotiationType: "LEILAO_SFI",
          negotiationDeadline: new Date("2026-10-15T00:00:00.000Z"),
        }),
      }),
    );
    expect(prisma.propertyStatusHistory.create).toHaveBeenCalledWith({
      data: { propertyId: "p-1", toStatus: "EM_ANALISE" },
    });
  });

  it("records cancellation for properties under analysis after their deadline", async () => {
    const prisma = {
      property: {
        findMany: jest.fn().mockResolvedValue([{ id: "p-1" }]),
        updateMany: jest.fn().mockResolvedValue({ count: 1 }),
      },
      propertyStatusHistory: { create: jest.fn().mockResolvedValue({}) },
    } as any;
    prisma.$transaction = jest.fn((callback: any) =>
      callback({
        property: prisma.property,
        propertyStatusHistory: prisma.propertyStatusHistory,
      }),
    );
    const service = new PropertiesService(prisma, {} as any);

    await service.cancelExpiredNegotiations();

    expect(prisma.property.findMany).toHaveBeenCalledWith({
      where: {
        status: "EM_ANALISE",
        negotiationDeadline: { lt: expect.any(Date) },
      },
      select: { id: true },
    });
    expect(prisma.property.updateMany).toHaveBeenCalledWith({
      where: { id: "p-1", status: "EM_ANALISE" },
      data: { status: "CANCELADO" },
    });
    expect(prisma.propertyStatusHistory.create).toHaveBeenCalledWith({
      data: {
        propertyId: "p-1",
        fromStatus: "EM_ANALISE",
        toStatus: "CANCELADO",
      },
    });
  });

  it("normalizes the negotiation deadline before updating a property", async () => {
    const prisma = {
      property: {
        findUnique: jest.fn().mockResolvedValue({
          id: "p-1",
          status: "EM_ANALISE",
        }),
        update: jest.fn().mockResolvedValue({ id: "p-1" }),
      },
      propertyStatusHistory: { create: jest.fn().mockResolvedValue({}) },
    } as any;
    prisma.$transaction = jest.fn((callback: any) =>
      callback({
        property: prisma.property,
        propertyStatusHistory: prisma.propertyStatusHistory,
      }),
    );
    const service = new PropertiesService(prisma, {} as any);

    await service.update("p-1", {
      negotiationType: "COMPRA_DIRETA",
      negotiationDeadline: "2026-09-28" as any,
    });

    expect(prisma.property.update).toHaveBeenCalledWith(
      expect.objectContaining({
        data: expect.objectContaining({
          negotiationType: "COMPRA_DIRETA",
          negotiationDeadline: new Date("2026-09-28T00:00:00.000Z"),
        }),
      }),
    );
  });

  it("records a manual status transition", async () => {
    const prisma = {
      property: {
        findUnique: jest.fn().mockResolvedValue({
          id: "p-1",
          status: "EM_ANALISE",
        }),
        update: jest.fn().mockResolvedValue({ id: "p-1", status: "REFORMA" }),
      },
      propertyStatusHistory: { create: jest.fn().mockResolvedValue({}) },
    } as any;
    prisma.$transaction = jest.fn((callback: any) =>
      callback({
        property: prisma.property,
        propertyStatusHistory: prisma.propertyStatusHistory,
      }),
    );
    const service = new PropertiesService(prisma, {} as any);

    await service.update("p-1", { status: "REFORMA" as any });

    expect(prisma.propertyStatusHistory.create).toHaveBeenCalledWith({
      data: {
        propertyId: "p-1",
        fromStatus: "EM_ANALISE",
        toStatus: "REFORMA",
      },
    });
  });
});
