import { ChecklistItemStatus } from "@prisma/client";
import { ChecklistsService } from "./checklists.service";
declare class ChecklistItemUpdateDto {
    status?: ChecklistItemStatus;
    responsible?: string;
    dueDate?: string;
    notes?: string;
}
declare class ChecklistItemCreateDto {
    phase: string;
    stage: string;
    task: string;
    responsible?: string;
    dueDate?: string;
    notes?: string;
}
export declare class ChecklistsController {
    private readonly service;
    constructor(service: ChecklistsService);
    get(propertyId: string): Promise<any>;
    add(propertyId: string, dto: ChecklistItemCreateDto): Promise<{
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
    update(itemId: string, dto: ChecklistItemUpdateDto): Promise<{
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
export {};
