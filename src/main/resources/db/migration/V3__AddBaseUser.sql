INSERT INTO users(id, email, is_active, password, username)
    VALUES (1, 'super@gmail.com', true, '$2a$08$Hj.1fatizGzEnajXyzcXZuU1U3wmdQx41kE8eMIkwuDIAihLn34e.', 'super');

INSERT INTO user_roles(user_id, role_id)
    VALUES (1, 3);