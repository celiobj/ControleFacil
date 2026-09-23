@echo off
setlocal EnableExtensions

cd /d "%~dp0.."
set "PROJECT_ROOT=%CD%"

echo.
echo ========================================
echo   ControleFacil - Startup
echo ========================================
echo.

where node >nul 2>&1
if errorlevel 1 (
  echo [ERRO] Node.js nao encontrado no PATH.
  exit /b 1
)

cd /d "%PROJECT_ROOT%"

if not exist "frontend\dist\index.html" (
  echo [ERRO] Frontend compilado nao encontrado em frontend\dist.
  echo [INFO] Execute o build antes de iniciar a aplicacao.
  exit /b 1
)

echo Preparando frontend para o backend...
node scripts\copy-frontend.js
if errorlevel 1 (
  echo [ERRO] Nao foi possivel preparar o frontend.
  exit /b 1
)

echo Iniciando backend e frontend...

start "ControleFacil Backend" /b cmd /c "node backend\dist\main.js"
start "ControleFacil Frontend" /b cmd /c "npm run preview --prefix frontend -- --host 0.0.0.0"

echo.
echo [ControleFacil] Backend: http://localhost:3000
echo [ControleFacil] Frontend: http://localhost:4173
echo [ControleFacil] Os servicos foram iniciados em paralelo.
exit /b 0
