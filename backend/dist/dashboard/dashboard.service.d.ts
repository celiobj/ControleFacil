import { PrismaService } from "../prisma.service";
export declare class DashboardService {
    private readonly prisma;
    constructor(prisma: PrismaService);
    summary(): Promise<{
        totalInvested: number;
        totalSold: number;
        accumulatedProfit: number;
        averageRoi: number;
        propertyCount: number;
        byStatus: Record<string, number>;
        operations: {
            propertyId: string;
            code: string;
            title: string;
            costTotal: number;
            sale: number;
            netProfit: number;
            roi: number;
            saleDate: Date | undefined;
        }[];
    }>;
}
