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
exports.PartnersService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma.service");
let PartnersService = class PartnersService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    findAll(query) {
        const search = query.search?.trim();
        return this.prisma.partner.findMany({
            where: {
                type: query.type,
                OR: search
                    ? [
                        { name: { contains: search, mode: "insensitive" } },
                        { company: { contains: search, mode: "insensitive" } },
                        { document: { contains: search, mode: "insensitive" } },
                        { email: { contains: search, mode: "insensitive" } },
                    ]
                    : undefined,
            },
            orderBy: { createdAt: "desc" },
        });
    }
    create(data) {
        return this.prisma.partner.create({ data });
    }
    async update(id, data) {
        const partner = await this.prisma.partner.findUnique({ where: { id } });
        if (!partner)
            throw new common_1.NotFoundException("Cadastro não encontrado");
        return this.prisma.partner.update({ where: { id }, data });
    }
    async remove(id) {
        const partner = await this.prisma.partner.findUnique({ where: { id } });
        if (!partner)
            throw new common_1.NotFoundException("Cadastro não encontrado");
        return this.prisma.partner.delete({ where: { id } });
    }
};
exports.PartnersService = PartnersService;
exports.PartnersService = PartnersService = __decorate([
    (0, common_1.Injectable)(),
    __param(0, (0, common_1.Inject)(prisma_service_1.PrismaService)),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], PartnersService);
//# sourceMappingURL=partners.service.js.map