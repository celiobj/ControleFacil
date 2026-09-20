import { Module } from '@nestjs/common';
import { JwtModule } from '@nestjs/jwt';
import { PrismaService } from '../prisma.service';
import { OperationsController } from './operations.controller';
import { OperationsService } from './operations.service';

@Module({ imports: [JwtModule.register({ secret: process.env.JWT_SECRET ?? 'change-me' })], controllers: [OperationsController], providers: [OperationsService, PrismaService] })
export class OperationsModule {}
