-- ---------------------------------------------------------------------------
-- INVENTORY VAULT ITEMS
-- ---------------------------------------------------------------------------
-- Stores extra physical card inventory outside a user's collection binder/profile.
--
-- This table is intentionally separate from collection completion tracking.
--
-- Collection Page:
--   "This card is assigned to my binder/profile goal."
--
-- Inventory Vault:
--   "I have extra copies of this card outside the binder."
--
-- Phase 1 only tracks quantity.
-- Future phases may add condition, centering, damage notes, trade/sale visibility,
-- print/export support, and have/want matching.
-- ---------------------------------------------------------------------------

CREATE EXTENSION IF NOT EXISTS pgcrypto;


-- ---------------------------------------------------------------------------
-- INVENTORY VAULT ITEMS TABLE
-- ---------------------------------------------------------------------------
-- One row represents one card variant held in the user's Vault.
--
-- Example:
--   user has 2 extra Base Set Charizard Holo copies outside their binder.
-- ---------------------------------------------------------------------------

CREATE TABLE inventory_vault_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL,

    card_variant_id UUID NOT NULL,

    quantity INTEGER NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_inventory_vault_items_card_variant
        FOREIGN KEY (card_variant_id)
        REFERENCES card_variants(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_inventory_vault_user_card_variant
        UNIQUE (user_id, card_variant_id),

    CONSTRAINT chk_inventory_vault_quantity_positive
        CHECK (quantity > 0)
);


-- ---------------------------------------------------------------------------
-- INDEXES
-- ---------------------------------------------------------------------------
-- These support looking up a user's Vault and future matching by card variant.
-- ---------------------------------------------------------------------------

CREATE INDEX idx_inventory_vault_items_user_id
    ON inventory_vault_items(user_id);

CREATE INDEX idx_inventory_vault_items_card_variant_id
    ON inventory_vault_items(card_variant_id);