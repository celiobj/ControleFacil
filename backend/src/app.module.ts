import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { ServeStaticModule } from '@nestjs/serve-static';
import { join } from 'node:path';
import { PrismaService } from './prisma.service';
import { AuthModule } from './auth/auth.module';
import { PropertiesModule } from './properties/properties.module';
import { OperationsModule } from './operations/operations.module';
import { DashboardModule } from './dashboard/dashboard.module';
import { ReportsModule } from './reports/reports.module';
import { UsersModule } from './users/users.module';
import { ChecklistsModule } from './checklists/checklists.module';

@Module({
	imports: [
		ConfigModule.forRoot({ isGlobal: true }),
		ServeStaticModule.forRoot({ rootPath: join(__dirname, '..', 'public') }),
		AuthModule,
		PropertiesModule,
		OperationsModule,
		DashboardModule,
		ReportsModule,
		UsersModule,
		ChecklistsModule,
	],
	providers: [PrismaService],
})
export class AppModule {}
