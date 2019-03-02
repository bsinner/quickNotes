DROP TABLE IF EXISTS notes;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY
    , username VARCHAR(30) NOT NULL UNIQUE
    , email VARCHAR(100) NOT NULL UNIQUE
    , password VARCHAR(25) NOT NULL
);

CREATE TABLE notes (
    id INT AUTO_INCREMENT PRIMARY KEY
    , user_id INT NOT NULL
    , title VARCHAR(40)
    , contents JSON
    , creation_date DATETIME DEFAULT CURRENT_TIMESTAMP
    , CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    , UNIQUE (user_id, title)
);

INSERT INTO users
    (id, username, email, password)
VALUES
    (1, "bsmith", "bsmith@gmail.com", "password1")
    , (2, "zsmith", "zsmith@gmail.com", "password2")
    , (3, "ldavis", "ldavis@gmail.com", "password35");

INSERT INTO notes
    (user_id, title, contents)
VALUES
    (2, "Hello World Note", '{"ops":[{"insert":"hello "},{"attributes":{"bold":true},"insert":"world"},{"insert":"\\n"}]}')
    , (3, "Hello World Note", '{"ops":[{"insert":"hello "},{"attributes":{"bold":true},"insert":"world"},{"insert":"\\n"}]}');