import { ApiPropertyOptional } from '@nestjs/swagger';
import { IsEnum, IsNumber, IsOptional, IsString, MaxLength } from 'class-validator';
import { PropertyStatus, PropertyType } from '@prisma/client';

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
  @IsOptional() @IsNumber() totalArea?: number;
  @IsOptional() @IsNumber() builtArea?: number;
  @IsOptional() @IsString() registryNumber?: string;
  @IsOptional() @IsString() description?: string;
  @ApiPropertyOptional({ enum: PropertyStatus }) @IsOptional() @IsEnum(PropertyStatus) status?: PropertyStatus;
  @IsOptional() @IsString() notes?: string;
}
