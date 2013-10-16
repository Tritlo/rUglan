CREATE TABLE CALEVENTS (
 name TEXT NOT NULL,
 description TEXT NOT NULL,
 location TEXT NOT NULL,
 start INTEGER NOT NULL,
 finish INTEGER NOT NULL,
 PRIMARY KEY(location, start)
);