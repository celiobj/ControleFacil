import { Inject, Injectable } from "@nestjs/common";
import { ExpenseCategory, FinancialCostCategory } from "@prisma/client";
import { PrismaService } from "../prisma.service";

@Injectable()
export class OperationsService {
  constructor(@Inject(PrismaService) private readonly prisma: PrismaService) {}
  listExpenses(propertyId?: string) {
    return this.prisma.expense.findMany({
      where: { propertyId },
      orderBy: { date: "desc" },
      include: { property: { select: { code: true, title: true } } },
    });
  }
  createExpense(data: {
    propertyId: string;
    category: ExpenseCategory;
    financialCategory?: FinancialCostCategory;
    renovationId?: string;
    description: string;
    amount: number;
    date: string;
    receiptPath?: string;
    contractor?: string;
    notes?: string;
  }) {
    return this.prisma.expense.create({
      data: { ...data, date: new Date(data.date) },
    });
  }
  deleteExpense(id: string) {
    return this.prisma.expense.delete({ where: { id } });
  }
  listRenovations(propertyId?: string) {
    return this.prisma.renovation.findMany({
      where: { propertyId },
      orderBy: { startDate: "desc" },
    });
  }
  createRenovation(data: any) {
    return this.prisma.renovation.create({
      data: {
        ...data,
        startDate: data.startDate ? new Date(data.startDate) : undefined,
        endDate: data.endDate ? new Date(data.endDate) : undefined,
      },
    });
  }
  listAuctions() {
    return this.prisma.auction.findMany({
      include: { property: true },
      orderBy: { auctionDate: "desc" },
    });
  }
  createAuction(data: any, userId?: string) {
    const { propertyId, ...auctionData } = data;
    const normalizedData = {
      ...auctionData,
      auctionDate: new Date(data.auctionDate),
      acquisitionDate: data.acquisitionDate
        ? new Date(data.acquisitionDate)
        : undefined,
    };
    return this.prisma.$transaction(async (transaction) => {
      const auction = await transaction.auction.upsert({
        where: { propertyId },
        create: { propertyId, ...normalizedData },
        update: normalizedData,
      });
      const current = await transaction.property.findUnique({
        where: { id: propertyId },
        select: { status: true },
      });
      await transaction.property.update({
        where: { id: propertyId },
        data: { status: "ARREMATADO" },
      });
      if (current && current.status !== "ARREMATADO") {
        await transaction.propertyStatusHistory.create({
          data: {
            propertyId,
            fromStatus: current.status,
            toStatus: "ARREMATADO",
          },
        });
      }
      await transaction.propertyEvent.create({
        data: {
          propertyId,
          userId,
          type: "ARREMATACAO",
          title: "Arrematação registrada",
          occurredAt: normalizedData.auctionDate,
        },
      });
      return auction;
    });
  }
  listSales() {
    return this.prisma.sale.findMany({
      include: { property: true },
      orderBy: { saleDate: "desc" },
    });
  }
  createSale(data: any, userId?: string) {
    const { propertyId, ...saleData } = data;
    const normalizedData = {
      ...saleData,
      saleDate: new Date(data.saleDate),
    };
    return this.prisma.$transaction(async (transaction) => {
      const sale = await transaction.sale.upsert({
        where: { propertyId },
        create: { propertyId, ...normalizedData },
        update: normalizedData,
      });
      const current = await transaction.property.findUnique({
        where: { id: propertyId },
        select: { status: true },
      });
      await transaction.property.update({
        where: { id: propertyId },
        data: { status: "VENDIDO" },
      });
      if (current && current.status !== "VENDIDO") {
        await transaction.propertyStatusHistory.create({
          data: {
            propertyId,
            fromStatus: current.status,
            toStatus: "VENDIDO",
          },
        });
      }
      await transaction.propertyEvent.create({
        data: {
          propertyId,
          userId,
          type: "VENDA",
          title: "Venda registrada",
          occurredAt: normalizedData.saleDate,
        },
      });
      return sale;
    });
  }
}
