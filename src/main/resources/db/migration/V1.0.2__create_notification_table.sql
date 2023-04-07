create table if not exists bot.notification
(
    id        bigint       not null primary key,
    user_id   bigint       not null,
    chat_id   varchar(50)  not null,
    type      varchar(20)  not null,
    send_status    varchar(30)  not null,
    text      varchar(500) not null,
    date_time TIMESTAMP    not null
);

create sequence if not exists bot.notification_id;
