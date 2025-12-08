/*
-- below query is for h2 database
create table users(
	username varchar_ignorecase(50) not null primary key,
	password varchar_ignorecase(500) not null,
	enabled boolean not null
);

create table authorities (
	username varchar_ignorecase(50) not null,
	authority varchar_ignorecase(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);
*/


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
