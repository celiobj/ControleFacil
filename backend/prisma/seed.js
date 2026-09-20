"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
const client_1 = require("@prisma/client");
const bcrypt = __importStar(require("bcryptjs"));
const prisma = new client_1.PrismaClient();
async function main() {
    const passwordHash = await bcrypt.hash('Admin@123', 10);
    await prisma.user.upsert({ where: { email: 'admin@admin.com' }, update: {}, create: { name: 'Administrador', email: 'admin@admin.com', passwordHash, role: client_1.Role.ADMIN } });
    const property = await prisma.property.upsert({ where: { code: 'CF-001' }, update: {}, create: { code: 'CF-001', title: 'Apartamento modelo', type: client_1.PropertyType.APARTAMENTO, address: 'Rua das Flores', number: '100', neighborhood: 'Centro', city: 'Sao Paulo', state: 'SP', status: client_1.PropertyStatus.EM_ANALISE, description: 'Imovel de exemplo para validar o primeiro acesso.' } });
    await prisma.auction.upsert({ where: { propertyId: property.id }, update: {}, create: { propertyId: property.id, auctioneer: 'Leiloeiro Oficial', auctionValue: 180000, auctionDate: new Date() } });
}
main().finally(() => prisma.$disconnect());
//# sourceMappingURL=seed.js.map