import { PartialType } from "@nestjs/swagger";
import {
  RegularizationStatus,
  RegularizationTaskCategory,
  RegularizationTaskStatus,
} from "@prisma/client";
import { Transform } from "class-transformer";
import {
  IsDate,
  IsEnum,
  IsNumber,
  IsOptional,
  IsString,
  MaxLength,
  Min,
} from "class-validator";

const toDate = ({ value }: { value: unknown }) =>
  value === null || value === "" || value === undefined
    ? value
    : new Date(value as string);
const toNumber = ({ value }: { value: unknown }) =>
  value === null || value === "" || value === undefined ? value : Number(value);

export class CreateRegularizationDto {
  @IsOptional()
  @IsEnum(RegularizationStatus)
  status?: RegularizationStatus;

  @IsOptional()
  @Transform(toDate)
  @IsDate()
  startedAt?: Date | null;

  @IsOptional()
  @Transform(toDate)
  @IsDate()
  completedAt?: Date | null;

  @IsOptional()
  @IsString()
  @MaxLength(2000)
  notes?: string | null;
}

export class UpdateRegularizationDto extends PartialType(
  CreateRegularizationDto,
) {}

export class CreateRegularizationTaskDto {
  @IsOptional()
  @IsEnum(RegularizationTaskCategory)
  category?: RegularizationTaskCategory;

  @IsString()
  @MaxLength(200)
  title!: string;

  @IsOptional()
  @IsString()
  @MaxLength(2000)
  description?: string | null;

  @IsOptional()
  @IsString()
  @MaxLength(120)
  responsible?: string | null;

  @IsOptional()
  @Transform(toDate)
  @IsDate()
  dueDate?: Date | null;

  @IsOptional()
  @Transform(toNumber)
  @IsNumber({ maxDecimalPlaces: 2 })
  @Min(0)
  cost?: number | null;

  @IsOptional()
  @IsString()
  @MaxLength(2000)
  notes?: string | null;
}

export class UpdateRegularizationTaskDto extends PartialType(
  CreateRegularizationTaskDto,
) {
  @IsOptional()
  @IsEnum(RegularizationTaskStatus)
  status?: RegularizationTaskStatus;
}