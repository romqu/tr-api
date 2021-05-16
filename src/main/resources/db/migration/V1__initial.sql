CREATE TABLE keypair
(
    id          BIGSERIAL PRIMARY KEY,
    private_key BYTEA NOT NULL,
    public_key  BYTEA NOT NULL
);

CREATE TABLE session
(
    id                BIGSERIAL PRIMARY KEY,
    uuid_id           UUID   NOT NULL UNIQUE,
    device_id         UUID   NOT NULL,
    phone_number_hash TEXT   NOT NULL UNIQUE,
    token             TEXT   NOT NULL,
    refresh_token     TEXT   NOT NULL,
    tracking_id       TEXT   NOT NULL,
    reset_process_id  UUID UNIQUE,

    keypair_id        BIGINT NOT NULL,

    CONSTRAINT fk_session_keypair
        FOREIGN KEY (keypair_id)
            REFERENCES keypair (id)
            ON DELETE CASCADE
);
