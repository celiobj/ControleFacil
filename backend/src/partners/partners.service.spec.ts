import { PartnersService } from "./partners.service";

describe("PartnersService", () => {
  it("converts blank optional fields to null when updating", async () => {
    const prisma = {
      partner: {
        findUnique: jest.fn().mockResolvedValue({ id: "partner-1" }),
        update: jest.fn().mockResolvedValue({ id: "partner-1" }),
      },
    } as any;
    const service = new PartnersService(prisma);

    await service.update("partner-1", {
      name: "Celio Prado",
      document: "",
      email: "celio@example.com",
      phone: "",
      company: "",
      notes: "",
    });

    expect(prisma.partner.update).toHaveBeenCalledWith({
      where: { id: "partner-1" },
      data: {
        name: "Celio Prado",
        document: null,
        email: "celio@example.com",
        phone: null,
        company: null,
        notes: null,
      },
    });
  });
});