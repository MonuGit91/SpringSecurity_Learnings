/*
Spring Boot sees schema.sql in src/main/resources.
On startup, it automatically executes this script against your DataSource (especially with H2 / dev DBs).
Result: you now have two empty tables users and authorities
This schema matches exactly what JdbcUserDetailsManager expects by default.
*/

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