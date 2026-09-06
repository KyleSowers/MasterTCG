UPDATE inventory_vault_items
SET card_condition = 'LIGHTLY_PLAYED'
WHERE card_condition = 'LIGHT_PLAY';

UPDATE inventory_vault_items
SET card_condition = 'MODERATELY_PLAYED'
WHERE card_condition = 'MODERATE_PLAY';

UPDATE inventory_vault_items
SET card_condition = 'HEAVILY_PLAYED'
WHERE card_condition = 'HEAVY_PLAY';

UPDATE inventory_vault_items
SET card_condition = 'NEAR_MINT'
WHERE card_condition IS NULL
   OR card_condition = 'UNKNOWN';