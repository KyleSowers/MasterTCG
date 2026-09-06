ALTER TABLE inventory_vault_items
ALTER COLUMN card_condition SET DEFAULT 'NEAR_MINT';

UPDATE inventory_vault_items
SET card_condition = 'NEAR_MINT'
WHERE card_condition = 'UNKNOWN';