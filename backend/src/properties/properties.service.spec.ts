import { PropertiesService } from './properties.service';

describe('PropertiesService', () => {
  it('generates a sequential code for a new property', async () => {
    const prisma = {
      property: {
        findMany: jest.fn().mockResolvedValue([
          { code: 'CF-0002' },
          { code: 'CF-0001' },
        ]),
        create: jest.fn().mockResolvedValue({
          id: 'p-1',
          code: 'CF-0003',
          title: 'Casa nova',
        }),
      },
    } as any;

    const checklists = {
      ensure: jest.fn().mockResolvedValue(undefined),
    } as any;

    const service = new PropertiesService(prisma, checklists);
    const result = await service.create({
      title: 'Casa nova',
      type: 'APARTAMENTO',
      address: 'Rua das Flores',
      neighborhood: 'Centro',
      city: 'São Paulo',
      state: 'SP',
    } as any);

    expect(result.code).toBe('CF-0003');
    expect(prisma.property.findMany).toHaveBeenCalled();
    expect(prisma.property.create).toHaveBeenCalledWith(
      expect.objectContaining({
        data: expect.objectContaining({
          code: 'CF-0003',
          title: 'Casa nova',
        }),
      }),
    );
  });
});
