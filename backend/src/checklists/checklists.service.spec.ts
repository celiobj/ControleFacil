import { ChecklistsService } from "./checklists.service";

describe("ChecklistsService", () => {
  it("adds new defaults without deleting existing checklist items", async () => {
    const existingItem = {
      id: "existing-item",
      phase: "ANTES DO LANCE",
      stage: "Conferência",
      task: "Conferir número do imóvel CAIXA",
      status: "CONCLUIDO",
      sortOrder: 0,
    };
    const customItem = {
      id: "custom-item",
      phase: "PERSONALIZADO",
      stage: "Operação",
      task: "Tarefa criada pelo usuário",
      status: "PENDENTE",
      sortOrder: 1,
    };
    const checklist = {
      id: "checklist-id",
      items: [existingItem, customItem],
    };
    const prisma = {
      property: { findUnique: jest.fn().mockResolvedValue({ id: "property-id" }) },
      propertyChecklist: {
        findUnique: jest.fn().mockResolvedValue(checklist),
        findUniqueOrThrow: jest.fn().mockResolvedValue(checklist),
      },
      checklistItem: {
        createMany: jest.fn(),
        deleteMany: jest.fn(),
      },
    };
    const service = new ChecklistsService(prisma as any);

    await service.ensure("property-id");

    expect(prisma.checklistItem.deleteMany).not.toHaveBeenCalled();
    const addedItems = prisma.checklistItem.createMany.mock.calls[0][0].data;
    expect(addedItems.some((item: any) => item.task === existingItem.task)).toBe(
      false,
    );
    expect(addedItems.every((item: any) => item.checklistId === checklist.id)).toBe(
      true,
    );
  });
});