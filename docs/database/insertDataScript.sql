-- Вставка данных в таблицу channel
INSERT INTO public.channel (id, name) VALUES
(1, 'channel1');

-- Вставка данных в таблицу person
INSERT INTO public.person (id, name, surname, login, password, role, identitycode, channelid) VALUES
(1, 'Иван', 'Иванов', 'ivanov', 'password123', 'admin', 'ID001', 1);

-- Вставка данных в таблицу message
INSERT INTO public.message (id, description, date, channelid, personid) VALUES
(1, 'Первое сообщение', '2023-01-01 10:00:00', 1, 1);