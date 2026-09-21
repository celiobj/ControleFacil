import { PrismaService } from '../prisma.service';
export declare class UsersService {
    private readonly prisma;
    constructor(prisma: PrismaService);
    list(): import(".prisma/client").Prisma.PrismaPromise<{
        id: string;
        email: string;
        name: string;
        role: import(".prisma/client").$Enums.Role;
        active: boolean;
        createdAt: Date;
        updatedAt: Date;
    }[]>;
    update(id: string, data: {
        name?: string;
        role?: any;
        active?: boolean;
        password?: string;
    }): Promise<{
        id: string;
        email: string;
        name: string;
        role: import(".prisma/client").$Enums.Role;
        active: boolean;
    }>;
    updatePassword(id: string, password: string): Promise<{
        id: string;
        email: string;
        name: string;
        role: import(".prisma/client").$Enums.Role;
        active: boolean;
    }>;
    remove(id: string): Promise<{
        id: string;
        email: string;
        name: string;
        role: import(".prisma/client").$Enums.Role;
        active: boolean;
    }>;
}
