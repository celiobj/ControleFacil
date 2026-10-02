import { Module } from "@nestjs/common";
import { JwtModule } from "@nestjs/jwt";
import { FinancialModule } from "../financial/financial.module";
import { PrismaService } from "../prisma.service";
import { ScenariosController } from "./scenarios.controller";
import { ScenariosService } from "./scenarios.service";

@Module({
  imports: [JwtModule.register({ secret: process.env.JWT_SECRET }), FinancialModule],
  controllers: [ScenariosController],
  providers: [PrismaService, ScenariosService],
})
export class ScenariosModule {}