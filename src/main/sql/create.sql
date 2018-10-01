CREATE TABLE game (
    id serial NOT NULL PRIMARY KEY,
    data jsonb NOT NULL
);

CREATE TABLE auth_ticket (
    player_id varchar(32) NOT NULL PRIMARY KEY,
    secret varchar(32) NOT NULL
);
