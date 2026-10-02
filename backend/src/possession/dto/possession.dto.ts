import { PartialType } from "@nestjs/swagger";
import { OccupationStatus, PossessionStatus } from "@prisma/client";
import { Transform } from "class-transformer";
import { IsDate, IsEnum, IsOptional, IsString, MaxLength } from "class-validator";

const toDate = ({ value }: { value: unknown }) =>
  value === null || value === "" || value === undefined
    ? value
    : new Date(value as string);

export class CreatePossessionDto {
  @IsOptional()
  @IsEnum(PossessionStatus)
  status?: PossessionStatus;

  @IsOptional()
  @IsEnum(OccupationStatus)
  occupationStatus?: OccupationStatus | null;

  @IsOptional()
  @Transform(toDate)
  @IsDate()
  possessionDate?: Date | null;

  @IsOptional()
  @Transform(toDate)
  @IsDate()
  keyReceivedDate?: Date | null;

  @IsOptional()
  @Transform(toDate)
  @IsDate()
  inspectionDate?: Date | null;

  @IsOptional()
  @IsString()
  @MaxLength(2000)
  notes?: string | null;
}

export class UpdatePossessionDto extends PartialType(CreatePossessionDto) {}