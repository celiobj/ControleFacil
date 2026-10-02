import {
  Body,
  Controller,
  Get,
  Inject,
  Param,
  Post,
  Req,
  UseGuards,
} from "@nestjs/common";
import {
  ApiBearerAuth,
  ApiOperation,
  ApiParam,
  ApiResponse,
  ApiTags,
} from "@nestjs/swagger";
import { Request } from "express";
import { JwtGuard } from "../auth/jwt.guard";
import { CreateEventDto } from "./dto/event.dto";
import { EventsService } from "./events.service";

type AuthenticatedRequest = Request & { user?: { sub?: string } };

@ApiTags("events")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller("properties/:propertyId/events")
export class EventsController {
  constructor(
    @Inject(EventsService) private readonly service: EventsService,
  ) {}

  @Get()
  @ApiOperation({ summary: "Lista eventos da timeline do imóvel" })
  @ApiParam({ name: "propertyId", format: "uuid" })
  @ApiResponse({ status: 200, description: "Eventos em ordem cronológica decrescente" })
  findForProperty(@Param("propertyId") propertyId: string) {
    return this.service.findForProperty(propertyId);
  }

  @Post()
  @ApiOperation({ summary: "Registra evento na timeline do imóvel" })
  @ApiParam({ name: "propertyId", format: "uuid" })
  @ApiResponse({ status: 201, description: "Evento registrado" })
  create(
    @Param("propertyId") propertyId: string,
    @Body() dto: CreateEventDto,
    @Req() request: AuthenticatedRequest,
  ) {
    return this.service.create(propertyId, dto, request.user?.sub);
  }
}