DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS reservation_time;
DROP TABLE IF EXISTS theme;
DROP TABLE IF EXISTS shop;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id UUID NOT NULL,
    name     VARCHAR(255)             NOT NULL,
    login_id VARCHAR(255)             NOT NULL,
    password VARCHAR(255)             NOT NULL,
    role     ENUM ('MEMBER', 'ADMIN') NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE shop
(
    id         UUID         NOT NULL,
    name       VARCHAR(255) NOT NULL,
    manager_id UUID         NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_shop_manager_id FOREIGN KEY (manager_id) REFERENCES users (id)
);

CREATE TABLE reservation_time
(
    id UUID NOT NULL,
    start_at TIME NOT NULL,
    shop_id UUID NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_reservation_time_shop_id FOREIGN KEY (shop_id) REFERENCES shop (id)
);

CREATE TABLE theme
(
    id UUID NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    image_url   VARCHAR(255) NOT NULL,
    shop_id UUID NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_theme_shop_id FOREIGN KEY (shop_id) REFERENCES shop (id)
);

CREATE TABLE reservation
(
    id UUID NOT NULL,
    date          date    NOT NULL,
    canceled      BOOLEAN NOT NULL DEFAULT false,
    time_id UUID NOT NULL,
    theme_id UUID NOT NULL,
    user_id UUID NOT NULL,
    shop_id UUID NOT NULL,

    unique_helper BOOLEAN GENERATED ALWAYS AS (CASE WHEN canceled = false THEN true END),
    PRIMARY KEY (id),
    CONSTRAINT fk_reservation_time_id FOREIGN KEY (time_id) REFERENCES reservation_time (id),
    CONSTRAINT fk_reservation_theme_id FOREIGN KEY (theme_id) REFERENCES theme (id),
    CONSTRAINT fk_reservation_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_reservation_shop_id FOREIGN KEY (shop_id) REFERENCES shop (id)
);

CREATE UNIQUE INDEX uq_not_canceled_reservation
    ON reservation (date, time_id, theme_id, unique_helper)
