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
exports.DashboardService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma.service");
const money = (value) => Number(value ?? 0);
let DashboardService = class DashboardService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    async summary() {
        const properties = await this.prisma.property.findMany({
            where: { status: { not: "CANCELADO" } },
            include: { auction: true, expenses: true, renovations: true, sale: true },
        });
        const rows = properties.map((property) => {
            const acquisition = money(property.auction?.auctionValue);
            const expenses = property.expenses.reduce((sum, item) => sum + money(item.amount), 0);
            const renovations = property.renovations.reduce((sum, item) => sum + money(item.actualAmount ?? item.plannedAmount), 0);
            const costTotal = acquisition + expenses + renovations;
            const sale = money(property.sale?.saleAmount);
            const netProfit = sale -
                costTotal -
                money(property.sale?.brokerage) -
                money(property.sale?.taxes);
            return {
                propertyId: property.id,
                code: property.code,
                title: property.title,
                costTotal,
                sale,
                netProfit,
                roi: costTotal ? (netProfit / costTotal) * 100 : 0,
                saleDate: property.sale?.saleDate,
            };
        });
        const sold = rows.filter((row) => row.sale > 0);
        const totalInvested = rows.reduce((sum, row) => sum + row.costTotal, 0);
        const byStatus = {};
        for (const property of properties)
            byStatus[property.status] = (byStatus[property.status] ?? 0) + 1;
        return {
            totalInvested,
            totalSold: sold.reduce((sum, row) => sum + row.sale, 0),
            accumulatedProfit: rows.reduce((sum, row) => sum + row.netProfit, 0),
            averageRoi: sold.length
                ? sold.reduce((sum, row) => sum + row.roi, 0) / sold.length
                : 0,
            propertyCount: properties.length,
            byStatus,
            operations: rows,
        };
    }
};
exports.DashboardService = DashboardService;
exports.DashboardService = DashboardService = __decorate([
    (0, common_1.Injectable)(),
    __param(0, (0, common_1.Inject)(prisma_service_1.PrismaService)),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], DashboardService);
//# sourceMappingURL=dashboard.service.js.map