import { ApiPropertyOptional } from "@nestjs/swagger";
import { Transform } from "class-transformer";
import {
  IsEnum,
  IsNumber,
  IsOptional,
  IsString,
  MaxLength,
} from "class-validator";
import { PropertyStatus, PropertyType } from "@prisma/client";

const decimalInput = ({ value }: { value: unknown }) => {
  if (value === "" || value === null || value === undefined) return undefined;
  if (typeof value === "string") return Number(value.replace(",", "."));
  return value;
};

export class PropertyDto {
  @IsOptional() @IsString() @MaxLength(30) code?: string;
  @IsString() title!: string;
  @IsEnum(PropertyType) type!: PropertyType;
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
