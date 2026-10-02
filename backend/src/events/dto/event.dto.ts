import { PropertyEventType } from "@prisma/client";
import { Transform } from "class-transformer";
import { IsDate, IsEnum, IsOptional, IsString, MaxLength } from "class-validator";

const toDate = ({ value }: { value: unknown }) =>
  value === null || value === "" || value === undefined
    ? value
    : new Date(value as string);

export class CreateEventDto {
  @IsEnum(PropertyEventType)
  type!: PropertyEventType;

  @IsString()
  @MaxLength(200)
  title!: string;

  @IsOptional()
  @IsString()
  @MaxLength(2000)
  description?: string | null;

  @IsOptional()
  @Transform(toDate)
  @IsDate()
  eventDate?: Date;
}