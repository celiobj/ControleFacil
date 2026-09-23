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
Object.defineProperty(exports, "__esModule", { value: true });
exports.ChecklistsService = void 0;
const common_1 = require("@nestjs/common");
const prisma_service_1 = require("../prisma.service");
const checklistRows = [
    [
        "ANTES DO LANCE",
        "Conferência",
        "Conferir número do imóvel CAIXA",
        "Comprador",
    ],
    [
        "ANTES DO LANCE",
        "Conferência",
        "Conferir matrícula, endereço e áreas",
        "Comprador",
    ],
    ["ANTES DO LANCE", "Jurídico", "Solicitar matrícula atualizada", "Advogado"],
    [
        "ANTES DO LANCE",
        "Jurídico",
        "Verificar penhoras, hipotecas, indisponibilidades e gravames",
        "Advogado",
    ],
    [
        "ANTES DO LANCE",
        "Jurídico",
        "Pesquisar processos relacionados ao imóvel e ocupante",
        "Advogado",
    ],
    [
        "ANTES DO LANCE",
        "Condomínio",
        "Solicitar declaração de débitos condominiais",
        "Comprador / Corretor",
    ],
    [
        "ANTES DO LANCE",
        "Condomínio",
        "Verificar extras extraordinárias e cobranças futuras",
        "Comprador / Corretor",
    ],
    [
        "ANTES DO LANCE",
        "Prefeitura",
        "Consultar IPTU e débitos municipais",
        "Comprador / Despachante",
    ],
    [
        "ANTES DO LANCE",
        "Prefeitura",
        "Conferir cadastro e área do imóvel",
        "Comprador / Despachante",
    ],
    ["ANTES DO LANCE", "Tributário", "Estimar ITBI", "Comprador / Despachante"],
    [
        "ANTES DO LANCE",
        "Vistoria",
        "Verificar estado físico do imóvel",
        "Engenheiro / Arquiteto",
    ],
    [
        "ANTES DO LANCE",
        "Vistoria",
        "Estimar custo da reforma",
        "Engenheiro / Arquiteto",
    ],
    [
        "ANTES DO LANCE",
        "Ocupação",
        "Confirmar se o imóvel está vazio ou ocupado",
        "Corretor / Advogado",
    ],
    [
        "ANTES DO LANCE",
        "Financeiro",
        "Levantar preço de mercado e preço provável de revenda",
        "Corretor",
    ],
    [
        "ANTES DO LANCE",
        "Financeiro",
        "Calcular investimento total projetado",
        "Comprador",
    ],
    [
        "ANTES DO LANCE",
        "Financeiro",
        "Definir lance ideal, máximo aceitável e limite absoluto",
        "Comprador",
    ],
    [
        "ANTES DO LANCE",
        "Financiamento/FGTS",
        "Confirmar previamente eventual financiamento/FGTS com CAIXA",
        "CAIXA / CCA",
    ],
    [
        "ARREMATAÇÃO",
        "Leilão",
        "Registrar comprovante do lance vencedor",
        "Comprador",
    ],
    [
        "ARREMATAÇÃO",
        "Documentação",
        "Guardar edital, termo e comunicações da arrematação",
        "Comprador",
    ],
    [
        "ARREMATAÇÃO",
        "Pagamento",
        "Pagar comissão de 5% ao leiloeiro",
        "Comprador",
    ],
    [
        "ARREMATAÇÃO",
        "CAIXA",
        "Acompanhar homologação da arrematação",
        "Comprador",
    ],
    [
        "ARREMATAÇÃO",
        "CAIXA",
        "Acessar portal CAIXA e selecionar forma de pagamento",
        "Comprador",
    ],
    [
        "PÓS-ARREMATAÇÃO",
        "CAIXA",
        "Selecionar agência de contratação",
        "Comprador",
    ],
    [
        "PÓS-ARREMATAÇÃO",
        "CAIXA",
        "Selecionar corretor/imobiliária credenciada CAIXA",
        "Comprador",
    ],
    [
        "PÓS-ARREMATAÇÃO",
        "Pagamento",
        "Emitir boleto conforme orientação da CAIXA",
        "Comprador / Corretor",
    ],
    [
        "PÓS-ARREMATAÇÃO",
        "Pagamento",
        "Pagar recursos próprios dentro do prazo do edital",
        "Comprador",
    ],
    [
        "PÓS-ARREMATAÇÃO",
        "Documentação",
        "Receber documentação da CAIXA",
        "Corretor / CAIXA",
    ],
    [
        "PÓS-ARREMATAÇÃO",
        "Escritura",
        "Contratar tabelião e providenciar escritura, quando aplicável",
        "Tabelião",
    ],
    [
        "PÓS-ARREMATAÇÃO",
        "ITBI",
        "Solicitar e pagar ITBI",
        "Comprador / Despachante",
    ],
    [
        "PÓS-ARREMATAÇÃO",
        "Registro",
        "Protocolar escritura/contrato no Registro de Imóveis",
        "Comprador / Despachante",
    ],
    [
        "PÓS-ARREMATAÇÃO",
        "Registro",
        "Obter matrícula atualizada com transferência para o comprador",
        "Cartório",
    ],
    [
        "PÓS-ARREMATAÇÃO",
        "Prefeitura",
        "Atualizar proprietário no cadastro municipal",
        "Comprador / Despachante",
    ],
    [
        "PÓS-ARREMATAÇÃO",
        "Condomínio",
        "Atualizar cadastro do proprietário no condomínio",
        "Comprador / Corretor",
    ],
    [
        "PÓS-ARREMATAÇÃO",
        "Imóvel vazio",
        "Receber chaves e realizar vistoria inicial",
        "Comprador / Corretor",
    ],
    [
        "POSSE",
        "Imóvel vazio",
        "Trocar fechaduras e assumir controle do imóvel",
        "Comprador",
    ],
    [
        "POSSE",
        "Imóvel ocupado",
        "Não realizar retirada por conta própria",
        "Comprador",
    ],
    [
        "POSSE",
        "Imóvel ocupado",
        "Analisar estratégia de desocupação com advogado",
        "Advogado",
    ],
    [
        "POSSE",
        "Imóvel ocupado",
        "Tentar solução amigável, se juridicamente adequada",
        "Advogado",
    ],
    [
        "POSSE",
        "Imóvel ocupado",
        "Ajuizar medida judicial, se necessária",
        "Advogado",
    ],
    [
        "REFORMA",
        "Planejamento",
        "Elaborar orçamento e escopo da reforma",
        "Engenheiro / Arquiteto",
    ],
    ["REFORMA", "Execução", "Contratar mão de obra", "Comprador"],
    ["REFORMA", "Execução", "Executar reforma", "Prestadores"],
    [
        "REFORMA",
        "Finalização",
        "Fazer limpeza e preparação para venda",
        "Prestadores",
    ],
    [
        "VENDA",
        "Mercado",
        "Atualizar avaliação de mercado após reforma",
        "Corretor",
    ],
    [
        "VENDA",
        "Financeiro",
        "Definir preço de anúncio e preço mínimo",
        "Comprador / Corretor",
    ],
    [
        "VENDA",
        "Tributário",
        "Calcular eventual ganho de capital e impostos",
        "Contador",
    ],
    ["VENDA", "Comercial", "Publicar imóvel e conduzir visitas", "Corretor"],
    ["VENDA", "Fechamento", "Vender e receber recursos", "Comprador / Corretor"],
    [
        "VENDA",
        "Fechamento",
        "Quitar eventual financiamento e calcular lucro líquido",
        "Comprador / Contador",
    ],
];
const defaultItems = checklistRows.map(([phase, stage, task, responsible], sortOrder) => ({
    phase,
    stage,
    task,
    responsible,
    category: stage,
    title: task,
    description: null,
    sortOrder,
}));
let ChecklistsService = class ChecklistsService {
    prisma;
    constructor(prisma) {
        this.prisma = prisma;
    }
    async ensure(propertyId) {
        const property = await this.prisma.property.findUnique({
            where: { id: propertyId },
        });
        if (!property)
            throw new common_1.NotFoundException("Imóvel não encontrado");
        let checklist = await this.prisma.propertyChecklist.findUnique({
            where: { propertyId },
            include: { items: { orderBy: { sortOrder: "asc" } } },
        });
        if (!checklist || checklist.items.length !== defaultItems.length) {
            if (checklist)
                await this.prisma.checklistItem.deleteMany({
                    where: { checklistId: checklist.id },
                });
            const checklistRecord = checklist ??
                (await this.prisma.propertyChecklist.create({ data: { propertyId } }));
            await this.prisma.checklistItem.createMany({
                data: defaultItems.map((item) => ({
                    ...item,
                    checklistId: checklistRecord.id,
                })),
            });
            checklist = await this.prisma.propertyChecklist.findUniqueOrThrow({
                where: { id: checklistRecord.id },
                include: { items: { orderBy: { sortOrder: "asc" } } },
            });
        }
        return this.withProgress(checklist);
    }
    withProgress(checklist) {
        const applicable = checklist.items.filter((item) => item.status !== "NAO_APLICAVEL");
        const completed = applicable.filter((item) => item.status === "CONCLUIDO").length;
        return {
            ...checklist,
            progress: {
                total: checklist.items.length,
                completed,
                percentage: applicable.length
                    ? Math.round((completed / applicable.length) * 100)
                    : 100,
            },
        };
    }
    async addItem(propertyId, data) {
        await this.ensure(propertyId);
        const checklist = await this.prisma.propertyChecklist.findUniqueOrThrow({
            where: { propertyId },
        });
        const last = await this.prisma.checklistItem.aggregate({
            where: { checklistId: checklist.id },
            _max: { sortOrder: true },
        });
        return this.prisma.checklistItem.create({
            data: {
                checklistId: checklist.id,
                phase: data.phase,
                stage: data.stage,
                task: data.task,
                category: data.stage,
                title: data.task,
                description: null,
                responsible: data.responsible,
                dueDate: data.dueDate ? new Date(data.dueDate) : undefined,
                notes: data.notes,
                sortOrder: (last._max.sortOrder ?? -1) + 1,
            },
        });
    }
    async updateItem(itemId, data) {
        const item = await this.prisma.checklistItem.findUnique({
            where: { id: itemId },
        });
        if (!item)
            throw new common_1.NotFoundException("Item do checklist não encontrado");
        const completedAt = data.status === "CONCLUIDO" ? new Date() : data.status ? null : undefined;
        return this.prisma.checklistItem.update({
            where: { id: itemId },
            data: {
                status: data.status,
                responsible: data.responsible,
                dueDate: data.dueDate ? new Date(data.dueDate) : undefined,
                notes: data.notes,
                completedAt,
            },
        });
    }
};
exports.ChecklistsService = ChecklistsService;
exports.ChecklistsService = ChecklistsService = __decorate([
    (0, common_1.Injectable)(),
    __param(0, (0, common_1.Inject)(prisma_service_1.PrismaService)),
    __metadata("design:paramtypes", [prisma_service_1.PrismaService])
], ChecklistsService);
//# sourceMappingURL=checklists.service.js.map