---
name: database-operations
description: >-
  Use this skill when connecting to, querying, migrating, or fixing master data in
  the Iyarkaivanam production or QA MySQL databases. Contains credentials, DDL migration
  safety rules, and master data ID references.
---

# Database Operations & Schema Runbook

Guidelines for querying and executing schema modifications or data updates on the production and QA MySQL databases.

## Database Connection Details

### Production Database
- **Host**: `143.244.134.163` (Port `3306`)
- **Database**: `iyarkaivanam_db`
- **User**: `app_user`
- **Password**: `2025*Rbeipaqct`
- **Hibernate DDL Setting**: `ddl-auto=none` (**Manual DDL is strictly mandatory**)

### QA Database
- **Host**: Clever Cloud MySQL (configured in `config/application-qa.properties`)
- **Hibernate DDL Setting**: `ddl-auto=update`

---

## Schema Migration Safety Policy

> [!CAUTION]
> Production runs with `spring.jpa.hibernate.ddl-auto=none`. Adding or modifying JPA `@Entity` fields will **NOT** alter the production database schema automatically. If a column is missing in production, Spring Boot queries will fail with SQL syntax errors!

### Workflow for Entity Changes:
1. Write the JPA entity changes in Java.
2. Formulate the explicit SQL DDL statement (e.g., `ALTER TABLE <table_name> ADD COLUMN <col> <type>;`).
3. Execute the DDL against the production database (`143.244.134.163`) before or during application deployment.
4. Backfill existing rows with sensible defaults.

---

## Master Data Reference & Mappings

### Standard Units (`unit` table)
| `unit_id` | Name | Tamil Name | Short Code | Type |
| :--- | :--- | :--- | :--- | :--- |
| **16** | Kilogram | கிலோ | kg | WEIGHT |
| **22** | Litre | லிட்டர் | L | VOLUME |
| **26** | Miscellaneous | இதர | msc | COUNT / MISC |

### Expense Items & Stock Categories
| Item Name | `expense_item_id` | Expected `unit_id` | Expected `category_id` |
| :--- | :--- | :--- | :--- |
| **Sugarcane Bundle** | `1` | `16` (Kilogram / kg) | `6` (Sugarcane Stocks) |
| **Milk** | `5` | `22` (Litre / L) | Stock / Milk Category |

### Important Historical Data Rules:
- **Never blindly map by item name alone**: In historical records, peeling labour (`கரும்பு சீவ`) was sometimes recorded with `unit_id=16` or under wages/salary categories (`category_id=7` or `9`).
- Stock items must match both the expected unit and inventory category (`category_id=6` for sugarcane stocks) before mapping to `expense_item_id=1`.
- Expense item names should be standardized in the database as `'Sugarcane Bundle'` and `'Milk'` for consistency across reporting queries.

---

## Example Administrative SQL Snippets

### Backfilling / Adding Column
```sql
ALTER TABLE product ADD COLUMN is_active TINYINT(1) DEFAULT 1;
UPDATE product SET is_active = 1 WHERE is_active IS NULL;
```

### Historical Expense Mapping Verification
```sql
-- Verify Sugarcane Stock expenses before updating
SELECT id, item, quantity, unit_id, category_id, expense_item_id
FROM expense
WHERE category_id = 6 AND unit_id = 16;

-- Map and standardize
UPDATE expense
SET expense_item_id = 1, item = 'Sugarcane Bundle'
WHERE category_id = 6 AND unit_id = 16 AND expense_item_id IS NULL;
```
