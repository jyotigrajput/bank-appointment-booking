USE bank_appointment_db;

-- Branches
INSERT INTO branches (branch_code, branch_name, address, city, state, pincode, phone, email, opening_time, closing_time, status) VALUES
('MB-001', 'Main Branch', '12 MG Road', 'Pune', 'Maharashtra', '411001', '020-25250001', 'main@bankexample.com', '09:00:00', '17:00:00', 'ACTIVE'),
('PC-002', 'Pimpri Branch', '18 Chinchwad Road', 'Pimpri-Chinchwad', 'Maharashtra', '411018', '020-27120002', 'pimpri@bankexample.com', '09:00:00', '17:00:00', 'ACTIVE'),
('HJ-003', 'Hinjewadi Branch', '5 Phase 1 Road', 'Hinjewadi', 'Maharashtra', '411057', '020-66550003', 'hinjewadi@bankexample.com', '10:00:00', '18:00:00', 'ACTIVE'),
('BN-004', 'Baner Branch', '28 Baner Road', 'Baner', 'Maharashtra', '411045', '020-67550004', 'baner@bankexample.com', '09:30:00', '17:30:00', 'ACTIVE'),
('CP-005', 'Pune Camp Branch', '14 Camp Road', 'Pune', 'Maharashtra', '411001', '020-25550005', 'camp@bankexample.com', '09:00:00', '17:00:00', 'ACTIVE');

-- Services
INSERT INTO services (name, description, estimated_duration, status) VALUES
('Account Opening', 'Open a new savings or current account for personal or business banking.', 30, 'ACTIVE'),
('Cash Deposit', 'Deposit cash and receive updated passbook or receipt.', 20, 'ACTIVE'),
('Cash Withdrawal', 'Withdraw cash for account holders with proper verification.', 20, 'ACTIVE'),
('Cheque Services', 'Issue cheque books and discuss cheque-related concerns.', 30, 'ACTIVE'),
('Demand Draft', 'Apply for demand draft services and assistance with documentation.', 30, 'ACTIVE'),
('Loan Enquiry', 'Understand loan options and eligibility requirements.', 30, 'ACTIVE'),
('Home Loan Consultation', 'Discuss home loan eligibility, rates, and documentation.', 30, 'ACTIVE'),
('Personal Loan Consultation', 'Discuss personal loan requirements and terms.', 30, 'ACTIVE'),
('Credit Card Services', 'Resolve credit card issues and application support.', 30, 'ACTIVE'),
('Debit Card Services', 'Activate or replace a debit card and check card-related issues.', 20, 'ACTIVE'),
('Internet Banking Support', 'Support for login, activation, and online banking guidance.', 30, 'ACTIVE'),
('Mobile Banking Support', 'Get help with mobile app registration and service usage.', 25, 'ACTIVE'),
('KYC Update', 'Update customer verification details and documentation.', 30, 'ACTIVE'),
('Address Update', 'Modify address details and related records.', 20, 'ACTIVE'),
('PAN Update', 'Submit PAN-related requests and documentation support.', 25, 'ACTIVE');

-- Customers
INSERT INTO customers (full_name, email, phone, customer_reference) VALUES
('Aarav Sharma', 'aarav@example.com', '9876500001', 'CUST-20261001-0001'),
('Meera Patel', 'meera@example.com', '9876500002', 'CUST-20261001-0002'),
('Rohan Iyer', 'rohan@example.com', '9876500003', 'CUST-20261001-0003');

-- Employees
INSERT INTO employees (employee_code, name, designation, email, phone, branch_id, status) VALUES
('EMP-1001', 'John Smith', 'Customer Service Executive', 'john.smith@bankexample.com', '9876500101', 1, 'ACTIVE'),
('EMP-1002', 'Priya Nair', 'Customer Service Executive', 'priya.nair@bankexample.com', '9876500102', 2, 'ACTIVE'),
('EMP-1003', 'Arjun Mehta', 'Loan Officer', 'arjun.mehta@bankexample.com', '9876500103', 1, 'ACTIVE'),
('EMP-1004', 'Sneha Kulkarni', 'Relationship Manager', 'sneha.kulkarni@bankexample.com', '9876500104', 3, 'ACTIVE'),
('EMP-1005', 'Vikas Rao', 'Loan Officer', 'vikas.rao@bankexample.com', '9876500105', 2, 'ACTIVE'),
('EMP-1006', 'Neha Joshi', 'Cashier', 'neha.joshi@bankexample.com', '9876500106', 4, 'ACTIVE'),
('EMP-1007', 'Rohit Deshmukh', 'Branch Manager', 'rohit.deshmukh@bankexample.com', '9876500107', 5, 'ACTIVE'),
('EMP-1008', 'Ananya Singh', 'Customer Service Executive', 'ananya.singh@bankexample.com', '9876500108', 3, 'ACTIVE');

-- Employee-service mapping
INSERT INTO employee_services (employee_id, service_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4),
(2, 1), (2, 5), (2, 13), (2, 14),
(3, 6), (3, 7), (3, 8),
(4, 7), (4, 9), (4, 10),
(5, 6), (5, 7), (5, 8),
(6, 2), (6, 3), (6, 4),
(7, 11), (7, 12), (7, 13),
(8, 1), (8, 9), (8, 10), (8, 11);

-- Example appointments
INSERT INTO appointments (appointment_reference, customer_id, branch_id, service_id, employee_id, appointment_date, start_time, end_time, status, notes) VALUES
('APT-20261006-0001', 1, 1, 7, 3, '2026-10-06', '10:00:00', '10:30:00', 'CONFIRMED', 'Home loan discussion'),
('APT-20261006-0002', 2, 2, 1, 2, '2026-10-06', '11:00:00', '11:30:00', 'BOOKED', 'Account opening assistance');

