ALTER TYPE "PropertyEventType" ADD VALUE 'ANALISE';
ALTER TYPE "PropertyEventType" ADD VALUE 'LANCE';
ALTER TYPE "PropertyEventType" ADD VALUE 'ARREMATACAO';
ALTER TYPE "PropertyEventType" ADD VALUE 'PAGAMENTO';
ALTER TYPE "PropertyEventType" ADD VALUE 'FGTS';
ALTER TYPE "PropertyEventType" ADD VALUE 'FINANCIAMENTO';
ALTER TYPE "PropertyEventType" ADD VALUE 'ITBI';
ALTER TYPE "PropertyEventType" ADD VALUE 'CARTORIO';
ALTER TYPE "PropertyEventType" ADD VALUE 'REFORMA';
ALTER TYPE "PropertyEventType" ADD VALUE 'DOCUMENTO';
ALTER TYPE "PropertyEventType" ADD VALUE 'REGULARIZACAO';
ALTER TYPE "PropertyEventType" ADD VALUE 'POSSE';
ALTER TYPE "PropertyEventType" ADD VALUE 'DESPESA';
ALTER TYPE "PropertyEventType" ADD VALUE 'VENDA';
ALTER TYPE "PropertyEventType" ADD VALUE 'OUTRO';

ALTER TABLE "property_events"
ADD COLUMN "user_id" TEXT;

CREATE INDEX "property_events_user_id_idx" ON "property_events"("user_id");

ALTER TABLE "property_events"
ADD CONSTRAINT "property_events_user_id_fkey"
FOREIGN KEY ("user_id") REFERENCES "users"("id")
ON DELETE SET NULL ON UPDATE CASCADE;