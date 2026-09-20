import { Module } from '@nestjs/common';
import { JwtModule } from '@nestjs/jwt';
import { PrismaService } from '../prisma.service';
import { DashboardController } from './dashboard.controller';
import { DashboardService } from './dashboard.service';

@Module({ imports: [JwtModule.register({ secret: process.env.JWT_SECRET ?? 'change-me' })], controllers: [DashboardController], providers: [DashboardService, PrismaService] })
export class DashboardModule {}