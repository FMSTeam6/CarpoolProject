use carpool;

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

create table additional_options
(
    options_id      int auto_increment primary key,
    luggage         BOOLEAN null,
    smoking         BOOLEAN null,
    eating          BOOLEAN null,
    pets            BOOLEAN null,
    air_condition   BOOLEAN null,
    power_outlet    BOOLEAN null,
    reclining_seats BOOLEAN null
);


create table travels
(
    travel_id         int auto_increment primary key,
    starting_location varchar(50) not null,
    end_location      varchar(50) not null,
    empty_seats       int         not null,
    date_of_departure datetime(6) null,
    price_per_person double not null,
    is_completed      BOOLEAN     not null,
    is_canceled       BOOLEAN     not null,
    driver_id         int         not null,
    options_id        int         not null,
    foreign key (driver_id) references users (user_id),
    foreign key (options_id) references additional_options (options_id)
);

create table feedbacks
(
    feedback_id int auto_increment primary key,
    text        TEXT null,
    rating      int not null,
    author_id   int not null,
    recipient_id     int not null,
    foreign key (author_id) references users (user_id),
    foreign key (recipient_id) references users (user_id)

);
