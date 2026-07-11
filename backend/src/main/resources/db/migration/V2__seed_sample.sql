-- Temporary sample set + cards so we can test endpoints immediately
INSERT INTO users (id, email, password_hash, created_at)
VALUES (
  'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
  'demo@mastertcg.local',
  'demo',
  NOW()
);

INSERT INTO sets (id, name, era, release_date,total_cards_main, total_cards_master)
VALUES ('11111111-1111-1111-1111-111111111111', 'Base Set (v0 sample)', 'Current Era', NULL, 3, 6);


