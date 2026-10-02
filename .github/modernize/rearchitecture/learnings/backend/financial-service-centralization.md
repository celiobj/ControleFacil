# Financial Service Centralization

Centralize property cost and ROI arithmetic while preserving dashboard/report response keys and V1 auction fallback behavior.

## What Happened
For ControleFacil t4, `FinancialService` became the single arithmetic owner. It prefers nullable `Acquisition.purchasePrice` and falls back to `Auction.auctionValue`; dashboard delegates to it and Reports retains its existing dashboard/CSV contract.

## Takeaway
Inject the shared service through a small `FinancialModule`, convert Decimal values at the service boundary, and keep Acquisition writes independent from legacy auction status/history side effects.

## History
- 2026-10-01 (ControleFacil/t4): initial
