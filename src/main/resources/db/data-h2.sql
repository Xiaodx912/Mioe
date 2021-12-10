DELETE FROM USER WHERE USER_ID<999;

INSERT INTO USER (user_id, password, nickname, role) VALUES
(1, '$2a$10$6uOn6A2WAWBGZGZEGvSW6.WuWrqkNwxwkWN1ya0I2//h7sxpkj5b6', 'nick01', 'ROLE_ADMIN,ROLE_USER'),
(2, '$2a$10$8q9tgFaMIUxSqnnlVqhmN.DcWVHgsknrueBdxBAzT1G7G5HCwsnAC', 'nick02', 'ROLE_USER'),
(3, '$2a$10$E86mKigOx1NeIr7D6CJM3OQnWdaPXOjWe4OoRqDqFgNgowvJW9nAi', 'nick13', '');
