import { Module } from "@nestjs/common";
import { JwtModule } from "@nestjs/jwt";
import { FinancialModule } from "../financial/financial.module";
import { PrismaService } from "../prisma.service";
import { DashboardService } from "../dashboard/dashboard.service";
import { ReportsController } from "./reports.controller";

@Module({
  imports: [
    JwtModule.register({ secret: process.env.JWT_SECRET }),
    FinancialModule,
  ],
  controllers: [ReportsController],
  providers: [PrismaService, DashboardService],
})
export class ReportsModule {}
