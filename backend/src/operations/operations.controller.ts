import {
  Body,
  Controller,
  Delete,
  Get,
  Inject,
  Param,
  Post,
  Query,
  Req,
  UseGuards,
} from "@nestjs/common";
import { ApiBearerAuth, ApiTags } from "@nestjs/swagger";
import { Request } from "express";
import { JwtGuard } from "../auth/jwt.guard";
import { OperationsService } from "./operations.service";

type AuthenticatedRequest = Request & { user?: { sub?: string } };

@ApiTags("operations")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller()
export class OperationsController {
  constructor(
    @Inject(OperationsService) private readonly service: OperationsService,
  ) {}
  @Get("expenses") expenses(@Query("propertyId") propertyId?: string) {
    return this.service.listExpenses(propertyId);
  }
  @Post("expenses") expense(@Body() body: any) {
    return this.service.createExpense(body);
  }
  @Delete("expenses/:id") removeExpense(@Param("id") id: string) {
    return this.service.deleteExpense(id);
  }
  @Get("renovations") renovations(@Query("propertyId") propertyId?: string) {
    return this.service.listRenovations(propertyId);
  }
  @Post("renovations") renovation(@Body() body: any) {
    return this.service.createRenovation(body);
  }
  @Get("auctions") auctions() {
    return this.service.listAuctions();
  }
  @Post("auctions") auction(
    @Body() body: any,
    @Req() request: AuthenticatedRequest,
  ) {
    return this.service.createAuction(body, request.user?.sub);
  }
  @Get("sales") sales() {
    return this.service.listSales();
  }
  @Post("sales") sale(
    @Body() body: any,
    @Req() request: AuthenticatedRequest,
  ) {
    return this.service.createSale(body, request.user?.sub);
  }
}
