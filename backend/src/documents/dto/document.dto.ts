import { PartialType } from "@nestjs/swagger";
import { PropertyDocumentStatus, PropertyDocumentType } from "@prisma/client";
import { Transform } from "class-transformer";
import {
  IsDate,
  IsEnum,
  IsOptional,
  IsString,
  MaxLength,
} from "class-validator";

const toDate = ({ value }: { value: unknown }) =>
  value === null || value === "" || value === undefined
    ? value
    : new Date(value as string);

export class CreateDocumentDto {
  @IsEnum(PropertyDocumentType)
  type!: PropertyDocumentType;

  @IsString()
  @MaxLength(200)
  title!: string;

  @IsOptional()
  @IsEnum(PropertyDocumentStatus)
  status?: PropertyDocumentStatus;

  @IsOptional()
  @Transform(toDate)
  @IsDate()
  documentDate?: Date | null;

  @IsOptional()
  @IsString()
  @MaxLength(2000)
  description?: string | null;
}

export class UpdateDocumentDto extends PartialType(CreateDocumentDto) {}