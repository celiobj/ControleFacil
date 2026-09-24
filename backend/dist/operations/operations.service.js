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
exports.OperationsService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma.service");
let OperationsService = class OperationsService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    listExpenses(propertyId) {
        return this.prisma.expense.findMany({
            where: { propertyId },
            orderBy: { date: "desc" },
            include: { property: { select: { code: true, title: true } } },
        });
    }
    createExpense(data) {
        return this.prisma.expense.create({
            data: { ...data, date: new Date(data.date) },
        });
    }
    listRenovations(propertyId) {
        return this.prisma.renovation.findMany({
            where: { propertyId },
            orderBy: { startDate: "desc" },
        });
    }
    createRenovation(data) {
        return this.prisma.renovation.create({
            data: {
                ...data,
                startDate: data.startDate ? new Date(data.startDate) : undefined,
                endDate: data.endDate ? new Date(data.endDate) : undefined,
            },
        });
    }
    listAuctions() {
        return this.prisma.auction.findMany({
            include: { property: true },
            orderBy: { auctionDate: "desc" },
        });
    }
    createAuction(data) {
        const { propertyId, ...auctionData } = data;
        const normalizedData = {
            ...auctionData,
            auctionDate: new Date(data.auctionDate),
            acquisitionDate: data.acquisitionDate
                ? new Date(data.acquisitionDate)
                : undefined,
        };
        return this.prisma.$transaction(async (transaction) => {
            const auction = await transaction.auction.upsert({
                where: { propertyId },
                create: { propertyId, ...normalizedData },
                update: normalizedData,
            });
            await transaction.property.update({
                where: { id: propertyId },
                data: { status: "ARREMATADO" },
            });
            return auction;
        });
    }
    listSales() {
        return this.prisma.sale.findMany({
            include: { property: true },
            orderBy: { saleDate: "desc" },
        });
    }
    createSale(data) {
        const { propertyId, ...saleData } = data;
        const normalizedData = {
            ...saleData,
            saleDate: new Date(data.saleDate),
        };
        return this.prisma.$transaction(async (transaction) => {
            const sale = await transaction.sale.upsert({
                where: { propertyId },
                create: { propertyId, ...normalizedData },
                update: normalizedData,
            });
            await transaction.property.update({
                where: { id: propertyId },
                data: { status: "VENDIDO" },
            });
            return sale;
        });
    }
};
exports.OperationsService = OperationsService;
exports.OperationsService = OperationsService = __decorate([
    (0, common_1.Injectable)(),
    __param(0, (0, common_1.Inject)(prisma_service_1.PrismaService)),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], OperationsService);
//# sourceMappingURL=operations.service.js.map