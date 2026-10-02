import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { Link, useLocation, useParams } from "react-router-dom";
import {
  CartesianGrid,
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";
import { api } from "./api";
import PropertyOperationTab from "./PropertyOperationTab";

const brl = (value: unknown) =>
  Number(value ?? 0).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
  });

const date = (value?: string) =>
  value
    ? value.slice(0, 10).split("-").reverse().join("/")
    : "-";

const negotiationTypeLabel: Record<string, string> = {
  LEILAO_SFI: "Leilão SFI",
  EXERCICIO_DIREITO_PREFERENCIA: "Exercício de direito de preferência",
  LICITACAO_ABERTA: "Licitação aberta",
  VENDA_ONLINE: "Venda online",
  COMPRA_DIRETA: "Compra direta",
};

const valueOrDash = (value: unknown) =>
  value === null || value === undefined || value === "" ? "-" : String(value);

const propertyStatuses = [
  "EM_ANALISE",
  "ARREMATADO",
  "REGULARIZACAO",
  "REFORMA",
  "PRONTO_PARA_VENDA",
  "VENDIDO",
  "CANCELADO",
];

const statusLabels: Record<string, string> = {
  EM_ANALISE: "Em análise",
  ARREMATADO: "Arrematado",
  REGULARIZACAO: "Regularização",
  REFORMA: "Reforma",
  PRONTO_PARA_VENDA: "Pronto para venda",
  VENDIDO: "Vendido",
  CANCELADO: "Cancelado",
};

const workspaceTabs = [
  ["summary", "Resumo"],
  ["acquisition", "Aquisição"],
  ["regularization", "Regularização"],
  ["possession", "Posse"],
  ["renovation", "Reforma"],
  ["expenses", "Despesas"],
  ["documents", "Documentos"],
  ["sale", "Venda"],
  ["financial", "Financeiro"],
  ["scenarios", "Cenários"],
  ["timeline", "Timeline"],
  ["checklist", "Checklist"],
] as const;

export default function PropertyDetailPage() {
  const { propertyId } = useParams();
  const location = useLocation();
  const [activeTab, setActiveTab] = useState<"details" | "timeline">("details");
  const pathParts = location.pathname.split("/").filter(Boolean);
  const requestedTab = pathParts[pathParts.length - 1];
  const operationTab = workspaceTabs.some(([id]) => id === requestedTab)
    ? requestedTab!
    : "summary";
  const { data: property, isLoading, isError } = useQuery({
    queryKey: ["property", propertyId],
    queryFn: async () => (await api.get(`/properties/${propertyId}`)).data,
    enabled: Boolean(propertyId),
  });

  if (isLoading) return <p>Carregando imóvel...</p>;
  if (isError || !property) return <p className="error">Não foi possível carregar o imóvel.</p>;

  const auction = property.auction;
  const sale = property.sale;
  const statusHistory = property.statusHistory ?? [];
  const recentStatusHistory = [...statusHistory].sort(
    (first: any, second: any) =>
      new Date(second.changedAt).getTime() - new Date(first.changedAt).getTime(),
  );
  const timelineData = statusHistory.map((event: any) => ({
    timestamp: new Date(event.changedAt).getTime(),
    status: Math.max(0, propertyStatuses.indexOf(event.toStatus)),
    ...event,
  }));
  const timelineDomain = timelineData.length === 1
    ? [timelineData[0].timestamp - 86_400_000, timelineData[0].timestamp + 86_400_000]
    : ["dataMin", "dataMax"] as ["dataMin", "dataMax"];

  return (
    <>
      <div className="section-heading property-detail-heading">
        <div>
          <Link className="back-link" to="/properties">&larr; Voltar para imóveis</Link>
          <p className="eyebrow">DETALHES DO ATIVO</p>
          <h1>{property.title}</h1>
          <p className="muted">{property.code} · {property.neighborhood}, {property.city}</p>
        </div>
        <Link className="secondary" to={`/properties/${property.id}/checklist`}>Checklist</Link>
      </div>

      <nav className="detail-tabs operation-tabs" role="tablist" aria-label="Etapas da operação">
        {workspaceTabs.map(([id, label]) => {
          const path = id === "summary" ? `/properties/${property.id}` : `/properties/${property.id}/${id}`;
          return <Link
            key={id}
            className={operationTab === id ? "detail-tab active" : "detail-tab"}
            to={path}
            role="tab"
            aria-selected={operationTab === id}
          >{label}</Link>;
        })}
      </nav>

      {operationTab === "summary" ? <>
      <div className="detail-tabs" role="tablist" aria-label="Visualizações do imóvel">
        <button
          id="property-details-tab"
          className={activeTab === "details" ? "detail-tab active" : "detail-tab"}
          type="button"
          role="tab"
          aria-selected={activeTab === "details"}
          aria-controls="property-details-panel"
          onClick={() => setActiveTab("details")}
        >
          Detalhes
        </button>
        <button
          id="property-timeline-tab"
          className={activeTab === "timeline" ? "detail-tab active" : "detail-tab"}
          type="button"
          role="tab"
          aria-selected={activeTab === "timeline"}
          aria-controls="property-timeline-panel"
          onClick={() => setActiveTab("timeline")}
        >
          Linha do tempo
        </button>
      </div>

      {activeTab === "details" ? <div id="property-details-panel" role="tabpanel" aria-labelledby="property-details-tab">
      <section className="panel detail-table-panel">
        <div className="panel-title"><h3>Imóvel</h3><span>{property.status.replaceAll("_", " ")}</span></div>
        <table className="detail-table"><tbody>
          <tr><th>Código</th><td>{property.code}</td><th>Tipo</th><td>{property.type}</td></tr>
          <tr><th>Tipo de negociação</th><td>{negotiationTypeLabel[property.negotiationType] ?? valueOrDash(property.negotiationType)}</td><th>Data de expiração</th><td>{date(property.negotiationDeadline)}</td></tr>
          <tr><th>Endereço</th><td colSpan={3}>{property.address}{property.number ? `, ${property.number}` : ""}{property.complement ? ` - ${property.complement}` : ""}</td></tr>
          <tr><th>Bairro</th><td>{property.neighborhood}</td><th>Cidade/UF</th><td>{property.city} / {property.state}</td></tr>
          <tr><th>CEP</th><td>{valueOrDash(property.zipCode)}</td><th>Matrícula</th><td>{valueOrDash(property.registryNumber)}</td></tr>
          <tr><th>Área total</th><td>{property.totalArea ? `${property.totalArea} m²` : "-"}</td><th>Área construída</th><td>{property.builtArea ? `${property.builtArea} m²` : "-"}</td></tr>
          <tr><th>Descrição</th><td colSpan={3}><textarea className="detail-textarea" rows={1} readOnly value={valueOrDash(property.description)} /></td></tr>
          <tr><th>Observações</th><td colSpan={3}><textarea className="detail-textarea" rows={4} readOnly value={valueOrDash(property.notes)} /></td></tr>
        </tbody></table>
      </section>

      <section className="panel detail-table-panel">
        <div className="panel-title"><h3>Arrematação</h3><span>{auction ? date(auction.auctionDate) : "Sem registro"}</span></div>
        {auction ? <table className="detail-table"><tbody>
          <tr><th>Leiloeiro</th><td>{auction.auctioneer}</td><th>Valor</th><td>{brl(auction.auctionValue)}</td></tr>
          <tr><th>Portal</th><td>{valueOrDash(auction.portal)}</td><th>Processo</th><td>{valueOrDash(auction.processNumber)}</td></tr>
          <tr><th>Corretor</th><td>{valueOrDash(auction.broker)}</td><th>Data da arrematação</th><td>{date(auction.auctionDate)}</td></tr>
        </tbody></table> : <p className="empty-state">Nenhuma arrematação registrada para este imóvel.</p>}
      </section>

      <section className="panel detail-table-panel">
        <div className="panel-title"><h3>Despesas</h3><span>{property.expenses.length} lançamentos</span></div>
        {property.expenses.length ? <table><thead><tr><th>Data</th><th>Categoria</th><th>Descrição</th><th>Valor</th><th>Observações</th></tr></thead><tbody>
          {property.expenses.map((expense: any) => <tr key={expense.id}><td>{date(expense.date)}</td><td>{expense.category}</td><td>{expense.description}</td><td>{brl(expense.amount)}</td><td>{valueOrDash(expense.notes)}</td></tr>)}
        </tbody></table> : <p className="empty-state">Nenhuma despesa registrada para este imóvel.</p>}
      </section>

      <section className="panel detail-table-panel">
        <div className="panel-title"><h3>Venda</h3><span>{sale ? date(sale.saleDate) : "Sem registro"}</span></div>
        {sale ? <table className="detail-table"><tbody>
          <tr><th>Comprador</th><td>{sale.buyer}</td><th>Valor da venda</th><td>{brl(sale.saleAmount)}</td></tr>
          <tr><th>Data da venda</th><td>{date(sale.saleDate)}</td><th>Corretagem</th><td>{brl(sale.brokerage)}</td></tr>
          <tr><th>Impostos</th><td>{brl(sale.taxes)}</td><th>Observações</th><td>{valueOrDash(sale.notes)}</td></tr>
        </tbody></table> : <p className="empty-state">Nenhuma venda registrada para este imóvel.</p>}
      </section>
      </div> : <section
        id="property-timeline-panel"
        className="panel status-timeline-panel"
        role="tabpanel"
        aria-labelledby="property-timeline-tab"
      >
        <div className="panel-title">
          <h3>Histórico de status</h3>
          <span>{statusHistory.length} registros</span>
        </div>
        {timelineData.length ? <>
          <div className="status-chart">
            <ResponsiveContainer width="100%" height={320}>
              <LineChart data={timelineData} margin={{ top: 16, right: 20, bottom: 8, left: 12 }}>
                <CartesianGrid stroke="#e8eeea" vertical={false} />
                <XAxis
                  type="number"
                  dataKey="timestamp"
                  scale="time"
                  domain={timelineDomain}
                  tickFormatter={(timestamp) => new Date(Number(timestamp)).toLocaleDateString("pt-BR", { day: "2-digit", month: "short", year: "2-digit" })}
                  tick={{ fill: "#74837c", fontSize: 12 }}
                  minTickGap={28}
                />
                <YAxis
                  type="number"
                  domain={[0, propertyStatuses.length - 1]}
                  ticks={propertyStatuses.map((_, index) => index)}
                  tickFormatter={(status) => statusLabels[propertyStatuses[Number(status)]]}
                  tick={{ fill: "#74837c", fontSize: 12 }}
                  width={132}
                />
                <Tooltip
                  labelFormatter={(timestamp) => new Date(Number(timestamp)).toLocaleString("pt-BR")}
                  formatter={(status) => [statusLabels[propertyStatuses[Number(status)]], "Status"]}
                />
                <Line
                  type="stepAfter"
                  dataKey="status"
                  stroke="#e36b3d"
                  strokeWidth={3}
                  dot={{ r: 5, fill: "#285c58", strokeWidth: 2 }}
                  activeDot={{ r: 7 }}
                  isAnimationActive={false}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>
          <ol className="status-event-list">
            {recentStatusHistory.map((event: any) => (
              <li key={event.id}>
                <time dateTime={event.changedAt}>
                  {new Date(event.changedAt).toLocaleString("pt-BR")}
                </time>
                <strong>
                  {event.fromStatus
                    ? `${statusLabels[event.fromStatus] ?? event.fromStatus} → ${statusLabels[event.toStatus] ?? event.toStatus}`
                    : statusLabels[event.toStatus] ?? event.toStatus}
                </strong>
              </li>
            ))}
          </ol>
        </> : <p className="empty-state">Ainda não há alterações de status registradas para este imóvel.</p>}
      </section>}
      </> : <PropertyOperationTab tab={operationTab} property={property} />}
    </>
  );
}