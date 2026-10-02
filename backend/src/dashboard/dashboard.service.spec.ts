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
            auction: { auctionValue: 100 },
            expenses: [{ amount: 20 }],
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
    expect(result.accumulatedProfit.toNumber()).toBe(35);
    expect(result.averageRoi.toNumber()).toBeCloseTo(23.333, 2);
    expect(result.projectedProfit.toNumber()).toBe(35);
  });
});
