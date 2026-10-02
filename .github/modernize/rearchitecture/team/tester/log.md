## [t5] Prisma and backend validation
- Codebase/domain discoveries: Prisma validation requires DATABASE_URL even for static schema validation; a process-only placeholder is sufficient and does not expose a real credential.
- Wrong assumptions and corrections: the execution wrapper's relative working directory was ambiguous, so validation was rerun using absolute workspace and local Prisma paths.
- Debugging dead-ends and what actually worked: aggregated validation output timed out/truncated; isolated commands produced reliable exit codes.
- Techniques/patterns worth reusing: use `npm ci --prefix backend`, local Prisma/Jest/tsc binaries, and redact all environment-sensitive output.
- Learnings consumed: [(none)]
