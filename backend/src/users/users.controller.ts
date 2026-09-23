import {
  Body,
  Controller,
  Delete,
  Get,
  Param,
  Patch,
  UseGuards,
} from "@nestjs/common";
import { ApiBearerAuth, ApiTags } from "@nestjs/swagger";
import { IsBoolean, IsOptional, IsString, MinLength } from "class-validator";
import { JwtGuard } from "../auth/jwt.guard";
import { UsersService } from "./users.service";

class UserUpdateDto {
  @IsOptional() @IsString() name?: string;
  @IsOptional() @IsString() role?: string;
  @IsOptional() @IsBoolean() active?: boolean;
  @IsOptional() @MinLength(8) password?: string;
}
@ApiTags("users")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller("users")
export class UsersController {
  constructor(private readonly service: UsersService) {}
  @Get() list() {
    return this.service.list();
  }
  @Patch(":id") update(@Param("id") id: string, @Body() dto: UserUpdateDto) {
    return this.service.update(id, dto);
  }
  @Patch(":id/password") password(
    @Param("id") id: string,
    @Body("password") password: string,
  ) {
    return this.service.updatePassword(id, password);
  }
  @Delete(":id") remove(@Param("id") id: string) {
    return this.service.remove(id);
  }
}
