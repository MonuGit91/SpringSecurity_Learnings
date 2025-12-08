/*
-- Best Practice (Use CITEXT for case-insensitive login as it is Perfect for authentication tables (admin, Admin, ADMIN → same user)
-- Step 1: Enable the extension
--	Run this ONCE inside your database:
		CREATE EXTENSION IF NOT EXISTS citext;
-- Step 2: Now Create tables using CITEXT (recommended)
*/
CREATE TABLE IF NOT EXISTS users (
    username CITEXT PRIMARY KEY,
    password VARCHAR(500) NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS authorities (
    username CITEXT NOT NULL,
    authority CITEXT NOT NULL,
    CONSTRAINT fk_authorities_users
        FOREIGN KEY (username)
        REFERENCES users(username)
);

CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username 
    ON authorities (username, authority);
