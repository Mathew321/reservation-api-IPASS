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

insert into restaurant_reservation.tables (table_id, location, seating_capacity, table_number) values (1, 'A', 4, '1');
insert into restaurant_reservation.tables (table_id, location, seating_capacity, table_number) values (2, 'B', 4, '2');
insert into restaurant_reservation.tables (table_id, location, seating_capacity, table_number) values (3, 'C', 4, '3');

INSERT INTO staff (email, name, password, roles) VALUES ('admin@resapi.com', 'mathew', '$2a$10$koDFarAYu2NlhVfF1mEOGec3s9RjVhoi2iRd4X0n8v2N3bXS9UtYO', 'ROLE_USER,ROLE_ADMIN');
