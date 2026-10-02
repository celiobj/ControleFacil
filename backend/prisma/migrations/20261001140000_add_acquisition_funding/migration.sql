CREATE TYPE "AcquisitionPaymentMethod" AS ENUM (
  'OWN_RESOURCES',
  'FGTS',
  'FINANCING',
  'FGTS_AND_OWN_RESOURCES',
  'FINANCING_AND_OWN_RESOURCES',
  'FGTS_AND_FINANCING',
  'MIXED',
  'OTHER'
);

ALTER TABLE "acquisitions"
ADD COLUMN "auctioneer_commission" DECIMAL(14,2),
ADD COLUMN "own_resources_amount" DECIMAL(14,2),
ADD COLUMN "fgts_amount" DECIMAL(14,2),
ADD COLUMN "financing_amount" DECIMAL(14,2),
ADD COLUMN "other_resources_amount" DECIMAL(14,2),
ADD COLUMN "acquisition_date" TIMESTAMP(3),
ADD COLUMN "payment_date" TIMESTAMP(3),
ADD COLUMN "payment_method" "AcquisitionPaymentMethod";