import { Inject, Injectable, NotFoundException } from "@nestjs/common";
import { Prisma } from "@prisma/client";
import { PrismaService } from "../prisma.service";
import { CreatePossessionDto, UpdatePossessionDto } from "./dto/possession.dto";

const occupiedAlert = "Imóvel ocupado. Avaliar procedimento com advogado especializado.";

@Injectable()
export class PossessionService {
  constructor(@Inject(PrismaService) private readonly prisma: PrismaService) {}

  async findForProperty(propertyId: string) {
    await this.ensureProperty(propertyId);
    const possession = await this.prisma.propertyPossession.findUnique({
      where: { propertyId },
    });
    return this.withOccupationAlert(possession);
  }

  async createForProperty(propertyId: string, dto: CreatePossessionDto) {
    await this.ensureProperty(propertyId);
    const possession = await this.prisma.propertyPossession.upsert({
      where: { propertyId },
      create: { propertyId, ...dto },
      update: dto,
    });
    return this.withOccupationAlert(possession);
  }

  async update(id: string, dto: UpdatePossessionDto) {
    try {
      const possession = await this.prisma.propertyPossession.update({
        where: { id },
        data: dto,
      });
      return this.withOccupationAlert(possession);
    } catch (error) {
      if (this.isPrismaError(error, "P2025")) {
        throw new NotFoundException("Registro de posse não encontrado");
      }
      throw error;
    }
  }

  private withOccupationAlert<T extends { occupationStatus?: string | null } | null>(
    possession: T,
  ) {
    if (!possession) return null;
    return {
      ...possession,
      alert: possession.occupationStatus === "OCCUPIED" ? occupiedAlert : null,
    };
  }

  private async ensureProperty(propertyId: string) {
    const property = await this.prisma.property.findUnique({
      where: { id: propertyId },
      select: { id: true },
    });
    if (!property) throw new NotFoundException("Imóvel não encontrado");
  }

  private isPrismaError(error: unknown, code: string) {
    return (
      (error instanceof Prisma.PrismaClientKnownRequestError ||
        (typeof error === "object" && error !== null && "code" in error)) &&
      (error as { code?: string }).code === code
    );
  }
}