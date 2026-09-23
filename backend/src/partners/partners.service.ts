import { Inject, Injectable, NotFoundException } from "@nestjs/common";
import { PrismaService } from "../prisma.service";
import { PartnerDto } from "./dto/partner.dto";

@Injectable()
export class PartnersService {
  constructor(@Inject(PrismaService) private readonly prisma: PrismaService) {}

  findAll(query: { type?: string; search?: string }) {
    const search = query.search?.trim();
    return this.prisma.partner.findMany({
      where: {
        type: query.type as any,
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

  create(data: PartnerDto) {
    return this.prisma.partner.create({ data });
  }

  async update(id: string, data: Partial<PartnerDto>) {
    const partner = await this.prisma.partner.findUnique({ where: { id } });
    if (!partner) throw new NotFoundException("Cadastro não encontrado");
    return this.prisma.partner.update({ where: { id }, data });
  }

  async remove(id: string) {
    const partner = await this.prisma.partner.findUnique({ where: { id } });
    if (!partner) throw new NotFoundException("Cadastro não encontrado");
    return this.prisma.partner.delete({ where: { id } });
  }
}
