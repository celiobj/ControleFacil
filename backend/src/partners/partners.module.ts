import { Module } from "@nestjs/common";
import { JwtModule } from "@nestjs/jwt";
import { PrismaService } from "../prisma.service";
import { PartnersController } from "./partners.controller";
import { PartnersService } from "./partners.service";

@Module({
  imports: [JwtModule.register({ secret: process.env.JWT_SECRET ?? "change-me" })],
  controllers: [PartnersController],
  providers: [PartnersService, PrismaService],
})
export class PartnersModule {}
