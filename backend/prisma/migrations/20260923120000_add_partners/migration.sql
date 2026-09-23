CREATE TYPE "PartnerType" AS ENUM ('LEILOEIRO', 'CORRETOR', 'EMPREITEIRA');

CREATE TABLE "partners" (
    "id" TEXT NOT NULL,
    "type" "PartnerType" NOT NULL,
    "name" TEXT NOT NULL,
    "document" VARCHAR(18),
    "email" TEXT,
    "phone" VARCHAR(30),
    "company" VARCHAR(120),
    "notes" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP(3) NOT NULL,
    CONSTRAINT "partners_pkey" PRIMARY KEY ("id")
);

CREATE UNIQUE INDEX "partners_document_key" ON "partners"("document");
CREATE INDEX "partners_type_name_idx" ON "partners"("type", "name");
