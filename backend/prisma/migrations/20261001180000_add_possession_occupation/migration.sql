CREATE TYPE "OccupationStatus" AS ENUM (
  'UNKNOWN',
  'VACANT',
  'OCCUPIED',
  'FORMER_OWNER',
  'TENANT',
  'THIRD_PARTY',
  'DISPUTED'
);

ALTER TABLE "property_possessions"
ADD COLUMN "occupation_status" "OccupationStatus",
ADD COLUMN "key_received_date" TIMESTAMP(3),
ADD COLUMN "inspection_date" TIMESTAMP(3);