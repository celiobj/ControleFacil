import { Module } from "@nestjs/common";
import { JwtModule } from "@nestjs/jwt";
import { PrismaService } from "../prisma.service";
import { PossessionController } from "./possession.controller";
import { PossessionService } from "./possession.service";

@Module({
  imports: [JwtModule.register({ secret: process.env.JWT_SECRET })],
  controllers: [PossessionController],
  providers: [PrismaService, PossessionService],
})
export class PossessionModule {}