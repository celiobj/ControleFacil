import { plainToInstance } from "class-transformer";
import { validate } from "class-validator";
import { CreateAcquisitionDto } from "./acquisition.dto";

describe("CreateAcquisitionDto", () => {
  it("accepts numeric price and ISO date after transformation", async () => {
    const dto = plainToInstance(CreateAcquisitionDto, {
      propertyId: "550e8400-e29b-41d4-a716-446655440000",
      purchasePrice: "125.50",
      purchaseDate: "2026-10-01T00:00:00.000Z",
    });

    expect((await validate(dto))).toHaveLength(0);
    expect(dto.purchasePrice).toBe(125.5);
    expect(dto.purchaseDate).toBeInstanceOf(Date);
  });

  it("rejects negative purchase prices", async () => {
    const dto = plainToInstance(CreateAcquisitionDto, {
      propertyId: "550e8400-e29b-41d4-a716-446655440000",
      purchasePrice: -1,
    });

    expect((await validate(dto)).some((error) => error.property === "purchasePrice")).toBe(true);
  });
});
