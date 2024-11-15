CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email varchar(320),
    name varchar(100)
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR,
    description TEXT,
    available BOOL,
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_id BIGINT REFERENCES items (id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    text TEXT
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start TIMESTAMP WITHOUT TIME ZONE,
    booking_end TIMESTAMP WITHOUT TIME ZONE,
    item_id BIGINT REFERENCES items (id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    status VARCHAR
)