import { Module } from '@nestjs/common';
import { JwtModule } from '@nestjs/jwt';
import { PrismaService } from '../prisma.service';
import { PropertiesController } from './properties.controller';
import { PropertiesService } from './properties.service';
import { ChecklistsModule } from '../checklists/checklists.module';

@Module({ imports: [JwtModule.register({ secret: process.env.JWT_SECRET ?? 'change-me' }), ChecklistsModule], controllers: [PropertiesController], providers: [PropertiesService, PrismaService] })
export class PropertiesModule {}
