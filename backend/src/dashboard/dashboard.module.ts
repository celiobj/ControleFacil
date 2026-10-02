import { Module } from "@nestjs/common";
import { JwtModule } from "@nestjs/jwt";
import { FinancialModule } from "../financial/financial.module";
import { PrismaService } from "../prisma.service";
import { DashboardController } from "./dashboard.controller";
import { DashboardService } from "./dashboard.service";

@Module({
  imports: [
    JwtModule.register({ secret: process.env.JWT_SECRET }),
    FinancialModule,
  ],
  controllers: [DashboardController],
  providers: [DashboardService, PrismaService],
})
export class DashboardModule {}
