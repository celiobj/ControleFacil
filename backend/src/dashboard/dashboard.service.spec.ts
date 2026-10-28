import { DashboardService } from "./dashboard.service";
import { FinancialService } from "../financial/financial.service";

describe("DashboardService", () => {
  it("calculates total cost, net profit and ROI from an operation", async () => {
    const prisma = {
      property: {
        count: jest.fn().mockResolvedValue(1),
        findMany: jest.fn().mockResolvedValue([
          {
            id: "p1",
            code: "CF-1",
            title: "Casa",
            status: "VENDIDO",
            createdAt: new Date("2025-01-15T12:00:00.000Z"),
            auction: { auctionValue: 100 },
            expenses: [
              { amount: 20, status: "EFETIVADA" },
              { amount: 15, status: "PENDENTE" },
            ],
            renovations: [{ actualAmount: 30, plannedAmount: 35 }],
            sale: {
              saleAmount: 200,
              brokerage: 10,
              taxes: 5,
              saleDate: new Date(),
            },
            saleScenarios: [
              {
                projectedSalePrice: 200,
                projectedBrokerage: 10,
                projectedTaxes: 5,
                otherSaleCosts: 0,
                netProfit: 999,
              },
            ],
          },
        ]),
      },
    } as any;
    const result = await new DashboardService(
      prisma,
      new FinancialService(prisma),
    ).summary();
    expect(result.totalInvested.toNumber()).toBe(150);
    expect(result.pendingExpenses.toNumber()).toBe(15);
    expect(result.accumulatedProfit.toNumber()).toBe(35);
    expect(result.averageRoi.toNumber()).toBeCloseTo(23.333, 2);
    expect(result.projectedProfit.toNumber()).toBe(20);
    expect(result.monitoredOperationsCount).toBe(0);
    expect(result.operationsByMonth).toEqual([]);
  });

  it("returns cumulative monthly counts for active operations only", async () => {
    const now = new Date();
    const monthDate = (offset: number) =>
      new Date(
        Date.UTC(now.getUTCFullYear(), now.getUTCMonth() + offset, 15),
      );
    const properties = [
      ...[-2, -1, 0].map((offset, index) => ({
        id: `active-${index}`,
        code: `CF-${index}`,
        title: "Imóvel ativo",
        status: "EM_ANALISE",
        createdAt: monthDate(offset),
        auction: { auctionValue: 100 },
        expenses: [],
        renovations: [],
        sale: null,
        saleScenarios: [],
      })),
      {
        id: "sold",
        code: "CF-SOLD",
        title: "Imóvel vendido",
        status: "VENDIDO",
        createdAt: monthDate(-1),
        auction: { auctionValue: 100 },
        expenses: [],
        renovations: [],
        sale: { saleAmount: 200, brokerage: 0, taxes: 0 },
        saleScenarios: [],
      },
      {
        id: "cancelled",
        code: "CF-CANCELLED",
        title: "Imóvel cancelado",
        status: "CANCELADO",
        createdAt: monthDate(0),
        auction: { auctionValue: 100 },
        expenses: [],
        renovations: [],
        sale: null,
        saleScenarios: [],
      },
    ];
    const prisma = {
      property: {
        count: jest.fn().mockResolvedValue(properties.length),
        findMany: jest.fn().mockResolvedValue(properties),
      },
    } as any;

    const result = await new DashboardService(
      prisma,
      new FinancialService(prisma),
    ).summary();
    const expectedMonths = [-2, -1, 0].map((offset) =>
      monthDate(offset).toISOString().slice(0, 7),
    );

    expect(result.monitoredOperationsCount).toBe(3);
    expect(result.operationsByMonth).toEqual(
      expectedMonths.map((month, index) => ({ month, count: index + 1 })),
    );
  });
});
