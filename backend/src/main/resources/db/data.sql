INSERT INTO customer (customer_login_id, customer_name, customer_nickname, customer_password, customer_phone,
                      customer_role)
VALUES ('loginId', '박상철', '상철', '1234', '010-1234-1234', 'ROLE_ADMIN');

INSERT INTO store_address(store_address_detail, store_address_gu, store_address_si, store_address_street_name)
VALUES ('상세주소', '구', '시', '도');

INSERT INTO store_address(store_address_detail, store_address_gu, store_address_si, store_address_street_name)
VALUES ('상세주소', '구', '시', '도');

INSERT INTO store (store_close_yn, store_minimum_order_price, created_date_time, modified_date_time, store_address_id,
                   store_close_time, store_closed_datetime, store_open_time, store_name, store_phone, store_picture_url)
VALUES (FALSE, 10000, NULL, NULL, 1, '22:00', NULL, '10:00', '맛집', '010-1234-1234', 'https://www.google.com');

INSERT INTO store (store_close_yn, store_minimum_order_price, created_date_time, modified_date_time, store_address_id,
                   store_close_time, store_closed_datetime, store_open_time, store_name, store_phone, store_picture_url)
VALUES (FALSE, 10000, NULL, NULL, 2, '22:00', NULL, '10:00', '맛집', '010-1234-1234', 'https://www.google.com');


INSERT INTO category(category_name)
VALUES ('HAMBURGER');

INSERT INTO menu (menu_available_yn, menu_deleted_datetime, menu_deleted_yn, menu_price, category_id, store_id,
                  menu_description, menu_name, menu_picture_url)
VALUES (TRUE, NULL, FALSE, 10000, 1, 1, '치즈버거', '치즈버거', 'https://www.google.com');

INSERT INTO menu (menu_available_yn, menu_deleted_datetime, menu_deleted_yn, menu_price, category_id, store_id,
                  menu_description, menu_name, menu_picture_url)
VALUES (TRUE, NULL, FALSE, 10000, 1, 1, '치즈버거', '치즈버거', 'https://www.google.com');

INSERT INTO menu_option_group (menu_option_group_name, menu_id)
VALUES ('토핑', 1);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
VALUES ('치즈', 1000, 1);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
VALUES ('피클', 500, 1);

