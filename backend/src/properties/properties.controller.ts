import {
  Body,
  Controller,
  Delete,
  Get,
  Inject,
  Param,
  Patch,
  Post,
  Query,
  Req,
  UseGuards,
} from "@nestjs/common";
import { ApiBearerAuth, ApiTags } from "@nestjs/swagger";
import { Request } from "express";
import { JwtGuard } from "../auth/jwt.guard";
import { PropertiesService } from "./properties.service";
import { PropertyDto } from "./dto/property.dto";

type AuthenticatedRequest = Request & { user?: { sub?: string } };

@ApiTags("properties")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller("properties")
export class PropertiesController {
  constructor(
    @Inject(PropertiesService) private readonly service: PropertiesService,
  ) {}
  @Get() findAll(@Query() query: any) {
    return this.service.findAll(query);
  }
  @Post("import/caixa/text")
  importFromCaixaText(@Body("text") text: string) {
    return this.service.importFromCaixaText(text);
  }
  @Get(":id") findOne(@Param("id") id: string) {
    return this.service.findOne(id);
  }
  @Post() create(@Body() dto: PropertyDto) {
    return this.service.create(dto);
  }
  @Patch(":id") update(
    @Param("id") id: string,
    @Body() dto: Partial<PropertyDto>,
    @Req() request: AuthenticatedRequest,
  ) {
    return this.service.update(id, dto, request.user?.sub);
  }
  @Delete(":id") remove(@Param("id") id: string) {
    return this.service.remove(id);
  }
}
