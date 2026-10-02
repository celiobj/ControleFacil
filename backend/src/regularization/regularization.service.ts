import { Inject, Injectable, NotFoundException } from "@nestjs/common";
import { Prisma } from "@prisma/client";
import { PrismaService } from "../prisma.service";
import {
  CreateRegularizationDto,
  CreateRegularizationTaskDto,
  UpdateRegularizationDto,
  UpdateRegularizationTaskDto,
} from "./dto/regularization.dto";

const defaultTasks = [
  ["CAIXA", "Receber documentação da CAIXA"],
  ["DOCUMENTACAO", "Conferir termo de arrematação"],
  ["DOCUMENTACAO", "Solicitar documentação necessária"],
  ["ITBI", "Emitir ou calcular ITBI"],
  ["ITBI", "Pagar ITBI"],
  ["CARTORIO", "Elaborar escritura quando aplicável"],
  ["CARTORIO", "Assinar escritura ou contrato"],
  ["CARTORIO", "Protocolar no Registro de Imóveis"],
  ["CARTORIO", "Acompanhar protocolo"],
  ["CARTORIO", "Obter matrícula atualizada"],
  ["JURIDICO", "Conferir titularidade"],
  ["PREFEITURA", "Atualizar cadastro municipal"],
  ["CONDOMINIO", "Atualizar cadastro do condomínio"],
  ["DOCUMENTACAO", "Arquivar documentação final"],
] as const;

const regularizationInclude = {
  tasks: { orderBy: { sortOrder: "asc" } },
} as const;

@Injectable()
export class RegularizationService {
  constructor(@Inject(PrismaService) private readonly prisma: PrismaService) {}

  async findForProperty(propertyId: string) {
    await this.ensureProperty(propertyId);
    return this.prisma.propertyRegularization.findUnique({
      where: { propertyId },
      include: regularizationInclude,
    });
  }

  async createForProperty(propertyId: string, dto: CreateRegularizationDto) {
    await this.ensureProperty(propertyId);
    const regularization = await this.prisma.propertyRegularization.upsert({
      where: { propertyId },
      create: { propertyId, ...dto },
      update: dto,
    });
    await this.addMissingDefaults(regularization.id);
    return this.prisma.propertyRegularization.findUniqueOrThrow({
      where: { id: regularization.id },
      include: regularizationInclude,
    });
  }

  async update(id: string, dto: UpdateRegularizationDto) {
    try {
      return await this.prisma.propertyRegularization.update({
        where: { id },
        data: dto,
        include: regularizationInclude,
      });
    } catch (error) {
      if (this.isPrismaError(error, "P2025")) {
        throw new NotFoundException("Regularização não encontrada");
      }
      throw error;
    }
  }

  async findTasksForProperty(propertyId: string) {
    const regularization = await this.findForProperty(propertyId);
    return regularization?.tasks ?? [];
  }

  async addTask(propertyId: string, dto: CreateRegularizationTaskDto) {
    const regularization = await this.createForProperty(propertyId, {});
    const last = await this.prisma.regularizationTask.aggregate({
      where: { regularizationId: regularization.id },
      _max: { sortOrder: true },
    });
    return this.prisma.regularizationTask.create({
      data: {
        ...dto,
        regularizationId: regularization.id,
        sortOrder: (last._max.sortOrder ?? -1) + 1,
      },
    });
  }

  async updateTask(id: string, dto: UpdateRegularizationTaskDto) {
    const data: Prisma.RegularizationTaskUpdateInput = {
      ...dto,
      completedAt:
        dto.status === "COMPLETED"
          ? new Date()
          : dto.status
            ? null
            : undefined,
    };
    try {
      return await this.prisma.regularizationTask.update({
        where: { id },
        data,
      });
    } catch (error) {
      if (this.isPrismaError(error, "P2025")) {
        throw new NotFoundException("Tarefa de regularização não encontrada");
      }
      throw error;
    }
  }

  private async addMissingDefaults(regularizationId: string) {
    const existing = await this.prisma.regularizationTask.findMany({
      where: { regularizationId },
      select: { title: true, sortOrder: true },
    });
    const titles = new Set(existing.map((task) => task.title));
    const maxSortOrder = existing.reduce(
      (maximum, task) => Math.max(maximum, task.sortOrder),
      -1,
    );
    const missing = defaultTasks.filter(([, title]) => !titles.has(title));
    if (!missing.length) return;
    await this.prisma.regularizationTask.createMany({
      data: missing.map(([category, title], index) => ({
        regularizationId,
        category,
        title,
        status: "PENDING",
        sortOrder: maxSortOrder + index + 1,
      })),
    });
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