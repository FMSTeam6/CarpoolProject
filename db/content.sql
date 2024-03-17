use carpool;

-- data additional_options

INSERT INTO additional_options(options_id, additional_options)
VALUES (1, 'smoking');
INSERT INTO additional_options(options_id, additional_options)
VALUES (2, 'air conditioner');
INSERT INTO additional_options(options_id, additional_options)
VALUES (3, 'luggage');
INSERT INTO additional_options(options_id, additional_options)
VALUES (4, 'small pets allowed');
INSERT INTO additional_options(options_id, additional_options)
VALUES (5, 'power outlet');


INSERT INTO images(image_id, file_name,file_path,file_size)
values (1,'name','name',10);

-- data users

INSERT INTO users(user_id, email, first_name, last_name, password, username, phone_number, rating, is_admin, is_banned,image_id, verification_code, verified)
VALUES (1,'ivan@abv.bg','Ivan','Ivanov','ivan123','siteAdmin',0888777666,5,true,false,1, 556, 1);
INSERT INTO users(user_id, email, first_name, last_name, password, username, phone_number, rating, is_admin, is_banned, image_id, verification_code, verified)
VALUES (2,'p.petrunov@gmail.com','Petar','Petrunov','pesho123','peshoOne',0885233242,2.4,false,false,1, 557, 1);
INSERT INTO users(user_id, email, first_name, last_name, password, username, phone_number, rating, is_admin, is_banned, image_id, verification_code, verified)
VALUES (3,'nadya@gmail.com','Nadya','Naidenova','nadya123','princess',0898446238,4,false,false,1, 558, 1);
INSERT INTO users(user_id, email, first_name, last_name, password, username, phone_number, rating, is_admin, is_banned, image_id, verification_code, verified)
VALUES (4,'geri@gmail.com','Gergana','Nikolova','geri123','mermaid',0876264593,0,false,true,1, 559, 1);
INSERT INTO users(user_id, email, first_name, last_name, password, username, phone_number, rating, is_admin, is_banned, image_id, verification_code, verified)
VALUES (5,'mery@mail.bg','Maria','Atanasova','mery123','travelFan',0898994523,0,false,false,1, 560, 1);

-- data travels

INSERT INTO travels(travel_id, starting_location, end_location, empty_seats, date_of_departure, price_per_person, is_completed, is_canceled, driver_id, kilometers, time_travel, time_Arrive)
VALUES (1,'Sofia','Burgas',3,'2024-01-23 09:00:00',35,true,false,1, 100, 1, 2);
INSERT INTO travels(travel_id, starting_location, end_location, empty_seats, date_of_departure, price_per_person, is_completed, is_canceled, driver_id, kilometers, time_travel, time_Arrive)
VALUES (2,'Burgas','Varna',3,'2024-01-28 12:00:00',22.50,true,false,2, 50, 1, 2);
INSERT INTO travels(travel_id, starting_location, end_location, empty_seats, date_of_departure, price_per_person, is_completed, is_canceled, driver_id, kilometers, time_travel, time_Arrive)
VALUES (3,'Sofia','Burgas',3,'2024-01-30 16:30:00',35,true,false,1, 87, 2 , 3);
INSERT INTO travels(travel_id, starting_location, end_location, empty_seats, date_of_departure, price_per_person, is_completed, is_canceled, driver_id, kilometers, time_travel, time_Arrive)
VALUES (4,'Silistra','Shumen',1,'2024-02-05 12:30:00',14.50,true,false,3, 94, 1 , 1);
INSERT INTO travels(travel_id, starting_location, end_location, empty_seats, date_of_departure, price_per_person, is_completed, is_canceled, driver_id, kilometers, time_travel, time_Arrive)
VALUES (5,'Burgas','Sofia',3,'2024-02-08 09:30:00',35,false,true,1, 100, 2, 2);

-- data feedbacks

INSERT INTO feedbacks(feedback_id, text, rating, author_id, travel_id)
VALUES (1,'The journey was very pleasant, and we arrived quickly.',5,5,1);
INSERT INTO feedbacks(feedback_id, text, rating, author_id, travel_id)
VALUES (2,'An exceptional driver with a cool car.',5,4,1);
INSERT INTO feedbacks(feedback_id, text, rating, author_id, travel_id)
VALUES (3,'The car broke down on the way, and we arrived late.',2.4,3,2);
INSERT INTO feedbacks(feedback_id, text, rating, author_id,travel_id)
VALUES (4,'For the second time, I’m traveling with him, and he’s always polite and punctual.',5,5,3);
INSERT INTO feedbacks(feedback_id, text, rating, author_id, travel_id)
VALUES (5,'Very punctual, and there was space for my huge suitcase.',5,2,5);

