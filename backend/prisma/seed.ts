import { PrismaClient, Role, PropertyType, PropertyStatus } from '@prisma/client';
import * as bcrypt from 'bcryptjs';

const prisma = new PrismaClient();
async function main() {
  const passwordHash = await bcrypt.hash('Admin@123', 10);
  await prisma.user.upsert({ where: { email: 'admin@admin.com' }, update: {}, create: { name: 'Administrador', email: 'admin@admin.com', passwordHash, role: Role.ADMIN } });
  const property = await prisma.property.upsert({ where: { code: 'CF-001' }, update: {}, create: { code: 'CF-001', title: 'Apartamento modelo', type: PropertyType.APARTAMENTO, address: 'Rua das Flores', number: '100', neighborhood: 'Centro', city: 'Sao Paulo', state: 'SP', status: PropertyStatus.EM_ANALISE, description: 'Imovel de exemplo para validar o primeiro acesso.' } });
  await prisma.auction.upsert({ where: { propertyId: property.id }, update: {}, create: { propertyId: property.id, auctioneer: 'Leiloeiro Oficial', auctionValue: 180000, auctionDate: new Date() } });
}
main().finally(() => prisma.$disconnect());
