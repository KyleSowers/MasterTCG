CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO card_variants (
    id,
    card_id,
    finish
)
SELECT
    gen_random_uuid(),
    c.id,
    'REVERSE_HOLO'
FROM cards c
WHERE c.set_id = 'ffffffff-ffff-ffff-ffff-ffffffffffff'
  AND c.card_number ~ '^[0-9]+$'
  AND CAST(c.card_number AS INTEGER) BETWEEN 1 AND 150
ON CONFLICT (card_id, finish) DO NOTHING;

UPDATE sets
SET total_cards_main = 182,
    total_cards_master = 332
WHERE id = 'ffffffff-ffff-ffff-ffff-ffffffffffff';
