import { Transform } from "class-transformer";
import { IsEmail, IsEnum, IsOptional, IsString, MaxLength } from "class-validator";
import { PartnerType } from "@prisma/client";

const emptyToUndefined = ({ value }: { value: unknown }) =>
  value === "" ? undefined : value;

export class PartnerDto {
  @IsEnum(PartnerType)
  type!: PartnerType;

  @IsString()
  @MaxLength(120)
  name!: string;

  @IsOptional()
  @IsString()
  @MaxLength(18)
  @Transform(emptyToUndefined)
  document?: string;

  @IsOptional()
  @IsEmail()
  @Transform(emptyToUndefined)
  email?: string;

  @IsOptional()
  @IsString()
  @MaxLength(30)
  @Transform(emptyToUndefined)
  phone?: string;

  @IsOptional()
  @IsString()
  @MaxLength(120)
  @Transform(emptyToUndefined)
  company?: string;

  @IsOptional()
  @IsString()
  @Transform(emptyToUndefined)
  notes?: string;
}
