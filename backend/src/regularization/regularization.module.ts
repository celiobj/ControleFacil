import { Module } from "@nestjs/common";
import { JwtModule } from "@nestjs/jwt";
import { PrismaService } from "../prisma.service";
import { RegularizationController } from "./regularization.controller";
import { RegularizationService } from "./regularization.service";

@Module({
  imports: [JwtModule.register({ secret: process.env.JWT_SECRET })],
  controllers: [RegularizationController],
  providers: [PrismaService, RegularizationService],
})
export class RegularizationModule {}