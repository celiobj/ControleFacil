import { PartnerDto } from "./dto/partner.dto";
import { PartnersService } from "./partners.service";
export declare class PartnersController {
    private readonly service;
    constructor(service: PartnersService);
    findAll(query: {
        type?: string;
        search?: string;
    }): import(".prisma/client").Prisma.PrismaPromise<{
        id: string;
        email: string | null;
        name: string;
        createdAt: Date;
        updatedAt: Date;
        type: import(".prisma/client").$Enums.PartnerType;
        notes: string | null;
        document: string | null;
        phone: string | null;
        company: string | null;
    }[]>;
    create(dto: PartnerDto): import(".prisma/client").Prisma.Prisma__PartnerClient<{
        id: string;
        email: string | null;
        name: string;
        createdAt: Date;
        updatedAt: Date;
        type: import(".prisma/client").$Enums.PartnerType;
        notes: string | null;
        document: string | null;
        phone: string | null;
        company: string | null;
    }, never, import("@prisma/client/runtime/library").DefaultArgs, import(".prisma/client").Prisma.PrismaClientOptions>;
    update(id: string, dto: Partial<PartnerDto>): Promise<{
        id: string;
        email: string | null;
        name: string;
        createdAt: Date;
        updatedAt: Date;
        type: import(".prisma/client").$Enums.PartnerType;
        notes: string | null;
        document: string | null;
        phone: string | null;
        company: string | null;
    }>;
    remove(id: string): Promise<{
        id: string;
        email: string | null;
        name: string;
        createdAt: Date;
        updatedAt: Date;
        type: import(".prisma/client").$Enums.PartnerType;
        notes: string | null;
        document: string | null;
        phone: string | null;
        company: string | null;
    }>;
}
