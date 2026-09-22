@echo off
setlocal
cd /d "%~dp0.."
echo [Controle Facil] Recompilando frontend e backend...
call npm run build
if errorlevel 1 (
  echo Falha ao compilar a aplicacao.
  exit /b %errorlevel%
)
cd /d "%~dp0..\backend"
echo [Controle Facil] Iniciando backend...
node dist\main.js