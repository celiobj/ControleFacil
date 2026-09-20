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
exports.PropertiesService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma.service");
const checklists_service_1 = require("../checklists/checklists.service");
let PropertiesService = class PropertiesService {
    prisma;
    checklists;
    constructor(prisma, checklists) {
        this.prisma = prisma;
        this.checklists = checklists;
    }
    findAll(query) {
        const page = Math.max(1, Number(query.page ?? 1));
        const limit = Math.min(100, Math.max(1, Number(query.limit ?? 20)));
        const where = { city: query.city ? { contains: query.city, mode: 'insensitive' } : undefined, neighborhood: query.neighborhood ? { contains: query.neighborhood, mode: 'insensitive' } : undefined, status: query.status, type: query.type };
        return Promise.all([this.prisma.property.findMany({ where, skip: (page - 1) * limit, take: limit, include: { auction: true, sale: true }, orderBy: { updatedAt: 'desc' } }), this.prisma.property.count({ where })]).then(([data, total]) => ({ data, meta: { page, limit, total, pages: Math.ceil(total / limit) } }));
    }
    async findOne(id) { const item = await this.prisma.property.findUnique({ where: { id }, include: { auction: true, expenses: true, renovations: true, sale: true } }); if (!item)
        throw new common_1.NotFoundException('Imóvel não encontrado'); return item; }
    async create(data) { const property = await this.prisma.property.create({ data }); await this.checklists.ensure(property.id); return property; }
    async update(id, data) { await this.findOne(id); return this.prisma.property.update({ where: { id }, data }); }
    async remove(id) { await this.findOne(id); return this.prisma.property.update({ where: { id }, data: { status: 'CANCELADO' } }); }
};
exports.PropertiesService = PropertiesService;
exports.PropertiesService = PropertiesService = __decorate([
    (0, common_1.Injectable)(),
    __param(0, (0, common_1.Inject)(prisma_service_1.PrismaService)),
    __param(1, (0, common_1.Inject)(checklists_service_1.ChecklistsService)),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService, checklists_service_1.ChecklistsService])
], PropertiesService);
//# sourceMappingURL=properties.service.js.map