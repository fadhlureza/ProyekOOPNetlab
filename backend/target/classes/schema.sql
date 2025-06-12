-- Membuat tabel players untuk menyimpan data pengguna
CREATE TABLE IF NOT EXISTS players (
    player_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Membuat tabel achievements sebagai tabel master untuk semua pencapaian
CREATE TABLE IF NOT EXISTS achievements (
    achievement_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    icon_class VARCHAR(50)
);

-- Membuat tabel game_records untuk mencatat setiap sesi permainan
CREATE TABLE IF NOT EXISTS game_records (
    record_id SERIAL PRIMARY KEY,
    player_id INTEGER NOT NULL REFERENCES players(player_id),
    score INTEGER NOT NULL,
    game_mode VARCHAR(50) NOT NULL,
    max_combo INTEGER DEFAULT 0,
    played_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabel penghubung untuk melacak pencapaian yang telah dibuka oleh pemain
CREATE TABLE IF NOT EXISTS player_achievements (
    player_id INTEGER NOT NULL REFERENCES players(player_id),
    achievement_id INTEGER NOT NULL REFERENCES achievements(achievement_id),
    unlocked_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (player_id, achievement_id)
);

-- Mengisi data master untuk tabel achievements (hanya jika kosong)
INSERT INTO achievements (name, description, icon_class)
SELECT 'Skor Pertama', 'Selesaikan permainan pertamamu.', 'fas fa-gamepad'
WHERE NOT EXISTS (SELECT 1 FROM achievements WHERE name = 'Skor Pertama');

INSERT INTO achievements (name, description, icon_class)
SELECT 'Combo Master', 'Capai kombo maksimal 20 atau lebih.', 'fas fa-bolt'
WHERE NOT EXISTS (SELECT 1 FROM achievements WHERE name = 'Combo Master');

INSERT INTO achievements (name, description, icon_class)
SELECT 'Pemain Profesional', 'Dapatkan skor lebih dari 1000.', 'fas fa-trophy'
WHERE NOT EXISTS (SELECT 1 FROM achievements WHERE name = 'Pemain Profesional');