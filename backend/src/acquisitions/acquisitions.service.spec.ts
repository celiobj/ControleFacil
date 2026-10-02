import {
  BadRequestException,
  ConflictException,
  NotFoundException,
} from "@nestjs/common";
import { AcquisitionStatus } from "@prisma/client";
import { AcquisitionsService } from "./acquisitions.service";

describe("AcquisitionsService", () => {
  it("translates duplicate properties to ConflictException", async () => {
    const prisma = {
      acquisition: {
        create: jest.fn().mockRejectedValue({ code: "P2002" }),
      },
    } as any;
    const service = new AcquisitionsService(prisma);

    await expect(
      service.create({ propertyId: "property-1" }),
    ).rejects.toBeInstanceOf(ConflictException);
  });

  it("returns 404 semantics when an acquisition does not exist", async () => {
    const prisma = {
      acquisition: {
        findUnique: jest.fn().mockResolvedValue(null),
      },
    } as any;
    const service = new AcquisitionsService(prisma);

    await expect(service.findOne("missing")).rejects.toBeInstanceOf(
      NotFoundException,
    );
  });

  it("preserves omitted fields during updates", async () => {
    const update = jest.fn().mockResolvedValue({ id: "a1" });
    const prisma = {
      acquisition: {
        update,
        findUnique: jest.fn().mockResolvedValue({
          id: "a1",
          status: AcquisitionStatus.PLANNED,
          purchasePrice: null,
          ownResourcesAmount: null,
          fgtsAmount: null,
          financingAmount: null,
          otherResourcesAmount: null,
        }),
      },
    } as any;
    const service = new AcquisitionsService(prisma);

    await service.update("a1", { notes: null });

    expect(update).toHaveBeenCalledWith(
      expect.objectContaining({
        where: { id: "a1" },
        data: { notes: null },
      }),
    );
  });

  it("allows partial funding while an acquisition is planned", async () => {
    const create = jest.fn().mockResolvedValue({ id: "a1" });
    const service = new AcquisitionsService({ acquisition: { create } } as any);

    await service.create({
      propertyId: "property-1",
      purchasePrice: 100,
      ownResourcesAmount: 30,
      status: AcquisitionStatus.PLANNED,
    });

    expect(create).toHaveBeenCalled();
  });

  it("rejects completed acquisitions with mismatched funding totals", async () => {
    const create = jest.fn();
    const service = new AcquisitionsService({ acquisition: { create } } as any);

    await expect(
      service.create({
        propertyId: "property-1",
        purchasePrice: 100,
        ownResourcesAmount: 40,
        fgtsAmount: 20,
        status: AcquisitionStatus.COMPLETED,
      }),
    ).rejects.toBeInstanceOf(BadRequestException);

    expect(create).not.toHaveBeenCalled();
  });
});
