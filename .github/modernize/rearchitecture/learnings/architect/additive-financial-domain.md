# Additive Financial Domain

For brownfield Prisma domains, keep new lifecycle records optional and centralize financial arithmetic behind a compatibility fallback.

## What Happened
Task t1 mapped ControleFacil V1: Auction.auctionValue is required and dashboard/report outputs already depend on it. The additive design makes Acquisition.purchasePrice nullable and selects it only when present, otherwise retaining Auction.auctionValue.

## Takeaway
Use new one-to-one/list relations from Property with mapped snake_case tables, preserve existing relation names, and make FinancialService return the existing dashboard keys after applying the new fallback.

## History
- 2026-10-01 (ControleFacil/t1): initial
