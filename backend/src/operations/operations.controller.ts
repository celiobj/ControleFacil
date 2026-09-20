import { Body, Controller, Get, Inject, Post, Query, UseGuards } from '@nestjs/common';
import { ApiBearerAuth, ApiTags } from '@nestjs/swagger';
import { JwtGuard } from '../auth/jwt.guard';
import { OperationsService } from './operations.service';

@ApiTags('operations') @ApiBearerAuth() @UseGuards(JwtGuard) @Controller()
export class OperationsController {
  constructor(@Inject(OperationsService) private readonly service: OperationsService) {}
  @Get('expenses') expenses(@Query('propertyId') propertyId?: string) { return this.service.listExpenses(propertyId); }
  @Post('expenses') expense(@Body() body: any) { return this.service.createExpense(body); }
  @Get('renovations') renovations(@Query('propertyId') propertyId?: string) { return this.service.listRenovations(propertyId); }
  @Post('renovations') renovation(@Body() body: any) { return this.service.createRenovation(body); }
  @Get('auctions') auctions() { return this.service.listAuctions(); }
  @Post('auctions') auction(@Body() body: any) { return this.service.createAuction(body); }
  @Get('sales') sales() { return this.service.listSales(); }
  @Post('sales') sale(@Body() body: any) { return this.service.createSale(body); }
}
