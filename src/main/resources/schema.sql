CREATE TABLE IF NOT EXISTS users (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
email varchar(320),
first_name varchar(100),
last_name varchar(100),
registration_date timestamp,
state varchar(50) );

CREATE TABLE IF NOT EXISTS items (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
user_id BIGINT,
url VARCHAR(1000),
resolved_url VARCHAR(1000),
mime_type VARCHAR(1000),
title VARCHAR(1000),
has_image BOOLEAN,
has_video BOOLEAN,
date_resolved timestamp,
CONSTRAINT fk_items_to_users FOREIGN KEY(user_id) REFERENCES users(id),
UNIQUE(id, url) );

CREATE TABLE IF NOT EXISTS item_notes (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
text VARCHAR(2000),
item_id BIGINT,
save_date timestamp,
CONSTRAINT fk_item_notes_to_items FOREIGN KEY(item_id) REFERENCES items(id)
);

CREATE TABLE IF NOT EXISTS tags (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
item_id BIGINT,
name VARCHAR(50),
CONSTRAINT fk_tags_to_items FOREIGN KEY(item_id) REFERENCES items(id) );