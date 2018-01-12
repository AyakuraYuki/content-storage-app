create table content (
  id        text primary key,
  item      text not null,
  json_data text
);

create table `settings` (
  id    text primary key,
  key   text not null,
  value text
);