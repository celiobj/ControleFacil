import { DashboardService } from "./dashboard.service";

describe("DashboardService", () => {
  it("calculates total cost, net profit and ROI from an operation", async () => {
    const prisma = {
      property: {
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
          },
        ]),
      },
    } as any;
    const result = await new DashboardService(prisma).summary();
    expect(result.totalInvested).toBe(150);
    expect(result.accumulatedProfit).toBe(35);
    expect(result.averageRoi).toBeCloseTo(23.333, 2);
  });
});
