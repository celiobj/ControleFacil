-- CreateEnum
CREATE TYPE "Role" AS ENUM ('ADMIN', 'INVESTIDOR', 'CONSULTA');

-- CreateEnum
CREATE TYPE "PropertyStatus" AS ENUM ('EM_ANALISE', 'ARREMATADO', 'REGULARIZACAO', 'REFORMA', 'PRONTO_PARA_VENDA', 'VENDIDO', 'CANCELADO');

-- CreateEnum
CREATE TYPE "PropertyType" AS ENUM ('CASA', 'APARTAMENTO', 'TERRENO', 'COMERCIAL', 'RURAL', 'OUTRO');

-- CreateEnum
CREATE TYPE "ExpenseCategory" AS ENUM ('ITBI', 'CARTORIO', 'ADVOGADO', 'CONDOMINIO', 'IPTU', 'REFORMA', 'LIMPEZA', 'ENERGIA', 'AGUA', 'FINANCIAMENTO', 'TAXAS', 'OUTROS');

-- CreateEnum
CREATE TYPE "RenovationStatus" AS ENUM ('PLANEJADA', 'EM_ANDAMENTO', 'CONCLUIDA');

-- CreateTable
CREATE TABLE "users" (
    "id" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "email" TEXT NOT NULL,
    "password_hash" TEXT NOT NULL,
    "role" "Role" NOT NULL DEFAULT 'INVESTIDOR',
    "active" BOOLEAN NOT NULL DEFAULT true,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "users_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "properties" (
    "id" TEXT NOT NULL,
    "code" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "type" "PropertyType" NOT NULL,
    "address" TEXT NOT NULL,
    "number" TEXT,
    "complement" TEXT,
    "neighborhood" TEXT NOT NULL,
    "city" TEXT NOT NULL,
    "state" TEXT NOT NULL,
    "zip_code" TEXT,
    "total_area" DECIMAL(12,2),
    "built_area" DECIMAL(12,2),
    "registry_number" TEXT,
    "description" TEXT,
    "status" "PropertyStatus" NOT NULL DEFAULT 'EM_ANALISE',
    "notes" TEXT,
    "created_at" TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updated_at" TIMESTAMP(3) NOT NULL,

    CONSTRAINT "properties_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "auctions" (
    "id" TEXT NOT NULL,
    "property_id" TEXT NOT NULL,
    "auctioneer" TEXT NOT NULL,
    "portal" TEXT,
    "process_number" TEXT,
    "appraisal_value" DECIMAL(14,2),
    "minimum_value" DECIMAL(14,2),
    "auction_value" DECIMAL(14,2) NOT NULL,
    "auction_date" TIMESTAMP(3) NOT NULL,
    "acquisition_date" TIMESTAMP(3),
    "notes" TEXT,

    CONSTRAINT "auctions_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "expenses" (
    "id" TEXT NOT NULL,
    "property_id" TEXT NOT NULL,
    "category" "ExpenseCategory" NOT NULL,
    "description" TEXT NOT NULL,
    "amount" DECIMAL(14,2) NOT NULL,
    "date" TIMESTAMP(3) NOT NULL,
    "receipt_path" TEXT,
    "notes" TEXT,

    CONSTRAINT "expenses_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "renovations" (
    "id" TEXT NOT NULL,
    "property_id" TEXT NOT NULL,
    "description" TEXT NOT NULL,
    "supplier" TEXT,
    "planned_amount" DECIMAL(14,2),
    "actual_amount" DECIMAL(14,2),
    "start_date" TIMESTAMP(3),
    "end_date" TIMESTAMP(3),
    "status" "RenovationStatus" NOT NULL DEFAULT 'PLANEJADA',

    CONSTRAINT "renovations_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "sales" (
    "id" TEXT NOT NULL,
    "property_id" TEXT NOT NULL,
    "buyer" TEXT NOT NULL,
    "sale_amount" DECIMAL(14,2) NOT NULL,
    "sale_date" TIMESTAMP(3) NOT NULL,
    "brokerage" DECIMAL(14,2) NOT NULL DEFAULT 0,
    "taxes" DECIMAL(14,2) NOT NULL DEFAULT 0,
    "notes" TEXT,

    CONSTRAINT "sales_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "users_email_key" ON "users"("email");

-- CreateIndex
CREATE UNIQUE INDEX "properties_code_key" ON "properties"("code");

-- CreateIndex
CREATE INDEX "properties_city_neighborhood_status_type_idx" ON "properties"("city", "neighborhood", "status", "type");

-- CreateIndex
CREATE UNIQUE INDEX "auctions_property_id_key" ON "auctions"("property_id");

-- CreateIndex
CREATE INDEX "expenses_property_id_category_date_idx" ON "expenses"("property_id", "category", "date");

-- CreateIndex
CREATE UNIQUE INDEX "sales_property_id_key" ON "sales"("property_id");

-- AddForeignKey
ALTER TABLE "auctions" ADD CONSTRAINT "auctions_property_id_fkey" FOREIGN KEY ("property_id") REFERENCES "properties"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "expenses" ADD CONSTRAINT "expenses_property_id_fkey" FOREIGN KEY ("property_id") REFERENCES "properties"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "renovations" ADD CONSTRAINT "renovations_property_id_fkey" FOREIGN KEY ("property_id") REFERENCES "properties"("id") ON DELETE CASCADE ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "sales" ADD CONSTRAINT "sales_property_id_fkey" FOREIGN KEY ("property_id") REFERENCES "properties"("id") ON DELETE CASCADE ON UPDATE CASCADE;
