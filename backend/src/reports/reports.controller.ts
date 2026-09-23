import { Controller, Get, Res, UseGuards } from "@nestjs/common";
import { ApiBearerAuth, ApiTags } from "@nestjs/swagger";
import { Response } from "express";
import { JwtGuard } from "../auth/jwt.guard";
import { PrismaService } from "../prisma.service";
import { DashboardService } from "../dashboard/dashboard.service";

@ApiTags("reports")
@ApiBearerAuth()
@UseGuards(JwtGuard)
@Controller("reports")
export class ReportsController {
  constructor(
    private readonly prisma: PrismaService,
    private readonly dashboard: DashboardService,
  ) {}
  @Get("profit") profit() {
    return this.dashboard.summary();
  }
  @Get("expenses") expenses() {
    return this.prisma.expense.groupBy({
      by: ["category"],
      _sum: { amount: true },
      _count: { id: true },
    });
  }
  @Get("profit.csv") async csv(@Res() response: Response) {
    const report = await this.dashboard.summary();
    const lines = [
      "codigo;imovel;custo_total;venda;lucro_liquido;roi",
      ...report.operations.map(
        (item) =>
          `${item.code};${item.title};${item.costTotal.toFixed(2)};${item.sale.toFixed(2)};${item.netProfit.toFixed(2)};${item.roi.toFixed(2)}`,
      ),
    ];
    response
      .header("Content-Type", "text/csv; charset=utf-8")
      .attachment("relatorio-lucro.csv")
      .send(lines.join("\n"));
  }
}
