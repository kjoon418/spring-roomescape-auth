-- 로컬 실행용 초기 데이터
INSERT INTO shop (id, name)
VALUES ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeee01', '브라운의 방탈출'),
       ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeee02', '제임스의 방탈출');

INSERT INTO users (id, login_id, password, name, role, managing_shop_id)
VALUES ('dddddddd-dddd-dddd-dddd-dddddddddd01', 'brown', 'password123', '브라운', 'MANAGER', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeee01'),
       ('dddddddd-dddd-dddd-dddd-dddddddddd02', 'gump', 'password456', '검프', 'ADMIN', NULL),
       ('dddddddd-dddd-dddd-dddd-dddddddddd03', 'jason', 'password789', '제이슨', 'MEMBER', NULL);

INSERT INTO reservation_time (id, start_at, shop_id)
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01', '10:00:00', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeee01'),
       ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa02', '14:00:00', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeee01');

INSERT INTO theme (id, name, description, image_url, shop_id)
VALUES ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb01', '웨스턴', '서부 시대 미국 배경 추리 테마입니다.', 'https://i.namu.wiki/i/A1AtvH502V57OxN_IuPqwui9jFHsjBZ18IFkFoBvHfBHSfGDYN9yFmARz6AlyM9AYJDhK1aiqnY5BcVIdWHFcA.webp', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeee01'),
       ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb02', '비밀의 화원', '잊혀진 정원 속 비밀을 풀어나가는 미스터리 테마입니다.', 'https://i.namu.wiki/i/OoI83MDV7W2tuNPf3NCSQADLcng4cRTqQ15nP6JEatDQniUxC800zbwzYBqq2TOE3KhFKXy140SpWfl6uL2d5A.webp', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeee01');

INSERT INTO reservation (id, date, time_id, theme_id, user_id, shop_id)
VALUES ('cccccccc-cccc-cccc-cccc-cccccccccc01', '2026-05-14', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb01', 'dddddddd-dddd-dddd-dddd-dddddddddd01', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeee01'),
       ('cccccccc-cccc-cccc-cccc-cccccccccc02', '2026-05-15', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa02', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb01', 'dddddddd-dddd-dddd-dddd-dddddddddd02', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeee01'),
       ('cccccccc-cccc-cccc-cccc-cccccccccc03', '2026-05-16', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb02', 'dddddddd-dddd-dddd-dddd-dddddddddd03', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeee01');
