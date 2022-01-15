DELETE FROM USER WHERE USER_ID<999;

INSERT INTO USER (user_id, password, nickname, role) VALUES
(1, '$2a$10$6uOn6A2WAWBGZGZEGvSW6.WuWrqkNwxwkWN1ya0I2//h7sxpkj5b6', 'nick01', 'ROLE_ADMIN,ROLE_USER'),
(2, '$2a$10$8q9tgFaMIUxSqnnlVqhmN.DcWVHgsknrueBdxBAzT1G7G5HCwsnAC', 'nick02', 'ROLE_USER'),
(3, '$2a$10$E86mKigOx1NeIr7D6CJM3OQnWdaPXOjWe4OoRqDqFgNgowvJW9nAi', 'nick13', '');

DELETE FROM TODO WHERE TODO_ID>0;

INSERT INTO TODO (creator_id, title, deadline, description) VALUES
( 1, 'user 1 test todo 1', PARSEDATETIME('2022.11.11','yyyy.MM.dd'), 'find a girlfriend before 11.11'),
( 1, 'user 1 test todo 2', PARSEDATETIME('2022.11.12','yyyy.MM.dd'), 'enjoy the 11.11 with GF!'),
( 3, 'user 3 test todo 1', PARSEDATETIME('2022.11.11','yyyy.MM.dd'), 'hack the webserver to mess up admins sweet time'),
( 2, 'user 2 test todo 1', PARSEDATETIME('2099.12.31','yyyy.MM.dd'), 'love user1 until forever');

DELETE FROM USER_META WHERE USER_ID<999;

INSERT INTO USER_META (user_id) VALUES
( 1 ),( 2 ),( 3 );
