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
import {
  CreateRegularizationDto,
  CreateRegularizationTaskDto,
  UpdateRegularizationDto,
  UpdateRegularizationTaskDto,
} from "./dto/regularization.dto";
import { RegularizationService } from "./regularization.service";

@ApiTags("regularization")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller()
export class RegularizationController {
  constructor(
    @Inject(RegularizationService)
    private readonly service: RegularizationService,
  ) {}

  @Get("properties/:propertyId/regularization")
  @ApiOperation({ summary: "Consulta regularização do imóvel" })
  @ApiParam({ name: "propertyId", format: "uuid" })
  @ApiResponse({ status: 200, description: "Regularização e tarefas" })
  findForProperty(@Param("propertyId") propertyId: string) {
    return this.service.findForProperty(propertyId);
  }

  @Post("properties/:propertyId/regularization")
  @ApiOperation({ summary: "Cria regularização e tarefas padrão ausentes" })
  @ApiParam({ name: "propertyId", format: "uuid" })
  createForProperty(
    @Param("propertyId") propertyId: string,
    @Body() dto: CreateRegularizationDto,
  ) {
    return this.service.createForProperty(propertyId, dto);
  }

  @Patch("regularization/:id")
  @ApiOperation({ summary: "Atualiza uma regularização" })
  @ApiParam({ name: "id", format: "uuid" })
  update(
    @Param("id") id: string,
    @Body() dto: UpdateRegularizationDto,
  ) {
    return this.service.update(id, dto);
  }

  @Get("properties/:propertyId/regularization/tasks")
  @ApiOperation({ summary: "Lista tarefas de regularização" })
  @ApiParam({ name: "propertyId", format: "uuid" })
  findTasksForProperty(@Param("propertyId") propertyId: string) {
    return this.service.findTasksForProperty(propertyId);
  }

  @Post("properties/:propertyId/regularization/tasks")
  @ApiOperation({ summary: "Adiciona tarefa de regularização" })
  @ApiParam({ name: "propertyId", format: "uuid" })
  createTask(
    @Param("propertyId") propertyId: string,
    @Body() dto: CreateRegularizationTaskDto,
  ) {
    return this.service.addTask(propertyId, dto);
  }

  @Patch("regularization/tasks/:id")
  @ApiOperation({ summary: "Atualiza tarefa de regularização" })
  @ApiParam({ name: "id", format: "uuid" })
  updateTask(
    @Param("id") id: string,
    @Body() dto: UpdateRegularizationTaskDto,
  ) {
    return this.service.updateTask(id, dto);
  }
}