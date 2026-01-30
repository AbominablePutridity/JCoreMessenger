-- Вставка данных в таблицу person
INSERT INTO public.person (id, name, surname, login, password, role, identitycode) VALUES
(1, 'Иван', 'Иванов', 'ivanov', 'password123', 'admin', 'ID123456'),
(2, 'Мария', 'Петрова', 'petrova', 'qwerty', 'user', 'ID789012'),
(3, 'Алексей', 'Сидоров', 'sidorov', 'pass123', 'user', 'ID345678'),
(4, 'Елена', 'Козлова', 'kozlova', 'elena2024', 'moderator', 'ID901234'),
(5, 'Дмитрий', 'Смирнов', 'smirnov', 'dima123', 'user', 'ID567890');

-- Вставка данных в таблицу channel
INSERT INTO public.channel (id, name, personid) VALUES
(1, 'Основной канал', 1),
(2, 'Технический чат', 2),
(3, 'Общий чат', 1),
(4, 'Поддержка', 3),
(5, 'Новости', 4);

-- Вставка данных в таблицу personchannel (связь пользователей с каналами)
INSERT INTO public.personchannel (id, personid, channelid) VALUES
(1, 1, 1),  -- Иван в Основном канале
(2, 1, 2),  -- Иван в Техническом чате
(3, 2, 1),  -- Мария в Основном канале
(4, 2, 3),  -- Мария в Общем чате
(5, 3, 1),  -- Алексей в Основном канале
(6, 3, 4),  -- Алексей в Поддержке
(7, 4, 1),  -- Елена в Основном канале
(8, 4, 2),  -- Елена в Техническом чате
(9, 5, 1),  -- Дмитрий в Основном канале
(10, 5, 5); -- Дмитрий в Новостях

-- Вставка данных в таблицу message
INSERT INTO public.message (id, description, date, personchannelid) VALUES
(1, 'Добро пожаловать в основной канал!', '2024-01-15 10:00:00', 1),
(2, 'Привет всем!', '2024-01-15 10:05:00', 3),
(3, 'Есть вопросы по техподдержке?', '2024-01-15 11:00:00', 6),
(4, 'Обновление системы запланировано на завтра', '2024-01-15 12:00:00', 2),
(5, 'Новые правила чата', '2024-01-15 13:00:00', 1),
(6, 'Как дела?', '2024-01-15 14:00:00', 5),
(7, 'Нужна помощь с настройкой', '2024-01-15 15:00:00', 8),
(8, 'Важные новости на этой неделе', '2024-01-15 16:00:00', 10),
(9, 'Спасибо за помощь!', '2024-01-15 17:00:00', 6),
(10, 'До встречи завтра!', '2024-01-15 18:00:00', 9);

-- Обновление последовательностей для автоинкрементных полей
SELECT pg_catalog.setval('public.channel_id_seq', (SELECT MAX(id) FROM public.channel), true);
SELECT pg_catalog.setval('public.person_id_seq', (SELECT MAX(id) FROM public.person), true);
SELECT pg_catalog.setval('public.personchannel_id_seq', (SELECT MAX(id) FROM public.personchannel), true);
SELECT pg_catalog.setval('public.message_id_seq', (SELECT MAX(id) FROM public.message), true);