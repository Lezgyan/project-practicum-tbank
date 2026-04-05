--liquibase formatted sql

--changeset andrey:001-create-weather-measurement
CREATE TABLE weather_measurement (
    id BIGSERIAL PRIMARY KEY,
    measured_at TIMESTAMP WITH TIME ZONE NOT NULL,
    temperature DOUBLE PRECISION NOT NULL,
    feels_like DOUBLE PRECISION,
    humidity INTEGER,
    pressure INTEGER,
    weather_main TEXT,
    weather_description TEXT,
    wind_speed DOUBLE PRECISION,
    cloudiness INTEGER,
    is_raining BOOLEAN NOT NULL,
    is_snowing BOOLEAN NOT NULL
);

--changeset andrey:002-create-room
CREATE TABLE room (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    lat DOUBLE PRECISION NOT NULL,
    timezone TEXT,
    weather_id BIGINT UNIQUE,
    CONSTRAINT fk_room_weather
    FOREIGN KEY (weather_id) REFERENCES weather_measurement (id)
);

--changeset andrey:003-create-device
CREATE TABLE device (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    type TEXT NOT NULL,
    external_id UUID NOT NULL,
    status TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    room_id BIGINT,
    CONSTRAINT uk_device_external_id UNIQUE (external_id),
    CONSTRAINT fk_device_room
    FOREIGN KEY (room_id) REFERENCES room (id)
);

--changeset andrey:004-create-device-state
CREATE TABLE device_state (
    id BIGSERIAL PRIMARY KEY,
    device_id BIGINT,
    device_state_payload jsonb NOT NULL,
    has_error BOOLEAN NOT NULL,
    error_message TEXT,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_device_state_device
    FOREIGN KEY (device_id) REFERENCES device (id)
);

--changeset andrey:005-create-device-settings
CREATE TABLE device_settings (
    id BIGSERIAL PRIMARY KEY,
    device_id BIGINT,
    blinds_open_time TIME,
    blinds_close_time TIME,
    cold_weather_temperature DOUBLE PRECISION,
    hot_weather_temperature DOUBLE PRECISION,
    radiator_temp_when_cold DOUBLE PRECISION,
    radiator_temp_when_hot DOUBLE PRECISION,
    min_cloudiness_when_normal DOUBLE PRECISION,
    min_temperature_outside_when_need_close_blinds DOUBLE PRECISION,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_device_settings_device
    FOREIGN KEY (device_id) REFERENCES device (id)
);

--changeset andrey:006-create-device-command-log
CREATE TABLE device_command_log (
    id BIGSERIAL PRIMARY KEY,
    command_type TEXT NOT NULL,
    command_value jsonb NOT NULL,
    status TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    device_id BIGINT NOT NULL,
    CONSTRAINT fk_device_command_log_device
    FOREIGN KEY (device_id) REFERENCES device (id)
);

--changeset andrey:007-create-device-event-log
CREATE TABLE device_event_log (
    id BIGSERIAL PRIMARY KEY,
    event_type TEXT NOT NULL,
    event_value jsonb NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    device_id BIGINT NOT NULL,
    CONSTRAINT fk_device_event_log_device
    FOREIGN KEY (device_id) REFERENCES device (id)
);

--changeset andrey:008-create-indexes
CREATE INDEX idx_device_room_id ON device (room_id);
CREATE INDEX idx_device_command_log_device_id ON device_command_log (device_id);
CREATE INDEX idx_device_event_log_device_id ON device_event_log (device_id);
CREATE INDEX idx_weather_measurement_measured_at ON weather_measurement (measured_at);
CREATE INDEX idx_device_created_at ON device (created_at);
CREATE INDEX idx_device_command_log_created_at ON device_command_log (created_at);
CREATE INDEX idx_device_event_log_created_at ON device_event_log (created_at);