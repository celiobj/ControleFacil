import { Body, Controller, Inject, Post } from '@nestjs/common';
import { ApiTags } from '@nestjs/swagger';
import { IsEmail, IsOptional, IsString, MinLength } from 'class-validator';
import { AuthService } from './auth.service';

class LoginDto { @IsEmail() email!: string; @IsString() password!: string; }
class RegisterDto { @IsString() name!: string; @IsEmail() email!: string; @MinLength(8) password!: string; @IsOptional() role?: 'ADMIN' | 'INVESTIDOR' | 'CONSULTA'; }
@ApiTags('auth')
@Controller('auth')
export class AuthController {
  constructor(@Inject(AuthService) private readonly service: AuthService) {}
  @Post('login') login(@Body() dto: LoginDto) { return this.service.login(dto.email, dto.password); }
  @Post('register') register(@Body() dto: RegisterDto) { return this.service.register(dto); }
}
