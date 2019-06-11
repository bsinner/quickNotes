DROP TABLE IF EXISTS refresh_tokens, user_roles, activation_tokens, notes, users;

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
    , username VARCHAR(30) NOT NULL UNIQUE
    , email VARCHAR(100) NOT NULL UNIQUE
    , password BLOB NOT NULL
);

CREATE TABLE notes (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
    , user_id INT NOT NULL
    , title VARCHAR(40) DEFAULT NULL
    , contents TEXT
    , creation_date DATETIME DEFAULT CURRENT_TIMESTAMP
    , CONSTRAINT users_notes_fk FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
    , UNIQUE (user_id, title)
);

CREATE TABLE user_roles (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
    , user_id INT NOT NULL
    , role ENUM ('UNACTIVATED', 'USER', 'ADMIN') NOT NULL
    , CONSTRAINT users_user_roles_fk FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
    , CONSTRAINT UNIQUE(user_id, role)
);

CREATE TABLE activation_tokens (
    id VARCHAR(255) NOT NULL PRIMARY KEY
    , user_id INT NOT NULL
    , creation_date DATETIME DEFAULT CURRENT_TIMESTAMP
    , expire_date DATETIME DEFAULT NULL
    , CONSTRAINT users_activation_tokens_fk FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE refresh_tokens (
    id VARCHAR(255) NOT NULL PRIMARY KEY
    , user_id INT NOT NULL
    , expire_date DATETIME
    , CONSTRAINT users_refresh_tokens_fk FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

DELIMITER ~~

    -- Give new users role UNACTIVATED by default
    CREATE TRIGGER newUserInsert AFTER INSERT ON users
        FOR EACH ROW
        BEGIN
            IF 0 = (SELECT COUNT(id) FROM user_roles WHERE user_id = NEW.id) THEN
               INSERT INTO user_roles (user_id, role) VALUES (NEW.id, 'UNACTIVATED');
            END IF;
        END;~~

    -- Set activation tokens to expire in 1 day
    CREATE TRIGGER newActTokenInsert BEFORE INSERT ON activation_tokens
        FOR EACH ROW
        BEGIN
            SET NEW.expire_date = ADDDATE(CURRENT_TIMESTAMP, INTERVAL 1 DAY);
        END;~~

    -- Set refresh tokens to expire in 5 days
    CREATE TRIGGER newRefreshTokenInsert BEFORE INSERT ON refresh_tokens
        FOR EACH ROW
        BEGIN
            SET NEW.expire_date = ADDDATE(CURRENT_TIMESTAMP, INTERVAL 5 DAY);
        END; ~~

DELIMITER ;

INSERT INTO users
    (id, username, email, password)
VALUES
    (1, 'bsmith', 'bsmith@gmail.555', 'password1')
    , (2, 'zsmith', 'zsmith@gmail.555', 'password2')
    , (3, 'ldavis', 'ldavis@gmail.555', 'password35');

INSERT INTO notes
    (user_id, title, contents)
VALUES
    (2, 'Hello World Note', '{"ops":[{"insert":"hello "},{"attributes":{"bold":true},"insert":"world"},{"insert":"\\\\n"}]}')
    , (3, 'Hello World Note', '{"ops":[{"insert":"hello "},{"attributes":{"bold":true},"insert":"world"},{"insert":"\\\\n"}]}')
    , (3, 'Lorem Ipsum', '{"ops":[{"attributes":{"italic":true},"insert":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."},{"insert":"\\\\n"}]}');
