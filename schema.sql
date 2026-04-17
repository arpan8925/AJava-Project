-- ============================================================
-- Smart Complaint Management System - Schema
-- ============================================================

CREATE DATABASE IF NOT EXISTS smart_complaint_system
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE smart_complaint_system;

DROP TABLE IF EXISTS analytics_log;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS complaint_replies;
DROP TABLE IF EXISTS complaints;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS users;

-- ============================================================
-- USERS
-- ============================================================
CREATE TABLE users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(64)  NOT NULL,
    role        ENUM('CITIZEN', 'ADMIN', 'OFFICER') NOT NULL DEFAULT 'CITIZEN',
    phone       VARCHAR(15),
    area        VARCHAR(100),
    is_active   BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================================
-- DEPARTMENTS
-- ============================================================
CREATE TABLE departments (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500)
) ENGINE=InnoDB;

-- ============================================================
-- COMPLAINTS
-- ============================================================
CREATE TABLE complaints (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    dept_id      BIGINT,
    title        VARCHAR(200) NOT NULL,
    description  TEXT NOT NULL,
    summary      VARCHAR(500),
    tags         VARCHAR(300),
    category     VARCHAR(100),
    sentiment    VARCHAR(20),
    priority     ENUM('LOW','MEDIUM','HIGH','CRITICAL') DEFAULT 'MEDIUM',
    status       ENUM('PENDING','ASSIGNED','IN_PROGRESS','RESOLVED','CLOSED') DEFAULT 'PENDING',
    image_path   VARCHAR(300),
    location     VARCHAR(150),
    eta_hours    INT,
    is_emergency BOOLEAN DEFAULT FALSE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    resolved_at  TIMESTAMP NULL,
    CONSTRAINT fk_complaints_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_complaints_dept FOREIGN KEY (dept_id) REFERENCES departments(id)
) ENGINE=InnoDB;

CREATE INDEX idx_complaints_user     ON complaints(user_id);
CREATE INDEX idx_complaints_dept     ON complaints(dept_id);
CREATE INDEX idx_complaints_status   ON complaints(status);
CREATE INDEX idx_complaints_priority ON complaints(priority);
CREATE INDEX idx_complaints_location ON complaints(location);
CREATE INDEX idx_complaints_created  ON complaints(created_at);

-- ============================================================
-- COMPLAINT REPLIES
-- ============================================================
CREATE TABLE complaint_replies (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    complaint_id  BIGINT NOT NULL,
    user_id       BIGINT NOT NULL,
    message       TEXT NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_replies_complaint FOREIGN KEY (complaint_id) REFERENCES complaints(id),
    CONSTRAINT fk_replies_user      FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE INDEX idx_replies_complaint ON complaint_replies(complaint_id);

-- ============================================================
-- NOTIFICATIONS
-- ============================================================
CREATE TABLE notifications (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    message    VARCHAR(500) NOT NULL,
    is_read    BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE INDEX idx_notif_user ON notifications(user_id, is_read);

-- ============================================================
-- ANALYTICS LOG
-- ============================================================
CREATE TABLE analytics_log (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_type    VARCHAR(50) NOT NULL,
    complaint_id  BIGINT,
    dept_id       BIGINT,
    location      VARCHAR(150),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE INDEX idx_analytics_date ON analytics_log(created_at);
CREATE INDEX idx_analytics_type ON analytics_log(event_type);
