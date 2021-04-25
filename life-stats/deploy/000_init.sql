CREATE TYPE event_type AS ENUM('POINT', 'SEGMENT', 'COUNT');

CREATE TABLE event_classes(
    ecid          SERIAL NOT NULL PRIMARY KEY,
    text_id       VARCHAR(50) NOT NULL,
    name          VARCHAR(50) NOT NULL,
    description   VARCHAR(50) DEFAULT NULL,
    type          event_type NOT NULL
);

CREATE TABLE events(
    eid           SERIAL NOT NULL PRIMARY KEY,
    ecid          INT NOT NULL,
    begin         TIMESTAMP DEFAULT NULL,
    "end"         TIMESTAMP DEFAULT NULL,
    count         INT DEFAULT NULL,
    data          JSONB DEFAULT NULL
);
