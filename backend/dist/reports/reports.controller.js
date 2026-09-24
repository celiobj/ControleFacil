"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.ReportsController = void 0;
const common_1 = require("@nestjs/common");
const swagger_1 = require("@nestjs/swagger");
const jwt_guard_1 = require("../auth/jwt.guard");
const prisma_service_1 = require("../prisma.service");
const dashboard_service_1 = require("../dashboard/dashboard.service");
let ReportsController = class ReportsController {
    prisma;
    dashboard;
    constructor(prisma, dashboard) {
        this.prisma = prisma;
        this.dashboard = dashboard;
    }
    profit() {
        return this.dashboard.summary();
    }
    expenses() {
        return this.prisma.expense.groupBy({
            by: ["category"],
            _sum: { amount: true },
            _count: { id: true },
        });
    }
    async csv(response) {
        const report = await this.dashboard.summary();
        const lines = [
            "codigo;imovel;custo_total;venda;lucro_liquido;roi",
            ...report.operations.map((item) => `${item.code};${item.title};${item.costTotal.toFixed(2)};${item.sale.toFixed(2)};${item.netProfit.toFixed(2)};${item.roi.toFixed(2)}`),
        ];
        response
            .header("Content-Type", "text/csv; charset=utf-8")
            .attachment("relatorio-lucro.csv")
            .send(lines.join("\n"));
    }
};
exports.ReportsController = ReportsController;
__decorate([
    (0, common_1.Get)("profit"),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", void 0)
], ReportsController.prototype, "profit", null);
__decorate([
    (0, common_1.Get)("expenses"),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", void 0)
], ReportsController.prototype, "expenses", null);
__decorate([
    (0, common_1.Get)("profit.csv"),
    __param(0, (0, common_1.Res)()),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", [Object]),
    __metadata("design:returntype", Promise)
], ReportsController.prototype, "csv", null);
exports.ReportsController = ReportsController = __decorate([
    (0, swagger_1.ApiTags)("reports"),
    (0, swagger_1.ApiBearerAuth)(),
    (0, common_1.UseGuards)(jwt_guard_1.JwtGuard),
    (0, common_1.Controller)("reports"),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService,
        dashboard_service_1.DashboardService])
], ReportsController);
//# sourceMappingURL=reports.controller.js.map