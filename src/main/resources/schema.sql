DROP TABLE IF EXISTS folders;

CREATE TABLE folders (
  id        int PRIMARY KEY,
  parent_id int REFERENCES folders (id),
  name      text
);

CREATE INDEX idx_folders_parent_id ON folders(parent_id);
