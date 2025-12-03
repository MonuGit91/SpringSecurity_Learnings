-- USERS table (simple Oracle SQL – no PL/SQL)
CREATE TABLE users (
    username VARCHAR2(100) PRIMARY KEY,
    password VARCHAR2(500) NOT NULL,
    enabled NUMBER(1) NOT NULL
);

-- AUTHORITIES table
CREATE TABLE authorities (
    username VARCHAR2(100) NOT NULL,
    authority VARCHAR2(100) NOT NULL,
    CONSTRAINT fk_authorities_users
        FOREIGN KEY (username) REFERENCES users(username)
);

-- UNIQUE INDEX
CREATE UNIQUE INDEX ix_auth_username 
    ON authorities (username, authority);
