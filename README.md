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
Push-Location backend
npx prisma migrate deploy
Pop-Location
npm run prisma:seed --prefix backend
```

O seed exige `SEED_ADMIN_PASSWORD` no ambiente e cria `admin@admin.com` com essa senha de bootstrap. Nao versione valores de ambiente; troque a senha após o primeiro acesso.

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

## V2 e compatibilidade

A V2 evolui o schema existente por migrations Prisma incrementais. Não recrie o banco V1 nem use `prisma migrate reset` para atualizá-lo. Antes de qualquer atualização, faça e valide um backup PostgreSQL.

Para atualizar um banco V1 existente:

```powershell
Push-Location backend
npx prisma migrate status
npx prisma migrate deploy
npx prisma generate
Pop-Location
```

`migrate deploy` aplica somente migrations pendentes, incluindo `20261001130000_add_acquisition_property_lifecycle` e as migrations V2 seguintes. Não execute `migrate dev` contra dados compartilhados ou de produção. Para uma instalação nova, crie um banco vazio, configure `backend/.env` a partir de `.env.example` e execute `migrate deploy` para aplicar todas as migrations desde a V1.

Acquisition, documentos, regularização, posse, eventos e cenários são complementos às entidades existentes. Auction, Expense, Renovation, Sale e ChecklistItem permanecem disponíveis. O `FinancialService` usa `Acquisition.purchasePrice` quando há aquisição registrada; em imóveis V1 sem Acquisition, mantém `Auction.auctionValue` como fallback. Nenhum valor de FGTS, financiamento ou recursos próprios é inferido para dados históricos.

O checklist mantém itens personalizados e concluídos. Quando novas tarefas padrão são adicionadas, `ensure()` insere somente as ausentes; não apaga itens existentes por diferença de contagem.

## Módulos e rotas V2

- Aquisição: `GET|POST /properties/:id/acquisition` e `PATCH /acquisitions/:id`.
- Documentos: `GET|POST /properties/:id/documents`, `PATCH|DELETE /documents/:id` e `GET /documents/:id/download`.
- Regularização: `GET|POST /properties/:id/regularization`, tarefas em `/properties/:id/regularization/tasks` e atualizações em `/regularization/:id` e `/regularization/tasks/:id`.
- Posse: `GET|POST /properties/:id/possession` e `PATCH /possession/:id`.
- Financeiro: `GET /properties/:id/financial`.
- Timeline: `GET|POST /properties/:id/events`.
- Cenários: `GET|POST /properties/:id/scenarios` e `PATCH /scenarios/:id`.

As rotas V2 usam o JWT atual e são documentadas em `/docs`. A área do imóvel contém abas para resumo, aquisição, regularização, posse, reforma, despesas, documentos, venda, financeiro, cenários, timeline e checklist.

## Uploads e segredos

Defina `UPLOAD_DIR` no ambiente da API; o padrão é `./uploads`. Os arquivos ficam em `properties/<propertyId>/documents`, fora do PostgreSQL, e os metadados/caminho relativo ficam no banco. O upload aceita arquivos de até 25 MiB. O diretório `uploads/` e arquivos `.env` são ignorados pelo Git. Use apenas valores locais em `.env`; mantenha `.env.example` com placeholders e rotacione qualquer credencial que tenha sido commitada anteriormente, pois removê-la da árvore atual não a remove do histórico Git.

## Backfill e dados históricos

Não há backfill automático de Acquisition: sem evidência de pagamento, forma de pagamento e fontes, criar aquisições históricas poderia inventar dados. Imóveis V1 continuam usando `Auction.auctionValue` até que uma Acquisition real seja registrada. Migrations não alteram nem apagam essas linhas.

## Testes V2

```powershell
npm test --prefix backend
npm run build --prefix backend
npm run build --prefix frontend
```

Os testes unitários cobrem fallback V1, fontes de aquisição, não duplicação de despesas de reforma, inclusão aditiva de checklist/regularização, upload seguro, cenário de venda e agregados do dashboard. A validação de upgrade contra um PostgreSQL V1 real deve ser feita em uma cópia restaurada do banco, nunca no banco original.