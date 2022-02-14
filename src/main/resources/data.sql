insert into guest(id, name, email, phone_number, is_active) values(null, 'Roger Federer', 'rf@atp.com', '123', true);
insert into guest(id, name, email, phone_number, is_active) values(null, 'Rafael Nadal', 'rn@atp.com', '456', true);

insert into tennis_court(id, name) values(null, 'Roland Garros - Court Philippe-Chatrier');
insert into tennis_court(id, name) values(null, 'Wimbledon Centre Court');
insert into tennis_court(id, name) values(null, 'Indian Wells Tennis Garden');

insert
    into schedule(id, start_date_time, end_date_time, tennis_court_id)
    values (null, '2020-12-20T16:00:00.0', '2020-12-20T17:00:00.0', 1);
insert
    into schedule(id, start_date_time, end_date_time, tennis_court_id)
    values (null, '2021-12-20T20:00:00.0', '2021-12-20T21:00:00.0', 1);
insert
    into schedule(id, start_date_time, end_date_time, tennis_court_id)
    values (null, '2021-12-21T20:00:00.0', '2021-12-21T21:00:00.0', 1);
insert
    into schedule(id, start_date_time, end_date_time, tennis_court_id)
    values (null, '2022-12-25T20:00:00.0', '2022-12-25T21:00:00.0', 1);
insert
    into schedule(id, start_date_time, end_date_time, tennis_court_id)
    values (null, '2022-12-26T20:00:00.0', '2022-12-26T21:00:00.0', 1);

insert into reservation(id, guest_id, schedule_id, refund_value, reservation_status, "VALUE") values(null, 1, 1, 0, 0, 10);
insert into reservation(id, guest_id, schedule_id, refund_value, reservation_status, "VALUE") values(null, 1, 2, 0, 0, 10);
insert into reservation(id, guest_id, schedule_id, refund_value, reservation_status, "VALUE") values(null, 1, 3, 0, 0, 10);
insert into reservation(id, guest_id, schedule_id, refund_value, reservation_status, "VALUE") values(null, 1, 4, 0, 0, 10);
insert into reservation(id, guest_id, schedule_id, refund_value, reservation_status, "VALUE") values(null, 1, 5, 0, 0, 10);
insert into reservation(id, guest_id, schedule_id, refund_value, reservation_status, "VALUE") values(null, 2, 1, 0, 0, 10);
insert into reservation(id, guest_id, schedule_id, refund_value, reservation_status, "VALUE") values(null, 2, 1, 0, 0, 10);
insert into reservation(id, guest_id, schedule_id, refund_value, reservation_status, "VALUE") values(null, 2, 2, 0, 0, 10);