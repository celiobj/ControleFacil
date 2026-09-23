import { Module } from "@nestjs/common";
import { JwtModule } from "@nestjs/jwt";
import { PrismaService } from "../prisma.service";
import { ChecklistsController } from "./checklists.controller";
import { ChecklistsService } from "./checklists.service";

@Module({
  imports: [
    JwtModule.register({ secret: process.env.JWT_SECRET ?? "change-me" }),
  ],
  controllers: [ChecklistsController],
  providers: [ChecklistsService, PrismaService],
  exports: [ChecklistsService],
})
export class ChecklistsModule {}
