ALTER TABLE "sale_scenarios"
ADD COLUMN "brokerage_percent" DECIMAL(7,4),
ADD COLUMN "other_sale_costs" DECIMAL(14,2) NOT NULL DEFAULT 0,
ADD COLUMN "net_profit" DECIMAL(14,2),
ADD COLUMN "roi" DECIMAL(10,4);