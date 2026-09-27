# Anish Crackers — Backend

Spring Boot + MySQL API that powers the Anish Crackers storefront and admin panel.
The frontend reads its entire catalog from here at runtime (`GET /api/products`,
`GET /api/categories`), so whatever is in this database is what customers see.

## Running locally

Requires Java 17+, Maven, and a MySQL database.

Set these environment variables (or put them in an `.env` your run config loads):

```
MYSQLHOST=localhost
MYSQLPORT=3306
MYSQLDATABASE=anish_crackers
MYSQLUSER=root
MYSQLPASSWORD=your_password
CLOUDINARY_CLOUD_NAME=...
CLOUDINARY_API_KEY=...
CLOUDINARY_API_SECRET=...
```

Then:

```bash
mvn spring-boot:run
```

The API starts on `http://localhost:8080`. `spring.jpa.hibernate.ddl-auto=update` creates
the tables automatically from the entity classes the first time it connects.

## Catalog seeding (2026 price list)

`src/main/java/.../config/CatalogSeeder.java` runs once on startup and, **only if the
`categories` and `products` tables are completely empty**, loads:

- `src/main/resources/seed/categories.json` — 17 categories
- `src/main/resources/seed/products.json` — 219 products, transcribed from
  `FINAL_ANISH_CRACKERS_PRICE_LIST_2026_AUG.pdf`

This means a fresh database is fully stocked the moment the app first starts — no manual
re-entry of 219 products through the admin panel. It is safe to redeploy: because it only
acts on an empty table, it will never duplicate rows or overwrite anything an admin has
since added, edited, or deleted.

Two items from the price list — **Magical Pots** and **Star Pots** — were left out of the
seed because the source PDF has no price listed for either (blank rate columns). Add them
from the admin panel once you have their rates.

To reseed from scratch (e.g. on a fresh environment), just make sure the tables are empty
before the first startup. To change what gets seeded, edit the two JSON files above before
first deploy — editing them after the tables already have data has no effect, by design.

## Admin login

Default admin password is set in `application.properties` as `app.admin.password`
(currently `anishcrackers2026`) — change this before going live.
