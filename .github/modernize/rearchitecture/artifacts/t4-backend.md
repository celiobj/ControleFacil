# t4 — FinancialService and Acquisition REST

## Summary
Implemented centralized financial calculation, dashboard integration, and the authenticated Acquisition REST module without changing V1 operation routes or response keys.

## Upstream Artifacts Consumed
- `.github/modernize/rearchitecture/artifacts/t1-architect.md` — financial fallback rules, REST contract, DTO validation, and V1 preservation requirements.
- `.github/modernize/rearchitecture/artifacts/t3-backend.md` — Prisma model/enums/relation names used by service queries and DTOs.
- `.github/modernize/rearchitecture/team/backend/inbox.md` — Acquisition remains optional and independent from Auction.

## Evidence Mapping
- `t1-architect.md#FinancialService Contract` -> `backend/src/financial/financial.service.ts`; dashboard delegates arithmetic and preserves `costTotal`, `sale`, `netProfit`, and `roi`.
- `t1-architect.md#Acquisition REST Contract` -> `backend/src/acquisitions/` controller, service, DTOs, module, JWT guard and Swagger decorators.
- `t3-backend.md#Deliverables` -> Prisma client relation names `acquisition`, `auction`, `expenses`, `renovations`, and `sale` used in financial queries.

## Deliverables
- `backend/src/financial/financial.service.ts` and `financial.module.ts` — centralized Decimal-to-number calculations, Acquisition-to-Auction fallback, costs, profit and ROI.
- `backend/src/dashboard/dashboard.service.ts` and module wiring — shared FinancialService integration with unchanged dashboard response keys.
- `backend/src/acquisitions/` — authenticated list/get/create/update REST API, DTO validation, Swagger metadata, duplicate/not-found exception translation.
- Focused tests for FinancialService, Acquisition service, DTO validation and dashboard compatibility.

## Test Results
- Command: `npx jest src/financial/financial.service.spec.ts src/acquisitions/acquisitions.service.spec.ts src/acquisitions/dto/acquisition.dto.spec.ts src/dashboard/dashboard.service.spec.ts --runInBand`
- Passed: 8
- Failed: 0
- Skipped: 0
- Command: `npx prisma validate`
- Passed: 1
- Failed: 0
- Skipped: 0
- Command: `npx prisma generate`
- Passed: 1
- Failed: 0
- Skipped: 0
- Command: `npm run build`
- Passed: 1
- Failed: 0
- Skipped: 0
- Command: `git diff --check`
- Passed: 1
- Failed: 0
- Skipped: 0

## Endpoint Contract
- `GET /acquisitions?propertyId=<uuid>` — implemented.
- `GET /acquisitions/:id` — implemented; missing records map to 404.
- `POST /acquisitions` — implemented; duplicate property maps to 409.
- `PATCH /acquisitions/:id` — implemented; omitted fields remain unchanged.

## Remaining Risks
- No live HTTP probe was run because the task requested the cheapest validation and no endpoint contract script was present; the module compiles and focused service/DTO tests pass.
- Workspace status contains unrelated pre-existing/generated changes; they were preserved and not reverted.
