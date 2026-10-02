import { RegularizationService } from "./regularization.service";

describe("RegularizationService", () => {
  it("adds missing default tasks without removing existing tasks", async () => {
    const existingTasks = [
      { title: "Receber documentação da CAIXA", sortOrder: 2 },
      { title: "Tarefa personalizada", sortOrder: 4 },
    ];
    const createMany = jest.fn();
    const prisma = {
      property: {
        findUnique: jest.fn().mockResolvedValue({ id: "property-1" }),
      },
      propertyRegularization: {
        upsert: jest.fn().mockResolvedValue({ id: "regularization-1" }),
        findUniqueOrThrow: jest.fn().mockResolvedValue({
          id: "regularization-1",
          tasks: existingTasks,
        }),
      },
      regularizationTask: {
        findMany: jest.fn().mockResolvedValue(existingTasks),
        createMany,
      },
    } as any;
    const service = new RegularizationService(prisma);

    await service.createForProperty("property-1", {});

    expect(createMany).toHaveBeenCalledTimes(1);
    const addedTasks = createMany.mock.calls[0][0].data;
    expect(addedTasks).toHaveLength(13);
    expect(addedTasks[0].sortOrder).toBe(5);
    expect(addedTasks.some((task: any) => task.title === existingTasks[0].title)).toBe(
      false,
    );
    expect(addedTasks.some((task: any) => task.title === "Tarefa personalizada")).toBe(
      false,
    );
  });
});