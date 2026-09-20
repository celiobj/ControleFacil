# Reports

The reports module exposes JSON summaries and a CSV export at `/reports/profit.csv`. The CSV opens directly in Excel and keeps the report deterministic for local operation; PDF generation can be added behind the same endpoint with a renderer such as Puppeteer when a PDF layout is specified.
