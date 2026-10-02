import {
  Body,
  Controller,
  Get,
  Inject,
  Param,
  Patch,
  Post,
  Query,
  UseGuards,
} from "@nestjs/common";
import {
  ApiBearerAuth,
  ApiOperation,
  ApiParam,
  ApiQuery,
  ApiResponse,
  ApiTags,
} from "@nestjs/swagger";
import { JwtGuard } from "../auth/jwt.guard";
import { AcquisitionsService } from "./acquisitions.service";
import {
  CreateAcquisitionDto,
  CreatePropertyAcquisitionDto,
  UpdateAcquisitionDto,
} from "./dto/acquisition.dto";

@ApiTags("acquisitions")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller("acquisitions")
export class AcquisitionsController {
  constructor(
    @Inject(AcquisitionsService) private readonly service: AcquisitionsService,
  ) {}

  @Get()
  @ApiOperation({ summary: "Lista aquisições" })
  @ApiQuery({ name: "propertyId", required: false, format: "uuid" })
  findAll(@Query("propertyId") propertyId?: string) {
    return this.service.findAll(propertyId);
  }

  @Get(":id")
  @ApiOperation({ summary: "Consulta uma aquisição" })
  @ApiParam({ name: "id", format: "uuid" })
  findOne(@Param("id") id: string) {
    return this.service.findOne(id);
  }

  @Post()
  @ApiOperation({ summary: "Cria uma aquisição" })
  create(@Body() dto: CreateAcquisitionDto) {
    return this.service.create(dto);
  }

  @Patch(":id")
  @ApiOperation({ summary: "Atualiza uma aquisição" })
  @ApiParam({ name: "id", format: "uuid" })
  update(@Param("id") id: string, @Body() dto: UpdateAcquisitionDto) {
    return this.service.update(id, dto);
  }
}

@ApiTags("acquisitions")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller("properties/:propertyId/acquisition")
export class PropertyAcquisitionsController {
  constructor(
    @Inject(AcquisitionsService) private readonly service: AcquisitionsService,
  ) {}

  @Get()
  @ApiOperation({ summary: "Consulta a aquisição efetiva do imóvel" })
  @ApiParam({ name: "propertyId", format: "uuid" })
  @ApiResponse({ status: 200, description: "Aquisição ou null quando ainda não criada" })
  findForProperty(@Param("propertyId") propertyId: string) {
    return this.service.findForProperty(propertyId);
  }

  @Post()
  @ApiOperation({ summary: "Registra a aquisição efetiva do imóvel" })
  @ApiParam({ name: "propertyId", format: "uuid" })
  @ApiResponse({ status: 201, description: "Aquisição registrada" })
  create(
    @Param("propertyId") propertyId: string,
    @Body() dto: CreatePropertyAcquisitionDto,
  ) {
    return this.service.create({ ...dto, propertyId });
  }
}
