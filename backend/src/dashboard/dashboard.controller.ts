import { Controller, Get, Inject, UseGuards } from "@nestjs/common";
import { ApiBearerAuth, ApiTags } from "@nestjs/swagger";
import { JwtGuard } from "../auth/jwt.guard";
import { DashboardService } from "./dashboard.service";

@ApiTags("dashboard")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller("dashboard")
export class DashboardController {
  constructor(
    @Inject(DashboardService) private readonly service: DashboardService,
  ) {}
  @Get() summary() {
    return this.service.summary();
  }
}
