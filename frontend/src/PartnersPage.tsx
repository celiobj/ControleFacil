import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { api } from "./api";

type PartnerType = "LEILOEIRO" | "CORRETOR" | "EMPREITEIRA" | "COMPRADOR";
type Partner = {
  id: string;
  type: PartnerType;
  name: string;
  document?: string;
  email?: string;
  phone?: string;
  company?: string;
  notes?: string;
  createdAt: string;
};

const typeLabels: Record<PartnerType, string> = {
  LEILOEIRO: "Leiloeiro",
  CORRETOR: "Corretor",
  EMPREITEIRA: "Empreiteira",
  COMPRADOR: "Comprador",
};
const emptyForm = {
  type: "LEILOEIRO" as PartnerType,
  name: "",
  document: "",
  email: "",
  phone: "",
  company: "",
  notes: "",
};
const partnerTypeOptions: Array<[PartnerType, string]> = [
  ["LEILOEIRO", "Leiloeiro"],
  ["CORRETOR", "Corretor"],
  ["EMPREITEIRA", "Empreiteira"],
  ["COMPRADOR", "Comprador"],
];

export default function PartnersPage() {
  const queryClient = useQueryClient();
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [search, setSearch] = useState("");
  const [filter, setFilter] = useState<"TODOS" | PartnerType>("TODOS");
  const [feedback, setFeedback] = useState("");
  const { data: partners = [], isLoading } = useQuery<Partner[]>({
    queryKey: ["partners"],
    queryFn: async () => (await api.get("/partners")).data,
  });
  const mutation = useMutation({
    mutationFn: () =>
      editingId
        ? api.patch(`/partners/${editingId}`, form)
        : api.post("/partners", form),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["partners"] });
      setForm(emptyForm);
      setEditingId(null);
      setFeedback("Cadastro salvo com sucesso.");
    },
    onError: () => setFeedback("Não foi possível salvar o cadastro."),
  });
  const removeMutation = useMutation({
    mutationFn: (id: string) => api.delete(`/partners/${id}`),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["partners"] });
      setFeedback("Cadastro excluído.");
    },
  });
  const visible = partners.filter(
    (partner) =>
      (filter === "TODOS" || partner.type === filter) &&
      [partner.name, partner.company, partner.document, partner.email].some(
        (value) =>
          value
            ?.toLocaleLowerCase()
            .includes(search.trim().toLocaleLowerCase()),
      ),
  );
  const update = (field: string, value: string) =>
    setForm((current) => ({ ...current, [field]: value }));
  const submit = (event: React.FormEvent) => {
    event.preventDefault();
    setFeedback("");
    mutation.mutate();
  };

  return (
    <>
      <div className="section-heading">
        <div>
          <p className="eyebrow">REDE DE SERVIÇOS</p>
          <h1>Cadastros</h1>
        </div>
        <span className="date-chip">{partners.length} registros</span>
      </div>
      {feedback && (
        <p
          className={
            feedback.includes("sucesso") || feedback.includes("excluído")
              ? "success"
              : "error"
          }
        >
          {feedback}
        </p>
      )}
      <div className="grid partner-layout">
        <form className="panel partner-form" onSubmit={submit}>
          <div className="panel-title">
            <h3>{editingId ? "Editar cadastro" : "Novo cadastro"}</h3>
            <span>Dados do parceiro</span>
          </div>
          <div className="form-grid single-grid">
            <label>
              Tipo *
              <select
                value={form.type}
                onChange={(event) => update("type", event.target.value)}
              >
                {partnerTypeOptions.map(([value, label]) => (
                  <option value={value} key={value}>
                    {label}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Nome *
              <input
                required
                value={form.name}
                onChange={(event) => update("name", event.target.value)}
                placeholder="Nome completo ou razão social"
              />
            </label>
            <label>
              CPF/CNPJ
              <input
                value={form.document}
                onChange={(event) => update("document", event.target.value)}
              />
            </label>
            <label>
              Empresa
              <input
                value={form.company}
                onChange={(event) => update("company", event.target.value)}
              />
            </label>
            <label>
              E-mail
              <input
                type="email"
                value={form.email}
                onChange={(event) => update("email", event.target.value)}
              />
            </label>
            <label>
              Telefone
              <input
                value={form.phone}
                onChange={(event) => update("phone", event.target.value)}
              />
            </label>
            <label>
              Observações
              <textarea
                rows={3}
                value={form.notes}
                onChange={(event) => update("notes", event.target.value)}
              />
            </label>
          </div>
          <div className="form-actions">
            <button
              type="button"
              className="secondary"
              onClick={() => {
                setForm(emptyForm);
                setEditingId(null);
              }}
            >
              Limpar
            </button>
            <button className="primary" disabled={mutation.isPending}>
              {mutation.isPending
                ? "Salvando..."
                : editingId
                  ? "Atualizar cadastro"
                  : "Cadastrar parceiro"}
            </button>
          </div>
        </form>
        <div className="panel table-panel">
          <div className="panel-title">
            <h3>Parceiros cadastrados</h3>
            <span>{visible.length} exibidos</span>
          </div>
          <div className="partner-filters">
            <input
              type="search"
              placeholder="Buscar por nome, empresa ou documento"
              value={search}
              onChange={(event) => setSearch(event.target.value)}
            />
            <select
              value={filter}
              onChange={(event) =>
                setFilter(event.target.value as typeof filter)
              }
            >
              <option value="TODOS">Todos os tipos</option>
              <option value="LEILOEIRO">Leiloeiros</option>
              <option value="CORRETOR">Corretores</option>
              <option value="EMPREITEIRA">Empreiteiras</option>
              <option value="COMPRADOR">Compradores</option>
            </select>
          </div>
          {isLoading ? (
            <p>Carregando cadastros...</p>
          ) : (
            <div className="partner-list">
              {visible.map((partner) => (
                <div className="partner-item" key={partner.id}>
                  <div>
                    <span className="eyebrow">{typeLabels[partner.type]}</span>
                    <strong>{partner.name}</strong>
                    <p>
                      {partner.company ||
                        partner.email ||
                        partner.phone ||
                        "Sem informações adicionais"}
                    </p>
                  </div>
                  <div className="row-actions">
                    <button
                      className="table-action"
                      onClick={() => {
                        setForm({
                          type: partner.type,
                          name: partner.name,
                          document: partner.document ?? "",
                          email: partner.email ?? "",
                          phone: partner.phone ?? "",
                          company: partner.company ?? "",
                          notes: partner.notes ?? "",
                        });
                        setEditingId(partner.id);
                      }}
                    >
                      Editar
                    </button>
                    <button
                      className="table-action danger"
                      onClick={() => removeMutation.mutate(partner.id)}
                    >
                      Excluir
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </>
  );
}
