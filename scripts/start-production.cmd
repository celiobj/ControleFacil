@echo off
setlocal EnableExtensions

cd /d "%~dp0.."
set "PROJECT_ROOT=%CD%"

echo.
echo ========================================
echo   ControleFacil - Startup
echo ========================================
echo.

where git >nul 2>&1
if errorlevel 1 (
  echo [ERRO] Git nao encontrado no PATH.
  exit /b 1
)

where node >nul 2>&1
if errorlevel 1 (
  echo [ERRO] Node.js nao encontrado no PATH.
  exit /b 1
)

echo [1/6] Buscando a ultima versao do GitHub...
git fetch origin main
if errorlevel 1 (
  echo [ERRO] Nao foi possivel acessar o GitHub.
  exit /b 1
)

echo [2/6] Atualizando codigo para origin/main...
git diff --quiet
if errorlevel 1 (
  echo [ERRO] Existem alteracoes locais no repositorio.
  echo [INFO] O startup nao sobrescrevera essas alteracoes.
  echo [INFO] Execute "git status" e resolva as alteracoes antes de iniciar.
  exit /b 1
)

git diff --cached --quiet
if errorlevel 1 (
  echo [ERRO] Existem alteracoes staged no repositorio.
  echo [INFO] O startup nao sobrescrevera essas alteracoes.
  exit /b 1
)

git reset --hard origin/main
if errorlevel 1 (
  echo [ERRO] Falha ao atualizar o repositorio.
  exit /b 1
)

echo [3/6] Iniciando PostgreSQL...
where docker >nul 2>&1
if errorlevel 1 (
  echo [AVISO] Docker nao encontrado. O PostgreSQL precisa estar disponivel externamente.
) else (
  docker compose up -d postgres
  if errorlevel 1 (
    echo [ERRO] Falha ao iniciar o PostgreSQL pelo Docker.
    exit /b 1
  )
)

echo [4/6] Instalando dependencias...
call npm ci
if errorlevel 1 (
  echo [ERRO] Falha ao instalar dependencias da raiz.
  exit /b 1
)

call npm ci --prefix backend
if errorlevel 1 (
  echo [ERRO] Falha ao instalar dependencias do backend.
  exit /b 1
)

call npm ci --prefix frontend
if errorlevel 1 (
  echo [ERRO] Falha ao instalar dependencias do frontend.
  exit /b 1
)

echo [5/6] Gerando build de producao...
call npm run build
if errorlevel 1 (
  echo [ERRO] Build falhou. O servico nao sera iniciado.
  exit /b 1
)

echo [6/6] Iniciando ControleFacil...
cd /d "%PROJECT_ROOT%\backend"
node dist\main.js

set "EXIT_CODE=%ERRORLEVEL%"
echo.
echo [ControleFacil] Processo finalizado com codigo %EXIT_CODE%.
exit /b %EXIT_CODE%
