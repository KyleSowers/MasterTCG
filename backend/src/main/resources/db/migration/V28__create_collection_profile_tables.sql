-- ---------------------------------------------------------------------------
-- COLLECTION PROFILE TABLES
-- ---------------------------------------------------------------------------
-- Stores a user's collection-tracking rules.
--
-- This is the backend version of the current frontend/localStorage profile:
-- - profile name
-- - collection style
-- - finish scope
-- - rarity scope
-- - catalog scope
-- - selected sets
--
-- For now, user_id is a placeholder UUID field.
-- Later, when real user accounts exist, this can be connected to a users table.
-- ---------------------------------------------------------------------------

CREATE EXTENSION IF NOT EXISTS pgcrypto;


-- ---------------------------------------------------------------------------
-- COLLECTION PROFILES
-- ---------------------------------------------------------------------------
-- One row represents one saved collection profile.
-- Example:
--   "Kyle's WOTC Master Set Profile"
--   style = MASTER_SET
-- ---------------------------------------------------------------------------

CREATE TABLE collection_profiles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL,

    name VARCHAR(120) NOT NULL,
    collection_style VARCHAR(40) NOT NULL,

    include_normal BOOLEAN NOT NULL DEFAULT TRUE,
    include_holo BOOLEAN NOT NULL DEFAULT TRUE,
    include_reverse_holo BOOLEAN NOT NULL DEFAULT TRUE,
    include_special_finishes BOOLEAN NOT NULL DEFAULT TRUE,

    include_common BOOLEAN NOT NULL DEFAULT TRUE,
    include_uncommon BOOLEAN NOT NULL DEFAULT TRUE,
    include_rare BOOLEAN NOT NULL DEFAULT TRUE,

    include_main_cards BOOLEAN NOT NULL DEFAULT TRUE,
    include_secret_cards BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ---------------------------------------------------------------------------
-- COLLECTION PROFILE SETS
-- ---------------------------------------------------------------------------
-- Join table between a collection profile and the sets it tracks.
--
-- A profile can include many sets.
-- A set can appear in many profiles.
-- ---------------------------------------------------------------------------

CREATE TABLE collection_profile_sets (
    profile_id UUID NOT NULL,
    set_id UUID NOT NULL,

    PRIMARY KEY (profile_id, set_id),

    CONSTRAINT fk_collection_profile_sets_profile
        FOREIGN KEY (profile_id)
        REFERENCES collection_profiles(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_collection_profile_sets_set
        FOREIGN KEY (set_id)
        REFERENCES sets(id)
        ON DELETE CASCADE
);


-- ---------------------------------------------------------------------------
-- INDEXES
-- ---------------------------------------------------------------------------
-- These make profile lookup faster when we later query by user or set.
-- ---------------------------------------------------------------------------

CREATE INDEX idx_collection_profiles_user_id
    ON collection_profiles(user_id);

CREATE INDEX idx_collection_profile_sets_set_id
    ON collection_profile_sets(set_id);