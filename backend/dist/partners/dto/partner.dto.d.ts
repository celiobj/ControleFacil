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
declare const UpdatePartnerDto_base: import("@nestjs/common").Type<Partial<PartnerDto>>;
export declare class UpdatePartnerDto extends UpdatePartnerDto_base {
}
export {};
