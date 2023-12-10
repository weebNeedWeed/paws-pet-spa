INSERT INTO roles (name, description) VALUES ("ADMINISTRATOR", "Admin được phép tạo, sửa các dịch vụ");
INSERT INTO roles (name, description) VALUES ("NORMAL_EMPLOYEE", "Nhân viên được phép bắt đầu các cuộc hẹn");
INSERT INTO roles (name, description) VALUES ("COORDINATOR", "Điều phối viên được phép điều phối các cuộc hẹn");

INSERT INTO employees
    (username, password, email, full_name, date_of_birth, address, phone_number, gender, first_working_day, identity_number)
VALUES ("emp1", "$2a$12$9FWh7rcKxIFvR.oIyy96EO2Vxj9.XPBZNzxj44d/ggZqSC0PAubvm", "emp1@gmail.com", "Nguyen van a", NOW(), "address", "phoneNum", 0, NOW(), "emp1id1");
INSERT INTO employees
    (username, password, email, full_name, date_of_birth, address, phone_number, gender, first_working_day, identity_number)
VALUES ("emp2", "$2a$12$f/koXhGHOu1.K03IuD62ruysTn89tiGxKHkLHW/V1uwIRp4YYZnq6", "emp2@gmail.com", "Le van b", NOW(), "address", "phoneNum", 0, NOW(), "emp2id2");

INSERT INTO employees
(username, password, email, full_name, date_of_birth, address, phone_number, gender, first_working_day, identity_number)
VALUES ("admin", "$2a$12$A591NPJZp20e1FJGjjExq.6cHx.tHWDlfSMzG2A.G22wnCtMrVV/G", "admin@gmail.com", "Nguyen van teo", NOW(), "address", "phoneNum", 0, NOW(), "adminid");

INSERT INTO employees
(username, password, email, full_name, date_of_birth, address, phone_number, gender, first_working_day, identity_number)
VALUES ("coordinator", "$2a$12$5BhAVQRXZt4pMNgtLIGvXemtpUzPLUVdeBlPvI9FqqbuStVX15./.", "coord@gmail.com", "Le van teo", NOW(), "address", "phoneNum", 0, NOW(), "coordid");

INSERT INTO employee_role(role_id, employee_id) VALUES (2,1);
INSERT INTO employee_role(role_id, employee_id) VALUES (2,2);
INSERT INTO employee_role(role_id, employee_id) VALUES (1,3);
INSERT INTO employee_role(role_id, employee_id) VALUES (3,4);

INSERT INTO pet_types(name) VALUES ("Chó");
INSERT INTO pet_types(name) VALUES ("Mèo");

INSERT INTO pet_weight_ranges(min_weight, max_weight, description) VALUES (0.0, 2.4, "Cân nặng từ 0kg tới 2.4kg");
INSERT INTO pet_weight_ranges(min_weight, max_weight, description) VALUES (2.5, 4.9, "Cân nặng từ 2.5kg tới 4.9kg");
INSERT INTO pet_weight_ranges(min_weight, max_weight, description) VALUES (5, 9.9, "Cân nặng từ 5kg tới 9.9kg");
INSERT INTO pet_weight_ranges(min_weight, max_weight, description) VALUES (10, 14.9, "Cân nặng từ 10kg tới 14.9kg");
INSERT INTO pet_weight_ranges(min_weight, max_weight, description) VALUES (15, 30, "Cân nặng từ 15kg tới 30kg");

INSERT INTO spa_services(name, description, default_price, default_estimated_completion_minutes)
    VALUES ("Tắm và sấy", "Dịch vụ tắm và sấy lông cho chó và mèo", 150000, 1);
INSERT INTO spa_services(name, description, default_price, default_estimated_completion_minutes)
    VALUES ("Cạo lông", "Dịch vụ cạo lông cho chó và mèo", 200000, 1);
INSERT INTO spa_services(name, description, default_price, default_estimated_completion_minutes)
    VALUES ("Cắt móng", "Dịch vụ cắt móng cho chó và mèo", 100000, 1);

INSERT INTO spa_service_details(spa_service_id, min_weight, max_weight, price, estimated_completion_minutes)
    VALUES (1, 0.0, 2.4, 150000, 1);
INSERT INTO spa_service_details(spa_service_id, min_weight, max_weight, price, estimated_completion_minutes)
    VALUES (1, 2.5, 4.9, 200000, 1);
INSERT INTO spa_service_details(spa_service_id, min_weight, max_weight, price, estimated_completion_minutes)
    VALUES (1, 5, 9.9, 250000, 20);
INSERT INTO spa_service_details(spa_service_id, min_weight, max_weight, price, estimated_completion_minutes)
    VALUES (1, 10, 14.9, 300000, 25);
INSERT INTO spa_service_details(spa_service_id, min_weight, max_weight, price, estimated_completion_minutes)
    VALUES (1, 15, 30, 350000, 30);

INSERT INTO customers(username, password, email, full_name, address, phone_number, gender)
    VALUES ("admin", "$2a$12$A591NPJZp20e1FJGjjExq.6cHx.tHWDlfSMzG2A.G22wnCtMrVV/G", "admin@gmail.com", "Nguyen van teo", "address", "phone", 1);

INSERT INTO appointments(location, appointment_time, status, customer_id)
    VALUES (0, "2023-12-11 07:30:00", 0, 1);

INSERT INTO appointment_items(pet_name, pet_type_id, appointment_id, employee_id)
    VALUES ("Meobeos", 1, 1, 1);

INSERT INTO appoinment_item_spa_service(appointment_item_id, spa_service_id)
    VALUES (1,1);