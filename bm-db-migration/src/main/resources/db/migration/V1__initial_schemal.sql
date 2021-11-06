CREATE TABLE IF NOT EXISTS `permissions` (
    `code` varchar(50) NOT NULL,
    `name` varchar(225),
    PRIMARY KEY (code)
);
INSERT INTO permissions(code, name)
VALUES ('EMPLOYEE_MANAGEMENT', 'Quản lý nhân viên')
,('CUSTOMER_MANAGEMENT','Quản lý khách hàng')
,('PRODUCT_MANAGEMENT','Quản lý sản phẩm')
,('SUPPLIER_MANAGEMENT','Quản lý nhà cung cấp')
,('TRANSACTION_MANAGEMENT','Quản lý hoạt động giao dịch');

CREATE TABLE IF NOT EXISTS `roles` (
    `id` varchar(50) NOT NULL,
    `name` varchar(225),
    `created_date` DATETIME,
    `updated_date` DATETIME,
    PRIMARY KEY (id)
);
INSERT INTO roles(id, name, created_date)
VALUES('326d6676-43a6-42c2-87d0-64e4a992bc3f', 'Admin', '2020-04-24');

CREATE TABLE IF NOT EXISTS `grant_permissions` (
    `id` varchar(50) NOT NULL,
    `role_id` varchar(50),
    `permission_code` varchar(50),
    PRIMARY KEY (id)
);
INSERT INTO grant_permissions(id,role_id, permission_code) VALUES
('0b07f2c2-66b5-4a15-91dc-a153cc17e7b0','326d6676-43a6-42c2-87d0-64e4a992bc3f', 'TRANSACTION_MANAGEMENT')
,('454ec9fc-4f9c-44d6-9b8e-bfdfb594faaa','326d6676-43a6-42c2-87d0-64e4a992bc3f', 'SUPPLIER_MANAGEMENT')
,('a221e674-a8f7-4ed6-9197-fa187dc62a11','326d6676-43a6-42c2-87d0-64e4a992bc3f', 'EMPLOYEE_MANAGEMENT')
,('d63c71b1-34dc-497e-95a3-58a69a21d331','326d6676-43a6-42c2-87d0-64e4a992bc3f', 'CUSTOMER_MANAGEMENT')
,('067f71b1-34dc-497e-95a3-58a69a21d456','326d6676-43a6-42c2-87d0-64e4a992bc3f', 'PRODUCT_MANAGEMENT');

CREATE TABLE IF NOT EXISTS `employees` (
    `id` varchar(50) NOT NULL,
    `name` varchar(225),
    `birth_date` DATETIME,
    `phone` varchar(50),
    `email` varchar(225),
    `password` varchar(225),
    `role_id` varchar(50),
    `created_date` DATETIME,
    `updated_date` DATETIME,
    PRIMARY KEY (id)
);

-- pass: 123456
INSERT INTO employees(id, name, birth_date, email, password, role_id)
VALUES('f7887a85-1e83-55f1-86d0-b3574b7fe3eb', 'Admin', '2020-04-24', 'admin@punhouse.com', '$2a$09$DlFMW.ofkjNQXaNhPp7ug..kWu5i3jSacX7w1HcKdm9cd6xq4C8FC', '326d6676-43a6-42c2-87d0-64e4a992bc3f');

CREATE TABLE IF NOT EXISTS `supplier` (
    `id` varchar(50) NOT NULL,
    `name` varchar(225),
    `address` TEXT,
    PRIMARY KEY (id)
);
INSERT INTO `supplier`
VALUES('093b36de-02ba-4a3a-83f4-be6ef380lpae', 'Moji', '422 Sư Vạn Hạnh');

CREATE TABLE IF NOT EXISTS `category` (
    `id` varchar(50) NOT NULL,
    `name` varchar(225),
    PRIMARY KEY (id)
);
INSERT INTO `category`
VALUES('ba2b36de-02ba-4a3a-83f4-fc6ef380c43e', 'Thú bông');

CREATE TABLE IF NOT EXISTS `sub_category` (
    `id` varchar(50) NOT NULL,
    `name` varchar(225),
    `category_id` varchar(50),
    PRIMARY KEY (id)
    );
INSERT INTO `sub_category`
VALUES('ba2b36de-02ba-4a3a-83f4-fc6ef380c43e', 'Thú bông cỡ nhỏ', 'ba2b36de-02ba-4a3a-83f4-fc6ef380c43e');

CREATE TABLE IF NOT EXISTS `color` (
    `id` varchar(50) NOT NULL,
    `name` varchar(225),
    PRIMARY KEY (id)
);
INSERT INTO `color`
VALUES('62bcbe89-c89a-4741-9789-6a6dd97f1afe', 'Hồng');

CREATE TABLE IF NOT EXISTS `size` (
    `id` varchar(50) NOT NULL,
    `name` varchar(225),
    PRIMARY KEY (id)
);
INSERT INTO `size`
VALUES('c00a580d-2958-4ea9-b966-41584dd4672f', '30cm');

CREATE TABLE IF NOT EXISTS `product` (
    `id` varchar(50) NOT NULL,
    `name` varchar(225),
    `quantity` int,
    `description` TEXT,
    `image_url` varchar(225),
    `selling_price` DOUBLE,
    `sub_category_id` varchar(50),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS `product_detail` (
    `id` varchar(50) NOT NULL,
    `name` varchar(225),
    `quantity` int,
    `selling_price` DOUBLE,
    `importing_price` DOUBLE,
    `image_url` varchar(225),
    `sub_category_id` varchar(50),
    `color_id` varchar(50),
    `size_id` varchar(50),
    `product_id` varchar(50),
    PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS `customer` (
    `id` varchar(50) NOT NULL,
    `name` varchar(225),
    `birth_date` DATE,
    `phone` varchar(50),
    `email` varchar(225),
    `password` varchar(225),
    `is_valid` BOOLEAN,
    `created_date` DATETIME,
    `updated_date` DATETIME,
    PRIMARY KEY (id)
);
-- pass: 123456
INSERT INTO customer(id, name, birth_date, phone, email, password, is_valid, created_date)
VALUES('f7887a85-1e83-55f1-86d0-b3574b7fe3eb', 'Admin', '2020-04-24', '0763233977', 'admin@punhouse.com', '$2a$09$DlFMW.ofkjNQXaNhPp7ug..kWu5i3jSacX7w1HcKdm9cd6xq4C8FC', true, '2020-04-24');


CREATE TABLE IF NOT EXISTS `importing_order` (
    `id` varchar(50) NOT NULL,
    `supplier_id` varchar(50),
    `employee_id` varchar(50),
    `status` varchar(50),
    `total` DOUBLE,
    `invoice_date` DATETIME,
    `delivery_date` DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS `importing_transaction` (
    `id` varchar(50) NOT NULL,
    `product_detail_id` varchar(50),
    `quantity` int,
    `price` DOUBLE,
    `importing_order_id` varchar(50),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS `selling_order` (
    `id` varchar(50) NOT NULL,
    `customer_id` varchar(50),
    `address` TEXT,
    `status` varchar(50),
    `total` DOUBLE,
    `invoice_date` DATETIME,
    `delivery_date` DATETIME,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS `selling_transaction` (
    `id` varchar(50) NOT NULL,
    `product_detail_id` varchar(50),
    `quantity` int,
    `price` DOUBLE,
    `selling_order_id` varchar(50),
    PRIMARY KEY (id)
);
