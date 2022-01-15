DROP TABLE IF EXISTS USER;
CREATE TABLE USER
(
    user_id BIGINT NOT NULL COMMENT 'userId',
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(30) NULL DEFAULT NULL,
    role VARCHAR(50) NOT NULL,
    avatar BLOB DEFAULT NULL COMMENT 'User Avatar',
    watch ARRAY NOT NULL DEFAULT () COMMENT 'watchList',
	PRIMARY KEY (user_id)
);



DROP TABLE IF EXISTS TODO;
CREATE TABLE TODO
(
    todo_id BIGINT NOT NULL auto_increment,
    creator_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description CLOB DEFAULT '',
    importance INT DEFAULT 0,
    parent_id BIGINT DEFAULT NULL,
    plant_time BIGINT DEFAULT 0,
    deadline TIMESTAMP NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    complete_at TIMESTAMP NOT NULL DEFAULT PARSEDATETIME('1970 8','yyyy hh'),
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    local_id CHAR(36) NOT NULL DEFAULT RANDOM_UUID(),
    primary key (todo_id, creator_id),
    foreign key (creator_id) references USER(user_id),
    foreign key (parent_id) references TODO(todo_id)
);
create index cid_idx on TODO(creator_id);

DROP TABLE IF EXISTS USER_META;
CREATE TABLE USER_META
(
    user_id BIGINT NOT NULL,
    plant_time BIGINT DEFAULT 0,
    sync_at TIMESTAMP NOT NULL DEFAULT PARSEDATETIME('1970 8','yyyy hh'),
    revoke_at TIMESTAMP NOT NULL DEFAULT PARSEDATETIME('1970 8','yyyy hh'),
    primary key (user_id),
    foreign key (user_id) references USER(user_id)
);

