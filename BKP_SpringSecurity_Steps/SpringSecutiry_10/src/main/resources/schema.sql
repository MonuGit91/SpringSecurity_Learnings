-- Now your CREATE statements
/*
-- uncomment only when table is not created
CREATE TABLE users (
    username VARCHAR2(256) NOT NULL,
    password VARCHAR2(256) NOT NULL,
    enabled NUMBER(1, 0) DEFAULT 1 NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (username)
);

CREATE TABLE authorities (
    username VARCHAR2(256) NOT NULL,
    authority VARCHAR2(256) NOT NULL,
    CONSTRAINT pk_authorities PRIMARY KEY (username, authority),
    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users (username)
);
*/


-- Note: In Oracle, foreign keys (like fk_authorities_users) must reference the table
-- before the referenced table is dropped, so drop 'authorities' first.