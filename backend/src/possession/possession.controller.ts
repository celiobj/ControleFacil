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
import {
  ApiBearerAuth,
  ApiOperation,
  ApiParam,
  ApiResponse,
  ApiTags,
} from "@nestjs/swagger";
import { JwtGuard } from "../auth/jwt.guard";
import { CreatePossessionDto, UpdatePossessionDto } from "./dto/possession.dto";
import { PossessionService } from "./possession.service";

@ApiTags("possession")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller()
export class PossessionController {
  constructor(
    @Inject(PossessionService) private readonly service: PossessionService,
  ) {}

  @Get("properties/:propertyId/possession")
  @ApiOperation({ summary: "Consulta a situação de posse do imóvel" })
  @ApiParam({ name: "propertyId", format: "uuid" })
  @ApiResponse({ status: 200, description: "Situação e alerta de ocupação" })
  findForProperty(@Param("propertyId") propertyId: string) {
    return this.service.findForProperty(propertyId);
  }

  @Post("properties/:propertyId/possession")
  @ApiOperation({ summary: "Registra a situação de posse do imóvel" })
  @ApiParam({ name: "propertyId", format: "uuid" })
  createForProperty(
    @Param("propertyId") propertyId: string,
    @Body() dto: CreatePossessionDto,
  ) {
    return this.service.createForProperty(propertyId, dto);
  }

  @Patch("possession/:id")
  @ApiOperation({ summary: "Atualiza a situação de posse" })
  @ApiParam({ name: "id", format: "uuid" })
  update(@Param("id") id: string, @Body() dto: UpdatePossessionDto) {
    return this.service.update(id, dto);
  }
}