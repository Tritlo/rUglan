CREATE TABLE CALEVENTS (
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    location TEXT NOT NULL,
    start INTEGER NOT NULL,
    finish INTEGER NOT NULL,
    hidden BOOLEAN NOT NULL,
    PRIMARY KEY(location, start)
);

CREATE TABLE COLORS (
    name TEXT NOT NULL,
    color INTEGER NOT NULL,
    PRIMARY KEY(name)
);