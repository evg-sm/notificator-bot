create table if not exists bot.notification
(
    id          bigserial primary key   not null,
    user_id     bigint                  not null,
    chat_id     varchar(50)             not null,
    type        varchar(20)             not null,
    send_status varchar(20)             not null,
    text        varchar(500)            not null,
    send_time   TIMESTAMP               not null,
    create_time TIMESTAMP DEFAULT now() not null,
    update_time TIMESTAMP DEFAULT now() not null
);
