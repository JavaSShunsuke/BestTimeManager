CREATE TABLE IF NOT EXISTS player(
    player_id VARCHAR(8)   PRIMARY KEY,
    player_name VARCHAR(256),
    player_is_showed BOOLEAN
);

CREATE TABLE IF NOT EXISTS event(
    event_id VARCHAR(8)   PRIMARY KEY,
    event_name VARCHAR(256),
    event_is_showed BOOLEAN
);

CREATE TABLE IF NOT EXISTS record(
    record_id VARCHAR(8)   PRIMARY KEY,
    player_id VARCHAR(8),
    event_id VARCHAR(8),
    record_time VARCHAR(32),
    best_is_showed BOOLEAN,
    add_now_date VARCHAR(32),
    FOREIGN KEY(player_id)
    REFERENCES player(player_id),
    FOREIGN KEY(event_id)
    REFERENCES event(event_id)
);

CREATE TABLE IF NOT EXISTS lap_time(
    lap_time_id VARCHAR(8)   PRIMARY KEY,
    record_id VARCHAR(8),
    lap_time_num VARCHAR(8),
    lap_time_record VARCHAR(32),
    lap_time_is_showed BOOLEAN,
    lap_time_memo VARCHAR(128),
    FOREIGN KEY(record_id)
    REFERENCES record(record_id)
);