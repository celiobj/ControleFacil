## [t3] Additive Prisma lifecycle schema and migration

## [t2] Harden configuration and ignore rules
- Codebase/domain discoveries: HML/PRD scripts, seed files, Compose, and frontend login state each contained credential-like defaults in addition to `backend/.env.example`.
- Wrong assumptions and corrections: the tracked `backend/dist` output is modified by the backend build, so generated changes were restored after validation; source changes were retained.
- Debugging dead-ends and what actually worked: a redacted path/line scan was safer than printing matching lines and still found all known hardcoded patterns.
- Techniques/patterns worth reusing: use explicit replacement placeholders in env templates, require secrets from the caller environment, and keep `.env.*.example` exceptions after broad `.env.*` ignores.
- Learnings consumed: [backend/additive-prisma-migrations]

## [t4] Financial service and Acquisition REST
- Codebase/domain discoveries: Dashboard was the only financial arithmetic owner; Reports already consumed its response and CSV shape.
- Wrong assumptions and corrections: Prisma error mocks may expose only `code`, so HTTP translation must not require a concrete Prisma error instance.
- Debugging dead-ends and what actually worked: Initial focused tests exposed the strict `instanceof` check; code-based detection fixed it without changing native Prisma handling.
- Techniques/patterns worth reusing: keep Acquisition writes independent from Auction writes and inject one shared FinancialService into dashboard/report module paths.
- Learnings consumed: [backend/additive-prisma-migrations, backend/configuration-secrets]
