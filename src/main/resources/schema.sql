DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS reservation_time;
DROP TABLE IF EXISTS theme;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id UUID NOT NULL,
  name VARCHAR(255) NOT NULL,
  login_id VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('MEMBER', 'ADMIN') NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE reservation_time (
  id UUID NOT NULL,
  start_at TIME NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE theme (
  id UUID NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  image_url VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE reservation (
  id UUID NOT NULL,
  date date NOT NULL,
  canceled BOOLEAN NOT NULL DEFAULT false,
  time_id UUID NOT NULL,
  theme_id UUID NOT NULL,
  user_id UUID NOT NULL,

  unique_helper BOOLEAN GENERATED ALWAYS AS (CASE WHEN canceled = false THEN true END),
  PRIMARY KEY (id),
  CONSTRAINT fk_reservation_time_id FOREIGN KEY (time_id) REFERENCES reservation_time (id),
  CONSTRAINT fk_reservation_theme_id FOREIGN KEY (theme_id) REFERENCES theme (id),
  CONSTRAINT fk_reservation_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE UNIQUE INDEX uq_not_canceled_reservation
ON reservation (date, time_id, theme_id, unique_helper)
