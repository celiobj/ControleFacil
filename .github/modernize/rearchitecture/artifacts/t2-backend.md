# t2 — Configuration Hardening

## Summary
Hardened environment handling and removed credential-bearing defaults from tracked configuration, scripts, seed files, frontend bootstrap state, and local Compose settings. Existing source changes, Prisma schema, and migration work were preserved.

## Upstream Artifacts Consumed
- `.github/modernize/rearchitecture/artifacts/t1-architect.md` — confirmed V1/API preservation and configuration-hardening scope.
- `.github/modernize/rearchitecture/team/backend/inbox.md` — confirmed that this task must remove hardcoded secrets without changing the additive domain work.
- `.github/modernize/rearchitecture/team/backend/log.md` — confirmed the prior Prisma validation constraint and avoided unrelated schema edits.

## Evidence Mapping
- `t1-architect.md#Handoff by Task` -> `.gitignore`, `backend/.env.example`, `backend/src/app.module.ts`, and local scripts.
- `t1-architect.md#NestJS conventions` -> environment loading remains global and existing API/module boundaries are unchanged.
- `backend/inbox.md` -> inline HML/PRD database and JWT values were removed; callers must provide environment variables.

## Changes
- Added ignores for local `.env*` files, build output, TypeScript incremental state, and upload directories while retaining example templates.
- Replaced `.env.example` credential-looking defaults with explicit replacement placeholders and documented `SEED_ADMIN_PASSWORD`.
- Added fallback env-file loading for the backend while preserving process environment precedence.
- Removed insecure JWT fallback secrets from all Nest modules.
- Removed hardcoded credentials from HML/PRD startup scripts, seed implementations, Docker Compose, and frontend login state.
- Made HML setup require `DATABASE_URL`, `JWT_SECRET`, and `SEED_ADMIN_PASSWORD` from the caller environment.
- Removed locally generated env files and compiled frontend/backend assets from the Git index without deleting their local copies.

## Test Results
- Command: `npm run build --prefix backend`
- Passed: 1
- Failed: 0
- Skipped: 0
- Command: `git diff --check`
- Passed: 1
- Failed: 0
- Skipped: 0
- Command: redacted literal scan for known hardcoded credential patterns
- Passed: 1
- Failed: 0
- Skipped: 0
- Scope: tracked files only; local ignored env files are intentionally retained on disk.
- Command: `git check-ignore -v` for local env/build paths
- Passed: 1
- Failed: 0
- Skipped: 0

## Remaining Risks
- Local HML setup now requires operators to export the three required variables before running the script; values are intentionally not supplied by versioned files.
- Prisma validation/generation and full backend tests remain assigned to t5.