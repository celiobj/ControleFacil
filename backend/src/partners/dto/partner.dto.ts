import { Transform } from "class-transformer";
import { IsEmail, IsEnum, IsOptional, IsString, MaxLength } from "class-validator";
import { PartialType } from "@nestjs/swagger";
import { PartnerType } from "@prisma/client";

const emptyToNull = ({ value }: { value: unknown }) =>
  value === "" ? null : value;

export class PartnerDto {
  @IsEnum(PartnerType)
  type!: PartnerType;

  @IsString()
  @MaxLength(120)
  name!: string;

  @IsOptional()
  @IsString()
  @MaxLength(18)
  @Transform(emptyToNull)
  document?: string;

  @IsOptional()
  @IsEmail()
  @Transform(emptyToNull)
  email?: string;

  @IsOptional()
  @IsString()
  @MaxLength(30)
  @Transform(emptyToNull)
  phone?: string;

  @IsOptional()
  @IsString()
  @MaxLength(120)
  @Transform(emptyToNull)
  company?: string;

  @IsOptional()
  @IsString()
  @Transform(emptyToNull)
  notes?: string;
}

export class UpdatePartnerDto extends PartialType(PartnerDto) {}
