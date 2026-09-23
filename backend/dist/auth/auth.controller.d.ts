import { AuthService } from "./auth.service";
declare class LoginDto {
  email: string;
  password: string;
}
declare class RegisterDto {
  name: string;
  email: string;
  password: string;
  role?: "ADMIN" | "INVESTIDOR" | "CONSULTA";
}
export declare class AuthController {
  private readonly service;
  constructor(service: AuthService);
  login(dto: LoginDto): Promise<{
    accessToken: string;
    user: {
      id: string;
      name: string;
      email: string;
      role: import(".prisma/client").$Enums.Role;
    };
  }>;
  register(dto: RegisterDto): Promise<{
    id: string;
    email: string;
    name: string;
    role: import(".prisma/client").$Enums.Role;
    active: boolean;
  }>;
}
export {};
