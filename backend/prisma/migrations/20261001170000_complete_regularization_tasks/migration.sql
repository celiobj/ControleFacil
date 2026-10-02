CREATE TYPE "RegularizationTaskCategory" AS ENUM (
  'CAIXA',
  'CARTORIO',
  'PREFEITURA',
  'ITBI',
  'CONDOMINIO',
  'DOCUMENTACAO',
  'JURIDICO',
  'OUTROS'
);

ALTER TYPE "RegularizationStatus" ADD VALUE 'PENDENTE';
ALTER TYPE "RegularizationStatus" ADD VALUE 'EM_ANDAMENTO';
ALTER TYPE "RegularizationStatus" ADD VALUE 'CONCLUIDA';
ALTER TYPE "RegularizationStatus" ADD VALUE 'BLOQUEADA';

ALTER TABLE "regularization_tasks"
ADD COLUMN "category" "RegularizationTaskCategory" NOT NULL DEFAULT 'OUTROS',
ADD COLUMN "cost" DECIMAL(14,2),
ADD COLUMN "sort_order" INTEGER NOT NULL DEFAULT 0;