CREATE TABLE IF NOT EXISTS player(
    playerId VARCHAR(8)   PRIMARY KEY,
    playerName VARCHAR(256),
    playerFlag BOOLEAN
);

CREATE TABLE IF NOT EXISTS event(
    eventId VARCHAR(8)   PRIMARY KEY,
    eventName VARCHAR(256),
    eventFlag BOOLEAN
);

CREATE TABLE IF NOT EXISTS record(
    recordId VARCHAR(8)   PRIMARY KEY,
    playerId VARCHAR(8),
    eventId VARCHAR(8),
    recordTime VARCHAR(32),
    recordFlag BOOLEAN,
    bestFlag BOOLEAN,
    FOREIGN KEY(playerId)
    REFERENCES player(playerId),
    FOREIGN KEY(eventId)
    REFERENCES event(eventId)
);