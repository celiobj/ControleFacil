import { ExpenseCategory } from "@prisma/client";
import { PrismaService } from "../prisma.service";
export declare class OperationsService {
    private readonly prisma;
    constructor(prisma: PrismaService);
    listExpenses(propertyId?: string): import(".prisma/client").Prisma.PrismaPromise<({
        property: {
            code: string;
            title: string;
        };
    } & {
        id: string;
        description: string;
        notes: string | null;
        propertyId: string;
        category: import(".prisma/client").$Enums.ExpenseCategory;
        amount: import("@prisma/client/runtime/library").Decimal;
        date: Date;
        receiptPath: string | null;
        contractor: string | null;
    })[]>;
    createExpense(data: {
        propertyId: string;
        category: ExpenseCategory;
        description: string;
        amount: number;
        date: string;
        receiptPath?: string;
        contractor?: string;
        notes?: string;
    }): import(".prisma/client").Prisma.Prisma__ExpenseClient<{
        id: string;
        description: string;
        notes: string | null;
        propertyId: string;
        category: import(".prisma/client").$Enums.ExpenseCategory;
        amount: import("@prisma/client/runtime/library").Decimal;
        date: Date;
        receiptPath: string | null;
        contractor: string | null;
    }, never, import("@prisma/client/runtime/library").DefaultArgs, import(".prisma/client").Prisma.PrismaClientOptions>;
    listRenovations(propertyId?: string): import(".prisma/client").Prisma.PrismaPromise<{
        id: string;
        description: string;
        status: import(".prisma/client").$Enums.RenovationStatus;
        propertyId: string;
        supplier: string | null;
        plannedAmount: import("@prisma/client/runtime/library").Decimal | null;
        actualAmount: import("@prisma/client/runtime/library").Decimal | null;
        startDate: Date | null;
        endDate: Date | null;
    }[]>;
    createRenovation(data: any): import(".prisma/client").Prisma.Prisma__RenovationClient<{
        id: string;
        description: string;
        status: import(".prisma/client").$Enums.RenovationStatus;
        propertyId: string;
        supplier: string | null;
        plannedAmount: import("@prisma/client/runtime/library").Decimal | null;
        actualAmount: import("@prisma/client/runtime/library").Decimal | null;
        startDate: Date | null;
        endDate: Date | null;
    }, never, import("@prisma/client/runtime/library").DefaultArgs, import(".prisma/client").Prisma.PrismaClientOptions>;
    listAuctions(): import(".prisma/client").Prisma.PrismaPromise<({
        property: {
            number: string | null;
            id: string;
            createdAt: Date;
            updatedAt: Date;
            code: string;
            title: string;
            type: import(".prisma/client").$Enums.PropertyType;
            address: string;
            complement: string | null;
            neighborhood: string;
            city: string;
            state: string;
            zipCode: string | null;
            totalArea: import("@prisma/client/runtime/library").Decimal | null;
            builtArea: import("@prisma/client/runtime/library").Decimal | null;
            registryNumber: string | null;
            description: string | null;
            status: import(".prisma/client").$Enums.PropertyStatus;
            notes: string | null;
        };
    } & {
        id: string;
        notes: string | null;
        propertyId: string;
        auctioneer: string;
        broker: string | null;
        portal: string | null;
        processNumber: string | null;
        appraisalValue: import("@prisma/client/runtime/library").Decimal | null;
        minimumValue: import("@prisma/client/runtime/library").Decimal | null;
        auctionValue: import("@prisma/client/runtime/library").Decimal;
        auctionDate: Date;
        acquisitionDate: Date | null;
    })[]>;
    createAuction(data: any): Promise<{
        id: string;
        notes: string | null;
        propertyId: string;
        auctioneer: string;
        broker: string | null;
        portal: string | null;
        processNumber: string | null;
        appraisalValue: import("@prisma/client/runtime/library").Decimal | null;
        minimumValue: import("@prisma/client/runtime/library").Decimal | null;
        auctionValue: import("@prisma/client/runtime/library").Decimal;
        auctionDate: Date;
        acquisitionDate: Date | null;
    }>;
    listSales(): import(".prisma/client").Prisma.PrismaPromise<({
        property: {
            number: string | null;
            id: string;
            createdAt: Date;
            updatedAt: Date;
            code: string;
            title: string;
            type: import(".prisma/client").$Enums.PropertyType;
            address: string;
            complement: string | null;
            neighborhood: string;
            city: string;
            state: string;
            zipCode: string | null;
            totalArea: import("@prisma/client/runtime/library").Decimal | null;
            builtArea: import("@prisma/client/runtime/library").Decimal | null;
            registryNumber: string | null;
            description: string | null;
            status: import(".prisma/client").$Enums.PropertyStatus;
            notes: string | null;
        };
    } & {
        id: string;
        notes: string | null;
        propertyId: string;
        buyer: string;
        saleAmount: import("@prisma/client/runtime/library").Decimal;
        saleDate: Date;
        brokerage: import("@prisma/client/runtime/library").Decimal;
        taxes: import("@prisma/client/runtime/library").Decimal;
    })[]>;
    createSale(data: any): Promise<{
        id: string;
        notes: string | null;
        propertyId: string;
        buyer: string;
        saleAmount: import("@prisma/client/runtime/library").Decimal;
        saleDate: Date;
        brokerage: import("@prisma/client/runtime/library").Decimal;
        taxes: import("@prisma/client/runtime/library").Decimal;
    }>;
}
