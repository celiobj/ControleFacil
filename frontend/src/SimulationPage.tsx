import { useState } from "react";

const currency = (value: number) =>
  value.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
const percent = (value: number) => `${value.toFixed(1).replace(".", ",")}%`;
const numberValue = (value: string) => Number(value.replace(",", ".")) || 0;

export default function SimulationPage() {
  const [description, setDescription] = useState("");
  const [marketValue, setMarketValue] = useState("160000");
  const [purchaseValue, setPurchaseValue] = useState("87228.13");
  const [expensePercent, setExpensePercent] = useState("50");
  const market = numberValue(marketValue);
  const purchase = numberValue(purchaseValue);
  const expenses = (purchase * numberValue(expensePercent)) / 100;
  const totalInvestment = purchase + expenses;
  const profit = market - totalInvestment;
  const margin = market ? (profit / market) * 100 : 0;
  const roi = totalInvestment ? (profit / totalInvestment) * 100 : 0;
  const discount = market ? ((market - purchase) / market) * 100 : 0;
  const scenarios = [
    { name: "Conservador", factor: 90 },
    { name: "Base", factor: 100 },
    { name: "Otimista", factor: 110 },
  ];
  const updateNumber = (setter: (value: string) => void, value: string) =>
    setter(value.replace(/[^0-9,.-]/g, ""));
  return (
    <main className="simulation-page">
      <div className="simulation-hero">
        <h1>SIMULAÇÃO DE LUCRO DO IMÓVEL</h1>
        <p>
          Preencha os campos em azul para estimar o lucro potencial antes da
          compra.
        </p>
        <em>Azul = preencher&nbsp; | &nbsp;Cinza = cálculo automático</em>
      </div>
      <div className="simulation-grid">
        <section className="simulation-card input-card">
          <h2>DADOS DA SIMULAÇÃO</h2>
          <label>
            Descrição do imóvel
            <input
              value={description}
              onChange={(event) => setDescription(event.target.value)}
              placeholder="Ex.: Apartamento Praia de Jangada"
            />
          </label>
          <label>
            Valor de mercado / venda estimada
            <input
              className="input-blue"
              value={marketValue}
              onChange={(event) =>
                updateNumber(setMarketValue, event.target.value)
              }
              inputMode="decimal"
            />
          </label>
          <label>
            Valor de compra
            <input
              className="input-blue"
              value={purchaseValue}
              onChange={(event) =>
                updateNumber(setPurchaseValue, event.target.value)
              }
              inputMode="decimal"
            />
          </label>
          <label>
            Estimativa de gastos (%)
            <input
              className="input-blue"
              value={expensePercent}
              onChange={(event) =>
                updateNumber(setExpensePercent, event.target.value)
              }
              inputMode="decimal"
            />
          </label>
        </section>
        <section className="simulation-card scenario-card">
          <h2>CENÁRIOS DE VENDA</h2>
          <div className="scenario-head">
            <span>Cenário</span>
            <span>% do mercado</span>
            <span>Venda estimada</span>
            <span>Lucro estimado</span>
          </div>
          {scenarios.map((scenario) => {
            const sale = (market * scenario.factor) / 100;
            const result = sale - totalInvestment;
            return (
              <div className="scenario-row" key={scenario.name}>
                <span>{scenario.name}</span>
                <span>{scenario.factor}%</span>
                <span>{currency(sale)}</span>
                <strong className={result >= 0 ? "profit" : "loss"}>
                  {currency(result)}
                </strong>
              </div>
            );
          })}
        </section>
        <section className="simulation-card result-card">
          <h2>RESULTADOS ESTIMADOS</h2>
          <div>
            <span>Gastos estimados (R$)</span>
            <strong>{currency(expenses)}</strong>
          </div>
          <div>
            <span>Investimento total</span>
            <strong>{currency(totalInvestment)}</strong>
          </div>
          <div className="highlight">
            <span>Lucro estimado</span>
            <strong>{currency(profit)}</strong>
          </div>
          <div>
            <span>Margem sobre a venda</span>
            <strong>{percent(margin)}</strong>
          </div>
          <div>
            <span>Retorno sobre o investimento</span>
            <strong>{percent(roi)}</strong>
          </div>
          <div>
            <span>Desconto da compra vs. mercado</span>
            <strong>{percent(discount)}</strong>
          </div>
        </section>
        <section className="simulation-card guide-card">
          <h2>COMO USAR</h2>
          <ol>
            <li>Informe o valor de mercado esperado para a venda.</li>
            <li>Digite o valor de compra do imóvel no leilão.</li>
            <li>Informe o percentual estimado de gastos sobre a compra.</li>
            <li>
              Consulte o lucro, as margens e os cenários calculados
              automaticamente.
            </li>
          </ol>
          <p className={profit >= 0 ? "decision-good" : "decision-bad"}>
            {profit >= 0
              ? "Esta simulação indica potencial de lucro."
              : "Esta simulação indica potencial de prejuízo."}
          </p>
        </section>
      </div>
    </main>
  );
}
