import { Inject, Injectable, NotFoundException } from "@nestjs/common";
import { Prisma } from "@prisma/client";
import { FinancialService } from "../financial/financial.service";
import { PrismaService } from "../prisma.service";
import { CreateScenarioDto, UpdateScenarioDto } from "./dto/scenario.dto";

@Injectable()
export class ScenariosService {
  constructor(
    @Inject(PrismaService) private readonly prisma: PrismaService,
    @Inject(FinancialService) private readonly financial: FinancialService,
  ) {}

  async findForProperty(propertyId: string) {
    await this.ensureProperty(propertyId);
    return this.prisma.saleScenario.findMany({
      where: { propertyId },
      orderBy: { createdAt: "asc" },
    });
  }

  async create(propertyId: string, dto: CreateScenarioDto) {
    await this.ensureProperty(propertyId);
    const result = await this.financial.calculatePropertyFinancials(propertyId);
    const calculation = this.calculate(dto, result.totalInvested, result.capitalInvested);
    return this.prisma.saleScenario.create({
      data: {
        propertyId,
        name: dto.name,
        status: dto.status,
        projectedSalePrice: dto.salePrice,
        brokeragePercent: dto.brokeragePercent,
        projectedBrokerage: calculation.brokerageAmount,
        projectedTaxes: dto.taxes ?? 0,
        otherSaleCosts: dto.otherSaleCosts ?? 0,
        netProfit: calculation.netProfit,
        roi: calculation.roi,
        notes: dto.notes,
      },
    });
  }

  async update(id: string, dto: UpdateScenarioDto) {
    const current = await this.prisma.saleScenario.findUnique({
      where: { id },
    });
    if (!current) throw new NotFoundException("Cenário não encontrado");
    const financial = await this.financial.calculatePropertyFinancials(
      current.propertyId,
    );
    const values = {
      name: dto.name ?? current.name,
      salePrice: dto.salePrice ?? current.projectedSalePrice.toNumber(),
      brokeragePercent:
        dto.brokeragePercent ?? current.brokeragePercent?.toNumber() ?? null,
      brokerageAmount: dto.brokerageAmount ??
        (dto.brokeragePercent !== undefined
          ? undefined
          : current.projectedBrokerage.toNumber()),
      taxes: dto.taxes ?? current.projectedTaxes.toNumber(),
      otherSaleCosts: dto.otherSaleCosts ?? current.otherSaleCosts.toNumber(),
    };
    const calculation = this.calculate(
      values,
      financial.totalInvested,
      financial.capitalInvested,
    );
    return this.prisma.saleScenario.update({
      where: { id },
      data: {
        name: dto.name,
        status: dto.status,
        projectedSalePrice: values.salePrice,
        brokeragePercent: values.brokeragePercent,
        projectedBrokerage: calculation.brokerageAmount,
        projectedTaxes: values.taxes,
        otherSaleCosts: values.otherSaleCosts,
        netProfit: calculation.netProfit,
        roi: calculation.roi,
        notes: dto.notes,
      },
    });
  }

  async remove(id: string) {
    const scenario = await this.prisma.saleScenario.findUnique({
      where: { id },
      select: { id: true },
    });
    if (!scenario) throw new NotFoundException("Cenário não encontrado");
    return this.prisma.saleScenario.delete({ where: { id } });
  }

  private calculate(
    data: {
      salePrice: number;
      brokeragePercent?: number | null;
      brokerageAmount?: number | null;
      taxes?: number;
      otherSaleCosts?: number;
    },
    totalInvested: Prisma.Decimal.Value,
    capitalInvested: Prisma.Decimal.Value,
  ) {
    const salePrice = new Prisma.Decimal(data.salePrice);
    const brokerageAmount =
      data.brokerageAmount ??
      (data.brokeragePercent == null
        ? new Prisma.Decimal(0)
        : salePrice
            .mul(data.brokeragePercent)
            .div(100)
            .toDecimalPlaces(2));
    const netProfit = salePrice
      .minus(totalInvested)
      .minus(brokerageAmount)
      .minus(data.taxes ?? 0)
      .minus(data.otherSaleCosts ?? 0);
    const capital = new Prisma.Decimal(capitalInvested);
    const roi = capital.greaterThan(0)
      ? netProfit.div(capital).mul(100).toDecimalPlaces(4)
      : new Prisma.Decimal(0);
    return { brokerageAmount, netProfit, roi };
  }

  private async ensureProperty(propertyId: string) {
    const property = await this.prisma.property.findUnique({
      where: { id: propertyId },
      select: { id: true },
    });
    if (!property) throw new NotFoundException("Imóvel não encontrado");
  }
}