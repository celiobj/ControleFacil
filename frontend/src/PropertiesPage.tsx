import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { api, Property } from "./api";

export default function PropertiesPage() {
  const navigate = useNavigate();
  const { data, isLoading } = useQuery({
    queryKey: ["properties"],
    queryFn: async () => (await api.get("/properties?limit=100")).data,
  });
  const properties: Property[] = data?.data ?? [];
  return (
    <>
      <div className="section-heading">
        <div>
          <p className="eyebrow">ATIVOS</p>
          <h1>Imóveis</h1>
        </div>
      </div>
      <div className="panel table-panel">
        {isLoading ? (
          <p>Carregando imóveis...</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Código</th>
                <th>Imóvel</th>
                <th>Localização</th>
                <th>Tipo</th>
                <th>Status</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {properties.map((property) => (
                <tr key={property.id}>
                  <td className="code">{property.code}</td>
                  <td>
                    <strong>{property.title}</strong>
                  </td>
                  <td>
                    {property.neighborhood}, {property.city}
                  </td>
                  <td>{property.type}</td>
                  <td>
                    <span className="tag">
                      {property.status.replaceAll("_", " ")}
                    </span>
                  </td>
                  <td style={{ display: "table-cell", visibility: "visible" }}>
                    <button
                      style={{
                        display: "block",
                        visibility: "visible",
                        opacity: 1,
                        color: "black",
                        background: "white",
                        border: "1px solid black",
                        padding: "8px 12px",
                        cursor: "pointer",
                      }}
                      onClick={() =>
                        navigate(`/properties/${property.id}/checklist`)
                      }
                    >
                      Checklist
                    </button>
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
