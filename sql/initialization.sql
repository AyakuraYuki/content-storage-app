DROP TABLE IF EXISTS content;
CREATE TABLE content (
  id       TEXT PRIMARY KEY,
  item     TEXT NOT NULL,
  jsonData TEXT
);

DROP TABLE IF EXISTS `settings`;
CREATE TABLE `settings` (
  id    TEXT PRIMARY KEY,
  key   TEXT NOT NULL,
  value TEXT
);