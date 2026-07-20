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
WHERE c.set_id = 'cccccccc-cccc-cccc-cccc-cccccccccccc'
ON CONFLICT (card_id, finish) DO NOTHING;

UPDATE sets
SET total_cards_master = 220
WHERE id = 'cccccccc-cccc-cccc-cccc-cccccccccccc';