/**
 * Author:  Benjamin La Madrid <bg.lamadrid at gmail.com>
 */

INSERT INTO `sales_statuses`
(`sell_status_id`, `sell_status_name`)
VALUES
(-6, 'Returned'),
(-5, 'Delivery Failed'),
(-4, 'Delivery Cancelled'),
(-3, 'Rejected'),
(-2, 'Payment Failed'),
(-1, 'Payment Cancelled'),
(01, 'Pending'),
(02, 'Payment Started'),
(03, 'Paid, Unconfirmed'),
(04, 'Paid, Confirmed'),
(05, 'Delivery On Route'),
(06, 'Delivery Complete');

INSERT INTO `sales_types`
(`sell_type_id`, `sell_type_name`)
VALUES
(01, 'Bill'),
(02, 'Enterprise Invoice');

INSERT INTO `products_categories`
(`product_category_id`, `product_category_name`, `parent_product_category_id`)
VALUES
(01, 'Vestuario y Calzado', null),
(02, 'Zapatillas', 01);

INSERT INTO `products`
(`product_id`, `product_name`, `product_code`, `product_price`, `product_stock_current`, `product_stock_critical`, `product_category_id`)
VALUES
(01, 'Zapatillas Nike Air Jordan Azul/Negro', 'NIKE-AZLNGR-1', 14990, 50, 05, 01),
(02, 'Zapatillas Nike Hi-Top Rojo/Negro', 'NIKE-ROJNGR-1', 14990, 50, 05, 01),
(03, 'Zapatillas Nike Hi-Top Rojo/Blanco', 'NIKE-ROJBCO-1', 13990, 50, 05, 01);

INSERT INTO `images`
(`image_id`, `image_filename`, `image_url`)
VALUES
(01, '1.png', 'assets/img/products/photo-1578116922645-3976907a7671.jpg'),
(02, '2.png', 'assets/img/products/photo-1578172433613-9f1b258f7d5b.jpg'),
(03, '3.png', 'assets/img/products/photo-1580143881495-b21dde95fc60.jpg');

INSERT INTO `products_images`
(`product_id`, `image_id`)
VALUES
(01, 01),
(02, 02),
(03, 03);

INSERT INTO `app_users_roles`
(`user_role_id`, `user_role_name`)
VALUES 
(01, 'Administrador'),
(02, 'Manager'),
(03, 'Vendedor'),
(04, 'Cliente');

INSERT INTO `people`
(`person_id`, `person_name`, `person_idcard`, `person_email`, `person_address`, `person_phone1`, `person_phone2`)
VALUES
(01, 'Test',  '1111111', 'test@example.com',  'example',   0, 0),
(02, 'Test2', '2222222', 'test2@example.com', 'example 2', 0, 0),
(03, 'Test3', '3333333', 'test3@example.com', 'example 3', 0, 0),
(04, 'Test4', '4444444', 'test4@example.com', 'example 4', 0, 0),
(05, 'Test5', '5555555', 'test5@example.com', 'example 5', 0, 0);

INSERT INTO `customers`
(`customer_id`, `person_id`)
VALUES
(01, 04);

INSERT INTO `app_permissions`
(`permission_id`, `permission_code`)
VALUES
(01, 'product_categories:delete'),
(02, 'product_categories:create'),
(03, 'product_categories:update'),
(04, 'product_categories:read'),
(05, ''), -- these may be of use later
(06, ''),
(07, ''),
(08, ''),
(09, 'sell_statuses:delete'),
(10, 'sell_statuses:create'),
(11, 'sell_statuses:update'),
(12, 'sell_statuses:read'),
(13, 'sell_types:delete'),
(14, 'sell_types:create'),
(15, 'sell_types:update'),
(16, 'sell_types:read'),
(17, 'customers:delete'),
(18, 'customers:create'),
(19, 'customers:update'),
(20, 'customers:read'),
(21, 'products:delete'),
(22, 'products:create'),
(23, 'products:update'),
(24, 'products:read'),
(25, 'sales:delete'),
(26, 'sales:create'),
(27, 'sales:update'),
(28, 'sales:read'),
(29, 'salespeople:delete'),
(30, 'salespeople:create'),
(31, 'salespeople:update'),
(32, 'salespeople:read'),
(33, 'users:delete'),
(34, 'users:create'),
(35, 'users:update'),
(36, 'users:read'),
(37, 'user_roles:delete'),
(38, 'user_roles:create'),
(39, 'user_roles:update'),
(40, 'user_roles:read'),
(41, 'people:delete'),
(42, 'people:create'),
(43, 'people:update'),
(44, 'people:read');

INSERT INTO `app_users_roles_permissions`
(`user_role_permission_id`, `permission_id`, `user_role_id`)
VALUES
(01, 01, 01),
(02, 02, 01),
(03, 03, 01),
(04, 04, 01),
(05, 05, 01),
(06, 06, 01),
(07, 07, 01),
(08, 08, 01),
(09, 09, 01),
(10, 10, 01),
(11, 11, 01),
(12, 12, 01),
(13, 13, 01),
(14, 14, 01),
(15, 15, 01),
(16, 16, 01),
(17, 17, 01),
(18, 18, 01),
(19, 19, 01),
(20, 20, 01),
(21, 21, 01),
(22, 22, 01),
(23, 23, 01),
(24, 24, 01),
(25, 25, 01),
(26, 26, 01),
(27, 27, 01),
(28, 28, 01),
(29, 29, 01),
(30, 30, 01),
(31, 31, 01),
(32, 32, 01),
(33, 33, 01),
(34, 34, 01),
(35, 35, 01),
(36, 36, 01),
(37, 37, 01),
(38, 38, 01),
(39, 39, 01),
(40, 40, 01),
(41, 41, 01),
(42, 42, 01),
(43, 43, 01),
(44, 44, 01),
(45, 02, 02),
(46, 03, 02),
(47, 04, 02),
(48, 06, 02),
(49, 07, 02),
(50, 08, 02),
(51, 10, 02),
(52, 11, 02),
(53, 12, 02),
(54, 14, 02),
(55, 15, 02),
(56, 16, 02),
(57, 19, 02),
(58, 20, 02),
(59, 21, 02),
(60, 22, 02),
(61, 23, 02),
(62, 24, 02),
(63, 26, 02),
(64, 27, 02),
(65, 28, 02),
(66, 29, 02),
(67, 30, 02),
(68, 31, 02),
(69, 32, 02),
(70, 36, 02),
(71, 40, 02),
(72, 42, 02),
(73, 43, 02),
(74, 44, 02),
(75, 04, 03),
(76, 08, 03),
(77, 12, 03),
(78, 16, 03),
(79, 20, 03),
(80, 24, 03),
(81, 28, 03),
(82, 32, 03),
(83, 44, 03);

INSERT INTO `app_users`
(`user_id`, `user_name`, `user_password`, `user_role_id`, `person_id`)
VALUES
(01, 'admin',    '$2a$10$U4vUIIlZDBvpwT.KkQuQHOfJrbGpGBol3WT9ryASiwPfYAX1bEq0K', 01, 01),
(02, 'manager',  '$2a$10$qgNTjPGucz6/ul9/GfVwvO.cWo9.lDokYi6GQ3H4bwvAVG49uV/1K', 02, 02),
(03, 'vendedor', '$2a$10$moc0.bFZsTztrgLrNvGC4uApdkBFihA6PlQsuy.dZ0k4A6eVhLwdu', 03, 03),
(04, 'cliente',  '$2a$10$Mi39GIZtguqvm2aLl0JpHOm0WcPoN5aMYlbD.L6IymAEBOQU/6m6u', 04, 04);
-- passwords equal usernames
