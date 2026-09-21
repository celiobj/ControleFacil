import { JwtService } from '@nestjs/jwt';
import { PrismaService } from '../prisma.service';
export declare class AuthService {
    private readonly prisma;
    private readonly jwt;
    constructor(prisma: PrismaService, jwt: JwtService);
    login(email: string, password: string): Promise<{
        accessToken: string;
        user: {
            id: string;
            name: string;
            email: string;
            role: import(".prisma/client").$Enums.Role;
        };
    }>;
    register(data: {
        name: string;
        email: string;
        password: string;
        role?: any;
    }): Promise<{
        id: string;
        email: string;
        name: string;
        role: import(".prisma/client").$Enums.Role;
        active: boolean;
    }>;
}
