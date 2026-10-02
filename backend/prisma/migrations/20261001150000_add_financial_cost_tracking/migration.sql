CREATE TYPE "FinancialCostCategory" AS ENUM (
  'ACQUISITION',
  'REGULARIZATION',
  'POSSESSION',
  'RENOVATION',
  'OPERATIONAL',
  'FINANCING',
  'SALE',
  'OTHER'
);

ALTER TABLE "expenses"
ADD COLUMN "financial_category" "FinancialCostCategory",
ADD COLUMN "renovation_id" TEXT;

ALTER TABLE "renovations"
ADD COLUMN "contracted_amount" DECIMAL(14,2);

CREATE INDEX "expenses_renovation_id_date_idx"
ON "expenses"("renovation_id", "date");

ALTER TABLE "expenses"
ADD CONSTRAINT "expenses_renovation_id_fkey"
FOREIGN KEY ("renovation_id") REFERENCES "renovations"("id")
ON DELETE SET NULL ON UPDATE CASCADE;