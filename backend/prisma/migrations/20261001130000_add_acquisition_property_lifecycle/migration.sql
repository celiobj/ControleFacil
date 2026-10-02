CREATE TYPE "AcquisitionStatus" AS ENUM ('PLANNED', 'COMPLETED', 'CANCELLED');
CREATE TYPE "AcquisitionSource" AS ENUM ('AUCTION', 'DIRECT', 'PREFERENCE', 'ONLINE', 'OTHER');
CREATE TYPE "PropertyDocumentType" AS ENUM ('REGISTRY', 'AUCTION_NOTICE', 'PURCHASE_CONTRACT', 'TAX', 'CERTIFICATE', 'PHOTO', 'OTHER');
CREATE TYPE "PropertyDocumentStatus" AS ENUM ('PENDING', 'VALID', 'EXPIRED', 'REJECTED');
CREATE TYPE "RegularizationStatus" AS ENUM ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED', 'BLOCKED');
CREATE TYPE "RegularizationTaskStatus" AS ENUM ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'BLOCKED', 'CANCELLED');
CREATE TYPE "PossessionStatus" AS ENUM ('NOT_TAKEN', 'PENDING', 'TAKEN', 'DISPUTED', 'VACANT');
CREATE TYPE "PropertyEventType" AS ENUM ('NOTE', 'STATUS_CHANGE', 'DOCUMENT', 'POSSESSION', 'REGULARIZATION', 'EXPENSE', 'SALE', 'OTHER');
CREATE TYPE "SaleScenarioStatus" AS ENUM ('DRAFT', 'ACTIVE', 'SELECTED', 'ARCHIVED');

CREATE TABLE "acquisitions" (
    "id" TEXT NOT NULL,
    "property_id" TEXT NOT NULL,
    "purchase_price" DECIMAL(14,2),
    "purchase_date" TIMESTAMP(3),
    "status" "AcquisitionStatus" NOT NULL DEFAULT 'PLANNED',
    "source" "AcquisitionSource" NOT NULL DEFAULT 'AUCTION',
    "notes" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "acquisitions_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "property_documents" (
    "id" TEXT NOT NULL,
    "property_id" TEXT NOT NULL,
    "type" "PropertyDocumentType" NOT NULL,
    "status" "PropertyDocumentStatus" NOT NULL DEFAULT 'PENDING',
    "title" TEXT NOT NULL,
    "file_path" TEXT,
    "issued_at" TIMESTAMP(3),
    "expires_at" TIMESTAMP(3),
    "notes" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "property_documents_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "property_regularizations" (
    "id" TEXT NOT NULL,
    "property_id" TEXT NOT NULL,
    "status" "RegularizationStatus" NOT NULL DEFAULT 'NOT_STARTED',
    "started_at" TIMESTAMP(3),
    "completed_at" TIMESTAMP(3),
    "notes" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "property_regularizations_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "regularization_tasks" (
    "id" TEXT NOT NULL,
    "regularization_id" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "description" TEXT,
    "status" "RegularizationTaskStatus" NOT NULL DEFAULT 'PENDING',
    "due_date" TIMESTAMP(3),
    "completed_at" TIMESTAMP(3),
    "responsible" TEXT,
    "notes" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "regularization_tasks_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "property_possessions" (
    "id" TEXT NOT NULL,
    "property_id" TEXT NOT NULL,
    "status" "PossessionStatus" NOT NULL DEFAULT 'NOT_TAKEN',
    "possession_date" TIMESTAMP(3),
    "occupant" TEXT,
    "notes" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "property_possessions_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "property_events" (
    "id" TEXT NOT NULL,
    "property_id" TEXT NOT NULL,
    "type" "PropertyEventType" NOT NULL,
    "title" TEXT NOT NULL,
    "description" TEXT,
    "occurred_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "metadata" JSONB,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "property_events_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "sale_scenarios" (
    "id" TEXT NOT NULL,
    "property_id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "status" "SaleScenarioStatus" NOT NULL DEFAULT 'DRAFT',
    "projected_sale_price" DECIMAL(14,2) NOT NULL,
    "projected_brokerage" DECIMAL(14,2) NOT NULL DEFAULT 0,
    "projected_taxes" DECIMAL(14,2) NOT NULL DEFAULT 0,
    "expected_sale_date" TIMESTAMP(3),
    "notes" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "sale_scenarios_pkey" PRIMARY KEY ("id")
);

CREATE UNIQUE INDEX "acquisitions_property_id_key" ON "acquisitions"("property_id");
CREATE INDEX "acquisitions_status_purchase_date_idx" ON "acquisitions"("status", "purchase_date");
CREATE INDEX "property_documents_property_id_type_status_idx" ON "property_documents"("property_id", "type", "status");
CREATE INDEX "property_documents_expires_at_idx" ON "property_documents"("expires_at");
CREATE UNIQUE INDEX "property_regularizations_property_id_key" ON "property_regularizations"("property_id");
CREATE INDEX "property_regularizations_status_idx" ON "property_regularizations"("status");
CREATE INDEX "regularization_tasks_regularization_id_status_due_date_idx" ON "regularization_tasks"("regularization_id", "status", "due_date");
CREATE UNIQUE INDEX "property_possessions_property_id_key" ON "property_possessions"("property_id");
CREATE INDEX "property_possessions_status_possession_date_idx" ON "property_possessions"("status", "possession_date");
CREATE INDEX "property_events_property_id_occurred_at_idx" ON "property_events"("property_id", "occurred_at");
CREATE INDEX "property_events_property_id_type_occurred_at_idx" ON "property_events"("property_id", "type", "occurred_at");
CREATE INDEX "sale_scenarios_property_id_status_idx" ON "sale_scenarios"("property_id", "status");
CREATE INDEX "sale_scenarios_property_id_expected_sale_date_idx" ON "sale_scenarios"("property_id", "expected_sale_date");

ALTER TABLE "acquisitions"
ADD CONSTRAINT "acquisitions_property_id_fkey"
FOREIGN KEY ("property_id") REFERENCES "properties"("id") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "property_documents"
ADD CONSTRAINT "property_documents_property_id_fkey"
FOREIGN KEY ("property_id") REFERENCES "properties"("id") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "property_regularizations"
ADD CONSTRAINT "property_regularizations_property_id_fkey"
FOREIGN KEY ("property_id") REFERENCES "properties"("id") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "regularization_tasks"
ADD CONSTRAINT "regularization_tasks_regularization_id_fkey"
FOREIGN KEY ("regularization_id") REFERENCES "property_regularizations"("id") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "property_possessions"
ADD CONSTRAINT "property_possessions_property_id_fkey"
FOREIGN KEY ("property_id") REFERENCES "properties"("id") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "property_events"
ADD CONSTRAINT "property_events_property_id_fkey"
FOREIGN KEY ("property_id") REFERENCES "properties"("id") ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE "sale_scenarios"
ADD CONSTRAINT "sale_scenarios_property_id_fkey"
FOREIGN KEY ("property_id") REFERENCES "properties"("id") ON DELETE CASCADE ON UPDATE CASCADE;
