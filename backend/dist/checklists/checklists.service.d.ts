import { ChecklistItemStatus } from '@prisma/client';
import { PrismaService } from '../prisma.service';
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
        sortOrder: number;
        checklistId: string;
        phase: string;
        stage: string;
        task: string;
        category: string;
        title: string;
        description: string | null;
        status: import(".prisma/client").$Enums.ChecklistItemStatus;
        responsible: string | null;
        dueDate: Date | null;
        completedAt: Date | null;
        notes: string | null;
    }>;
    updateItem(itemId: string, data: {
        status?: ChecklistItemStatus;
        responsible?: string;
        dueDate?: string;
        notes?: string;
    }): Promise<{
        id: string;
        sortOrder: number;
        checklistId: string;
        phase: string;
        stage: string;
        task: string;
        category: string;
        title: string;
        description: string | null;
        status: import(".prisma/client").$Enums.ChecklistItemStatus;
        responsible: string | null;
        dueDate: Date | null;
        completedAt: Date | null;
        notes: string | null;
    }>;
}
