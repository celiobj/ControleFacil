import { Module } from "@nestjs/common";
import { JwtModule } from "@nestjs/jwt";
import { PrismaService } from "../prisma.service";
import { FinancialController } from "./financial.controller";
import { FinancialService } from "./financial.service";

@Module({
  imports: [JwtModule.register({ secret: process.env.JWT_SECRET })],
  controllers: [FinancialController],
  providers: [PrismaService, FinancialService],
  exports: [FinancialService],
})
export class FinancialModule {}
