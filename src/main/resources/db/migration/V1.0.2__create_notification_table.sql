create table if not exists bot.notification
(
    id     bigint       not null primary key,
    status varchar(30)  not null,
    text   varchar(500) not null,
    date   date         not null,
    time   time         not null
);

create sequence if not exists bot.notification_id;
