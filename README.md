# Controle Fácil

Sistema web para gestão do ciclo de imóveis adquiridos em leilão: arremate, regularização, reforma, despesas, venda e apuração de resultado.

## Stack

- Backend: Node.js 22, NestJS, TypeScript, Prisma, PostgreSQL, JWT, Swagger e class-validator.
- Frontend: React, Vite, TypeScript, Material UI, React Router, Axios, React Query e Recharts.
- Operação local: Docker Compose para PostgreSQL.

## Instalação

Pré-requisitos: Node.js 22+, npm 10+ e Docker Desktop (ou PostgreSQL 16+ local).

```powershell
docker compose up -d postgres
npm install --prefix backend
npm install --prefix frontend
Copy-Item backend/.env.example backend/.env
npm run prisma:generate --prefix backend
npm run prisma:migrate --prefix backend
npm run prisma:seed --prefix backend
```

O seed cria `admin@admin.com` com senha `Admin@123`. Troque a senha após o primeiro acesso.

## Execução

```powershell
npm run start:dev --prefix backend
npm run dev --prefix frontend
```

Frontend: http://localhost:5173  
API: http://localhost:3000  
Swagger: http://localhost:3000/docs

## Ambientes locais

Há dois ambientes locais independentes para testar alterações sem modificar os dados de produção:

- PRD: banco `controle_facil`, API em `http://localhost:3000`
- HML: banco `controle_facil_hml`, API em `http://localhost:3001`

Inicialize o banco HML uma vez:

```powershell
.\scripts\setup-hml.cmd
```

Depois do build, inicie os dois ambientes:

```powershell
.\scripts\start-local-environments.cmd
```

Abra `http://localhost:4173` e escolha `Homologação` ou `Produção local` no login. Os tokens e os dados permanecem separados por ambiente. Para iniciar somente um ambiente, use `scripts\start-hml.cmd` ou `scripts\start-prd.cmd`.

Para subir os dois processos pelo diretório raiz: `npm install`, `npm run install:all` e `npm run dev`.

## Build e inicialização com o Windows

O build de produção compila o frontend, compila a API e configura a API para servir os arquivos do frontend no mesmo endereço:

```powershell
npm run build
```

Depois do build, registre a inicialização automática para o usuário atual:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\install-startup-task.ps1
```

Ao entrar no Windows, o atalho `ControleFacil` inicia a aplicação em `http://localhost:3000`. Para remover a inicialização automática:

```powershell
Remove-Item "$([Environment]::GetFolderPath('Startup'))\ControleFacil.lnk"
```

## Variáveis de ambiente

`backend/.env` usa `DATABASE_URL`, `JWT_SECRET`, `PORT` e `UPLOAD_DIR`. O exemplo está em `backend/.env.example`.

## API principal

- `POST /auth/login` e `POST /auth/register`
- `GET|POST /properties`, `GET|PATCH|DELETE /properties/:id`
- `GET|POST /auctions`, `/expenses`, `/renovations` e `/sales`
- `GET /dashboard`
- `GET /reports/profit`, `/reports/expenses` e `/reports/profit.csv`

As rotas protegidas usam `Authorization: Bearer <token>` e estão documentadas no Swagger.

## Estrutura

```text
backend/prisma/schema.prisma
backend/prisma/seed.ts
backend/src/{auth,properties,operations,dashboard,reports}
frontend/src/{App.tsx,api.ts,styles.css}
docker-compose.yml
```

## Qualidade

```powershell
npm run build --prefix backend
npm test --prefix backend
npm run build --prefix frontend
```

O backend usa ValidationPipe global, DTOs, paginação/filtros de imóveis, guard JWT, Swagger e o serviço financeiro compartilhado pelo dashboard e relatórios.