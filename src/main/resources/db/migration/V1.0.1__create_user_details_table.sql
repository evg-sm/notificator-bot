create schema if not exists bot;

create table if not exists bot.user_details
(
    id         bigint      not null primary key,
    first_name varchar(80) not null,
    user_name  varchar(80)
);
