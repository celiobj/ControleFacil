import { Injectable, NotFoundException } from "@nestjs/common";
import { PrismaService } from "../prisma.service";
import * as bcrypt from "bcryptjs";

@Injectable()
export class UsersService {
  constructor(private readonly prisma: PrismaService) {}
  list() {
    return this.prisma.user.findMany({
      select: {
        id: true,
        name: true,
        email: true,
        role: true,
        active: true,
        createdAt: true,
        updatedAt: true,
      },
      orderBy: { name: "asc" },
    });
  }
  async update(
    id: string,
    data: { name?: string; role?: any; active?: boolean; password?: string },
  ) {
    if (!(await this.prisma.user.findUnique({ where: { id } })))
      throw new NotFoundException("Usuário não encontrado");
    const { password, ...rest } = data;
    return this.prisma.user.update({
      where: { id },
      data: {
        ...rest,
        ...(password ? { passwordHash: await bcrypt.hash(password, 10) } : {}),
      },
      select: { id: true, name: true, email: true, role: true, active: true },
    });
  }
  updatePassword(id: string, password: string) {
    return this.update(id, { password });
  }
  async remove(id: string) {
    return this.update(id, { active: false });
  }
}
