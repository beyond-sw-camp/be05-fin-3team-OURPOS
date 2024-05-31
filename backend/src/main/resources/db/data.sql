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
INSERT INTO category(category_name,category_deleted_yn)
VALUES ('BURGERS',FALSE);
--
INSERT INTO category(category_name,category_deleted_yn)
VALUES ('FRIES',FALSE);

INSERT INTO category(category_name,category_deleted_yn)
VALUES ('MILKSHAKES',FALSE);
--
INSERT INTO category(category_name,category_deleted_yn)
VALUES ('DRINKS',FALSE);
--
--
--
INSERT INTO menu_option_group (menu_option_group_name, category_id,menu_option_group_deleted_yn)
VALUES ('토핑', 1,FALSE);
--
INSERT INTO menu_option_group (menu_option_group_name, category_id,menu_option_group_deleted_yn)
VALUES ('사이즈', 1,FALSE);
--
INSERT INTO menu_option_group (menu_option_group_name, category_id,menu_option_group_deleted_yn)
VALUES ('밀크쉐이크 토핑', 3,FALSE);
--
INSERT INTO menu_option_group (menu_option_group_name, category_id,menu_option_group_deleted_yn)
VALUES ('FRIES 사이즈', 2,FALSE);
--


INSERT INTO menu (menu_deleted_datetime, menu_deleted_yn, menu_price, category_id,
                  menu_description, menu_name, menu_picture_url)
VALUES (NULL, FALSE, 9900, 1, '햄버거', '햄버거', 'https://www.google.com');

INSERT INTO menu (menu_deleted_datetime, menu_deleted_yn, menu_price, category_id,
                  menu_description, menu_name, menu_picture_url)
VALUES (NULL, FALSE, 11400, 1, '치즈버거', '치즈버거', 'https://www.google.com');

INSERT INTO menu (menu_deleted_datetime, menu_deleted_yn, menu_price, category_id,
                  menu_description, menu_name, menu_picture_url)
VALUES (NULL, FALSE, 12400, 1, '베이컨 버거', '베이컨 버거', 'https://www.google.com');

INSERT INTO menu (menu_deleted_datetime, menu_deleted_yn, menu_price, category_id,
                  menu_description, menu_name, menu_picture_url)
VALUES (NULL, FALSE, 13900, 1, '베이컨 치즈버거', '베이컨 치즈버거', 'https://www.google.com');

INSERT INTO menu (menu_deleted_datetime, menu_deleted_yn, menu_price, category_id,
                  menu_description, menu_name, menu_picture_url)
VALUES (NULL, FALSE, 6900, 2, '감자튀김', 'FRIES', 'https://www.google.com');

INSERT INTO menu (menu_deleted_datetime, menu_deleted_yn, menu_price, category_id,
                  menu_description, menu_name, menu_picture_url)
VALUES (NULL, FALSE, 6900, 3, '밀크쉐이크', '파이브가이즈 쉐이크', 'https://www.google.com');



INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('올 더 웨이', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('마요네즈', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('케첩', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('머스타드', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('양상추', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('피클', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('토마토', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('그릴드 어니언', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('그릴드 머쉬룸', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('랠리시', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('어니언', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('할라피뇨', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('피망', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('스테이크 소스', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('바비큐 소스', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('핫소스', 0, 1,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('보통', 3500, 2,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('리틀', 0, 2,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('초콜릿', 0, 3,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('피너 버터', 0, 3,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('솔티드 카라멜', 0, 3,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('바나나', 0, 3,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('딸기', 0, 3,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('오레오', 0, 3,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('로투스 비스코프', 0, 3,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('베이컨', 0, 3,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('리틀', 0, 4,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('레귤러', 2000, 4,FALSE);

INSERT INTO menu_option (menu_option_name, menu_option_price, menu_option_group_id,menu_option_deleted_yn)
VALUES ('라지', 4000, 4,FALSE);