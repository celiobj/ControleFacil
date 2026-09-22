@echo off
setlocal
cd /d "%~dp0.."
echo [Controle Facil] Iniciando ambiente em modo watch...
call npm run dev
