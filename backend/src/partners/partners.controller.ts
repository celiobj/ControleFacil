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
  UseGuards,
} from "@nestjs/common";
import { ApiBearerAuth, ApiTags } from "@nestjs/swagger";
import { JwtGuard } from "../auth/jwt.guard";
import { PartnerDto } from "./dto/partner.dto";
import { PartnersService } from "./partners.service";

@ApiTags("partners")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller("partners")
export class PartnersController {
  constructor(@Inject(PartnersService) private readonly service: PartnersService) {}

  @Get()
  findAll(@Query() query: { type?: string; search?: string }) {
    return this.service.findAll(query);
  }

  @Post()
  create(@Body() dto: PartnerDto) {
    return this.service.create(dto);
  }

  @Patch(":id")
  update(@Param("id") id: string, @Body() dto: Partial<PartnerDto>) {
    return this.service.update(id, dto);
  }

  @Delete(":id")
  remove(@Param("id") id: string) {
    return this.service.remove(id);
  }
}
