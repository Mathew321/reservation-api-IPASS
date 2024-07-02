CREATE DATABASE restaurant_reservation;

USE restaurant_reservation;

create table staff
(
    id       int auto_increment
        primary key,
    email    varchar(255) not null,
    name     varchar(255) not null,
    password varchar(255) not null,
    roles    varchar(255) not null,
    constraint UKec472ckb69kcg093ysma3fyen
        unique (name)
);

create table tables
(
    table_id         bigint auto_increment
        primary key,
    location         varchar(255) null,
    seating_capacity int          null,
    table_number     varchar(255) null,
    constraint UKfjmmqyocmsfsje61iybqifd96
        unique (table_number)
);

create table reservations
(
    reservation_id    bigint auto_increment
        primary key,
    customer_name     varchar(255) not null,
    email             varchar(255) not null,
    number_of_persons int          not null,
    phone             varchar(255) null,
    reservation_time  datetime(6)  not null,
    reservation_token varchar(255) not null,
    table_id          bigint       not null,
    constraint FKritru50ljg3q6ytxriivjlmq6
        foreign key (table_id) references tables (table_id)
);

