import { PartnerType } from "@prisma/client";
export declare class PartnerDto {
    type: PartnerType;
    name: string;
    document?: string;
    email?: string;
    phone?: string;
    company?: string;
    notes?: string;
}
