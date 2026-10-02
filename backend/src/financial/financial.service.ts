import { Inject, Injectable, NotFoundException } from "@nestjs/common";
import { Prisma } from "@prisma/client";
import { PrismaService } from "../prisma.service";

const money = (value: unknown) => new Prisma.Decimal(value == null ? 0 : String(value));

export interface FinancialResult {
  purchasePrice: Prisma.Decimal;
  purchaseCost: Prisma.Decimal;
  auctionCommission: Prisma.Decimal;
  acquisitionCosts: Prisma.Decimal;
  regularizationCosts: Prisma.Decimal;
  possessionCosts: Prisma.Decimal;
  renovationCosts: Prisma.Decimal;
  operationalCosts: Prisma.Decimal;
  financingCosts: Prisma.Decimal;
  totalInvested: Prisma.Decimal;
  capitalInvested: Prisma.Decimal;
  saleAmount: Prisma.Decimal;
  expenses: Prisma.Decimal;
  renovations: Prisma.Decimal;
  costTotal: Prisma.Decimal;
  sale: Prisma.Decimal;
  saleCosts: Prisma.Decimal;
  netProfit: Prisma.Decimal;
  roi: Prisma.Decimal;
}

@Injectable()
export class FinancialService {
  constructor(@Inject(PrismaService) private readonly prisma: PrismaService) {}

  private legacyCategory(category?: string) {
    switch (category) {
      case "REFORMA":
        return "RENOVATION";
      case "ITBI":
      case "CARTORIO":
      case "ADVOGADO":
      case "TAXAS":
        return "REGULARIZATION";
      case "FINANCIAMENTO":
        return "FINANCING";
      case "OUTROS":
        return "OTHER";
      default:
        return "OPERATIONAL";
    }
  }

  calculate(property: {
    acquisition?: {
      purchasePrice: unknown;
      auctioneerCommission?: unknown;
      financingAmount?: unknown;
    } | null;
    auction?: { auctionValue: unknown } | null;
    expenses: Array<{
      amount: unknown;
      category?: string;
      financialCategory?: string | null;
      renovationId?: string | null;
    }>;
    renovations: Array<{
      id?: string;
      actualAmount: unknown;
      plannedAmount: unknown;
    }>;
    sale?: { saleAmount: unknown; brokerage: unknown; taxes: unknown } | null;
  }): FinancialResult {
    const purchasePrice =
      property.acquisition?.purchasePrice != null
        ? money(property.acquisition.purchasePrice)
        : money(property.auction?.auctionValue);
    const auctionCommission = money(property.acquisition?.auctioneerCommission);
    const buckets: Record<string, Prisma.Decimal> = {
      ACQUISITION: money(0),
      REGULARIZATION: money(0),
      POSSESSION: money(0),
      RENOVATION: money(0),
      OPERATIONAL: money(0),
      FINANCING: money(0),
      SALE: money(0),
      OTHER: money(0),
    };
    const renovationExpenseIds = new Set<string>();
    let hasUnlinkedRenovationExpenses = false;
    for (const expense of property.expenses) {
      const category = expense.financialCategory ?? this.legacyCategory(expense.category);
      buckets[category] = (buckets[category] ?? money(0)).plus(
        money(expense.amount),
      );
      if (category === "RENOVATION") {
        if (expense.renovationId) renovationExpenseIds.add(expense.renovationId);
        else hasUnlinkedRenovationExpenses = true;
      }
    }
    // Linked renovation expenses replace the aggregate actual amount. For legacy
    // unlinked entries, prefer itemized expenses over renovation totals to avoid
    // counting the same payments twice.
    const renovationActualFallback = hasUnlinkedRenovationExpenses
      ? money(0)
      : property.renovations.reduce(
          (sum, item) =>
            item.id && renovationExpenseIds.has(item.id)
              ? sum
              : sum.plus(money(item.actualAmount)),
          money(0),
        );
    const renovationCosts = buckets.RENOVATION.plus(renovationActualFallback);
    const acquisitionCosts = buckets.ACQUISITION.plus(auctionCommission);
    const regularizationCosts = buckets.REGULARIZATION;
    const possessionCosts = buckets.POSSESSION;
    const operationalCosts = buckets.OPERATIONAL;
    const financingCosts = buckets.FINANCING;
    const expenses = property.expenses.reduce(
      (sum, item) => sum.plus(money(item.amount)),
      money(0),
    );
    const renovations = renovationCosts;
    const costTotal = [
      purchasePrice,
      acquisitionCosts,
      regularizationCosts,
      possessionCosts,
      renovationCosts,
      operationalCosts,
      financingCosts,
      buckets.OTHER,
    ].reduce((total, amount) => total.plus(amount), money(0));
    const sale = money(property.sale?.saleAmount);
    const saleCosts = money(property.sale?.brokerage)
      .plus(money(property.sale?.taxes))
      .plus(buckets.SALE);
    const netProfit = sale.minus(costTotal).minus(saleCosts);
    const capitalInvested = costTotal
      .minus(money(property.acquisition?.financingAmount))
      .greaterThan(0)
      ? costTotal.minus(money(property.acquisition?.financingAmount))
      : money(0);
    const roi = capitalInvested.greaterThan(0)
      ? netProfit.div(capitalInvested).mul(100).toDecimalPlaces(4)
      : money(0);

    return {
      purchasePrice,
      purchaseCost: purchasePrice,
      auctionCommission,
      acquisitionCosts,
      regularizationCosts,
      possessionCosts,
      renovationCosts,
      operationalCosts,
      financingCosts,
      totalInvested: costTotal,
      capitalInvested,
      saleAmount: sale,
      expenses,
      renovations,
      costTotal,
      sale,
      saleCosts,
      netProfit,
      roi,
    };
  }

  async forProperty(propertyId: string) {
    const property = await this.prisma.property.findUnique({
      where: { id: propertyId },
      include: {
        acquisition: true,
        auction: true,
        expenses: true,
        renovations: true,
        sale: true,
      },
    });
    if (!property) throw new NotFoundException("Imóvel não encontrado");
    return this.calculate(property);
  }

  calculatePropertyFinancials(propertyId: string) {
    return this.forProperty(propertyId);
  }
}
