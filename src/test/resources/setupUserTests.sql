DELETE FROM users;

INSERT INTO users
    (id, username, email, password)
VALUES
    (1, "bsmith", "bsmith@gmail.com", "password1")
    , (2, "admin", "admin@gmail.com", "admin")
    , (3, "ldavis", "ldavis@gmail.com", "password35");