import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { api, Property } from "./api";

type Buyer = { id: string; name: string; company?: string };
type SaleForm = {
  propertyId: string;
  propertySearch: string;
  buyer: string;
  saleAmount: string;
  saleDate: string;
  brokerage: string;
  taxes: string;
  notes: string;
};
const initialSale: SaleForm = {
  propertyId: "",
  propertySearch: "",
  buyer: "",
  saleAmount: "",
  saleDate: new Date().toISOString().slice(0, 10),
  brokerage: "0",
  taxes: "0",
  notes: "",
};

export default function SaleTransaction() {
  const queryClient = useQueryClient();
  const [sale, setSale] = useState(initialSale);
  const [feedback, setFeedback] = useState("");
  const { data: propertiesData } = useQuery({
    queryKey: ["properties"],
    queryFn: async () => (await api.get("/properties?limit=100")).data,
  });
  const { data: buyers = [] } = useQuery<Buyer[]>({
    queryKey: ["partners", "COMPRADOR"],
    queryFn: async () => (await api.get("/partners?type=COMPRADOR")).data,
  });
  const properties: Property[] = (propertiesData?.data ?? []).filter(
    (property: Property) => property.status !== "CANCELADO",
  );
  const mutation = useMutation({
    mutationFn: () => {
      const { propertySearch: _propertySearch, ...payload } = sale;
      return api.post("/sales", {
        ...payload,
        saleAmount: Number(sale.saleAmount),
        brokerage: Number(sale.brokerage),
        taxes: Number(sale.taxes),
        saleDate: sale.saleDate,
      });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["dashboard"] });
      setSale(initialSale);
      setFeedback("Venda registrada com sucesso.");
    },
    onError: () => setFeedback("Não foi possível registrar a venda."),
  });
  const propertyLabel = (property: Property) =>
    `${property.code} · ${property.title}`;
  const selectProperty = (value: string) => {
    const selected = properties.find(
      (property) => propertyLabel(property) === value,
    );
    setSale((current) => ({
      ...current,
      propertySearch: value,
      propertyId: selected?.id ?? "",
    }));
  };
  const update = (field: keyof SaleForm, value: string) =>
    setSale((current) => ({ ...current, [field]: value }));
  const submit = (event: React.FormEvent) => {
    event.preventDefault();
    if (!sale.propertyId) {
      setFeedback("Selecione um imóvel cadastrado.");
      return;
    }
    if (!buyers.some((buyer) => buyer.name === sale.buyer)) {
      setFeedback("Selecione um comprador cadastrado.");
      return;
    }
    setFeedback("");
    mutation.mutate();
  };

  return (
    <>
      <div className="section-heading operation-heading">
        <div>
          <p className="eyebrow">CICLO DO ATIVO</p>
          <h2>Vendas</h2>
        </div>
      </div>
      {feedback && (
        <p className={feedback.includes("sucesso") ? "success" : "error"}>
          {feedback}
        </p>
      )}
      <form className="panel" onSubmit={submit}>
        <div className="panel-title">
          <h3>Registrar venda</h3>
          <span>Saída do imóvel</span>
        </div>
        <div className="form-grid single-grid">
          <label>
            Imóvel *
            <input
              required
              list="sale-property-options"
              value={sale.propertySearch}
              onChange={(event) => selectProperty(event.target.value)}
              placeholder="Pesquise um imóvel cadastrado"
            />
            <datalist id="sale-property-options">
              {properties.map((property) => (
                <option value={propertyLabel(property)} key={property.id}>
                  {property.city} · {property.neighborhood}
                </option>
              ))}
            </datalist>
          </label>
          <label>
            Comprador *
            <input
              required
              list="buyer-options"
              value={sale.buyer}
              onChange={(event) => update("buyer", event.target.value)}
              placeholder="Pesquise um comprador cadastrado"
            />
            <datalist id="buyer-options">
              {buyers.map((buyer) => (
                <option value={buyer.name} key={buyer.id}>
                  {buyer.company || buyer.name}
                </option>
              ))}
            </datalist>
          </label>
          <label>
            Valor da venda *
            <input
              required
              type="number"
              min="0.01"
              step="0.01"
              value={sale.saleAmount}
              onChange={(event) => update("saleAmount", event.target.value)}
            />
          </label>
          <label>
            Data da venda *
            <input
              required
              type="date"
              value={sale.saleDate}
              onChange={(event) => update("saleDate", event.target.value)}
            />
          </label>
          <label>
            Corretagem
            <input
              type="number"
              min="0"
              step="0.01"
              value={sale.brokerage}
              onChange={(event) => update("brokerage", event.target.value)}
            />
          </label>
          <label>
            Impostos
            <input
              type="number"
              min="0"
              step="0.01"
              value={sale.taxes}
              onChange={(event) => update("taxes", event.target.value)}
            />
          </label>
          <label>
            Observações
            <textarea
              rows={2}
              value={sale.notes}
              onChange={(event) => update("notes", event.target.value)}
            />
          </label>
        </div>
        <button className="primary" disabled={mutation.isPending}>
          {mutation.isPending ? "Salvando..." : "Registrar venda"}
        </button>
      </form>
    </>
  );
}
