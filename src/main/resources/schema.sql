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
    lapTimeFlag VARCHAR(32),
    bestFlag BOOLEAN,
    FOREIGN KEY(playerId)
    REFERENCES player(playerId),
    FOREIGN KEY(eventId)
    REFERENCES event(eventId)
);

CREATE TABLE IF NOT EXISTS lapTime(
lapTimeId VARCHAR(8)   PRIMARY KEY,
recordId VARCHAR(8),
lapTimeNum VARCHAR(8),
lapTimeRecord VARCHAR(32),
lapTimeFlag BOOLEAN,
lapTimeMemo VARCHAR(128),
FOREIGN KEY(recordId)
REFERENCES record(recordId)

);