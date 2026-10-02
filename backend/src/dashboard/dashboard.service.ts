import { Inject, Injectable } from "@nestjs/common";
import { Prisma } from "@prisma/client";
import { FinancialService } from "../financial/financial.service";
import { PrismaService } from "../prisma.service";

const decimal = (value: unknown) =>
  new Prisma.Decimal(value == null ? 0 : String(value));

@Injectable()
export class DashboardService {
  constructor(
    @Inject(PrismaService) private readonly prisma: PrismaService,
    @Inject(FinancialService) private readonly financial: FinancialService,
  ) {}
  async summary() {
    const properties = await this.prisma.property.findMany({
      where: { status: { not: "CANCELADO" } },
      include: {
        auction: true,
        acquisition: true,
        expenses: true,
        renovations: true,
        sale: true,
        saleScenarios: true,
        regularization: { include: { tasks: true } },
        possession: true,
        documents: true,
        checklist: { include: { items: true } },
      },
    });
    const totalProperties = await this.prisma.property.count();
    const rows = properties.map((property) => {
      const finances = this.financial.calculate(property);
      return {
        propertyId: property.id,
        code: property.code,
        title: property.title,
        costTotal: finances.costTotal,
        capitalInvested: finances.capitalInvested,
        sale: finances.sale,
        netProfit: finances.netProfit,
        roi: finances.roi,
        saleDate: property.sale?.saleDate,
        scenarios: property.saleScenarios ?? [],
      };
    });
    const sold = rows.filter((row) => row.sale.greaterThan(0));
    const totalInvested = rows.reduce(
      (sum, row) => sum.plus(row.costTotal),
      decimal(0),
    );
    const capitalInvested = rows.reduce(
      (sum, row) => sum.plus(row.capitalInvested),
      decimal(0),
    );
    const byStatus: Record<string, number> = {};
    for (const property of properties)
      byStatus[property.status] = (byStatus[property.status] ?? 0) + 1;
    const scenarioAverages = rows.flatMap((row) => {
      if (!row.scenarios.length) return [];
      return [
        {
          saleAmount:
            row.scenarios.reduce(
              (sum, scenario) => sum.plus(decimal(scenario.projectedSalePrice)),
              decimal(0),
            ).div(row.scenarios.length),
          profit:
            row.scenarios.reduce(
              (sum, scenario) =>
                sum
                  .plus(decimal(scenario.projectedSalePrice))
                  .minus(row.costTotal)
                  .minus(decimal(scenario.projectedBrokerage))
                  .minus(decimal(scenario.projectedTaxes))
                  .minus(decimal(scenario.otherSaleCosts)),
              decimal(0),
            ).div(row.scenarios.length),
        },
      ];
    });
    const allTasks = properties.flatMap((property) => [
      ...(property.checklist?.items ?? []),
      ...(property.regularization?.tasks ?? []),
    ]);
    const now = Date.now();
    const dueSoonLimit = now + 7 * 24 * 60 * 60 * 1000;
    const openTasks = allTasks.filter(
      (task) =>
        !["COMPLETED", "CONCLUIDO", "CANCELLED", "NAO_APLICAVEL"].includes(
          task.status,
        ),
    );
    const overdueTasks = openTasks.filter(
      (task) => task.dueDate && new Date(task.dueDate).getTime() < now,
    ).length;
    const tasksDueSoon = openTasks.filter((task) => {
      if (!task.dueDate) return false;
      const dueAt = new Date(task.dueDate).getTime();
      return dueAt >= now && dueAt <= dueSoonLimit;
    }).length;
    const pendingDocuments = properties.reduce(
      (sum, property) =>
        sum + (property.documents ?? []).filter((document) => document.status === "PENDING").length,
      0,
    );
    const regularizationsInProgress = properties.filter((property) =>
      ["IN_PROGRESS", "EM_ANDAMENTO"].includes(
        property.regularization?.status ?? "",
      ),
    ).length;
    return {
      totalInvested,
      capitalInvested,
      totalSold: sold.reduce((sum, row) => sum.plus(row.sale), decimal(0)),
      accumulatedProfit: sold.reduce(
        (sum, row) => sum.plus(row.netProfit),
        decimal(0),
      ),
      realizedProfit: sold.reduce(
        (sum, row) => sum.plus(row.netProfit),
        decimal(0),
      ),
      projectedSaleAmount: scenarioAverages.reduce(
        (sum, scenario) => sum.plus(scenario.saleAmount),
        decimal(0),
      ),
      projectedProfit: scenarioAverages.reduce(
        (sum, scenario) => sum.plus(scenario.profit),
        decimal(0),
      ),
      averageRoi: sold.length
        ? sold
            .reduce((sum, row) => sum.plus(row.roi), decimal(0))
            .div(sold.length)
            .toDecimalPlaces(4)
        : decimal(0),
      propertyCount: properties.length,
      totalProperties,
      byStatus,
      overdueTasks,
      tasksDueSoon,
      pendingDocuments,
      regularizationsInProgress,
      operations: rows,
    };
  }
}
