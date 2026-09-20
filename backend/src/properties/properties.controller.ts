import { Body, Controller, Delete, Get, Inject, Param, Patch, Post, Query, UseGuards } from '@nestjs/common';
import { ApiBearerAuth, ApiTags } from '@nestjs/swagger';
import { JwtGuard } from '../auth/jwt.guard';
import { PropertiesService } from './properties.service';
import { PropertyDto } from './dto/property.dto';

@ApiTags('properties') @ApiBearerAuth() @UseGuards(JwtGuard)
@Controller('properties')
export class PropertiesController {
  constructor(@Inject(PropertiesService) private readonly service: PropertiesService) {}
  @Get() findAll(@Query() query: any) { return this.service.findAll(query); }
  @Get(':id') findOne(@Param('id') id: string) { return this.service.findOne(id); }
  @Post() create(@Body() dto: PropertyDto) { return this.service.create(dto); }
  @Patch(':id') update(@Param('id') id: string, @Body() dto: Partial<PropertyDto>) { return this.service.update(id, dto); }
  @Delete(':id') remove(@Param('id') id: string) { return this.service.remove(id); }
}
