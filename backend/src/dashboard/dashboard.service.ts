import { Inject, Injectable } from "@nestjs/common";
import { PrismaService } from "../prisma.service";

const money = (value: any) => Number(value ?? 0);

@Injectable()
export class DashboardService {
  constructor(@Inject(PrismaService) private readonly prisma: PrismaService) {}
  async summary() {
    const properties = await this.prisma.property.findMany({
      where: { status: { not: "CANCELADO" } },
      include: { auction: true, expenses: true, renovations: true, sale: true },
    });
    const rows = properties.map((property) => {
      const acquisition = money(property.auction?.auctionValue);
      const expenses = property.expenses.reduce(
        (sum, item) => sum + money(item.amount),
        0,
      );
      const renovations = property.renovations.reduce(
        (sum, item) => sum + money(item.actualAmount ?? item.plannedAmount),
        0,
      );
      const costTotal = acquisition + expenses + renovations;
      const sale = money(property.sale?.saleAmount);
      const netProfit =
        sale -
        costTotal -
        money(property.sale?.brokerage) -
        money(property.sale?.taxes);
      return {
        propertyId: property.id,
        code: property.code,
        title: property.title,
        costTotal,
        sale,
        netProfit,
        roi: costTotal ? (netProfit / costTotal) * 100 : 0,
        saleDate: property.sale?.saleDate,
      };
    });
    const sold = rows.filter((row) => row.sale > 0);
    const totalInvested = rows.reduce((sum, row) => sum + row.costTotal, 0);
    const byStatus: Record<string, number> = {};
    for (const property of properties)
      byStatus[property.status] = (byStatus[property.status] ?? 0) + 1;
    return {
      totalInvested,
      totalSold: sold.reduce((sum, row) => sum + row.sale, 0),
      accumulatedProfit: rows.reduce((sum, row) => sum + row.netProfit, 0),
      averageRoi: sold.length
        ? sold.reduce((sum, row) => sum + row.roi, 0) / sold.length
        : 0,
      propertyCount: properties.length,
      byStatus,
      operations: rows,
    };
  }
}
