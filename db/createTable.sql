use carpool;

create table images
(
    image_id  INT AUTO_INCREMENT PRIMARY KEY,
    file_name VARCHAR(255) NULL,
    file_path VARCHAR(255) NULL,
    file_size INT          NULL
);

create table users
(
    user_id      int auto_increment primary key,
    email        varchar(100) not null UNIQUE,
    first_name   varchar(50)  not null,
    last_name    varchar(50)  not null,
    password     varchar(50)  not null,
    username     varchar(50)  not null UNIQUE,
    phone_number varchar(10)  not null,
    rating       double       null,
    is_admin     BOOLEAN      null,
    is_banned    BOOLEAN      null,
    image_id     VARCHAR(255)          null,
    verification_code varchar(250) not null,
    verified BOOLEAN null

);


create table additional_options
(
    options_id         int auto_increment primary key,
    additional_options TEXT null

);

create table travels
(
    travel_id         int auto_increment primary key,
    starting_location varchar(50) not null,
    end_location      varchar(50) not null,
    empty_seats       int         not null,
    date_of_departure datetime(6) null,
    price_per_person  double      not null,
    is_completed      BOOLEAN     not null,
    is_canceled       BOOLEAN     not null,
    driver_id         int         not null,
    kilometers        varchar(50) not null,
    time_travel       varchar(50) not null,
    time_Arrive       varchar(50) not null,
    foreign key (driver_id) references users (user_id)
);

create table feedbacks
(
    feedback_id  int auto_increment primary key,
    text         TEXT null,
    rating       int  not null,
    author_id    int  not null,
    travel_id    int  not null,
    foreign key (author_id) references users (user_id),
    foreign key (travel_id) references travels (travel_id)
);

create table travels_additional_options
(

    travel_id int,
    option_id int,
    primary key (travel_id, option_id),
    foreign key (travel_id) references travels (travel_id),
    foreign key (option_id) references additional_options (options_id)
);

create table users_travels
(
    user_id int,
    travel_id int,
    primary key (user_id, travel_id),
    foreign key (user_id) references users (user_id),
    foreign key (travel_id) references travels (travel_id)
);

create table passengers
(
    travel_id int,
    user_id int,
    primary key (user_id, travel_id),
    foreign key (user_id) references users (user_id),
    foreign key (travel_id) references travels (travel_id)
);

create table candidates
(
    travel_id int,
    user_id int,
    primary key (user_id, travel_id),
    foreign key (user_id) references users (user_id),
    foreign key (travel_id) references travels (travel_id)
);

create table feedbacks_users
(
    feedback_id int,
    recipient_id int,
    primary key (recipient_id, feedback_id),
    foreign key (recipient_id) references users (user_id),
    foreign key (feedback_id) references feedbacks (feedback_id)
);

