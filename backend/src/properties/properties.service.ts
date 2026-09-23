import {
  BadRequestException,
  Inject,
  Injectable,
  NotFoundException,
} from "@nestjs/common";
import { PrismaService } from "../prisma.service";
import { ChecklistsService } from "../checklists/checklists.service";
import { PropertyDto } from "./dto/property.dto";

@Injectable()
export class PropertiesService {
  constructor(
    @Inject(PrismaService) private readonly prisma: PrismaService,
    @Inject(ChecklistsService) private readonly checklists: ChecklistsService,
  ) {}

  private async generateCode(): Promise<string> {
    const properties = await this.prisma.property.findMany({
      select: { code: true },
      orderBy: { createdAt: "desc" },
      take: 200,
    });

    const highestNumber = properties.reduce((max, item) => {
      const match = item.code?.match(/(\d+)/);
      if (!match) return max;
      const value = Number(match[0]);
      return Number.isFinite(value) ? Math.max(max, value) : max;
    }, 0);

    let nextNumber = highestNumber + 1;
    let code = `CF-${String(nextNumber).padStart(4, "0")}`;

    while (
      await this.prisma.property
        .findUnique({ where: { code } })
        .catch(() => null)
    ) {
      nextNumber += 1;
      code = `CF-${String(nextNumber).padStart(4, "0")}`;
    }

    return code;
  }

  findAll(query: {
    city?: string;
    neighborhood?: string;
    status?: any;
    type?: any;
    page?: number;
    limit?: number;
  }) {
    const page = Math.max(1, Number(query.page ?? 1));
    const limit = Math.min(100, Math.max(1, Number(query.limit ?? 20)));
    const where = {
      city: query.city
        ? { contains: query.city, mode: "insensitive" as const }
        : undefined,
      neighborhood: query.neighborhood
        ? { contains: query.neighborhood, mode: "insensitive" as const }
        : undefined,
      status: query.status,
      type: query.type,
    };
    return Promise.all([
      this.prisma.property.findMany({
        where,
        skip: (page - 1) * limit,
        take: limit,
        include: { auction: true, sale: true },
        orderBy: { createdAt: "desc" },
      }),
      this.prisma.property.count({ where }),
    ]).then(([data, total]) => ({
      data,
      meta: { page, limit, total, pages: Math.ceil(total / limit) },
    }));
  }
  async findOne(id: string) {
    const item = await this.prisma.property.findUnique({
      where: { id },
      include: { auction: true, expenses: true, renovations: true, sale: true },
    });
    if (!item) throw new NotFoundException("Imóvel não encontrado");
    return item;
  }
  async create(data: PropertyDto) {
    const { code: _ignoredCode, ...propertyData } = data;
    const property = await this.prisma.property.create({
      data: {
        ...propertyData,
        code: await this.generateCode(),
      },
    });
    await this.checklists.ensure(property.id);
    return property;
  }
  async update(id: string, data: Partial<PropertyDto>) {
    await this.findOne(id);
    const editable = {
      title: data.title,
      type: data.type,
      address: data.address,
      number: data.number,
      complement: data.complement,
      neighborhood: data.neighborhood,
      city: data.city,
      state: data.state,
      zipCode: data.zipCode,
      totalArea: data.totalArea,
      builtArea: data.builtArea,
      registryNumber: data.registryNumber,
      description: data.description,
      status: data.status,
      notes: data.notes,
    };
    for (const field of ["totalArea", "builtArea"] as const) {
      const value = editable[field];
      const rawValue: unknown = value;
      const numericValue =
        typeof rawValue === "string"
          ? Number(rawValue.replace(",", "."))
          : rawValue;
      if (numericValue !== undefined && !Number.isFinite(Number(numericValue))) {
        throw new BadRequestException(`${field} deve ser um número válido`);
      }
      if (typeof value === "string") {
        editable[field] = numericValue as never;
      }
    }
    return this.prisma.property.update({ where: { id }, data: editable });
  }
  async remove(id: string) {
    await this.findOne(id);
    return this.prisma.property.update({
      where: { id },
      data: { status: "CANCELADO" },
    });
  }
}
