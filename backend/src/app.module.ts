import { Module } from "@nestjs/common";
import { ConfigModule } from "@nestjs/config";
import { ServeStaticModule } from "@nestjs/serve-static";
import { join } from "node:path";
import { PrismaService } from "./prisma.service";
import { AuthModule } from "./auth/auth.module";
import { PropertiesModule } from "./properties/properties.module";
import { OperationsModule } from "./operations/operations.module";
import { DashboardModule } from "./dashboard/dashboard.module";
import { ReportsModule } from "./reports/reports.module";
import { UsersModule } from "./users/users.module";
import { ChecklistsModule } from "./checklists/checklists.module";
import { PartnersModule } from "./partners/partners.module";
import { AcquisitionsModule } from "./acquisitions/acquisitions.module";
import { DocumentsModule } from "./documents/documents.module";
import { RegularizationModule } from "./regularization/regularization.module";
import { PossessionModule } from "./possession/possession.module";
import { EventsModule } from "./events/events.module";
import { ScenariosModule } from "./scenarios/scenarios.module";

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: [
        join(
          __dirname,
          "..",
          process.env.APP_ENV === "hml" ? ".env.hml" : ".env.prd",
        ),
        join(__dirname, "..", ".env"),
      ],
      ignoreEnvFile: false,
    }),
    ServeStaticModule.forRoot({ rootPath: join(__dirname, "..", "public") }),
    AuthModule,
    PropertiesModule,
    OperationsModule,
    DashboardModule,
    ReportsModule,
    UsersModule,
    ChecklistsModule,
    PartnersModule,
    AcquisitionsModule,
    DocumentsModule,
    RegularizationModule,
    PossessionModule,
    EventsModule,
    ScenariosModule,
  ],
  providers: [PrismaService],
})
export class AppModule {}
