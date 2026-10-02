import { Inject, Injectable, NotFoundException } from "@nestjs/common";
import { PrismaService } from "../prisma.service";
import { CreateEventDto } from "./dto/event.dto";

@Injectable()
export class EventsService {
  constructor(@Inject(PrismaService) private readonly prisma: PrismaService) {}

  async findForProperty(propertyId: string) {
    await this.ensureProperty(propertyId);
    return this.prisma.propertyEvent.findMany({
      where: { propertyId },
      orderBy: [{ occurredAt: "desc" }, { createdAt: "desc" }],
      include: { user: { select: { id: true, name: true } } },
    });
  }

  async create(
    propertyId: string,
    dto: CreateEventDto,
    userId?: string,
  ) {
    await this.ensureProperty(propertyId);
    return this.prisma.propertyEvent.create({
      data: {
        propertyId,
        userId,
        type: dto.type,
        title: dto.title,
        description: dto.description,
        occurredAt: dto.eventDate,
      },
      include: { user: { select: { id: true, name: true } } },
    });
  }

  private async ensureProperty(propertyId: string) {
    const property = await this.prisma.property.findUnique({
      where: { id: propertyId },
      select: { id: true },
    });
    if (!property) throw new NotFoundException("Imóvel não encontrado");
  }
}