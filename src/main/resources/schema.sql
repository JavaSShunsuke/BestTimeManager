CREATE TABLE IF NOT EXISTS swimmer(
    swimmerId VARCHAR(8)   PRIMARY KEY,
    swimmerName VARCHAR(256),
    swimmerFlag BOOLEAN
);

CREATE TABLE IF NOT EXISTS event(
    eventId VARCHAR(8)   PRIMARY KEY,
    eventName VARCHAR(256),
    eventFlag BOOLEAN
);

CREATE TABLE IF NOT EXISTS record(
    recordId VARCHAR(8)   PRIMARY KEY,
    swimmerId VARCHAR(8),
    eventId VARCHAR(8),
    recordTime TIME,
    recordFlag BOOLEAN,
    FOREIGN KEY(swimmerId)
    REFERENCES swimmer(swimmerId),
    FOREIGN KEY(eventId)
    REFERENCES event(eventId)
);