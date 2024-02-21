create table users
(
    user_id      int auto_increment primary key,
    email        varchar(100) not null UNIQUE,
    first_name   varchar(50)  not null,
    last_name    varchar(50)  not null,
    password     varchar(50)  not null,
    username     varchar(50)  not null UNIQUE,
    phone_number varchar(10)  not null,
    pictures     BLOB,
    is_admin     BOOLEAN      not null,
    is_banned    BOOLEAN      not null
);

create table travel
(
    travel_id         int auto_increment primary key,
    starting_location varchar(50) not null,
    end_location      varchar(50) not null,
    empty_seats       int         not null,
    date_of_departure datetime(6) null,
    price_per_person  double      not null,
    driver_id         int         not null

);

create table feedback
(

    feedback_id int auto_increment primary key,
    text        TEXT null,
    rating      int  not null,
    author_id   int  not null,
    travel_id   int  not null

);
