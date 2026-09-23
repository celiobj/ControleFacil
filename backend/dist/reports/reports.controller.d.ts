import { Response } from "express";
import { PrismaService } from "../prisma.service";
import { DashboardService } from "../dashboard/dashboard.service";
export declare class ReportsController {
  private readonly prisma;
  private readonly dashboard;
  constructor(prisma: PrismaService, dashboard: DashboardService);
  profit(): Promise<{
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
  expenses(): import(".prisma/client").Prisma.GetExpenseGroupByPayload<{
    by: "category"[];
    _sum: {
      amount: true;
    };
    _count: {
      id: true;
    };
  }>;
  csv(response: Response): Promise<void>;
}
