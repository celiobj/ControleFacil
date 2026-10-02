import {
  Body,
  Delete,
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
import { CreateScenarioDto, UpdateScenarioDto } from "./dto/scenario.dto";
import { ScenariosService } from "./scenarios.service";

@ApiTags("scenarios")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller()
export class ScenariosController {
  constructor(
    @Inject(ScenariosService) private readonly service: ScenariosService,
  ) {}

  @Get("properties/:propertyId/scenarios")
  @ApiOperation({ summary: "Lista cenários de venda do imóvel" })
  @ApiParam({ name: "propertyId", format: "uuid" })
  @ApiResponse({ status: 200, description: "Cenários com valores calculados" })
  findForProperty(@Param("propertyId") propertyId: string) {
    return this.service.findForProperty(propertyId);
  }

  @Post("properties/:propertyId/scenarios")
  @ApiOperation({ summary: "Cria um cenário de venda" })
  @ApiParam({ name: "propertyId", format: "uuid" })
  @ApiResponse({ status: 201, description: "Cenário calculado e persistido" })
  create(
    @Param("propertyId") propertyId: string,
    @Body() dto: CreateScenarioDto,
  ) {
    return this.service.create(propertyId, dto);
  }

  @Patch("scenarios/:id")
  @ApiOperation({ summary: "Atualiza e recalcula um cenário de venda" })
  @ApiParam({ name: "id", format: "uuid" })
  update(@Param("id") id: string, @Body() dto: UpdateScenarioDto) {
    return this.service.update(id, dto);
  }

  @Delete("scenarios/:id")
  @ApiOperation({ summary: "Exclui um cenário de venda" })
  @ApiParam({ name: "id", format: "uuid" })
  @ApiResponse({ status: 200, description: "Cenário excluído" })
  remove(@Param("id") id: string) {
    return this.service.remove(id);
  }
}