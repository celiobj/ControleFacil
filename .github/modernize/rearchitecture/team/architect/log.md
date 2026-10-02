## [t1] Mapped V1 contracts and additive domain design
- Codebase/domain discoveries: V1 acquisition writes are `POST /auctions` with required `Auction.auctionValue`; dashboard currently calculates purchase cost from auction value and reports consume dashboard output.
- Wrong assumptions and corrections: Acquisition must be optional and independent from Auction; `purchasePrice` cannot be required or V1 rows would lose their fallback path.
- Debugging dead-ends and what actually worked: No implementation/debugging was needed; targeted schema/controller/service/test inspection was sufficient.
- Techniques/patterns worth reusing for future tasks: Keep new lifecycle relations optional at `Property`; centralize Decimal-to-number conversion in FinancialService; inspect generated SQL for destructive operations before applying.
- Learnings consumed: [(none)]
