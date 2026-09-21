import { DashboardService } from './dashboard.service';
export declare class DashboardController {
    private readonly service;
    constructor(service: DashboardService);
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
