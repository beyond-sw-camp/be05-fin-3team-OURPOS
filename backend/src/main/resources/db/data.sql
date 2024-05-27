INSERT INTO customer (customer_login_id, customer_name, customer_nickname, customer_gender, customer_age_range,
                      customer_phone, customer_role)
VALUES ('test', '테스트', '닉네임', 'M', '20', '010-1234-1234', 'ROLE_USER');
--
INSERT INTO store_address(store_address_detail, store_address_base, store_address_zipcode)
VALUES ('서울시 강남구 역삼동 123-4', '서울시 강남구 역삼동', '123-4');
--
--
INSERT INTO store (store_close_yn, store_minimum_order_price, created_date_time, modified_date_time, store_address_id,
                   store_close_time, store_closed_datetime, store_open_time, store_name, store_phone, store_picture_url)
VALUES (FALSE, 10000, NULL, NULL, 1, '22:00', NULL, '10:00', '맛집', '010-1234-1234', 'https://www.google.com');
--
--
INSERT INTO category(category_name)
VALUES ('BURGERS');
--
INSERT INTO category(category_name)
VALUES ('FRIES');

INSERT INTO category(category_name)
VALUES ('MILKSHAKES');
--
INSERT INTO category(category_name)
VALUES ('DRINKS');
--
INSERT INTO menu (menu_deleted_datetime, menu_deleted_yn, menu_price, category_id, menu_description, menu_name,
                  menu_picture_url)
VALUES (NULL, FALSE, 9900, 1, '햄버거', '햄버거', 'https://www.google.com');
--
INSERT INTO menu (menu_deleted_datetime, menu_deleted_yn, menu_price, category_id, menu_description, menu_name,
                  menu_picture_url)
VALUES (NULL, FALSE, 10900, 1, '치즈버거', '치즈버거', 'https://www.google.com');

INSERT INTO menu (menu_deleted_datetime, menu_deleted_yn, menu_price, category_id, menu_description, menu_name,
                  menu_picture_url)
VALUES (NULL, FALSE, 11900, 1, '더블버거', '더블버거', 'https://www.google.com');

INSERT INTO menu (menu_deleted_datetime, menu_deleted_yn, menu_price, category_id, menu_description, menu_name,
                  menu_picture_url)
VALUES (NULL, FALSE, 12900, 1, '치킨버거', '치킨버거', 'https://www.google.com');

INSERT INTO menu (menu_deleted_datetime, menu_deleted_yn, menu_price, category_id, menu_description, menu_name,
                  menu_picture_url)
VALUES (NULL, FALSE, 13900, 1, '치킨치즈버거', '치킨치즈버거', 'https://www.google.com');

INSERT INTO menu (menu_deleted_datetime, menu_deleted_yn, menu_price, category_id, menu_description, menu_name,
                  menu_picture_url)
VALUES (NULL, FALSE, 14900, 1, '치킨더블버거', '치킨더블버거', 'https://www.google.com');
--
--
INSERT INTO menu_option_group (menu_option_group_name, category_id)
VALUES ('토핑', 1);
--
INSERT INTO menu_option_group (menu_option_group_name, category_id)
VALUES ('사이즈', 1);
--
INSERT INTO menu_option_group (menu_option_group_name, category_id)
VALUES ('밀크쉐이크 토핑', 2);
--
INSERT INTO menu_option_group (menu_option_group_name, category_id)
VALUES ('FRIES 사이즈', 3);
--
INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
VALUES ('올 더 웨이', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('마요네즈', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('케첩', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('머스타드', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('양상추', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('피클', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('토마토', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('그릴드 어니언', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('그릴드 머쉬룸', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('랠리시', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('어니언', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('할라피뇨', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('피망', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('스테이크 소스', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('바비큐 소스', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('핫소스', 0, 1);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('보통', 3500, 2);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('리틀', 0, 2);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('초콜릿', 0, 3);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('피너 버터', 0, 3);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('솔티드 카라멜', 0, 3);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('바나나', 0, 3);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('딸기', 0, 3);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('오레오', 0, 3);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('로투스 비스코프', 0, 3);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('베이컨', 0, 3);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('리틀', 0, 4);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('레귤러', 2000, 4);
--
-- INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id)
-- VALUES ('라지', 4000, 4);