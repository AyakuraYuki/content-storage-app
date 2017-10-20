create schema if not exists `content_storage`
  char set utf8;

use `content_storage`;

drop table if exists `content`;
create table `content` (
  id        varchar(512) primary key,
  item      varchar(512) not null,
  json_data text
)
  char set utf8;

drop table if exists `settings`;
create table `settings` (
  id    varchar(512) primary key,
  `key` varchar(512) not null,
  value text
)
  char set utf8;
