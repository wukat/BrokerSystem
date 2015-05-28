# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table test_entity (
  id                        integer not null,
  name                      varchar(255),
  constraint pk_test_entity primary key (id))
;

create sequence test_entity_seq;




# --- !Downs

drop table if exists test_entity cascade;

drop sequence if exists test_entity_seq;

