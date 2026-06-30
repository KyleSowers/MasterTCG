CREATE TABLE sets (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    era TEXT NOT NULL,
    release_date DATE,
    total_cards_main INT NOT NULL,
    total_cards_master INT NOT NULL
);

CREATE TABLE cards (
    id UUID PRIMARY KEY,
    set_id UUID NOT NULL REFERENCES sets(id) ON DELETE CASCADE,
    card_number TEXT NOT NULL,
    name TEXT NOT NULL,
    rarity TEXT NOT NULL,
    -- finish TEXT NOT NULL DEFAULT 'NORMAL',
    -- variant_group TEXT,
    CONSTRAINT uq_cards_set_number UNIQUE (set_id, card_number)
);

CREATE TABLE card_variants (
    id UUID PRIMARY KEY,
    card_id UUID NOT NULL REFERENCES cards(id) ON DELETE CASCADE,
    finish TEXT NOT NULL,
    CONSTRAINT uq_card_variants_card_finish UNIQUE (card_id, finish)
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE user_cards (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    card_variant_id UUID NOT NULL REFERENCES card_variants(id) ON DELETE CASCADE,
    owned_count INT NOT NULL DEFAULT 0,
    condition TEXT,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT uq_user_card_variant UNIQUE (user_id, card_variant_id)
);