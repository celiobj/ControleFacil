import {
  Body,
  Controller,
  Delete,
  Get,
  Inject,
  Param,
  Patch,
  Post,
  Res,
  UploadedFile,
  UseGuards,
  UseInterceptors,
} from "@nestjs/common";
import {
  ApiBearerAuth,
  ApiBody,
  ApiConsumes,
  ApiOperation,
  ApiParam,
  ApiResponse,
  ApiTags,
} from "@nestjs/swagger";
import { FileInterceptor } from "@nestjs/platform-express";
import { Response } from "express";
import { JwtGuard } from "../auth/jwt.guard";
import { CreateDocumentDto, UpdateDocumentDto } from "./dto/document.dto";
import { DocumentsService } from "./documents.service";

@ApiTags("documents")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller()
export class DocumentsController {
  constructor(
    @Inject(DocumentsService) private readonly service: DocumentsService,
  ) {}

  @Get("properties/:propertyId/documents")
  @ApiOperation({ summary: "Lista documentos do imóvel" })
  @ApiParam({ name: "propertyId", format: "uuid" })
  @ApiResponse({ status: 200, description: "Documentos cadastrados" })
  findForProperty(@Param("propertyId") propertyId: string) {
    return this.service.findForProperty(propertyId);
  }

  @Post("properties/:propertyId/documents")
  @UseInterceptors(FileInterceptor("file", { limits: { fileSize: 25 * 1024 * 1024 } }))
  @ApiOperation({ summary: "Envia um documento para o imóvel" })
  @ApiConsumes("multipart/form-data")
  @ApiParam({ name: "propertyId", format: "uuid" })
  @ApiBody({
    schema: {
      type: "object",
      required: ["file", "type", "title"],
      properties: {
        file: { type: "string", format: "binary" },
        type: { type: "string" },
        title: { type: "string" },
        documentDate: { type: "string", format: "date-time" },
        status: { type: "string" },
        description: { type: "string" },
      },
    },
  })
  @ApiResponse({ status: 201, description: "Documento armazenado" })
  create(
    @Param("propertyId") propertyId: string,
    @Body() dto: CreateDocumentDto,
    @UploadedFile() file?: Express.Multer.File,
  ) {
    return this.service.create(propertyId, dto, file);
  }

  @Get("documents/:id/download")
  @ApiOperation({ summary: "Baixa um documento" })
  @ApiParam({ name: "id", format: "uuid" })
  async download(@Param("id") id: string, @Res() response: Response) {
    const file = await this.service.getDownload(id);
    response.type(file.mimeType).download(file.path, file.fileName);
  }

  @Patch("documents/:id")
  @ApiOperation({ summary: "Atualiza metadados ou status do documento" })
  @ApiParam({ name: "id", format: "uuid" })
  update(@Param("id") id: string, @Body() dto: UpdateDocumentDto) {
    return this.service.update(id, dto);
  }

  @Delete("documents/:id")
  @ApiOperation({ summary: "Remove um documento e seu arquivo" })
  @ApiParam({ name: "id", format: "uuid" })
  delete(@Param("id") id: string) {
    return this.service.delete(id);
  }
}