import {
  BadRequestException,
  ConflictException,
  Inject,
  Injectable,
  NotFoundException,
} from "@nestjs/common";
import { AcquisitionStatus, Prisma } from "@prisma/client";
import { PrismaService } from "../prisma.service";
import { CreateAcquisitionDto, UpdateAcquisitionDto } from "./dto/acquisition.dto";

const acquisitionInclude = {
  property: { include: { auction: true } },
} as const;

@Injectable()
export class AcquisitionsService {
  constructor(@Inject(PrismaService) private readonly prisma: PrismaService) {}

  findAll(propertyId?: string) {
    return this.prisma.acquisition.findMany({
      where: propertyId ? { propertyId } : undefined,
      orderBy: { createdAt: "desc" },
      include: acquisitionInclude,
    });
  }

  async findForProperty(propertyId: string) {
    const property = await this.prisma.property.findUnique({
      where: { id: propertyId },
      select: { id: true },
    });
    if (!property) throw new NotFoundException("Imóvel não encontrado");
    return this.prisma.acquisition.findUnique({
      where: { propertyId },
      include: acquisitionInclude,
    });
  }

  async findOne(id: string) {
    const acquisition = await this.prisma.acquisition.findUnique({
      where: { id },
      include: acquisitionInclude,
    });
    if (!acquisition) throw new NotFoundException("Aquisição não encontrada");
    return acquisition;
  }

  async create(dto: CreateAcquisitionDto) {
    this.assertFundingTotals(dto);
    try {
      return await this.prisma.acquisition.create({
        data: {
          propertyId: dto.propertyId,
          purchasePrice: dto.purchasePrice,
          auctioneerCommission: dto.auctioneerCommission,
          ownResourcesAmount: dto.ownResourcesAmount,
          fgtsAmount: dto.fgtsAmount,
          financingAmount: dto.financingAmount,
          otherResourcesAmount: dto.otherResourcesAmount,
          purchaseDate: dto.purchaseDate,
          acquisitionDate: dto.acquisitionDate,
          paymentDate: dto.paymentDate,
          paymentMethod: dto.paymentMethod,
          status: dto.status,
          source: dto.source,
          notes: dto.notes,
        },
        include: acquisitionInclude,
      });
    } catch (error) {
      if (this.isPrismaError(error, "P2002")) {
        throw new ConflictException("Já existe uma aquisição para este imóvel");
      }
      if (this.isPrismaError(error, "P2003")) {
        throw new NotFoundException("Imóvel não encontrado");
      }
      throw error;
    }
  }

  async update(id: string, dto: UpdateAcquisitionDto) {
    const current = await this.findOne(id);
    this.assertFundingTotals({
      status: dto.status ?? current.status,
      purchasePrice: dto.purchasePrice ?? current.purchasePrice?.toNumber() ?? null,
      ownResourcesAmount:
        dto.ownResourcesAmount ?? current.ownResourcesAmount?.toNumber() ?? null,
      fgtsAmount: dto.fgtsAmount ?? current.fgtsAmount?.toNumber() ?? null,
      financingAmount:
        dto.financingAmount ?? current.financingAmount?.toNumber() ?? null,
      otherResourcesAmount:
        dto.otherResourcesAmount ?? current.otherResourcesAmount?.toNumber() ?? null,
    });
    const data: Prisma.AcquisitionUpdateInput = {
      ...(dto.propertyId === undefined ? {} : { property: { connect: { id: dto.propertyId } } }),
      ...(dto.purchasePrice === undefined ? {} : { purchasePrice: dto.purchasePrice }),
      ...(dto.auctioneerCommission === undefined ? {} : { auctioneerCommission: dto.auctioneerCommission }),
      ...(dto.ownResourcesAmount === undefined ? {} : { ownResourcesAmount: dto.ownResourcesAmount }),
      ...(dto.fgtsAmount === undefined ? {} : { fgtsAmount: dto.fgtsAmount }),
      ...(dto.financingAmount === undefined ? {} : { financingAmount: dto.financingAmount }),
      ...(dto.otherResourcesAmount === undefined ? {} : { otherResourcesAmount: dto.otherResourcesAmount }),
      ...(dto.purchaseDate === undefined ? {} : { purchaseDate: dto.purchaseDate }),
      ...(dto.acquisitionDate === undefined ? {} : { acquisitionDate: dto.acquisitionDate }),
      ...(dto.paymentDate === undefined ? {} : { paymentDate: dto.paymentDate }),
      ...(dto.paymentMethod === undefined ? {} : { paymentMethod: dto.paymentMethod }),
      ...(dto.status === undefined ? {} : { status: dto.status }),
      ...(dto.source === undefined ? {} : { source: dto.source }),
      ...(dto.notes === undefined ? {} : { notes: dto.notes }),
    };
    try {
      return await this.prisma.acquisition.update({
        where: { id },
        data,
        include: acquisitionInclude,
      });
    } catch (error) {
      if (this.isPrismaError(error, "P2025")) {
        throw new NotFoundException("Aquisição não encontrada");
      }
      if (this.isPrismaError(error, "P2002")) {
        throw new ConflictException("Já existe uma aquisição para este imóvel");
      }
      if (this.isPrismaError(error, "P2003")) {
        throw new NotFoundException("Imóvel não encontrado");
      }
      throw error;
    }
  }

  private assertFundingTotals(data: {
    status?: AcquisitionStatus;
    purchasePrice?: number | null;
    ownResourcesAmount?: number | null;
    fgtsAmount?: number | null;
    financingAmount?: number | null;
    otherResourcesAmount?: number | null;
  }) {
    if (data.status !== AcquisitionStatus.COMPLETED) return;
    if (data.purchasePrice == null) {
      throw new BadRequestException(
        "Informe o valor da aquisição antes de concluí-la",
      );
    }
    const fundingTotal = [
      data.ownResourcesAmount,
      data.fgtsAmount,
      data.financingAmount,
      data.otherResourcesAmount,
    ].reduce(
      (total, amount) => total.plus(amount ?? 0),
      new Prisma.Decimal(0),
    );
    if (!fundingTotal.equals(new Prisma.Decimal(data.purchasePrice))) {
      throw new BadRequestException(
        "A soma das fontes de recursos deve ser igual ao valor da aquisição",
      );
    }
  }

  private isPrismaError(error: unknown, code: string): error is Prisma.PrismaClientKnownRequestError {
    return (
      (error instanceof Prisma.PrismaClientKnownRequestError ||
        (typeof error === "object" && error !== null && "code" in error)) &&
      (error as { code?: string }).code === code
    );
  }
}
