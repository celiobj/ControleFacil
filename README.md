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

Para subir os dois processos pelo diretório raiz: `npm install`, `npm run install:all` e `npm run dev`.

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