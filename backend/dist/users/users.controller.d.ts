import { UsersService } from './users.service';
declare class UserUpdateDto {
    name?: string;
    role?: string;
    active?: boolean;
    password?: string;
}
export declare class UsersController {
    private readonly service;
    constructor(service: UsersService);
    list(): import(".prisma/client").Prisma.PrismaPromise<{
        id: string;
        email: string;
        name: string;
        role: import(".prisma/client").$Enums.Role;
        active: boolean;
        createdAt: Date;
        updatedAt: Date;
    }[]>;
    update(id: string, dto: UserUpdateDto): Promise<{
        id: string;
        email: string;
        name: string;
        role: import(".prisma/client").$Enums.Role;
        active: boolean;
    }>;
    password(id: string, password: string): Promise<{
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
export {};
