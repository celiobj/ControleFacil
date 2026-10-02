import { Module } from "@nestjs/common";
import { JwtModule } from "@nestjs/jwt";
import { PrismaService } from "../prisma.service";
import {
  AcquisitionsController,
  PropertyAcquisitionsController,
} from "./acquisitions.controller";
import { AcquisitionsService } from "./acquisitions.service";

@Module({
  imports: [JwtModule.register({ secret: process.env.JWT_SECRET })],
  controllers: [AcquisitionsController, PropertyAcquisitionsController],
  providers: [AcquisitionsService, PrismaService],
})
export class AcquisitionsModule {}
