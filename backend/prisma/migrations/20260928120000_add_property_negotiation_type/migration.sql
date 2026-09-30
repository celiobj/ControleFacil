CREATE TYPE "PropertyNegotiationType" AS ENUM (
  'LEILAO_SFI',
  'EXERCICIO_DIREITO_PREFERENCIA',
  'LICITACAO_ABERTA',
  'VENDA_ONLINE',
  'COMPRA_DIRETA'
);

ALTER TABLE "properties"
ADD COLUMN "negotiation_type" "PropertyNegotiationType";