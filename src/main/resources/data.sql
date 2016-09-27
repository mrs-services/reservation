INSERT INTO meeting_room (room_name) VALUES ('新木場');
INSERT INTO meeting_room (room_name) VALUES ('辰巳');
INSERT INTO meeting_room (room_name) VALUES ('豊洲');
INSERT INTO meeting_room (room_name) VALUES ('月島');
INSERT INTO meeting_room (room_name) VALUES ('新富町');
INSERT INTO meeting_room (room_name) VALUES ('銀座一丁目');
INSERT INTO meeting_room (room_name) VALUES ('有楽町');
--

INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE, (SELECT room_id FROM meeting_room WHERE room_name = '新木場'));
INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 1, (SELECT room_id FROM meeting_room WHERE room_name = '新木場'));
INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 2, (SELECT room_id FROM meeting_room WHERE room_name = '新木場'));
-- INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 3, (SELECT room_id FROM meeting_room WHERE room_name = '新木場'));
-- INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 4, (SELECT room_id FROM meeting_room WHERE room_name = '新木場'));

INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE, (SELECT room_id FROM meeting_room WHERE room_name = '辰巳'));
INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 1, (SELECT room_id FROM meeting_room WHERE room_name = '辰巳'));
INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 2, (SELECT room_id FROM meeting_room WHERE room_name = '辰巳'));
-- INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 3, (SELECT room_id FROM meeting_room WHERE room_name = '辰巳'));
-- INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 4, (SELECT room_id FROM meeting_room WHERE room_name = '辰巳'));

INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE, (SELECT room_id FROM meeting_room WHERE room_name = '豊洲'));
INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 1, (SELECT room_id FROM meeting_room WHERE room_name = '豊洲'));
INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 2, (SELECT room_id FROM meeting_room WHERE room_name = '豊洲'));
-- INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 3, (SELECT room_id FROM meeting_room WHERE room_name = '豊洲'));
-- INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 4, (SELECT room_id FROM meeting_room WHERE room_name = '豊洲'));

INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE, (SELECT room_id FROM meeting_room WHERE room_name = '新富町'));
INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 1, (SELECT room_id FROM meeting_room WHERE room_name = '新富町'));
INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 2, (SELECT room_id FROM meeting_room WHERE room_name = '新富町'));
-- INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 3, (SELECT room_id FROM meeting_room WHERE room_name = '新富町'));
-- INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 4, (SELECT room_id FROM meeting_room WHERE room_name = '新富町'));

INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE, (SELECT room_id FROM meeting_room WHERE room_name = '銀座一丁目'));
INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 1, (SELECT room_id FROM meeting_room WHERE room_name = '銀座一丁目'));
INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 2, (SELECT room_id FROM meeting_room WHERE room_name = '銀座一丁目'));
-- INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 3, (SELECT room_id FROM meeting_room WHERE room_name = '銀座一丁目'));
-- INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 4, (SELECT room_id FROM meeting_room WHERE room_name = '銀座一丁目'));

INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE, (SELECT room_id FROM meeting_room WHERE room_name = '有楽町'));
INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 1, (SELECT room_id FROM meeting_room WHERE room_name = '有楽町'));
INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 2, (SELECT room_id FROM meeting_room WHERE room_name = '有楽町'));
-- INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 3, (SELECT room_id FROM meeting_room WHERE room_name = '有楽町'));
-- INSERT INTO reservable_room (reserved_date, room_id) VALUES (CURRENT_DATE + 4, (SELECT room_id FROM meeting_room WHERE room_name = '有楽町'));