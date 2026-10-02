import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Link, useParams } from "react-router-dom";
import { api } from "./api";
import ChecklistPage from "./ChecklistPage";

const brl = (value: unknown) =>
  Number(value ?? 0).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
  });
const date = (value?: string | null) =>
  value ? value.slice(0, 10).split("-").reverse().join("/") : "-";
const documentTypes = [
  "EDITAL", "ANUNCIO_CAIXA", "MATRICULA", "CERTIDAO_MATRICULA",
  "TERMO_ARREMATACAO", "COMPROVANTE_PAGAMENTO", "COMPROVANTE_FGTS",
  "CONTRATO_CAIXA", "ITBI", "COMPROVANTE_ITBI", "PROTOCOLO_CARTORIO",
  "REGISTRO_IMOVEL", "MATRICULA_ATUALIZADA", "IPTU", "CONDOMINIO",
  "VISTORIA", "ORCAMENTO", "NOTA_FISCAL", "FOTO", "CONTRATO_VENDA", "OUTRO",
];
const documentStatuses = ["PENDENTE", "RECEBIDO", "VALIDADO", "VENCIDO", "CANCELADO", "PENDING", "RECEIVED", "VALIDATED", "EXPIRED", "CANCELLED", "VALID", "REJECTED"];

export default function PropertyOperationTab({
  tab,
  property,
}: {
  tab: string;
  property: any;
}) {
  const { propertyId = "" } = useParams();
  const queryClient = useQueryClient();
  const [feedback, setFeedback] = useState("");
  const mutation = useMutation({
    mutationFn: ({ url, method, data }: { url: string; method: "post" | "patch" | "delete"; data?: unknown }) =>
      api.request({ url, method, data }),
    onSuccess: async () => {
      setFeedback("Alterações salvas.");
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: ["property", propertyId] }),
        queryClient.invalidateQueries({ queryKey: ["property-v2", propertyId] }),
        queryClient.invalidateQueries({ queryKey: ["properties"] }),
        queryClient.invalidateQueries({ queryKey: ["dashboard"] }),
      ]);
    },
    onError: () => setFeedback("Não foi possível salvar as alterações."),
  });
  const query = useQuery({
    queryKey: ["property-v2", propertyId, tab],
    enabled: Boolean(propertyId) && ["acquisition", "regularization", "possession", "documents", "financial", "scenarios", "timeline"].includes(tab),
    queryFn: async () => {
      const paths: Record<string, string> = {
        acquisition: `/properties/${propertyId}/acquisition`,
        regularization: `/properties/${propertyId}/regularization`,
        possession: `/properties/${propertyId}/possession`,
        documents: `/properties/${propertyId}/documents`,
        financial: `/properties/${propertyId}/financial`,
        scenarios: `/properties/${propertyId}/scenarios`,
        timeline: `/properties/${propertyId}/events`,
      };
      return (await api.get(paths[tab])).data;
    },
  });

  const submit = (event: React.FormEvent<HTMLFormElement>, url: string, method: "post" | "patch" = "post") => {
    event.preventDefault();
    const payload = Object.fromEntries(
      [...new FormData(event.currentTarget).entries()].filter(([, value]) => value !== ""),
    );
    mutation.mutate({ url, method, data: payload });
  };
  const updateStatus = (url: string, status: string) =>
    mutation.mutate({ url, method: "patch", data: { status } });
  const uploadDocument = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    for (const [key, value] of data.entries()) {
      if (value === "") data.delete(key);
    }
    mutation.mutate({ url: `/properties/${propertyId}/documents`, method: "post", data });
    event.currentTarget.reset();
  };
  const openDocument = async (id: string, fileName: string, download = false) => {
    try {
      const response = await api.get(`/documents/${id}/download`, { responseType: "blob" });
      const url = URL.createObjectURL(response.data);
      if (download) {
        const anchor = document.createElement("a");
        anchor.href = url;
        anchor.download = fileName;
        anchor.click();
      } else {
        window.open(url, "_blank", "noopener,noreferrer");
      }
      window.setTimeout(() => URL.revokeObjectURL(url), 60_000);
    } catch {
      setFeedback("Não foi possível abrir o arquivo.");
    }
  };

  if (tab === "checklist") return <ChecklistPage />;
  if (query.isLoading && query.fetchStatus !== "idle") return <p>Carregando operação...</p>;

  if (tab === "acquisition") {
    const acquisition = query.data;
    return <section className="panel operation-panel">
      <div className="panel-title"><h3>Aquisição efetiva</h3><span>{acquisition?.status ?? "Ainda não registrada"}</span></div>
      <p className="muted">Os dados do leilão permanecem em Auction; este registro descreve os valores financeiros efetivamente pagos.</p>
      <form className="form-grid" onSubmit={(event) => submit(event, acquisition ? `/acquisitions/${acquisition.id}` : `/properties/${propertyId}/acquisition`, acquisition ? "patch" : "post")}>
        <label>Preço de aquisição<input name="purchasePrice" type="number" min="0" step="0.01" defaultValue={acquisition?.purchasePrice ?? ""} /></label>
        <label>Comissão do leiloeiro<input name="auctioneerCommission" type="number" min="0" step="0.01" defaultValue={acquisition?.auctioneerCommission ?? ""} /></label>
        <label>Recursos próprios<input name="ownResourcesAmount" type="number" min="0" step="0.01" defaultValue={acquisition?.ownResourcesAmount ?? ""} /></label>
        <label>FGTS<input name="fgtsAmount" type="number" min="0" step="0.01" defaultValue={acquisition?.fgtsAmount ?? ""} /></label>
        <label>Financiamento<input name="financingAmount" type="number" min="0" step="0.01" defaultValue={acquisition?.financingAmount ?? ""} /></label>
        <label>Outros recursos<input name="otherResourcesAmount" type="number" min="0" step="0.01" defaultValue={acquisition?.otherResourcesAmount ?? ""} /></label>
        <label>Origem da operação<select name="source" defaultValue={acquisition?.source ?? "AUCTION"}><option value="AUCTION">Leilão</option><option value="DIRECT">Compra direta</option><option value="PREFERENCE">Preferência</option><option value="ONLINE">Venda online</option><option value="OTHER">Outra</option></select></label>
        <label>Data da aquisição<input name="acquisitionDate" type="date" defaultValue={acquisition?.acquisitionDate?.slice(0, 10) ?? ""} /></label>
        <label>Data do pagamento<input name="paymentDate" type="date" defaultValue={acquisition?.paymentDate?.slice(0, 10) ?? ""} /></label>
        <label>Forma de pagamento<select name="paymentMethod" defaultValue={acquisition?.paymentMethod ?? "OTHER"}><option value="OWN_RESOURCES">Recursos próprios</option><option value="FGTS">FGTS</option><option value="FINANCING">Financiamento</option><option value="FGTS_AND_OWN_RESOURCES">FGTS e recursos próprios</option><option value="FINANCING_AND_OWN_RESOURCES">Financiamento e recursos próprios</option><option value="FGTS_AND_FINANCING">FGTS e financiamento</option><option value="MIXED">Misto</option><option value="OTHER">Outro</option></select></label>
        <label>Status<select name="status" defaultValue={acquisition?.status ?? "PLANNED"}><option value="PLANNED">Em andamento</option><option value="COMPLETED">Concluída</option><option value="CANCELLED">Cancelada</option></select></label>
        <label className="wide">Observações<textarea name="notes" rows={2} defaultValue={acquisition?.notes ?? ""} /></label>
        <div className="form-actions wide"><button className="primary" disabled={mutation.isPending}>Salvar aquisição</button></div>
      </form>
      {acquisition && <p className="muted">Origem dos recursos: próprios {brl(acquisition.ownResourcesAmount)} · FGTS {brl(acquisition.fgtsAmount)} · financiamento {brl(acquisition.financingAmount)} · outros {brl(acquisition.otherResourcesAmount)}</p>}
      {feedback && <p className="success" role="status">{feedback}</p>}
    </section>;
  }

  if (tab === "financial") {
    const data = query.data;
    const rows = [
      ["Aquisição", data?.purchasePrice, data?.auctionCommission],
      ["Custos de aquisição", data?.acquisitionCosts],
      ["Regularização", data?.regularizationCosts],
      ["Posse", data?.possessionCosts],
      ["Reforma", data?.renovationCosts],
      ["Operação", data?.operationalCosts],
      ["Financeiro", data?.financingCosts],
      ["Venda", data?.saleCosts],
    ];
    return <section className="panel operation-panel"><div className="panel-title"><h3>Financeiro consolidado</h3><span>Calculado pela API</span></div><table className="detail-table"><tbody>{rows.map(([label, amount, extra]) => <tr key={String(label)}><th>{label}</th><td>{brl(amount)}{extra ? ` · comissão ${brl(extra)}` : ""}</td></tr>)}<tr><th>Total investido</th><td><strong>{brl(data?.totalInvested)}</strong></td></tr><tr><th>Capital próprio investido</th><td><strong>{brl(data?.capitalInvested)}</strong></td></tr><tr><th>Venda</th><td>{brl(data?.saleAmount)}</td></tr><tr><th>Resultado líquido</th><td><strong>{brl(data?.netProfit)}</strong></td></tr><tr><th>ROI</th><td>{Number(data?.roi ?? 0).toFixed(2)}%</td></tr></tbody></table></section>;
  }

  if (tab === "documents") {
    const documents = query.data ?? [];
    return <section className="panel operation-panel"><div className="panel-title"><h3>Documentos</h3><span>{documents.length} arquivos</span></div>
      <form className="form-grid document-upload" onSubmit={uploadDocument}>
        <label>Tipo<select name="type" defaultValue="OUTRO" required>{documentTypes.map((type) => <option value={type} key={type}>{type.replaceAll("_", " ")}</option>)}</select></label>
        <label>Nome<input name="title" required maxLength={200} /></label>
        <label>Data do documento<input name="documentDate" type="date" /></label>
        <label className="wide">Arquivo<input name="file" type="file" required /></label>
        <label className="wide">Descrição<textarea name="description" rows={2} /></label>
        <div className="form-actions wide"><button className="primary" disabled={mutation.isPending}>Adicionar documento</button></div>
      </form>
      {documents.length ? <div className="table-panel"><table><thead><tr><th>Tipo</th><th>Nome</th><th>Data</th><th>Status</th><th>Tamanho</th><th>Ações</th></tr></thead><tbody>{documents.map((item: any) => <tr key={item.id}><td>{item.type}</td><td>{item.title}</td><td>{date(item.documentDate)}</td><td><select aria-label={`Status do documento ${item.title}`} value={item.status} onChange={(event) => updateStatus(`/documents/${item.id}`, event.target.value)}>{documentStatuses.map((status) => <option value={status} key={status}>{status}</option>)}</select></td><td>{item.fileSize ? `${Number(item.fileSize).toLocaleString("pt-BR")} B` : "-"}</td><td><div className="row-actions"><button className="table-action" type="button" onClick={() => void openDocument(item.id, item.fileName ?? item.title)}>Visualizar</button><button className="table-action" type="button" onClick={() => void openDocument(item.id, item.fileName ?? item.title, true)}>Download</button><button className="table-action danger" type="button" onClick={() => mutation.mutate({ url: `/documents/${item.id}`, method: "delete" })}>Excluir</button></div></td></tr>)}</tbody></table></div> : <p className="empty-state">Nenhum documento cadastrado.</p>}
      {feedback && <p className="success" role="status">{feedback}</p>}
    </section>;
  }

  if (tab === "regularization") {
    const regularization = query.data;
    const tasks = regularization?.tasks ?? [];
    return <section className="panel operation-panel"><div className="panel-title"><h3>Regularização</h3><span>{regularization?.status ?? "Não iniciada"}</span></div>
      {!regularization && <button className="primary small" type="button" onClick={() => mutation.mutate({ url: `/properties/${propertyId}/regularization`, method: "post", data: {} })}>Iniciar regularização</button>}
      {regularization && <><form className="property-search" onSubmit={(event) => submit(event, `/regularization/${regularization.id}`, "patch")}><select name="status" defaultValue={regularization.status}><option value="PENDENTE">Pendente</option><option value="EM_ANDAMENTO">Em andamento</option><option value="CONCLUIDA">Concluída</option><option value="BLOQUEADA">Bloqueada</option><option value="NOT_STARTED">Não iniciada (legado)</option><option value="IN_PROGRESS">Em andamento (legado)</option><option value="COMPLETED">Concluída (legado)</option><option value="BLOCKED">Bloqueada (legado)</option></select><input name="startedAt" type="date" aria-label="Data de início" defaultValue={regularization.startedAt?.slice(0, 10) ?? ""} /><input name="notes" placeholder="Observações" defaultValue={regularization.notes ?? ""} /><button className="secondary">Salvar</button></form>
      <div className="table-panel"><table><thead><tr><th>Categoria</th><th>Tarefa</th><th>Responsável</th><th>Prazo</th><th>Custo</th><th>Status</th></tr></thead><tbody>{tasks.map((task: any) => <tr key={task.id}><td>{task.category}</td><td>{task.title}</td><td>{task.responsible ?? "-"}</td><td>{date(task.dueDate)}</td><td>{brl(task.cost)}</td><td><select value={task.status} onChange={(event) => updateStatus(`/regularization/tasks/${task.id}`, event.target.value)}><option value="PENDING">Pendente</option><option value="IN_PROGRESS">Em andamento</option><option value="COMPLETED">Concluída</option><option value="BLOCKED">Bloqueada</option><option value="CANCELLED">Cancelada</option></select></td></tr>)}</tbody></table></div>
      <form className="form-grid" onSubmit={(event) => submit(event, `/properties/${propertyId}/regularization/tasks`)}><label>Categoria<select name="category"><option value="OUTROS">Outros</option><option value="CAIXA">CAIXA</option><option value="CARTORIO">Cartório</option><option value="PREFEITURA">Prefeitura</option><option value="ITBI">ITBI</option><option value="CONDOMINIO">Condomínio</option><option value="DOCUMENTACAO">Documentação</option><option value="JURIDICO">Jurídico</option></select></label><label>Tarefa<input name="title" required maxLength={200} /></label><label>Responsável<input name="responsible" /></label><label>Prazo<input name="dueDate" type="date" /></label><label>Custo previsto<input name="cost" type="number" min="0" step="0.01" /></label><div className="form-actions"><button className="secondary">Adicionar tarefa</button></div></form></>}
      {feedback && <p className="success" role="status">{feedback}</p>}
    </section>;
  }

  if (tab === "possession") {
    const possession = query.data;
    return <section className="panel operation-panel"><div className="panel-title"><h3>Posse e ocupação</h3><span>{possession?.occupationStatus ?? "Situação desconhecida"}</span></div>
      {possession?.alert && <p className="warning" role="alert">{possession.alert}</p>}
      <form className="form-grid" onSubmit={(event) => submit(event, possession ? `/possession/${possession.id}` : `/properties/${propertyId}/possession`, possession ? "patch" : "post")}>
        <label>Situação da posse<select name="status" defaultValue={possession?.status ?? "NOT_TAKEN"}><option value="NOT_TAKEN">Não tomada</option><option value="PENDING">Pendente</option><option value="TAKEN">Tomada</option><option value="VACANT">Vazia</option><option value="DISPUTED">Em disputa</option></select></label>
        <label>Ocupação<select name="occupationStatus" defaultValue={possession?.occupationStatus ?? "UNKNOWN"}><option value="UNKNOWN">Desconhecida</option><option value="VACANT">Vazio</option><option value="OCCUPIED">Ocupado</option><option value="FORMER_OWNER">Ex-proprietário</option><option value="TENANT">Locatário</option><option value="THIRD_PARTY">Terceiro</option><option value="DISPUTED">Em disputa</option></select></label>
        <label>Data da posse<input name="possessionDate" type="date" defaultValue={possession?.possessionDate?.slice(0, 10) ?? ""} /></label>
        <label>Recebimento das chaves<input name="keyReceivedDate" type="date" defaultValue={possession?.keyReceivedDate?.slice(0, 10) ?? ""} /></label>
        <label>Vistoria inicial<input name="inspectionDate" type="date" defaultValue={possession?.inspectionDate?.slice(0, 10) ?? ""} /></label>
        <label className="wide">Observações<textarea name="notes" rows={3} defaultValue={possession?.notes ?? ""} /></label>
        <div className="form-actions wide"><button className="primary">Salvar posse</button></div>
      </form>
      {feedback && <p className="success" role="status">{feedback}</p>}
    </section>;
  }

  if (tab === "renovation") {
    const renovations = property.renovations ?? [];
    return <section className="panel operation-panel"><div className="panel-title"><h3>Reforma</h3><span>{renovations.length} etapas</span></div>
      {renovations.length > 0 && <div className="table-panel"><table><thead><tr><th>Descrição</th><th>Fornecedor</th><th>Orçamento</th><th>Contratado</th><th>Realizado</th><th>Status</th></tr></thead><tbody>{renovations.map((item: any) => <tr key={item.id}><td>{item.description}</td><td>{item.supplier ?? "-"}</td><td>{brl(item.plannedAmount)}</td><td>{brl(item.contractedAmount)}</td><td>{brl(item.actualAmount)}</td><td>{item.status}</td></tr>)}</tbody></table></div>}
      <form className="form-grid" onSubmit={(event) => submit(event, "/operations/renovations")}><input type="hidden" name="propertyId" value={propertyId} /><label>Descrição<input name="description" required /></label><label>Fornecedor<input name="supplier" /></label><label>Orçamento<input name="plannedAmount" type="number" min="0" step="0.01" /></label><label>Valor contratado<input name="contractedAmount" type="number" min="0" step="0.01" /></label><label>Valor realizado<input name="actualAmount" type="number" min="0" step="0.01" /></label><label>Status<select name="status"><option value="PLANEJADA">Planejada</option><option value="EM_ANDAMENTO">Em andamento</option><option value="CONCLUIDA">Concluída</option></select></label><div className="form-actions"><button className="primary">Adicionar reforma</button></div></form>
      <p className="muted">Pagamentos vinculados a uma reforma são contabilizados pelas despesas associadas, substituindo o valor agregado realizado.</p>{feedback && <p className="success" role="status">{feedback}</p>}
    </section>;
  }

  if (tab === "expenses") {
    const expenses = property.expenses ?? [];
    return <section className="panel operation-panel"><div className="panel-title"><h3>Despesas</h3><span>{expenses.length} lançamentos</span></div><div className="table-panel"><table><thead><tr><th>Data</th><th>Categoria</th><th>Descrição</th><th>Valor</th><th>Responsável</th><th>Ações</th></tr></thead><tbody>{expenses.map((item: any) => <tr key={item.id}><td>{date(item.date)}</td><td>{item.category}</td><td>{item.description}</td><td>{brl(item.amount)}</td><td>{item.contractor ?? "-"}</td><td><button className="table-action danger" type="button" disabled={mutation.isPending} onClick={() => { if (window.confirm("Excluir esta despesa?")) mutation.mutate({ url: `/expenses/${item.id}`, method: "delete" }); }}>Excluir</button></td></tr>)}</tbody></table></div><Link className="secondary" to="/operations/expenses">Registrar despesas</Link></section>;
  }

  if (tab === "sale") {
    const sale = property.sale;
    return <section className="panel operation-panel"><div className="panel-title"><h3>Venda</h3><span>{sale ? date(sale.saleDate) : "Sem venda registrada"}</span></div>{sale ? <table className="detail-table"><tbody><tr><th>Comprador</th><td>{sale.buyer}</td><th>Valor</th><td>{brl(sale.saleAmount)}</td></tr><tr><th>Corretagem</th><td>{brl(sale.brokerage)}</td><th>Impostos</th><td>{brl(sale.taxes)}</td></tr></tbody></table> : <p className="empty-state">Nenhuma venda registrada para este imóvel.</p>}<Link className="secondary" to="/operations/sales">Registrar venda</Link></section>;
  }

  if (tab === "scenarios") {
    const scenarios = query.data ?? [];
    return (
      <section className="panel operation-panel">
        <div className="panel-title"><h3>Cenários de venda</h3><span>{scenarios.length} cenários</span></div>
        <form className="form-grid" onSubmit={(event) => submit(event, `/properties/${propertyId}/scenarios`)}>
          <label>Nome<input name="name" placeholder="Conservador, Base, Otimista" required /></label>
          <label>Preço de venda<input name="salePrice" type="number" min="0" step="0.01" required /></label>
          <label>Corretagem (%)<input name="brokeragePercent" type="number" min="0" step="0.01" /></label>
          <label>Impostos<input name="taxes" type="number" min="0" step="0.01" /></label>
          <label>Outros custos de venda<input name="otherSaleCosts" type="number" min="0" step="0.01" /></label>
          <div className="form-actions"><button className="primary">Criar cenário</button></div>
        </form>
        <div className="table-panel"><table><thead><tr><th>Cenário</th><th>Venda</th><th>Corretagem</th><th>Impostos</th><th>Lucro líquido</th><th>ROI</th><th>Ações</th></tr></thead><tbody>{scenarios.map((item: any) => <tr key={item.id}><td>{item.name}</td><td>{brl(item.projectedSalePrice)}</td><td>{brl(item.projectedBrokerage)}</td><td>{brl(item.projectedTaxes)}</td><td>{brl(item.netProfit)}</td><td>{Number(item.roi ?? 0).toFixed(2)}%</td><td><button className="table-action danger" type="button" disabled={mutation.isPending} onClick={() => { if (window.confirm(`Excluir o cenário \"${item.name}\"?`)) mutation.mutate({ url: `/scenarios/${item.id}`, method: "delete" }); }}>Excluir</button></td></tr>)}</tbody></table></div>
        {feedback && <p className="success" role="status">{feedback}</p>}
      </section>
    );
  }

  if (tab === "timeline") {
    const events = query.data ?? [];
    return (
      <section className="panel operation-panel">
        <div className="panel-title"><h3>Timeline da operação</h3><span>{events.length} eventos</span></div>
        <form className="form-grid" onSubmit={(event) => submit(event, `/properties/${propertyId}/events`)}>
          <label>Tipo<select name="type"><option value="NOTE">Observação</option><option value="ANALISE">Análise</option><option value="LANCE">Lance</option><option value="ARREMATACAO">Arrematação</option><option value="PAGAMENTO">Pagamento</option><option value="FGTS">FGTS</option><option value="FINANCIAMENTO">Financiamento</option><option value="DOCUMENT">Documento</option><option value="REGULARIZATION">Regularização</option><option value="ITBI">ITBI</option><option value="CARTORIO">Cartório</option><option value="POSSESSION">Posse</option><option value="REFORMA">Reforma</option><option value="EXPENSE">Despesa</option><option value="SALE">Venda</option><option value="OTHER">Outro</option></select></label>
          <label>Título<input name="title" required maxLength={200} /></label>
          <label>Data<input name="eventDate" type="datetime-local" /></label>
          <label className="wide">Descrição<textarea name="description" rows={2} /></label>
          <div className="form-actions wide"><button className="primary">Registrar evento</button></div>
        </form>
        <ol className="status-event-list">{events.map((event: any) => <li key={event.id}><time>{new Date(event.occurredAt).toLocaleString("pt-BR")}</time><strong>{event.title}</strong><span>{event.description}</span><small>{event.user?.name ?? "Sistema"}</small></li>)}</ol>
        {feedback && <p className="success" role="status">{feedback}</p>}
      </section>
    );
  }

  return <p className="empty-state">Selecione uma aba da operação.</p>;
}
