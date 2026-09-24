import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { api, Property } from "./api";

type Partner = { id: string; name: string; company?: string };

type PurchaseForm = {
  propertyId: string;
  auctioneer: string;
  broker: string;
  portal: string;
  processNumber: string;
  auctionValue: string;
  auctionDate: string;
  notes: string;
};

const initialPurchase: PurchaseForm = {
  propertyId: "",
  auctioneer: "",
  broker: "",
  portal: "",
  processNumber: "",
  auctionValue: "",
  auctionDate: new Date().toISOString().slice(0, 10),
  notes: "",
};

export default function PurchaseTransaction() {
  const queryClient = useQueryClient();
  const [purchase, setPurchase] = useState(initialPurchase);
  const [propertySearch, setPropertySearch] = useState("");
  const [feedback, setFeedback] = useState("");
  const { data: propertiesData } = useQuery({
    queryKey: ["properties"],
    queryFn: async () => (await api.get("/properties?limit=100")).data,
  });
  const { data: auctioneers = [] } = useQuery<Partner[]>({
    queryKey: ["partners", "LEILOEIRO"],
    queryFn: async () => (await api.get("/partners?type=LEILOEIRO")).data,
  });
  const { data: brokers = [] } = useQuery<Partner[]>({
    queryKey: ["partners", "CORRETOR"],
    queryFn: async () => (await api.get("/partners?type=CORRETOR")).data,
  });
  const properties: Property[] = (propertiesData?.data ?? []).filter(
    (property: Property) => property.status === "EM_ANALISE",
  );
  const mutation = useMutation({
    mutationFn: () =>
      api.post("/auctions", {
        ...purchase,
        auctionValue: Number(purchase.auctionValue),
        auctionDate: purchase.auctionDate,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["dashboard"] });
      queryClient.invalidateQueries({ queryKey: ["property", purchase.propertyId] });
      setPurchase(initialPurchase);
      setPropertySearch("");
      setFeedback("Compra registrada com sucesso.");
    },
    onError: (error: any) =>
      setFeedback(
        error.response?.data?.message ?? "Não foi possível registrar a compra.",
      ),
  });
  const update = (field: keyof PurchaseForm, value: string) =>
    setPurchase((current) => ({ ...current, [field]: value }));
  const propertyLabel = (property: Property) =>
    `${property.code} · ${property.title}`;
  const selectProperty = (value: string) => {
    setPropertySearch(value);
    const selected = properties.find(
      (property) => propertyLabel(property) === value,
    );
    update("propertyId", selected?.id ?? "");
  };

  return (
    <>
      <div className="section-heading operation-heading">
        <div>
          <p className="eyebrow">CICLO DO ATIVO</p>
          <h2>Compras</h2>
        </div>
      </div>
      {feedback && (
        <p className={feedback.includes("sucesso") ? "success" : "error"}>
          {feedback}
        </p>
      )}
      <form
        className="panel"
        onSubmit={(event) => {
          event.preventDefault();
          if (!purchase.propertyId) {
            setFeedback("Selecione um imóvel cadastrado.");
            return;
          }
          setFeedback("");
          mutation.mutate();
        }}
      >
        <div className="panel-title">
          <h3>Registrar compra</h3>
          <span>Arrematação do leilão</span>
        </div>
        <div className="form-grid single-grid">
          <label>
            Imóvel *
            <input
              required
              list="property-options"
              value={propertySearch}
              onChange={(event) => selectProperty(event.target.value)}
              placeholder="Pesquise um imóvel cadastrado"
            />
            <datalist id="property-options">
              {properties.map((property) => (
                <option value={propertyLabel(property)} key={property.id}>
                  {property.city} · {property.neighborhood}
                </option>
              ))}
            </datalist>
          </label>
          <label>
            Leiloeiro *
            <input
              required
              list="auctioneer-options"
              value={purchase.auctioneer}
              onChange={(event) => update("auctioneer", event.target.value)}
              placeholder="Pesquise um leiloeiro cadastrado"
            />
            <datalist id="auctioneer-options">
              {auctioneers.map((partner) => (
                <option value={partner.name} key={partner.id}>
                  {partner.company || partner.name}
                </option>
              ))}
            </datalist>
          </label>
          <label>
            Corretor
            <input
              list="broker-options"
              value={purchase.broker}
              onChange={(event) => update("broker", event.target.value)}
              placeholder="Pesquise um corretor cadastrado"
            />
            <datalist id="broker-options">
              {brokers.map((partner) => (
                <option value={partner.name} key={partner.id}>
                  {partner.company || partner.name}
                </option>
              ))}
            </datalist>
          </label>
          <label>
            Valor da arrematação *
            <input
              required
              type="number"
              min="0.01"
              step="0.01"
              value={purchase.auctionValue}
              onChange={(event) => update("auctionValue", event.target.value)}
            />
          </label>
          <label>
            Data da compra *
            <input
              required
              type="date"
              value={purchase.auctionDate}
              onChange={(event) => update("auctionDate", event.target.value)}
            />
          </label>
          <label>
            Portal
            <input
              value={purchase.portal}
              onChange={(event) => update("portal", event.target.value)}
            />
          </label>
          <label>
            Número do processo
            <input
              value={purchase.processNumber}
              onChange={(event) => update("processNumber", event.target.value)}
            />
          </label>
          <label>
            Observações
            <textarea
              rows={2}
              value={purchase.notes}
              onChange={(event) => update("notes", event.target.value)}
            />
          </label>
        </div>
        <button className="primary" disabled={mutation.isPending}>
          {mutation.isPending ? "Salvando..." : "Registrar compra"}
        </button>
      </form>
    </>
  );
}
