import { Transform, Type } from "class-transformer";
import { IsDate, IsEnum, IsNumber, IsOptional, IsString, IsUUID, MaxLength, Min } from "class-validator";
import { OmitType, PartialType } from "@nestjs/swagger";
import {
  AcquisitionPaymentMethod,
  AcquisitionSource,
  AcquisitionStatus,
} from "@prisma/client";

const toNumber = ({ value }: { value: unknown }) =>
  value === null || value === "" || value === undefined ? value : Number(value);
const toDate = ({ value }: { value: unknown }) =>
  value === null || value === "" || value === undefined ? value : new Date(value as string);

export class CreateAcquisitionDto {
  @IsUUID()
  propertyId!: string;

  @IsOptional()
  @Transform(toNumber)
  @IsNumber({ maxDecimalPlaces: 2 })
  @Min(0)
  purchasePrice?: number | null;

  @IsOptional()
  @Transform(toNumber)
  @IsNumber({ maxDecimalPlaces: 2 })
  @Min(0)
  auctioneerCommission?: number | null;

  @IsOptional()
  @Transform(toNumber)
  @IsNumber({ maxDecimalPlaces: 2 })
  @Min(0)
  ownResourcesAmount?: number | null;

  @IsOptional()
  @Transform(toNumber)
  @IsNumber({ maxDecimalPlaces: 2 })
  @Min(0)
  fgtsAmount?: number | null;

  @IsOptional()
  @Transform(toNumber)
  @IsNumber({ maxDecimalPlaces: 2 })
  @Min(0)
  financingAmount?: number | null;

  @IsOptional()
  @Transform(toNumber)
  @IsNumber({ maxDecimalPlaces: 2 })
  @Min(0)
  otherResourcesAmount?: number | null;

  @IsOptional()
  @Transform(toDate)
  @IsDate()
  purchaseDate?: Date | null;

  @IsOptional()
  @Transform(toDate)
  @IsDate()
  acquisitionDate?: Date | null;

  @IsOptional()
  @Transform(toDate)
  @IsDate()
  paymentDate?: Date | null;

  @IsOptional()
  @IsEnum(AcquisitionPaymentMethod)
  paymentMethod?: AcquisitionPaymentMethod | null;

  @IsOptional()
  @IsEnum(AcquisitionStatus)
  status?: AcquisitionStatus;

  @IsOptional()
  @IsEnum(AcquisitionSource)
  source?: AcquisitionSource;

  @IsOptional()
  @IsString()
  @MaxLength(2000)
  notes?: string | null;
}

export class UpdateAcquisitionDto extends PartialType(CreateAcquisitionDto) {}

export class CreatePropertyAcquisitionDto extends OmitType(
  CreateAcquisitionDto,
  ["propertyId"] as const,
) {}
