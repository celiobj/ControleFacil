@echo off
setlocal EnableExtensions
cd /d "%~dp0.."
set "PROJECT_ROOT=%CD%"

where node >nul 2>&1
if errorlevel 1 (
  echo [ERRO] Node.js nao encontrado no PATH.
  exit /b 1
)
if not exist "frontend\dist\index.html" (
  echo [ERRO] Frontend compilado nao encontrado em frontend\dist.
  exit /b 1
)

node scripts\copy-frontend.js
if errorlevel 1 exit /b 1

start "ControleFacil PRD Backend" /b cmd /c "set APP_ENV=prd&&set PORT=3000&&set DATABASE_URL=postgresql://postgres:slipclown@localhost:5432/controle_facil?schema=public&&set JWT_SECRET=controle-facil-prd-local-secret&&node backend\dist\main.js"
start "ControleFacil Frontend" /b cmd /c "npm run preview --prefix frontend -- --host 0.0.0.0 --port 4173"

echo [ControleFacil] PRD: http://localhost:3000
echo [ControleFacil] Frontend: http://localhost:4173
exit /b 0
