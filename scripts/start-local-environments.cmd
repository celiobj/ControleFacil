@echo off
setlocal EnableExtensions
cd /d "%~dp0.."

call scripts\start-prd.cmd
if errorlevel 1 exit /b 1
call scripts\start-hml.cmd
if errorlevel 1 exit /b 1

echo.
echo [ControleFacil] Ambientes locais iniciados.
echo [ControleFacil] Selecione o ambiente no login:
echo [ControleFacil] PRD: http://localhost:3000
echo [ControleFacil] HML: http://localhost:3001
echo [ControleFacil] Frontend: http://localhost:4173
exit /b 0
