@echo off
setlocal
cd /d "%~dp0.."

echo [Controle Facil] Iniciando frontend e backend em modo watch...
call npx concurrently --names backend,frontend --kill-others "npm run start:dev --prefix backend" "npm run dev --prefix frontend"
