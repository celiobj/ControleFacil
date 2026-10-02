# Configuration Secret Hygiene

Tracked scripts and seed files can expose the same credentials as an env file, so hardening must scan all runtime/configuration surfaces.

## What Happened
For ControleFacil/t2, credential-like defaults were present in HML/PRD scripts, the seed, Docker Compose, frontend login state, and a JWT module fallback. They were replaced with required environment variables or empty state; `.env.*` and generated output were added to ignore rules while examples remain tracked.

## Takeaway
When removing secret exposure, scan scripts, seed code, Compose, frontend bootstrap state, and module fallbacks, not only `.env.example`. Use redacted path/line scans during validation.

## History
- 2026-10-01 (ControleFacil/t2): initial