import {
  BadRequestException,
  Inject,
  Injectable,
  NotFoundException,
} from "@nestjs/common";
import { Prisma } from "@prisma/client";
import { randomUUID } from "node:crypto";
import { access, mkdir, rm, writeFile } from "node:fs/promises";
import { basename, extname, isAbsolute, relative, resolve, sep } from "node:path";
import { PrismaService } from "../prisma.service";
import { CreateDocumentDto, UpdateDocumentDto } from "./dto/document.dto";

@Injectable()
export class DocumentsService {
  constructor(@Inject(PrismaService) private readonly prisma: PrismaService) {}

  async findForProperty(propertyId: string) {
    await this.ensurePropertyExists(propertyId);
    return this.prisma.propertyDocument.findMany({
      where: { propertyId },
      orderBy: { uploadedAt: "desc" },
    });
  }

  async create(
    propertyId: string,
    dto: CreateDocumentDto,
    file?: Express.Multer.File,
  ) {
    await this.ensurePropertyExists(propertyId);
    if (!file) throw new BadRequestException("Selecione um arquivo");

    const fileName = this.safeOriginalName(file.originalname);
    const extension = extname(fileName).toLowerCase();
    const diskName = `${randomUUID()}${/^\.[a-z0-9]{1,10}$/.test(extension) ? extension : ""}`;
    const uploadDirectory = this.uploadDirectory();
    const propertyDirectory = this.safeResolve(
      uploadDirectory,
      "properties",
      propertyId,
      "documents",
    );
    const absolutePath = this.safeResolve(propertyDirectory, diskName);

    await mkdir(propertyDirectory, { recursive: true });
    await writeFile(absolutePath, file.buffer, { flag: "wx" });
    try {
      return await this.prisma.propertyDocument.create({
        data: {
          propertyId,
          type: dto.type,
          title: dto.title,
          status: dto.status,
          documentDate: dto.documentDate,
          notes: dto.description,
          fileName,
          filePath: relative(uploadDirectory, absolutePath).split(sep).join("/"),
          mimeType: file.mimetype,
          fileSize: file.size,
        },
      });
    } catch (error) {
      await rm(absolutePath, { force: true });
      throw error;
    }
  }

  async update(id: string, dto: UpdateDocumentDto) {
    try {
      return await this.prisma.propertyDocument.update({
        where: { id },
        data: {
          type: dto.type,
          title: dto.title,
          status: dto.status,
          documentDate: dto.documentDate,
          notes: dto.description,
        },
      });
    } catch (error) {
      if (this.isPrismaError(error, "P2025")) {
        throw new NotFoundException("Documento não encontrado");
      }
      throw error;
    }
  }

  async getDownload(id: string) {
    const document = await this.prisma.propertyDocument.findUnique({
      where: { id },
    });
    if (!document?.filePath) throw new NotFoundException("Arquivo não encontrado");
    const absolutePath = this.safeResolve(this.uploadDirectory(), document.filePath);
    try {
      await access(absolutePath);
    } catch {
      throw new NotFoundException("Arquivo não encontrado no armazenamento");
    }
    return {
      path: absolutePath,
      fileName: document.fileName ?? document.title,
      mimeType: document.mimeType ?? "application/octet-stream",
    };
  }

  async delete(id: string) {
    const document = await this.prisma.propertyDocument.findUnique({ where: { id } });
    if (!document) throw new NotFoundException("Documento não encontrado");
    if (document.filePath) {
      const absolutePath = this.safeResolve(this.uploadDirectory(), document.filePath);
      await rm(absolutePath, { force: true });
    }
    await this.prisma.propertyDocument.delete({ where: { id } });
    return { deleted: true };
  }

  private uploadDirectory() {
    return resolve(process.env.UPLOAD_DIR ?? "./uploads");
  }

  private safeResolve(root: string, ...segments: string[]) {
    const target = resolve(root, ...segments);
    const rel = relative(root, target);
    if (!rel || rel.startsWith(`..${sep}`) || rel === ".." || isAbsolute(rel)) {
      throw new BadRequestException("Caminho de arquivo inválido");
    }
    return target;
  }

  private safeOriginalName(name: string) {
    const leaf = basename(name.replace(/\\/g, "/"));
    const safe = leaf.replace(/[<>:"|?*\u0000-\u001f]/g, "_").trim();
    return safe.slice(0, 255) || "documento";
  }

  private async ensurePropertyExists(propertyId: string) {
    const property = await this.prisma.property.findUnique({
      where: { id: propertyId },
      select: { id: true },
    });
    if (!property) throw new NotFoundException("Imóvel não encontrado");
  }

  private isPrismaError(
    error: unknown,
    code: string,
  ): error is Prisma.PrismaClientKnownRequestError {
    return (
      (error instanceof Prisma.PrismaClientKnownRequestError ||
        (typeof error === "object" && error !== null && "code" in error)) &&
      (error as { code?: string }).code === code
    );
  }
}