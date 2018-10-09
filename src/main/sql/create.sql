CREATE TABLE game (
    id serial NOT NULL PRIMARY KEY,
    data jsonb NOT NULL
);

CREATE TABLE player (
    player_id varchar(32) NOT NULL PRIMARY KEY,
    display_name varchar(32) NOT NULL,
    secret varchar(32) NOT NULL
);
