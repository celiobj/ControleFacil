# t1 — Existing Contracts and Additive Domain Design

## Scope
This artifact is the implementation contract for the requested brownfield enhancement. It is based on the current NestJS 11 + Prisma 6 PostgreSQL application on branch `secondVersion`. No production source files or existing migrations were modified by this task.

## Upstream Artifacts Consumed
- `.github/modernize/rearchitecture/artifacts/project-profile.yaml` — confirmed the single NestJS/Prisma module, V1 preservation requirement, and task topology.
- `.github/modernize/rearchitecture/board.md` — confirmed task dependencies and that t3 owns Prisma changes while t4 owns services and REST.
- `backend/prisma/schema.prisma` — source of current model names, enum style, maps, relations, and V1 financial fields.
- `backend/src/dashboard/dashboard.service.ts` and `backend/src/reports/reports.controller.ts` — current financial calculation and report contract.
- `backend/src/operations/operations.service.ts` and `backend/src/operations/operations.controller.ts` — current V1 auction/sale write paths that must remain unchanged.
- `backend/src/main.ts`, existing DTOs/controllers, and `backend/package.json` — validation pipe, Swagger/JWT conventions, and build/test commands.

## Evidence Mapping
- `schema.prisma: Property, Auction, Expense, Renovation, Sale` -> additive relations below and FinancialService fallback.
- `operations.controller.ts: /auctions and /sales` -> preserve existing endpoints; Acquisition is a separate resource.
- `dashboard.service.ts: summary()` -> delegate cost/profit/ROI arithmetic to FinancialService while preserving response keys.
- `reports.controller.ts: /reports/profit and /reports/profit.csv` -> continue consuming dashboard-shaped results.
- `main.ts: global ValidationPipe({ whitelist: true, transform: true })` -> Acquisition DTOs must use class-validator decorators and Swagger metadata.

## Existing Contracts That Must Not Break

### V1 persistence and operations
- `Property.id` is a UUID primary key; all new records reference it using `property_id`.
- `Auction` is one-to-one with `Property` through unique `property_id`; `auctionValue` is required and is the current purchase-cost source.
- `Sale` is one-to-one with `Property`; `saleAmount`, `brokerage`, and `taxes` feed current profit calculations.
- `Expense` and `Renovation` are one-to-many from `Property`; actual renovation amount falls back to planned amount in the current calculation.
- Existing `POST /auctions`, `GET /auctions`, `POST /sales`, `GET /sales`, `GET /dashboard`, and report routes remain available with their current payload/response shape.
- Existing migrations are immutable. The new migration must only create new enum types/tables/indexes/foreign keys and must not issue `DROP`, `TRUNCATE`, or destructive alterations.

### NestJS conventions
- Controllers use `@ApiTags`, `@ApiBearerAuth`, `@UseGuards(JwtGuard)`, and a path-level `@Controller`.
- `ValidationPipe` is global, so DTOs should reject unknown fields through `whitelist` and transform numeric/date input explicitly.
- Services inject `PrismaService`; module providers/imports must avoid creating a second incompatible Prisma singleton.
- Swagger decorators are currently minimal, but Acquisition DTO fields should still expose enum, UUID, date, and decimal intent.

## Additive Prisma Design

The following names and mapped columns are the contract for t3. Exact enum values are intentionally stable uppercase identifiers because they are persisted PostgreSQL enum labels.

### New enums

```text
AcquisitionStatus: PLANNED | COMPLETED | CANCELLED
AcquisitionSource: AUCTION | DIRECT | PREFERENCE | ONLINE | OTHER
PropertyDocumentType: REGISTRY | AUCTION_NOTICE | PURCHASE_CONTRACT | TAX | CERTIFICATE | PHOTO | OTHER
PropertyDocumentStatus: PENDING | VALID | EXPIRED | REJECTED
RegularizationStatus: NOT_STARTED | IN_PROGRESS | COMPLETED | BLOCKED
RegularizationTaskStatus: PENDING | IN_PROGRESS | COMPLETED | BLOCKED | CANCELLED
PossessionStatus: NOT_TAKEN | PENDING | TAKEN | DISPUTED | VACANT
PropertyEventType: NOTE | STATUS_CHANGE | DOCUMENT | POSSESSION | REGULARIZATION | EXPENSE | SALE | OTHER
SaleScenarioStatus: DRAFT | ACTIVE | SELECTED | ARCHIVED
```

### `Acquisition` (one optional record per property)

```text
id             String @id @default(uuid())
propertyId     String @unique @map("property_id")
purchasePrice  Decimal? @map("purchase_price") @db.Decimal(14,2)
purchaseDate   DateTime? @map("purchase_date")
status         AcquisitionStatus @default(PLANNED)
source         AcquisitionSource @default(AUCTION)
notes          String?
createdAt      DateTime @default(now()) @map("created_at")
updatedAt      DateTime @updatedAt @map("updated_at")
property       Property @relation(fields: [propertyId], references: [id], onDelete: Cascade)
@@index([status, purchaseDate])
@@map("acquisitions")
```

`purchasePrice` must remain nullable. FinancialService uses it only when non-null and otherwise falls back to `Auction.auctionValue`, preserving all V1 records that have no Acquisition row.

### `PropertyDocument` (many documents per property)

```text
id           String @id @default(uuid())
propertyId   String @map("property_id")
type         PropertyDocumentType
status       PropertyDocumentStatus @default(PENDING)
title        String
filePath     String? @map("file_path")
issuedAt     DateTime? @map("issued_at")
expiresAt    DateTime? @map("expires_at")
notes        String?
createdAt    DateTime @default(now()) @map("created_at")
updatedAt    DateTime @updatedAt @map("updated_at")
property     Property @relation(fields: [propertyId], references: [id], onDelete: Cascade)
@@index([propertyId, type, status])
@@index([expiresAt])
@@map("property_documents")
```

### `PropertyRegularization` (one record per property)

```text
id          String @id @default(uuid())
propertyId  String @unique @map("property_id")
status      RegularizationStatus @default(NOT_STARTED)
startedAt   DateTime? @map("started_at")
completedAt DateTime? @map("completed_at")
notes       String?
createdAt   DateTime @default(now()) @map("created_at")
updatedAt   DateTime @updatedAt @map("updated_at")
property    Property @relation(fields: [propertyId], references: [id], onDelete: Cascade)
tasks       RegularizationTask[]
@@index([status])
@@map("property_regularizations")
```

### `RegularizationTask` (many tasks per regularization)

```text
id               String @id @default(uuid())
regularizationId String @map("regularization_id")
title            String
description      String?
status           RegularizationTaskStatus @default(PENDING)
dueDate          DateTime? @map("due_date")
completedAt      DateTime? @map("completed_at")
responsible      String?
notes            String?
createdAt        DateTime @default(now()) @map("created_at")
updatedAt        DateTime @updatedAt @map("updated_at")
regularization   PropertyRegularization @relation(fields: [regularizationId], references: [id], onDelete: Cascade)
@@index([regularizationId, status, dueDate])
@@map("regularization_tasks")
```

### `PropertyPossession` (one record per property)

```text
id            String @id @default(uuid())
propertyId    String @unique @map("property_id")
status        PossessionStatus @default(NOT_TAKEN)
possessionDate DateTime? @map("possession_date")
occupant      String?
notes         String?
createdAt     DateTime @default(now()) @map("created_at")
updatedAt     DateTime @updatedAt @map("updated_at")
property      Property @relation(fields: [propertyId], references: [id], onDelete: Cascade)
@@index([status, possessionDate])
@@map("property_possessions")
```

### `PropertyEvent` (append-only timeline records)

```text
id          String @id @default(uuid())
propertyId  String @map("property_id")
type        PropertyEventType
title       String
description String?
occurredAt  DateTime @default(now()) @map("occurred_at")
metadata    Json?
createdAt   DateTime @default(now()) @map("created_at")
property    Property @relation(fields: [propertyId], references: [id], onDelete: Cascade)
@@index([propertyId, occurredAt])
@@index([propertyId, type, occurredAt])
@@map("property_events")
```

Do not use `PropertyEvent` as a replacement for the existing `PropertyStatusHistory`; that V1 table and API behavior remain intact. Event creation may be added later, but the additive schema must not backfill or mutate historical rows.

### `SaleScenario` (many projections per property)

```text
id                 String @id @default(uuid())
propertyId         String @map("property_id")
name               String
status             SaleScenarioStatus @default(DRAFT)
projectedSalePrice Decimal @map("projected_sale_price") @db.Decimal(14,2)
projectedBrokerage Decimal @default(0) @map("projected_brokerage") @db.Decimal(14,2)
projectedTaxes     Decimal @default(0) @map("projected_taxes") @db.Decimal(14,2)
expectedSaleDate   DateTime? @map("expected_sale_date")
notes              String?
createdAt          DateTime @default(now()) @map("created_at")
updatedAt          DateTime @updatedAt @map("updated_at")
property           Property @relation(fields: [propertyId], references: [id], onDelete: Cascade)
@@index([propertyId, status])
@@index([propertyId, expectedSaleDate])
@@map("sale_scenarios")
```

Add these optional/list relations to `Property`: `acquisition Acquisition?`, `documents PropertyDocument[]`, `regularization PropertyRegularization?`, `possession PropertyPossession?`, `events PropertyEvent[]`, and `saleScenarios SaleScenario[]`. Do not change existing relation names or requiredness.

## Migration Strategy

1. Add the enums and models to `schema.prisma` while preserving all existing model definitions.
2. Create one new timestamped migration after `20260929120000_add_property_status_history` using the repository's normal Prisma naming convention, for example `20261001130000_add_acquisition_property_lifecycle`.
3. The generated SQL may contain `CREATE TYPE`, `CREATE TABLE`, `CREATE INDEX`, and `ALTER TABLE ... ADD CONSTRAINT ... FOREIGN KEY`; it must not contain `DROP`, `TRUNCATE`, or changes to V1 tables/columns.
4. New tables are empty and nullable/optional at the relation boundary, so deployment is backward compatible. No data backfill is required.
5. Rollback is migration-directory reversal in a disposable/pre-deployment environment only; production rollback must not be implemented by destructive SQL. If a deployment must be reverted, leave additive tables in place and roll back application code to the V1 paths.
6. Run `prisma validate` and `prisma generate`; t5 owns the focused build/test verification.

## FinancialService Contract

Create a shared `FinancialModule`/`FinancialService` used by Dashboard and Reports. Keep all arithmetic in one service and convert Prisma Decimal values with `Number(value ?? 0)` at the boundary.

### Input query
For a property, load `acquisition`, `auction`, `expenses`, `renovations`, `sale`, and `saleScenarios` in one Prisma read. For dashboard summary, preserve the current non-cancelled property filter and response keys.

### Calculation rules

```text
purchaseCost = acquisition.purchasePrice when it is not null,
               otherwise auction.auctionValue,
               otherwise 0
expenseCost  = sum(expense.amount)
renovationCost = sum(renovation.actualAmount ?? renovation.plannedAmount)
costTotal = purchaseCost + expenseCost + renovationCost
saleGross = sale.saleAmount, or 0 when unsold
saleCosts = sale.brokerage + sale.taxes
netProfit = saleGross - costTotal - saleCosts
roi = costTotal > 0 ? (netProfit / costTotal) * 100 : 0
```

For a SaleScenario projection, use `projectedSalePrice - projectedBrokerage - projectedTaxes - costTotal` and the same denominator. Do not let a scenario replace the actual Sale result in existing dashboard/report rows unless a new explicit scenario endpoint is added.

The service should expose a stable result object containing at least `purchaseCost`, `expenses`, `renovations`, `costTotal`, `sale`, `saleCosts`, `netProfit`, and `roi`. Dashboard maps it back to the existing `costTotal`, `sale`, `netProfit`, and `roi` fields, preserving CSV output and V1 clients.

## Acquisition REST Contract

Add an `AcquisitionsModule` registered in `AppModule`, with a controller at `/acquisitions`, `JwtGuard`, `@ApiTags("acquisitions")`, and `@ApiBearerAuth()`.

Required operations:

- `GET /acquisitions?propertyId=<uuid>` — list, optionally filtered by property, ordered by `createdAt desc`.
- `GET /acquisitions/:id` — return one acquisition with its property and auction context; return 404 for a missing id.
- `POST /acquisitions` — create or reject a duplicate for the unique property relation; do not silently mutate an existing V1 Auction row.
- `PATCH /acquisitions/:id` — update only Acquisition fields; preserve omitted values and allow explicit null for optional fields.

DTO rules:

- `propertyId`: `@IsUUID()` and required on create.
- `purchasePrice`: optional numeric transform accepting decimal form, must be non-negative when present.
- `purchaseDate`: optional ISO/date transform with `@IsDate()`.
- `status` and `source`: `@IsEnum()` using generated Prisma enums.
- `notes`: optional string with a bounded length.
- Update DTO uses `PartialType(CreateAcquisitionDto)` and retains Swagger metadata.

Use Nest exceptions for invalid duplicate/not-found cases. Acquisition writes should not change `Property.status`, `Auction`, or `PropertyStatusHistory`; those remain owned by the V1 auction workflow.

## Risks and Mitigations

- **HIGH — Prisma migration drift:** generated migration may try to alter existing tables if schema formatting or relation names are inconsistent. Mitigation: generate against a disposable/current database, inspect SQL, and reject any `DROP`/`TRUNCATE` or V1 alteration before applying.
- **HIGH — financial regression:** changing dashboard arithmetic can break existing mocks and CSV consumers. Mitigation: preserve response keys and add tests for both `Acquisition.purchasePrice` and V1 auction fallback.
- **MEDIUM — duplicate acquisition semantics:** one-to-one unique `property_id` can surface P2002. Mitigation: translate to `ConflictException` and keep V1 `/auctions` independent.
- **MEDIUM — Decimal serialization:** Prisma Decimal values may serialize differently from numbers. Mitigation: convert only in FinancialService output and retain existing numeric dashboard response behavior.
- **MEDIUM — relation naming collision:** `property.statusHistory` must remain unchanged. Mitigation: use `events` for `PropertyEvent` and do not overload existing status history.
- **LOW — empty migration tables:** no automatic historical backfill means new lifecycle views are initially empty. Mitigation: document that existing V1 data remains available through Auction/Expense/Renovation/Sale and may be linked by future explicit import.

## Handoff by Task

- **t2 security:** configuration hardening is orthogonal; do not add secrets or alter the additive schema contract.
- **t3 backend/DB:** implement exactly the enum/model/migration contract above, inspect SQL for non-destructive additive operations, and preserve all V1 relations/routes.
- **t4 backend:** implement FinancialService and Acquisition module against these contracts; add focused tests for fallback, duplicate/not-found behavior, DTO validation, and dashboard/report compatibility.
- **t5 tester:** validate Prisma and execute the focused backend build/tests; specifically verify both financial source paths.
- **t6 architect:** review the final diff against this artifact and the no-destructive-migration/no-secret constraints.
