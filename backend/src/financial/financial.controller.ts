import { Controller, Get, Inject, Param, UseGuards } from "@nestjs/common";
import {
  ApiBearerAuth,
  ApiOperation,
  ApiParam,
  ApiResponse,
  ApiTags,
} from "@nestjs/swagger";
import { JwtGuard } from "../auth/jwt.guard";
import { FinancialService } from "./financial.service";

@ApiTags("financial")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller("properties/:propertyId/financial")
export class FinancialController {
  constructor(
    @Inject(FinancialService) private readonly service: FinancialService,
  ) {}

  @Get()
  @ApiOperation({ summary: "Calcula os custos e o resultado financeiro do imóvel" })
  @ApiParam({ name: "propertyId", format: "uuid" })
  @ApiResponse({ status: 200, description: "Resumo financeiro consolidado" })
  getForProperty(@Param("propertyId") propertyId: string) {
    return this.service.calculatePropertyFinancials(propertyId);
  }
}