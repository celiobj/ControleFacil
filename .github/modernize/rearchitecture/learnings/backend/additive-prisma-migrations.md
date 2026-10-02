# Additive Prisma Migrations

Keep brownfield lifecycle additions isolated in a new timestamped migration with optional relations and no historical backfill.

## What Happened
For ControleFacil t3, the schema contract required new PostgreSQL enums and lifecycle tables while preserving V1 auction and financial tables. The migration was authored as CREATE TYPE/TABLE/INDEX plus ADD CONSTRAINT only because the local Prisma executable was unavailable for generated migration output.

## Takeaway
Use explicit snake_case mappings and unique indexes for one-to-one Property relations. Validate the schema first, then audit migration SQL for DROP/TRUNCATE and V1 table alterations before handoff.

## History
- 2026-10-01 (ControleFacil/t3): initial
