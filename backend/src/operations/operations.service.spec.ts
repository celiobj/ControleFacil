import { OperationsService } from "./operations.service";

describe("OperationsService status history", () => {
  const createPrisma = () => {
    const transaction = {
      auction: { upsert: jest.fn().mockResolvedValue({ id: "auction-1" }) },
      sale: { upsert: jest.fn().mockResolvedValue({ id: "sale-1" }) },
      property: {
        findUnique: jest.fn().mockResolvedValue({ status: "EM_ANALISE" }),
        update: jest.fn().mockResolvedValue({}),
      },
      propertyStatusHistory: { create: jest.fn().mockResolvedValue({}) },
    };
    const prisma = {
      $transaction: jest.fn((callback: any) => callback(transaction)),
    } as any;
    return { prisma, transaction };
  };

  it("records the transition when an auction is created", async () => {
    const { prisma, transaction } = createPrisma();
    const service = new OperationsService(prisma);

    await service.createAuction({
      propertyId: "p-1",
      auctionDate: "2026-09-01",
    });

    expect(transaction.propertyStatusHistory.create).toHaveBeenCalledWith({
      data: {
        propertyId: "p-1",
        fromStatus: "EM_ANALISE",
        toStatus: "ARREMATADO",
      },
    });
  });

  it("records the transition when a sale is created", async () => {
    const { prisma, transaction } = createPrisma();
    const service = new OperationsService(prisma);

    await service.createSale({
      propertyId: "p-1",
      saleDate: "2026-09-01",
    });

    expect(transaction.propertyStatusHistory.create).toHaveBeenCalledWith({
      data: {
        propertyId: "p-1",
        fromStatus: "EM_ANALISE",
        toStatus: "VENDIDO",
      },
    });
  });
});