import { PartialType } from "@nestjs/swagger";
import { SaleScenarioStatus } from "@prisma/client";
import { Transform } from "class-transformer";
import {
  IsEnum,
  IsNumber,
  IsOptional,
  IsString,
  MaxLength,
  Min,
} from "class-validator";

const toNumber = ({ value }: { value: unknown }) =>
  value === null || value === "" || value === undefined ? value : Number(value);

export class CreateScenarioDto {
  @IsString()
  @MaxLength(120)
  name!: string;

  @Transform(toNumber)
  @IsNumber({ maxDecimalPlaces: 2 })
  @Min(0)
  salePrice!: number;

  @IsOptional()
  @Transform(toNumber)
  @IsNumber({ maxDecimalPlaces: 4 })
  @Min(0)
  brokeragePercent?: number | null;

  @IsOptional()
  @Transform(toNumber)
  @IsNumber({ maxDecimalPlaces: 2 })
  @Min(0)
  brokerageAmount?: number | null;

  @IsOptional()
  @Transform(toNumber)
  @IsNumber({ maxDecimalPlaces: 2 })
  @Min(0)
  taxes?: number;

  @IsOptional()
  @Transform(toNumber)
  @IsNumber({ maxDecimalPlaces: 2 })
  @Min(0)
  otherSaleCosts?: number;

  @IsOptional()
  @IsEnum(SaleScenarioStatus)
  status?: SaleScenarioStatus;

  @IsOptional()
  @IsString()
  @MaxLength(2000)
  notes?: string | null;
}

export class UpdateScenarioDto extends PartialType(CreateScenarioDto) {}