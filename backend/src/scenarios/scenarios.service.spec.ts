import { ScenariosService } from "./scenarios.service";

describe("ScenariosService", () => {
  it("deletes an existing scenario", async () => {
    const scenario = { id: "scenario-1" };
    const prisma = {
      saleScenario: {
        findUnique: jest.fn().mockResolvedValue(scenario),
        delete: jest.fn().mockResolvedValue(scenario),
      },
    } as any;
    const service = new ScenariosService(prisma, {} as any);

    await expect(service.remove("scenario-1")).resolves.toEqual(scenario);
    expect(prisma.saleScenario.delete).toHaveBeenCalledWith({
      where: { id: "scenario-1" },
    });
  });

  it("calculates scenario results without selecting a preferred scenario", async () => {
    const create = jest.fn(({ data }) => Promise.resolve(data));
    const prisma = {
      property: { findUnique: jest.fn().mockResolvedValue({ id: "property-1" }) },
      saleScenario: { create },
    } as any;
    const financial = {
      calculatePropertyFinancials: jest.fn().mockResolvedValue({
        totalInvested: 80_000,
        capitalInvested: 80_000,
      }),
    } as any;
    const service = new ScenariosService(prisma, financial);

    const scenario = await service.create("property-1", {
      name: "Base",
      salePrice: 100_000,
      brokeragePercent: 5,
      taxes: 500,
      otherSaleCosts: 1_000,
    });

    expect(scenario.projectedBrokerage.toString()).toBe("5000");
    expect(scenario.netProfit?.toString()).toBe("13500");
    expect(scenario.roi?.toString()).toBe("16.875");
    expect(scenario.status).toBeUndefined();
  });
});