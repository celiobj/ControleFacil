import { ChecklistItemStatus } from "@prisma/client";
import { PrismaService } from "../prisma.service";
export declare class ChecklistsService {
    private readonly prisma;
    constructor(prisma: PrismaService);
    ensure(propertyId: string): Promise<any>;
    private withProgress;
    addItem(propertyId: string, data: {
        phase: string;
        stage: string;
        task: string;
        responsible?: string;
        dueDate?: string;
        notes?: string;
    }): Promise<{
        id: string;
        phase: string;
        stage: string;
        task: string;
        responsible: string | null;
        title: string;
        description: string | null;
        status: import(".prisma/client").$Enums.ChecklistItemStatus;
        notes: string | null;
        sortOrder: number;
        checklistId: string;
        category: string;
        dueDate: Date | null;
        completedAt: Date | null;
    }>;
    updateItem(itemId: string, data: {
        status?: ChecklistItemStatus;
        responsible?: string;
        dueDate?: string;
        notes?: string;
    }): Promise<{
        id: string;
        phase: string;
        stage: string;
        task: string;
        responsible: string | null;
        title: string;
        description: string | null;
        status: import(".prisma/client").$Enums.ChecklistItemStatus;
        notes: string | null;
        sortOrder: number;
        checklistId: string;
        category: string;
        dueDate: Date | null;
        completedAt: Date | null;
    }>;
}
