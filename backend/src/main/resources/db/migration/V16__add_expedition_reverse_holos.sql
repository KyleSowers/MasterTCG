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
WHERE c.set_id = 'dddddddd-dddd-dddd-dddd-dddddddddddd'
  AND c.card_number ~ '^[0-9]+$'
  AND CAST(c.card_number AS INTEGER) BETWEEN 1 AND 159
ON CONFLICT (card_id, finish) DO NOTHING;

UPDATE sets
SET total_cards_master = 324
WHERE id = 'dddddddd-dddd-dddd-dddd-dddddddddddd';
