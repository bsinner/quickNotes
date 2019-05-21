DROP TABLE IF EXISTS user_roles, activation_tokens, notes, users;

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
    , username VARCHAR(30) NOT NULL UNIQUE
    , email VARCHAR(100) NOT NULL UNIQUE
    , password VARCHAR(25) NOT NULL
    , activated BOOLEAN DEFAULT FALSE
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
    , role ENUM ("USER", "ADMIN") DEFAULT "USER"
    , CONSTRAINT users_user_roles_fk FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE activation_tokens (
    id INT NOT NULL PRIMARY KEY
    , user_id INT NOT NULL
    , creation_date DATETIME DEFAULT CURRENT_TIMESTAMP
    , CONSTRAINT users_activation_tokens_fk FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

DELIMITER ~~
    CREATE TRIGGER newUserInsert AFTER INSERT ON users
        FOR EACH ROW
        BEGIN
            IF 0 = (SELECT COUNT(id) FROM user_roles WHERE user_id = NEW.id) THEN
               INSERT INTO user_roles (user_id) VALUES (NEW.id);
            END IF;
        END;~~
DELIMITER ;

INSERT INTO users
    (id, username, email, password)
VALUES
    (1, "bsmith", "bsmith@gmail.com", "password1")
    , (2, "zsmith", "zsmith@gmail.com", "password2")
    , (3, "ldavis", "ldavis@gmail.com", "password35");

INSERT INTO notes
    (user_id, title, contents)
VALUES
    (2, "Hello World Note", '{"ops":[{"insert":"hello "},{"attributes":{"bold":true},"insert":"world"},{"insert":"\\\\n"}]}')
    , (3, "Hello World Note", '{"ops":[{"insert":"hello "},{"attributes":{"bold":true},"insert":"world"},{"insert":"\\\\n"}]}')
    , (3, "Lorem Ipsum", '{"ops":[{"attributes":{"italic":true},"insert":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."},{"insert":"\\\\n"}]}');
