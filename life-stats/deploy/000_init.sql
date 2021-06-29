CREATE TABLE users
(
    uid        INT         NOT NULL PRIMARY KEY,
    state      VARCHAR(50) NOT NULL,
    state_data text        NOT NULL
);

CREATE TABLE event_classes
(
    ecid        SERIAL      NOT NULL PRIMARY KEY,
    uid         INT         NOT NULL,
    text_id     VARCHAR(50) NOT NULL,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(50) DEFAULT NULL,
    type        VARCHAR(10) NOT NULL
);

CREATE TABLE events
(
    eid   SERIAL NOT NULL PRIMARY KEY,
    ecid  INT    NOT NULL,
    begin TIMESTAMP DEFAULT NULL,
    "end" TIMESTAMP DEFAULT NULL,
    count INT       DEFAULT NULL,
    data  text      DEFAULT NULL
);
