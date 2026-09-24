import { PropertyStatus, PropertyType } from "@prisma/client";
export declare class PropertyDto {
    code?: string;
    title: string;
    type: PropertyType;
    address: string;
    number?: string;
    complement?: string;
    neighborhood: string;
    city: string;
    state: string;
    zipCode?: string;
    totalArea?: number;
    builtArea?: number;
    registryNumber?: string;
    description?: string;
    status?: PropertyStatus;
    notes?: string;
}
