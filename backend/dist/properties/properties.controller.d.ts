import { PropertiesService } from "./properties.service";
import { PropertyDto } from "./dto/property.dto";
export declare class PropertiesController {
  private readonly service;
  constructor(service: PropertiesService);
  findAll(query: any): Promise<{
    data: ({
      auction: {
        id: string;
        notes: string | null;
        propertyId: string;
        auctioneer: string;
        broker: string | null;
        portal: string | null;
        processNumber: string | null;
        appraisalValue: import("@prisma/client/runtime/library").Decimal | null;
        minimumValue: import("@prisma/client/runtime/library").Decimal | null;
        auctionValue: import("@prisma/client/runtime/library").Decimal;
        auctionDate: Date;
        acquisitionDate: Date | null;
      } | null;
      sale: {
        id: string;
        notes: string | null;
        propertyId: string;
        buyer: string;
        saleAmount: import("@prisma/client/runtime/library").Decimal;
        saleDate: Date;
        brokerage: import("@prisma/client/runtime/library").Decimal;
        taxes: import("@prisma/client/runtime/library").Decimal;
      } | null;
    } & {
      number: string | null;
      id: string;
      createdAt: Date;
      updatedAt: Date;
      code: string;
      title: string;
      type: import(".prisma/client").$Enums.PropertyType;
      address: string;
      complement: string | null;
      neighborhood: string;
      city: string;
      state: string;
      zipCode: string | null;
      totalArea: import("@prisma/client/runtime/library").Decimal | null;
      builtArea: import("@prisma/client/runtime/library").Decimal | null;
      registryNumber: string | null;
      description: string | null;
      status: import(".prisma/client").$Enums.PropertyStatus;
      notes: string | null;
    })[];
    meta: {
      page: number;
      limit: number;
      total: number;
      pages: number;
    };
  }>;
  findOne(id: string): Promise<
    {
      auction: {
        id: string;
        notes: string | null;
        propertyId: string;
        auctioneer: string;
        broker: string | null;
        portal: string | null;
        processNumber: string | null;
        appraisalValue: import("@prisma/client/runtime/library").Decimal | null;
        minimumValue: import("@prisma/client/runtime/library").Decimal | null;
        auctionValue: import("@prisma/client/runtime/library").Decimal;
        auctionDate: Date;
        acquisitionDate: Date | null;
      } | null;
      sale: {
        id: string;
        notes: string | null;
        propertyId: string;
        buyer: string;
        saleAmount: import("@prisma/client/runtime/library").Decimal;
        saleDate: Date;
        brokerage: import("@prisma/client/runtime/library").Decimal;
        taxes: import("@prisma/client/runtime/library").Decimal;
      } | null;
      expenses: {
        id: string;
        description: string;
        notes: string | null;
        propertyId: string;
        category: import(".prisma/client").$Enums.ExpenseCategory;
        amount: import("@prisma/client/runtime/library").Decimal;
        date: Date;
        receiptPath: string | null;
        contractor: string | null;
      }[];
      renovations: {
        id: string;
        description: string;
        status: import(".prisma/client").$Enums.RenovationStatus;
        propertyId: string;
        supplier: string | null;
        plannedAmount: import("@prisma/client/runtime/library").Decimal | null;
        actualAmount: import("@prisma/client/runtime/library").Decimal | null;
        startDate: Date | null;
        endDate: Date | null;
      }[];
    } & {
      number: string | null;
      id: string;
      createdAt: Date;
      updatedAt: Date;
      code: string;
      title: string;
      type: import(".prisma/client").$Enums.PropertyType;
      address: string;
      complement: string | null;
      neighborhood: string;
      city: string;
      state: string;
      zipCode: string | null;
      totalArea: import("@prisma/client/runtime/library").Decimal | null;
      builtArea: import("@prisma/client/runtime/library").Decimal | null;
      registryNumber: string | null;
      description: string | null;
      status: import(".prisma/client").$Enums.PropertyStatus;
      notes: string | null;
    }
  >;
  create(dto: PropertyDto): Promise<{
    number: string | null;
    id: string;
    createdAt: Date;
    updatedAt: Date;
    code: string;
    title: string;
    type: import(".prisma/client").$Enums.PropertyType;
    address: string;
    complement: string | null;
    neighborhood: string;
    city: string;
    state: string;
    zipCode: string | null;
    totalArea: import("@prisma/client/runtime/library").Decimal | null;
    builtArea: import("@prisma/client/runtime/library").Decimal | null;
    registryNumber: string | null;
    description: string | null;
    status: import(".prisma/client").$Enums.PropertyStatus;
    notes: string | null;
  }>;
  update(
    id: string,
    dto: Partial<PropertyDto>,
  ): Promise<{
    number: string | null;
    id: string;
    createdAt: Date;
    updatedAt: Date;
    code: string;
    title: string;
    type: import(".prisma/client").$Enums.PropertyType;
    address: string;
    complement: string | null;
    neighborhood: string;
    city: string;
    state: string;
    zipCode: string | null;
    totalArea: import("@prisma/client/runtime/library").Decimal | null;
    builtArea: import("@prisma/client/runtime/library").Decimal | null;
    registryNumber: string | null;
    description: string | null;
    status: import(".prisma/client").$Enums.PropertyStatus;
    notes: string | null;
  }>;
  remove(id: string): Promise<{
    number: string | null;
    id: string;
    createdAt: Date;
    updatedAt: Date;
    code: string;
    title: string;
    type: import(".prisma/client").$Enums.PropertyType;
    address: string;
    complement: string | null;
    neighborhood: string;
    city: string;
    state: string;
    zipCode: string | null;
    totalArea: import("@prisma/client/runtime/library").Decimal | null;
    builtArea: import("@prisma/client/runtime/library").Decimal | null;
    registryNumber: string | null;
    description: string | null;
    status: import(".prisma/client").$Enums.PropertyStatus;
    notes: string | null;
  }>;
}
