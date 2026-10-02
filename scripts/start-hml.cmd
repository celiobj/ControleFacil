@echo off
setlocal EnableExtensions
cd /d "%~dp0.."
set "PROJECT_ROOT=%CD%"

where node >nul 2>&1
if errorlevel 1 (
  echo [ERRO] Node.js nao encontrado no PATH.
  exit /b 1
)

start "ControleFacil HML Backend" /b cmd /c "set APP_ENV=hml&&set PORT=3001&&node backend\dist\main.js"

echo [ControleFacil] HML Backend: http://localhost:3001
exit /b 0
