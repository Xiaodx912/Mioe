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
    title VARCHAR(100) NOT NULL,
    description CLOB DEFAULT NULL,
    importance INT DEFAULT 0,
    parent_id BIGINT DEFAULT NULL,
    plant_time BIGINT DEFAULT 0,
    deadline TIMESTAMP NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    complete_at TIMESTAMP DEFAULT NULL,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
    primary key (todo_id),
    foreign key (parent_id) references TODO(todo_id)
);