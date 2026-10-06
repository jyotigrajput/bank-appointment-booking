CREATE DATABASE IF NOT EXISTS bank_appointment_db;
USE bank_appointment_db;

CREATE TABLE IF NOT EXISTS branches (
    id BIGINT NOT NULL AUTO_INCREMENT,
    branch_code VARCHAR(50) NOT NULL,
    branch_name VARCHAR(150) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(20) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(150) NOT NULL,
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_branch_code (branch_code),
    UNIQUE KEY uk_branch_email (email),
    KEY idx_branch_city (city),
    KEY idx_branch_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS services (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(255) NOT NULL,
    estimated_duration INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_service_name (name),
    KEY idx_service_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS customers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    customer_reference VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_customer_email (email),
    UNIQUE KEY uk_customer_phone (phone),
    UNIQUE KEY uk_customer_reference (customer_reference),
    KEY idx_customer_name (full_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS employees (
    id BIGINT NOT NULL AUTO_INCREMENT,
    employee_code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    designation VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    branch_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_employee_code (employee_code),
    UNIQUE KEY uk_employee_email (email),
    KEY idx_employee_branch_id (branch_id),
    KEY idx_employee_status (status),
    CONSTRAINT fk_employees_branch FOREIGN KEY (branch_id) REFERENCES branches (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS employee_services (
    employee_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    PRIMARY KEY (employee_id, service_id),
    KEY idx_employee_service_service_id (service_id),
    CONSTRAINT fk_employee_services_employee FOREIGN KEY (employee_id) REFERENCES employees (id),
    CONSTRAINT fk_employee_services_service FOREIGN KEY (service_id) REFERENCES services (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS appointments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    appointment_reference VARCHAR(50) NOT NULL,
    customer_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    notes VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_appointment_reference (appointment_reference),
    KEY idx_appointments_customer_id (customer_id),
    KEY idx_appointments_branch_id (branch_id),
    KEY idx_appointments_service_id (service_id),
    KEY idx_appointments_employee_id (employee_id),
    KEY idx_appointments_date (appointment_date),
    KEY idx_appointments_status (status),
    CONSTRAINT fk_appointments_customer FOREIGN KEY (customer_id) REFERENCES customers (id),
    CONSTRAINT fk_appointments_branch FOREIGN KEY (branch_id) REFERENCES branches (id),
    CONSTRAINT fk_appointments_service FOREIGN KEY (service_id) REFERENCES services (id),
    CONSTRAINT fk_appointments_employee FOREIGN KEY (employee_id) REFERENCES employees (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

