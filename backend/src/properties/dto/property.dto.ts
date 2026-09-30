import { ApiPropertyOptional } from "@nestjs/swagger";
import { Transform } from "class-transformer";
import {
  IsEnum,
  IsDate,
  IsNumber,
  IsOptional,
  IsString,
  MaxLength,
} from "class-validator";
import {
  PropertyNegotiationType,
  PropertyStatus,
  PropertyType,
} from "@prisma/client";

const decimalInput = ({ value }: { value: unknown }) => {
  if (value === "" || value === null || value === undefined) return undefined;
  if (typeof value === "string") return Number(value.replace(",", "."));
  return value;
};

const dateInput = ({ value }: { value: unknown }) => {
  if (value === "" || value === null) return null;
  if (value === undefined) return undefined;
  if (typeof value === "string") {
    return new Date(`${value}T00:00:00.000Z`);
  }
  return value;
};

export class PropertyDto {
  @IsOptional() @IsString() @MaxLength(30) code?: string;
  @IsString() title!: string;
  @IsEnum(PropertyType) type!: PropertyType;
  @ApiPropertyOptional({ enum: PropertyNegotiationType })
  @IsOptional() @IsEnum(PropertyNegotiationType) negotiationType?: PropertyNegotiationType;
  @ApiPropertyOptional({ type: String, format: "date" })
  @IsOptional() @Transform(dateInput) @IsDate() negotiationDeadline?: Date;
  @IsString() address!: string;
  @IsOptional() @IsString() number?: string;
  @IsOptional() @IsString() complement?: string;
  @IsString() neighborhood!: string;
  @IsString() city!: string;
  @IsString() state!: string;
  @IsOptional() @IsString() zipCode?: string;
  @IsOptional() @Transform(decimalInput) @IsNumber() totalArea?: number;
  @IsOptional() @Transform(decimalInput) @IsNumber() builtArea?: number;
  @IsOptional() @IsString() registryNumber?: string;
  @IsOptional() @IsString() description?: string;
  @ApiPropertyOptional({ enum: PropertyStatus })
  @IsOptional()
  @IsEnum(PropertyStatus)
  status?: PropertyStatus;
  @IsOptional() @IsString() notes?: string;
}
