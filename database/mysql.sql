# change USERNAME and PASSWORD

create database if not exists datebook;
grant all privileges on datebook.* to USERNAME@localhost identified by "PASSWORD";
flush privileges;

use datebook;

drop table if exists tasks;

create table tasks (
  id int not null auto_increment,
  title varchar(200) not null,
  creation_time datetime not null,
  scheduled_time datetime,
  status varchar(50) not null,
  primary key(id)
) default charset=utf8;
