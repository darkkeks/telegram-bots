CREATE TABLE submissions(
    id            SERIAL NOT NULL PRIMARY KEY,
    login         VARCHAR(50) NOT NULL,
    contest_id    INT NOT NULL,
    submit_time   TIMESTAMP NOT NULL,
    standings     JSONB NOT NULL
)
