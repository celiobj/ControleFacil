import { useQuery } from "@tanstack/react-query";
import { Link, useParams } from "react-router-dom";
import { api } from "./api";

const brl = (value: unknown) =>
  Number(value ?? 0).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
  });

const date = (value?: string) =>
  value ? new Date(value).toLocaleDateString("pt-BR") : "-";

const valueOrDash = (value: unknown) =>
  value === null || value === undefined || value === "" ? "-" : String(value);

export default function PropertyDetailPage() {
  const { propertyId } = useParams();
  const { data: property, isLoading, isError } = useQuery({
    queryKey: ["property", propertyId],
    queryFn: async () => (await api.get(`/properties/${propertyId}`)).data,
    enabled: Boolean(propertyId),
  });

  if (isLoading) return <p>Carregando imóvel...</p>;
  if (isError || !property) return <p className="error">Não foi possível carregar o imóvel.</p>;

  const auction = property.auction;
  const sale = property.sale;

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

      <section className="panel detail-table-panel">
        <div className="panel-title"><h3>Imóvel</h3><span>{property.status.replaceAll("_", " ")}</span></div>
        <table className="detail-table"><tbody>
          <tr><th>Código</th><td>{property.code}</td><th>Tipo</th><td>{property.type}</td></tr>
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
    </>
  );
}