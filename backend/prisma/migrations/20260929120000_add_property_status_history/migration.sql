CREATE TABLE "property_status_history" (
    "id" TEXT NOT NULL,
    "property_id" TEXT NOT NULL,
    "from_status" "PropertyStatus",
    "to_status" "PropertyStatus" NOT NULL,
    "changed_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT "property_status_history_pkey" PRIMARY KEY ("id")
);

CREATE INDEX "property_status_history_property_id_changed_at_idx"
ON "property_status_history"("property_id", "changed_at");

ALTER TABLE "property_status_history"
ADD CONSTRAINT "property_status_history_property_id_fkey"
FOREIGN KEY ("property_id") REFERENCES "properties"("id") ON DELETE CASCADE ON UPDATE CASCADE;