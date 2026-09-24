@echo off
setlocal EnableExtensions
cd /d "%~dp0.."
set "DATABASE_URL=postgresql://postgres:slipclown@localhost:5432/controle_facil_hml?schema=public"
set "APP_ENV=hml"
set "JWT_SECRET=controle-facil-hml-local-secret"
set "PORT=3001"

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
