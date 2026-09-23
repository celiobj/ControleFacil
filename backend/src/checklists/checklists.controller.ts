import {
  Body,
  Controller,
  Get,
  Inject,
  Param,
  Patch,
  Post,
  UseGuards,
} from "@nestjs/common";
import { ApiBearerAuth, ApiTags } from "@nestjs/swagger";
import {
  IsDateString,
  IsEnum,
  IsNotEmpty,
  IsOptional,
  IsString,
} from "class-validator";
import { ChecklistItemStatus } from "@prisma/client";
import { JwtGuard } from "../auth/jwt.guard";
import { ChecklistsService } from "./checklists.service";

class ChecklistItemUpdateDto {
  @IsOptional() @IsEnum(ChecklistItemStatus) status?: ChecklistItemStatus;
  @IsOptional() @IsString() responsible?: string;
  @IsOptional() @IsDateString() dueDate?: string;
  @IsOptional() @IsString() notes?: string;
}
class ChecklistItemCreateDto {
  @IsString() @IsNotEmpty() phase!: string;
  @IsString() @IsNotEmpty() stage!: string;
  @IsString() @IsNotEmpty() task!: string;
  @IsOptional() @IsString() responsible?: string;
  @IsOptional() @IsDateString() dueDate?: string;
  @IsOptional() @IsString() notes?: string;
}
@ApiTags("checklists")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller()
export class ChecklistsController {
  constructor(
    @Inject(ChecklistsService) private readonly service: ChecklistsService,
  ) {}
  @Get("properties/:propertyId/checklist") get(
    @Param("propertyId") propertyId: string,
  ) {
    return this.service.ensure(propertyId);
  }
  @Post("properties/:propertyId/checklist/items") add(
    @Param("propertyId") propertyId: string,
    @Body() dto: ChecklistItemCreateDto,
  ) {
    return this.service.addItem(propertyId, dto);
  }
  @Patch("checklist/items/:itemId") update(
    @Param("itemId") itemId: string,
    @Body() dto: ChecklistItemUpdateDto,
  ) {
    return this.service.updateItem(itemId, dto);
  }
}
