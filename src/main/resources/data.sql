/**
 * Author:  Benjamin La Madrid <bg.lamadrid at gmail.com>
 */

INSERT INTO `app_params`
(`param_id`, `param_category`, `param_name`, `param_value`)
VALUES
(01, 'company', 'name', 'Trébol Demo'),
(02, 'company', 'description', 'This is a demostration of integrated eCommerce system Trébol'),
(03, 'company', 'bannerImageURL', 'https://fakeimg.pl/400x150'),
(04, 'company', 'logoImageURL', 'https://fakeimg.pl/250');

INSERT INTO `sales_statuses`
(`sell_status_id`, `sell_status_code`, `sell_status_name`)
VALUES
(01, -6, 'Returned'),
(02, -5, 'Delivery Failed'),
(03, -4, 'Delivery Cancelled'),
(04, -3, 'Rejected'),
(05, -2, 'Payment Failed'),
(06, -1, 'Payment Cancelled'),
(07, 01, 'Pending'),
(08, 02, 'Payment Started'),
(09, 03, 'Paid, Unconfirmed'),
(10, 04, 'Paid, Confirmed'),
(11, 05, 'Delivery On Route'),
(12, 06, 'Delivery Complete');

INSERT INTO `billing_types`
(`billing_type_id`, `billing_type_name`)
VALUES
(01, 'Bill'),
(02, 'Enterprise Invoice');

INSERT INTO `payment_types`
(`payment_type_id`, `payment_type_name`)
VALUES
(01, 'WebPay Plus');

INSERT INTO `app_users_roles`
(`user_role_id`, `user_role_name`)
VALUES 
(01, 'Administrator'),
(02, 'Manager'),
(03, 'Salesperson'),
(04, 'Customer');

INSERT INTO `app_permissions`
(`permission_id`, `permission_code`)
VALUES
(01, 'product_categories:delete'),
(02, 'product_categories:create'),
(03, 'product_categories:update'),
(04, 'product_categories:read'),
(05, 'sell_statuses:delete'),
(06, 'sell_statuses:create'),
(07, 'sell_statuses:update'),
(08, 'sell_statuses:read'),
(09, 'sell_types:delete'),
(10, 'sell_types:create'),
(11, 'sell_types:update'),
(12, 'sell_types:read'),
(13, 'customers:delete'),
(14, 'customers:create'),
(15, 'customers:update'),
(16, 'customers:read'),
(17, 'products:delete'),
(18, 'products:create'),
(19, 'products:update'),
(20, 'products:read'),
(21, 'sales:delete'),
(22, 'sales:create'),
(23, 'sales:update'),
(24, 'sales:read'),
(25, 'salespeople:delete'),
(26, 'salespeople:create'),
(27, 'salespeople:update'),
(28, 'salespeople:read'),
(29, 'users:delete'),
(30, 'users:create'),
(31, 'users:update'),
(32, 'users:read'),
(33, 'user_roles:delete'),
(34, 'user_roles:create'),
(35, 'user_roles:update'),
(36, 'user_roles:read'),
(37, 'people:delete'),
(38, 'people:create'),
(39, 'people:update'),
(40, 'people:read'),
(41, 'images:delete'),
(42, 'images:create'),
(43, 'images:update'),
(44, 'images:read'),
(45, 'checkout');

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
(45, 45, 01),
(46, 01, 02),
(47, 02, 02),
(48, 03, 02),
(55, 04, 02),
(57, 12, 02),
(58, 16, 02),
(59, 17, 02),
(60, 18, 02),
(61, 19, 02),
(63, 20, 02),
(64, 23, 02),
(66, 24, 02),
(67, 26, 02),
(68, 27, 02),
(69, 28, 02),
(70, 36, 02),
(71, 40, 02),
(72, 41, 02),
(73, 42, 02),
(74, 43, 02),
(75, 44, 02),
(76, 45, 02),
(77, 03, 03),
(78, 04, 03),
(79, 08, 03),
(80, 12, 03),
(81, 16, 03),
(82, 17, 03),
(83, 18, 03),
(84, 19, 03),
(85, 20, 03),
(86, 22, 03),
(87, 24, 03),
(88, 28, 03),
(89, 40, 03),
(90, 45, 03),
(91, 45, 04);

INSERT INTO `app_users`
(`user_id`, `user_name`, `user_password`, `user_role_id`)
VALUES
(01, 'admin',    '$2a$10$OiJF.agD0/Rdx/i3MGAmceGkxdrdmacBUXNyGFgCGKWaoBxY4FaOe', 01),
(02, 'manager',  '$2a$10$sDmKrVsFaQ7O.hKwxiZQV.vnwZFZkLBY4XyKVOO9UjaupDvhRoUye', 02),
(03, 'salesperson', '$2a$10$j3fyIRTlgzANU8b2uDAsL.k2djo9Ywj3IJHeKse7TyeMT1dfcs8q2', 03),
(04, 'customer',  '$2a$10$jQDzZpBh8JE0Yi4tOrx2XuJP3vPgepZFNH0UNlrIQJJFtrzd/XO8q', 04);
-- passwords equal usernames, bcrypt logarithm cost factor of 10
