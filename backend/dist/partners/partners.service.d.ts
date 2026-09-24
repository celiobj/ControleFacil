import { PrismaService } from "../prisma.service";
import { PartnerDto } from "./dto/partner.dto";
export declare class PartnersService {
    private readonly prisma;
    constructor(prisma: PrismaService);
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
    create(data: PartnerDto): import(".prisma/client").Prisma.Prisma__PartnerClient<{
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
    update(id: string, data: Partial<PartnerDto>): Promise<{
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
