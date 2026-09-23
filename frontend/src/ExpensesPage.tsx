import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { api, Property } from "./api";

type Partner = { id: string; name: string; company?: string };
const expenseCategories = [
  "ITBI",
  "CARTORIO",
  "ADVOGADO",
  "CONDOMINIO",
  "IPTU",
  "REFORMA",
  "LIMPEZA",
  "ENERGIA",
  "AGUA",
  "FINANCIAMENTO",
  "TAXAS",
  "OUTROS",
];
const initialForm = {
  propertyId: "",
  propertySearch: "",
  category: "OUTROS",
  contractor: "",
  description: "",
  amount: "",
  date: new Date().toISOString().slice(0, 10),
  notes: "",
};

export default function ExpensesPage() {
  const queryClient = useQueryClient();
  const [form, setForm] = useState(initialForm);
  const [feedback, setFeedback] = useState("");
  const { data: propertiesData } = useQuery({
    queryKey: ["properties"],
    queryFn: async () => (await api.get("/properties?limit=100")).data,
  });
  const { data: contractors = [] } = useQuery<Partner[]>({
    queryKey: ["partners", "EMPREITEIRA"],
    queryFn: async () => (await api.get("/partners?type=EMPREITEIRA")).data,
  });
  const { data: expenses = [], isLoading } = useQuery({
    queryKey: ["expenses"],
    queryFn: async () => (await api.get("/expenses")).data,
  });
  const properties: Property[] = (propertiesData?.data ?? []).filter(
    (property: Property) => property.status !== "CANCELADO",
  );
  const activePropertyIds = new Set(properties.map((property) => property.id));
  const visibleExpenses = expenses.filter((expense: any) =>
    activePropertyIds.has(expense.propertyId),
  );
  const mutation = useMutation({
    mutationFn: () => {
      const { propertySearch: _propertySearch, ...expense } = form;
      return api.post("/expenses", {
        ...expense,
        amount: Number(form.amount),
        contractor: form.category === "REFORMA" ? form.contractor : undefined,
      });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["expenses"] });
      queryClient.invalidateQueries({ queryKey: ["dashboard"] });
      setForm(initialForm);
      setFeedback("Despesa registrada com sucesso.");
    },
    onError: () => setFeedback("Não foi possível registrar a despesa."),
  });
  const update = (field: string, value: string) =>
    setForm((current) => ({ ...current, [field]: value }));
  const selectProperty = (value: string) => {
    const selected = properties.find(
      (property) => `${property.code} · ${property.title}` === value,
    );
    setForm((current) => ({
      ...current,
      propertySearch: value,
      propertyId: selected?.id ?? "",
    }));
  };
  const submit = (event: React.FormEvent) => {
    event.preventDefault();
    if (!form.propertyId) {
      setFeedback("Selecione um imóvel cadastrado.");
      return;
    }
    if (form.category === "REFORMA" && !form.contractor) {
      setFeedback("Selecione uma empreiteira cadastrada.");
      return;
    }
    setFeedback("");
    mutation.mutate();
  };

  return (
    <>
      <div className="section-heading">
        <div>
          <p className="eyebrow">CUSTOS DA OPERAÇÃO</p>
          <h1>Despesas</h1>
        </div>
      </div>
      {feedback && (
        <p className={feedback.startsWith("Despesa") ? "success" : "error"}>
          {feedback}
        </p>
      )}
      <form className="panel expense-form" onSubmit={submit}>
        <div className="panel-title">
          <h3>Registrar despesa</h3>
          <span>O custo será incluído no resultado do imóvel</span>
        </div>
        <div className="form-grid">
          <label>
            Imóvel *
            <input
              required
              list="expense-property-options"
              value={form.propertySearch}
              onChange={(event) => selectProperty(event.target.value)}
              placeholder="Pesquise um imóvel cadastrado"
            />
            <datalist id="expense-property-options">
              {properties.map((property) => (
                <option
                  value={`${property.code} · ${property.title}`}
                  key={property.id}
                >
                  {property.city} · {property.neighborhood}
                </option>
              ))}
            </datalist>
          </label>
          <label>
            Categoria *
            <select
              value={form.category}
              onChange={(event) => update("category", event.target.value)}
            >
              {expenseCategories.map((category) => (
                <option key={category}>{category}</option>
              ))}
            </select>
          </label>
          {form.category === "REFORMA" && (
            <label>
              Empreiteira *
              <input
                required
                list="contractor-options"
                value={form.contractor}
                onChange={(event) => update("contractor", event.target.value)}
                placeholder="Pesquise uma empreiteira cadastrada"
              />
              <datalist id="contractor-options">
                {contractors.map((partner) => (
                  <option value={partner.name} key={partner.id}>
                    {partner.company || partner.name}
                  </option>
                ))}
              </datalist>
            </label>
          )}
          <label>
            Descrição *
            <input
              required
              value={form.description}
              onChange={(event) => update("description", event.target.value)}
              placeholder="Ex.: taxa de cartório"
            />
          </label>
          <label>
            Valor *
            <input
              required
              min="0.01"
              step="0.01"
              type="number"
              value={form.amount}
              onChange={(event) => update("amount", event.target.value)}
            />
          </label>
          <label>
            Data *
            <input
              required
              type="date"
              value={form.date}
              onChange={(event) => update("date", event.target.value)}
            />
          </label>
          <label className="wide">
            Observações
            <textarea
              rows={2}
              value={form.notes}
              onChange={(event) => update("notes", event.target.value)}
            />
          </label>
        </div>
        <div className="form-actions">
          <button className="primary" disabled={mutation.isPending}>
            {mutation.isPending ? "Salvando..." : "Registrar despesa"}
          </button>
        </div>
      </form>
      <div className="panel table-panel">
        <div className="panel-title">
          <h3>Despesas registradas</h3>
          <span>{visibleExpenses.length} lançamentos</span>
        </div>
        {isLoading ? (
          <p>Carregando despesas...</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Data</th>
                <th>Imóvel</th>
                <th>Categoria</th>
                <th>Descrição</th>
                <th>Empreiteira</th>
                <th>Valor</th>
              </tr>
            </thead>
            <tbody>
              {visibleExpenses.map((expense: any) => (
                <tr key={expense.id}>
                  <td>{new Date(expense.date).toLocaleDateString("pt-BR")}</td>
                  <td className="code">
                    {expense.property?.code ?? expense.propertyId}
                  </td>
                  <td>
                    <span className="tag">{expense.category}</span>
                  </td>
                  <td>{expense.description}</td>
                  <td>{expense.contractor || "-"}</td>
                  <td>
                    <strong>
                      {Number(expense.amount).toLocaleString("pt-BR", {
                        style: "currency",
                        currency: "BRL",
                      })}
                    </strong>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </>
  );
}
