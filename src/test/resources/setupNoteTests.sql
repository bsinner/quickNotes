DELETE FROM notes;

INSERT INTO notes
    (id, user_id, title, contents)
VALUES
    (1, 2, "Hello World Note", '{"ops":[{"insert":"hello "},{"attributes":{"bold":true},"insert":"world"},{"insert":"\\n"}]}')
    , (2, 3, "Hello World Note", '{"ops":[{"insert":"hello "},{"attributes":{"bold":true},"insert":"world"},{"insert":"\\n"}]}')
    , (3, 3, "Lorem Ipsum", '{"ops":[{"attributes":{"italic":true},"insert":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."},{"insert":"\\n"}]}');