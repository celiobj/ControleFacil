"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.AppModule = void 0;
const common_1 = require("@nestjs/common");
const config_1 = require("@nestjs/config");
const serve_static_1 = require("@nestjs/serve-static");
const node_path_1 = require("node:path");
const prisma_service_1 = require("./prisma.service");
const auth_module_1 = require("./auth/auth.module");
const properties_module_1 = require("./properties/properties.module");
const operations_module_1 = require("./operations/operations.module");
const dashboard_module_1 = require("./dashboard/dashboard.module");
const reports_module_1 = require("./reports/reports.module");
const users_module_1 = require("./users/users.module");
const checklists_module_1 = require("./checklists/checklists.module");
const partners_module_1 = require("./partners/partners.module");
let AppModule = class AppModule {
};
exports.AppModule = AppModule;
exports.AppModule = AppModule = __decorate([
    (0, common_1.Module)({
        imports: [
            config_1.ConfigModule.forRoot({ isGlobal: true }),
            serve_static_1.ServeStaticModule.forRoot({ rootPath: (0, node_path_1.join)(__dirname, "..", "public") }),
            auth_module_1.AuthModule,
            properties_module_1.PropertiesModule,
            operations_module_1.OperationsModule,
            dashboard_module_1.DashboardModule,
            reports_module_1.ReportsModule,
            users_module_1.UsersModule,
            checklists_module_1.ChecklistsModule,
            partners_module_1.PartnersModule,
        ],
        providers: [prisma_service_1.PrismaService],
    })
], AppModule);
//# sourceMappingURL=app.module.js.map