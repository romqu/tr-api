CREATE TABLE keypair
(
    id          BIGSERIAL PRIMARY KEY,
    private_key BYTEA NOT NULL UNIQUE,
    public_key  BYTEA NOT NULL UNIQUE
);

CREATE TABLE session
(
    id               BIGSERIAL PRIMARY KEY,
    uuid_id          UUID         NOT NULL UNIQUE,
    device_id        UUID         NOT NULL UNIQUE,
    token            VARCHAR(100) NOT NULL UNIQUE,
    refresh_token    VARCHAR(100) NOT NULL UNIQUE,
    tracking_id      VARCHAR(100) NOT NULL UNIQUE,
    reset_process_id UUID UNIQUE,

    keypair_id       BIGINT       NOT NULL UNIQUE,

    CONSTRAINT fk_session_keypair
        FOREIGN KEY (keypair_id)
            REFERENCES keypair (id)
            ON DELETE CASCADE
);
