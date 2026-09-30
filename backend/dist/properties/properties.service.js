"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
var PropertiesService_1;
Object.defineProperty(exports, "__esModule", { value: true });
exports.PropertiesService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma.service");
const checklists_service_1 = require("../checklists/checklists.service");
let PropertiesService = PropertiesService_1 = class PropertiesService {
    prisma;
    checklists;
    logger = new common_1.Logger(PropertiesService_1.name);
    expirationTimer;
    constructor(prisma, checklists) {
        this.prisma = prisma;
        this.checklists = checklists;
    }
    onModuleInit() {
        this.runNegotiationDeadlineCheck();
        this.expirationTimer = setInterval(() => this.runNegotiationDeadlineCheck(), 60_000);
        this.expirationTimer.unref();
    }
    onModuleDestroy() {
        if (this.expirationTimer)
            clearInterval(this.expirationTimer);
    }
    runNegotiationDeadlineCheck() {
        void this.cancelExpiredNegotiations().catch((error) => {
            this.logger.error("Falha ao cancelar imóveis com negociação vencida", error instanceof Error ? error.stack : String(error));
        });
    }
    async cancelExpiredNegotiations() {
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        return this.prisma.$transaction(async (transaction) => {
            const expiredProperties = await transaction.property.findMany({
                where: {
                    status: "EM_ANALISE",
                    negotiationDeadline: { lt: today },
                },
                select: { id: true },
            });
            let count = 0;
            for (const property of expiredProperties) {
                const result = await transaction.property.updateMany({
                    where: { id: property.id, status: "EM_ANALISE" },
                    data: { status: "CANCELADO" },
                });
                if (!result.count)
                    continue;
                count += result.count;
                await transaction.propertyStatusHistory.create({
                    data: {
                        propertyId: property.id,
                        fromStatus: "EM_ANALISE",
                        toStatus: "CANCELADO",
                    },
                });
            }
            return { count };
        });
    }
    async generateCode() {
        const properties = await this.prisma.property.findMany({
            select: { code: true },
            orderBy: { createdAt: "desc" },
            take: 200,
        });
        const highestNumber = properties.reduce((max, item) => {
            const match = item.code?.match(/(\d+)/);
            if (!match)
                return max;
            const value = Number(match[0]);
            return Number.isFinite(value) ? Math.max(max, value) : max;
        }, 0);
        let nextNumber = highestNumber + 1;
        let code = `CF-${String(nextNumber).padStart(4, "0")}`;
        while (await this.prisma.property
            .findUnique({ where: { code } })
            .catch(() => null)) {
            nextNumber += 1;
            code = `CF-${String(nextNumber).padStart(4, "0")}`;
        }
        return code;
    }
    findAll(query) {
        const page = Math.max(1, Number(query.page ?? 1));
        const limit = Math.min(100, Math.max(1, Number(query.limit ?? 20)));
        const where = {
            city: query.city
                ? { contains: query.city, mode: "insensitive" }
                : undefined,
            neighborhood: query.neighborhood
                ? { contains: query.neighborhood, mode: "insensitive" }
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
    importFromCaixaText(text) {
        if (typeof text !== "string" || !text.trim()) {
            throw new common_1.BadRequestException("Cole o texto do anúncio da CAIXA.");
        }
        if (text.length > 30_000) {
            throw new common_1.BadRequestException("O texto colado excede o limite permitido.");
        }
        const normalizedText = text
            .replace(/\r\n?/g, "\n")
            .replace(/\[([^\]]+)\]\([^)]+\)/g, "$1\n")
            .replace(/\*\*/g, "")
            .replace(/^[ \t]*#{1,6}[ \t]*/gm, "")
            .replace(/[ \t]+/g, " ")
            .replace(/[ \t]*\n[ \t]*/g, "\n")
            .trim();
        const lines = normalizedText.split("\n").map((line) => line.trim());
        const title = lines.find(Boolean) ?? "";
        const valueAfterLabel = (label) => {
            const normalizedLabel = label.toLocaleLowerCase("pt-BR");
            const line = lines.find((candidate) => candidate.toLocaleLowerCase("pt-BR").startsWith(`${normalizedLabel}:`));
            return line?.slice(line.indexOf(":") + 1).trim() ?? "";
        };
        const blockAfter = (label, stopLabels) => {
            const start = normalizedText.toLocaleLowerCase("pt-BR").indexOf(label.toLocaleLowerCase("pt-BR"));
            if (start < 0)
                return "";
            const valueStart = start + label.length;
            const remainder = normalizedText.slice(valueStart).replace(/^[\s:]+/, "");
            const stops = stopLabels
                .map((stopLabel) => {
                const match = new RegExp(`\\n\\s*${stopLabel}`, "i").exec(remainder);
                return match?.index ?? -1;
            })
                .filter((index) => index >= 0);
            const end = stops.length ? Math.min(...stops) : remainder.length;
            return remainder.slice(0, end).trim();
        };
        const propertyType = valueAfterLabel("Tipo de imóvel").toLocaleLowerCase("pt-BR");
        const type = propertyType.includes("apartamento")
            ? "APARTAMENTO"
            : propertyType.includes("casa")
                ? "CASA"
                : propertyType.includes("terreno")
                    ? "TERRENO"
                    : propertyType.includes("rural")
                        ? "RURAL"
                        : propertyType.includes("comercial")
                            ? "COMERCIAL"
                            : "OUTRO";
        const negotiationOptions = [
            ["Leilão SFI", "LEILAO_SFI"],
            ["Exercício de Direito de Preferência", "EXERCICIO_DIREITO_PREFERENCIA"],
            ["Licitação Aberta", "LICITACAO_ABERTA"],
            ["Venda Online", "VENDA_ONLINE"],
            ["Compra Direta", "COMPRA_DIRETA"],
        ];
        const negotiationLine = lines.find((line) => negotiationOptions.some(([label]) => line.toLocaleLowerCase("pt-BR").startsWith(label.toLocaleLowerCase("pt-BR"))));
        const negotiationType = negotiationOptions.find(([label]) => negotiationLine
            ?.toLocaleLowerCase("pt-BR")
            .startsWith(label.toLocaleLowerCase("pt-BR")))?.[1] ?? "";
        const addressText = blockAfter("Endereço:", [
            "Baixar matrícula do imóvel",
            "Descrição:",
        ]).replace(/\nBaixar matrícula do imóvel\s*/i, "").trim();
        const addressParts = addressText.match(/^(.*?),\s*N\.\s*(\d+)(?:\s+(.*?))?,\s*(.*?)\s*-\s*CEP:\s*([\d.-]+),\s*(.*?)\s*-\s*(.*?)$/i);
        if (!title || !addressParts) {
            throw new common_1.BadRequestException("Não foi possível identificar o título e o endereço. Cole o texto completo do anúncio.");
        }
        const complement = addressParts[3]?.trim() ?? "";
        const propertyTitle = complement &&
            !title.toLocaleUpperCase("pt-BR").includes(complement.toLocaleUpperCase("pt-BR"))
            ? `${title} - ${complement}`
            : title;
        const stateNames = {
            acre: "AC",
            alagoas: "AL",
            amapa: "AP",
            amazonas: "AM",
            bahia: "BA",
            ceara: "CE",
            "distrito federal": "DF",
            "espirito santo": "ES",
            goias: "GO",
            maranhao: "MA",
            "mato grosso": "MT",
            "mato grosso do sul": "MS",
            "minas gerais": "MG",
            para: "PA",
            paraiba: "PB",
            parana: "PR",
            pernambuco: "PE",
            piaui: "PI",
            "rio de janeiro": "RJ",
            "rio grande do norte": "RN",
            "rio grande do sul": "RS",
            rondonia: "RO",
            roraima: "RR",
            "santa catarina": "SC",
            "sao paulo": "SP",
            sergipe: "SE",
            tocantins: "TO",
        };
        const normalizeText = (value) => value
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "")
            .toLocaleLowerCase("pt-BR")
            .trim();
        const decimalValue = (value) => value ? String(Number(value.replace(/\./g, "").replace(",", "."))) : "";
        const totalArea = normalizedText.match(/Área total\s*=\s*([\d.,]+)/i)?.[1];
        const privateArea = normalizedText.match(/Área privativa\s*=\s*([\d.,]+)/i)?.[1];
        const appraisal = valueAfterLabel("Valor de avaliação");
        const minimumSale = valueAfterLabel("Valor mínimo de venda");
        const paymentTerms = blockAfter("FORMAS DE PAGAMENTO ACEITAS:", []);
        const extraNotes = [
            valueAfterLabel("Número do imóvel") &&
                `Código CAIXA: ${valueAfterLabel("Número do imóvel")}`,
            appraisal && `Valor de avaliação: ${appraisal}`,
            minimumSale &&
                `Valor mínimo de venda: ${minimumSale.replace(/\(\s+/, "(")}`,
            valueAfterLabel("Quartos") && `Quartos: ${valueAfterLabel("Quartos")}`,
            valueAfterLabel("Comarca") && `Comarca: ${valueAfterLabel("Comarca")}`,
            valueAfterLabel("Ofício") && `Ofício: ${valueAfterLabel("Ofício")}`,
            valueAfterLabel("Inscrição imobiliária") &&
                `Inscrição imobiliária: ${valueAfterLabel("Inscrição imobiliária")}`,
            valueAfterLabel("Averbação dos leilões negativos") &&
                `Averbação dos leilões negativos: ${valueAfterLabel("Averbação dos leilões negativos")}`,
            privateArea && `Área privativa CAIXA: ${privateArea} m²`,
            paymentTerms && `Condições e despesas CAIXA: ${paymentTerms.replace(/\n+/g, " ")}`,
        ]
            .filter(Boolean)
            .join("\n");
        const description = blockAfter("Descrição:", [
            "FORMAS DE PAGAMENTO ACEITAS:",
        ]).replace(/(?:\.\s*)+$/, ".");
        return {
            title: propertyTitle,
            type,
            negotiationType,
            negotiationDeadline: "",
            address: addressParts[1].trim(),
            number: addressParts[2].trim(),
            complement,
            neighborhood: addressParts[4].trim(),
            zipCode: addressParts[5].trim(),
            city: addressParts[6].trim(),
            state: stateNames[normalizeText(addressParts[7])] ??
                (addressParts[7].length === 2 ? addressParts[7].toUpperCase() : ""),
            totalArea: decimalValue(totalArea),
            builtArea: decimalValue(privateArea),
            registryNumber: valueAfterLabel("Matrícula(s)"),
            description,
            status: "EM_ANALISE",
            notes: extraNotes,
        };
    }
    async findOne(id) {
        const item = await this.prisma.property.findUnique({
            where: { id },
            include: {
                auction: true,
                expenses: true,
                renovations: true,
                sale: true,
                statusHistory: { orderBy: { changedAt: "asc" } },
            },
        });
        if (!item)
            throw new common_1.NotFoundException("Imóvel não encontrado");
        return item;
    }
    async create(data) {
        const { code: _ignoredCode, ...propertyData } = data;
        const code = await this.generateCode();
        const property = await this.prisma.$transaction(async (transaction) => {
            const created = await transaction.property.create({
                data: { ...propertyData, code },
            });
            await transaction.propertyStatusHistory.create({
                data: {
                    propertyId: created.id,
                    toStatus: created.status,
                },
            });
            return created;
        });
        await this.checklists.ensure(property.id);
        return property;
    }
    async update(id, data) {
        const current = await this.findOne(id);
        const rawDeadline = data.negotiationDeadline;
        const negotiationDeadline = rawDeadline === "" || rawDeadline === null
            ? null
            : typeof rawDeadline === "string"
                ? new Date(`${rawDeadline}T00:00:00.000Z`)
                : rawDeadline;
        if (negotiationDeadline instanceof Date &&
            Number.isNaN(negotiationDeadline.getTime())) {
            throw new common_1.BadRequestException("negotiationDeadline deve ser uma data válida");
        }
        const editable = {
            title: data.title,
            type: data.type,
            negotiationType: data.negotiationType,
            negotiationDeadline,
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
        for (const field of ["totalArea", "builtArea"]) {
            const value = editable[field];
            const rawValue = value;
            const numericValue = typeof rawValue === "string"
                ? Number(rawValue.replace(",", "."))
                : rawValue;
            if (numericValue !== undefined && !Number.isFinite(Number(numericValue))) {
                throw new common_1.BadRequestException(`${field} deve ser um número válido`);
            }
            if (typeof value === "string") {
                editable[field] = numericValue;
            }
        }
        return this.prisma.$transaction(async (transaction) => {
            const updated = await transaction.property.update({
                where: { id },
                data: editable,
            });
            if (data.status !== undefined && data.status !== current.status) {
                await transaction.propertyStatusHistory.create({
                    data: {
                        propertyId: id,
                        fromStatus: current.status,
                        toStatus: data.status,
                    },
                });
            }
            return updated;
        });
    }
    async remove(id) {
        await this.findOne(id);
        return this.update(id, { status: "CANCELADO" });
    }
};
exports.PropertiesService = PropertiesService;
exports.PropertiesService = PropertiesService = PropertiesService_1 = __decorate([
    (0, common_1.Injectable)(),
    __param(0, (0, common_1.Inject)(prisma_service_1.PrismaService)),
    __param(1, (0, common_1.Inject)(checklists_service_1.ChecklistsService)),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService,
        checklists_service_1.ChecklistsService])
], PropertiesService);
//# sourceMappingURL=properties.service.js.map