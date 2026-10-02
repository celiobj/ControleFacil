import { FinancialService } from "./financial.service";

describe("FinancialService", () => {
  const service = new FinancialService({} as any);

  it("prefers acquisition purchase price over the V1 auction value", () => {
    const result = service.calculate({
      acquisition: { purchasePrice: "120" },
      auction: { auctionValue: "999" },
      expenses: [{ amount: "20" }],
      renovations: [{ actualAmount: "30", plannedAmount: "35" }],
      sale: { saleAmount: "250", brokerage: "10", taxes: "5" },
    });

    expect(result.purchaseCost.toNumber()).toBe(120);
    expect(result.costTotal.toNumber()).toBe(170);
    expect(result.saleCosts.toNumber()).toBe(15);
    expect(result.netProfit.toNumber()).toBe(65);
    expect(result.roi.toNumber()).toBeCloseTo(38.235, 2);
  });

  it("falls back to auction value for V1 properties without an acquisition", () => {
    const result = service.calculate({
      acquisition: null,
      auction: { auctionValue: 100 },
      expenses: [],
      renovations: [],
      sale: null,
    });

    expect(result.purchaseCost.toNumber()).toBe(100);
    expect(result.costTotal.toNumber()).toBe(100);
    expect(result.netProfit.toNumber()).toBe(-100);
    expect(result.roi.toNumber()).toBe(-100);
  });

  it("does not count a renovation expense and actual amount twice", () => {
    const result = service.calculate({
      acquisition: {
        purchasePrice: 100,
        auctioneerCommission: 5,
        financingAmount: 40,
      },
      expenses: [
        {
          amount: 25,
          category: "REFORMA",
          financialCategory: "RENOVATION",
          renovationId: "renovation-1",
        },
      ],
      renovations: [
        { id: "renovation-1", actualAmount: 25, plannedAmount: 30 },
      ],
      sale: { saleAmount: 200, brokerage: 10, taxes: 5 },
    });

    expect(result.renovationCosts.toNumber()).toBe(25);
    expect(result.totalInvested.toNumber()).toBe(130);
    expect(result.capitalInvested.toNumber()).toBe(90);
    expect(result.netProfit.toNumber()).toBe(55);
    expect(result.roi.toNumber()).toBeCloseTo(61.111, 2);
  });

  it("keeps planned renovation budgets out of invested costs", () => {
    const result = service.calculate({
      auction: { auctionValue: 100 },
      expenses: [],
      renovations: [{ actualAmount: null, plannedAmount: 50 }],
      sale: null,
    });

    expect(result.totalInvested.toNumber()).toBe(100);
  });

  it("preserves cents when summing decimal expense values", () => {
    const result = service.calculate({
      auction: { auctionValue: "0.10" },
      expenses: [{ amount: "0.20" }],
      renovations: [],
      sale: null,
    });

    expect(result.totalInvested.toFixed(2)).toBe("0.30");
  });
});
