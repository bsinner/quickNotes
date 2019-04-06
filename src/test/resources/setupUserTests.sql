DELETE FROM users;

INSERT INTO users
    (id, username, email, password, role)
VALUES
    (1, "bsmith", "bsmith@gmail.com", "password1", "USER")
    , (2, "admin", "admin@gmail.com", "admin", "ADMIN")
    , (3, "ldavis", "ldavis@gmail.com", "password35", "USER");