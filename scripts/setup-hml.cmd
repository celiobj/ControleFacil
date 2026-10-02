@echo off
setlocal EnableExtensions
cd /d "%~dp0.."
set "APP_ENV=hml"
set "PORT=3001"

if not defined DATABASE_URL (
  echo [ERRO] Defina DATABASE_URL no ambiente antes de executar este script.
  exit /b 1
)
if not defined JWT_SECRET (
  echo [ERRO] Defina JWT_SECRET no ambiente antes de executar este script.
  exit /b 1
)
if not defined SEED_ADMIN_PASSWORD (
  echo [ERRO] Defina SEED_ADMIN_PASSWORD no ambiente antes de executar este script.
  exit /b 1
)

pushd backend
npm exec -- prisma migrate deploy --schema prisma\schema.prisma
if errorlevel 1 (
  popd
  exit /b 1
)
npm run prisma:seed
set "EXIT_CODE=%ERRORLEVEL%"
popd
exit /b %EXIT_CODE%
