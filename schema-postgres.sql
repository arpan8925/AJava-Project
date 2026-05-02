-- ============================================================
-- Smart Complaint Management System - Schema (PostgreSQL / Supabase)
-- Run this in Supabase SQL Editor.
-- ============================================================

DROP TABLE IF EXISTS analytics_log      CASCADE;
DROP TABLE IF EXISTS notifications      CASCADE;
DROP TABLE IF EXISTS complaint_replies  CASCADE;
DROP TABLE IF EXISTS complaints         CASCADE;
DROP TABLE IF EXISTS departments        CASCADE;
DROP TABLE IF EXISTS users              CASCADE;

-- ============================================================
-- USERS
-- ============================================================
CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(64)  NOT NULL,
    role        VARCHAR(20)  NOT NULL DEFAULT 'CITIZEN',
    phone       VARCHAR(15),
    area        VARCHAR(100),
    is_active   BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- DEPARTMENTS
-- ============================================================
CREATE TABLE departments (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500)
);

-- ============================================================
-- COMPLAINTS
-- ============================================================
CREATE TABLE complaints (
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    dept_id      BIGINT,
    title        VARCHAR(200) NOT NULL,
    description  TEXT NOT NULL,
    summary      VARCHAR(500),
    tags         VARCHAR(300),
    category     VARCHAR(100),
    sentiment    VARCHAR(20),
    priority     VARCHAR(20) DEFAULT 'MEDIUM',
    status       VARCHAR(20) DEFAULT 'PENDING',
    image_path   VARCHAR(300),
    location     VARCHAR(150),
    eta_hours    INT,
    is_emergency BOOLEAN DEFAULT FALSE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at  TIMESTAMP NULL,
    CONSTRAINT fk_complaints_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_complaints_dept FOREIGN KEY (dept_id) REFERENCES departments(id)
);

CREATE INDEX idx_complaints_user     ON complaints(user_id);
CREATE INDEX idx_complaints_dept     ON complaints(dept_id);
CREATE INDEX idx_complaints_status   ON complaints(status);
CREATE INDEX idx_complaints_priority ON complaints(priority);
CREATE INDEX idx_complaints_location ON complaints(location);
CREATE INDEX idx_complaints_created  ON complaints(created_at);

-- Postgres has no MySQL-style "ON UPDATE CURRENT_TIMESTAMP" — use a trigger.
CREATE OR REPLACE FUNCTION set_updated_at() RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_complaints_updated_at
BEFORE UPDATE ON complaints
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- ============================================================
-- COMPLAINT REPLIES
-- ============================================================
CREATE TABLE complaint_replies (
    id            BIGSERIAL PRIMARY KEY,
    complaint_id  BIGINT NOT NULL,
    user_id       BIGINT NOT NULL,
    message       TEXT NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_replies_complaint FOREIGN KEY (complaint_id) REFERENCES complaints(id),
    CONSTRAINT fk_replies_user      FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_replies_complaint ON complaint_replies(complaint_id);

-- ============================================================
-- NOTIFICATIONS
-- ============================================================
CREATE TABLE notifications (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    message    VARCHAR(500) NOT NULL,
    is_read    BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_notif_user ON notifications(user_id, is_read);

-- ============================================================
-- ANALYTICS LOG
-- ============================================================
CREATE TABLE analytics_log (
    id            BIGSERIAL PRIMARY KEY,
    event_type    VARCHAR(50) NOT NULL,
    complaint_id  BIGINT,
    dept_id       BIGINT,
    location      VARCHAR(150),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_analytics_date ON analytics_log(created_at);
CREATE INDEX idx_analytics_type ON analytics_log(event_type);
