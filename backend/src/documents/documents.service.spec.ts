import { BadRequestException } from "@nestjs/common";
import { mkdtemp, readFile, rm } from "node:fs/promises";
import { tmpdir } from "node:os";
import { resolve } from "node:path";
import { DocumentsService } from "./documents.service";

describe("DocumentsService", () => {
  it("stores uploads under UPLOAD_DIR using a generated disk name", async () => {
    const uploadDirectory = await mkdtemp(resolve(tmpdir(), "controle-facil-"));
    const previousUploadDirectory = process.env.UPLOAD_DIR;
    process.env.UPLOAD_DIR = uploadDirectory;
    const create = jest.fn(({ data }) => Promise.resolve(data));
    const prisma = {
      property: { findUnique: jest.fn().mockResolvedValue({ id: "property-1" }) },
      propertyDocument: { create },
    } as any;
    const service = new DocumentsService(prisma);

    try {
      const result = await service.create(
        "property-1",
        { type: "OTHER", title: "Matrícula" } as any,
        {
          originalname: "..\\outside.pdf",
          mimetype: "application/pdf",
          size: 4,
          buffer: Buffer.from("test"),
        } as Express.Multer.File,
      );

      expect(result.fileName).toBe("outside.pdf");
      expect(result.filePath).toMatch(/^properties\/property-1\/documents\//);
      expect(result.filePath).not.toContain("outside.pdf");
      if (!result.filePath) throw new Error("Arquivo não foi persistido");
      const savedPath = resolve(uploadDirectory, ...result.filePath.split("/"));
      await expect(readFile(savedPath)).resolves.toEqual(Buffer.from("test"));
    } finally {
      if (previousUploadDirectory === undefined) delete process.env.UPLOAD_DIR;
      else process.env.UPLOAD_DIR = previousUploadDirectory;
      await rm(uploadDirectory, { recursive: true, force: true });
    }
  });

  it("rejects a property path traversal before persisting an upload", async () => {
    const create = jest.fn();
    const prisma = {
      property: { findUnique: jest.fn().mockResolvedValue({ id: "../../outside" }) },
      propertyDocument: { create },
    } as any;
    const service = new DocumentsService(prisma);

    await expect(
      service.create(
        "../../outside",
        { type: "OTHER", title: "Documento" } as any,
        {
          originalname: "documento.pdf",
          mimetype: "application/pdf",
          size: 1,
          buffer: Buffer.from("x"),
        } as Express.Multer.File,
      ),
    ).rejects.toBeInstanceOf(BadRequestException);

    expect(create).not.toHaveBeenCalled();
  });
});