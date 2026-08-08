CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Legendary Collection: all 110 cards get Reverse Holo
INSERT INTO card_variants (id, card_id, finish)
SELECT gen_random_uuid(), c.id, 'REVERSE_HOLO'
FROM cards c
WHERE c.set_id = 'cccccccc-cccc-cccc-cccc-cccccccccccc'
ON CONFLICT (card_id, finish) DO NOTHING;

UPDATE sets
SET total_cards_main = 110,
    total_cards_master = 220
WHERE id = 'cccccccc-cccc-cccc-cccc-cccccccccccc';


-- Expedition Base Set: 001-159 get Reverse Holo
INSERT INTO card_variants (id, card_id, finish)
SELECT gen_random_uuid(), c.id, 'REVERSE_HOLO'
FROM cards c
WHERE c.set_id = 'dddddddd-dddd-dddd-dddd-dddddddddddd'
  AND c.card_number ~ '^[0-9]+$'
  AND CAST(c.card_number AS INTEGER) BETWEEN 1 AND 159
ON CONFLICT (card_id, finish) DO NOTHING;

UPDATE sets
SET total_cards_main = 165,
    total_cards_master = 324
WHERE id = 'dddddddd-dddd-dddd-dddd-dddddddddddd';


-- Aquapolis: 001-147 get Reverse Holo
INSERT INTO card_variants (id, card_id, finish)
SELECT gen_random_uuid(), c.id, 'REVERSE_HOLO'
FROM cards c
WHERE c.set_id = 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee'
  AND c.card_number ~ '^[0-9]+$'
  AND CAST(c.card_number AS INTEGER) BETWEEN 1 AND 147
ON CONFLICT (card_id, finish) DO NOTHING;

UPDATE sets
SET total_cards_main = 182,
    total_cards_master = 329
WHERE id = 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee';


-- Skyridge: 001-150 get Reverse Holo
INSERT INTO card_variants (id, card_id, finish)
SELECT gen_random_uuid(), c.id, 'REVERSE_HOLO'
FROM cards c
WHERE c.set_id = 'ffffffff-ffff-ffff-ffff-ffffffffffff'
  AND c.card_number ~ '^[0-9]+$'
  AND CAST(c.card_number AS INTEGER) BETWEEN 1 AND 150
ON CONFLICT (card_id, finish) DO NOTHING;

UPDATE sets
SET total_cards_main = 182,
    total_cards_master = 332
WHERE id = 'ffffffff-ffff-ffff-ffff-ffffffffffff';
