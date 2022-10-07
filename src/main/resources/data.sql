INSERT INTO `app_params`
(`param_id`, `param_category`, `param_name`, `param_value`)
VALUES
(01, 'company', 'name', 'Trébol Demo'),
(02, 'company', 'description', 'This is a demostration of integrated eCommerce system Trébol'),
(03, 'company', 'bannerImageURL', 'https://fakeimg.pl/400x150'),
(04, 'company', 'logoImageURL', 'https://fakeimg.pl/250');

INSERT INTO `sell_statuses`
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

INSERT INTO `app_user_roles`
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
(45, 'checkout'),
(46, 'shippers:delete'),
(47, 'shippers:create'),
(48, 'shippers:update'),
(49, 'shippers:read'),
(50, 'product_lists:delete'),
(51, 'product_lists:create'),
(52, 'product_lists:update'),
(53, 'product_lists:read'),
(54, 'product_lists:contents');

INSERT INTO `app_user_role_permissions`
(`permission_id`, `user_role_id`)
VALUES
(01, 01),
(02, 01),
(03, 01),
(04, 01),
(05, 01),
(06, 01),
(07, 01),
(08, 01),
(09, 01),
(10, 01),
(11, 01),
(12, 01),
(13, 01),
(14, 01),
(15, 01),
(16, 01),
(17, 01),
(18, 01),
(19, 01),
(20, 01),
(21, 01),
(22, 01),
(23, 01),
(24, 01),
(25, 01),
(26, 01),
(27, 01),
(28, 01),
(29, 01),
(30, 01),
(31, 01),
(32, 01),
(33, 01),
(34, 01),
(35, 01),
(36, 01),
(37, 01),
(38, 01),
(39, 01),
(40, 01),
(41, 01),
(42, 01),
(43, 01),
(44, 01),
(45, 01),
(46, 01),
(47, 01),
(48, 01),
(49, 01),
(50, 01),
(51, 01),
(52, 01),
(53, 01),
(54, 01);

INSERT INTO `app_user_role_permissions`
(`permission_id`, `user_role_id`)
VALUES
(01, 02),
(02, 02),
(03, 02),
(04, 02),
(12, 02),
(16, 02),
(17, 02),
(18, 02),
(19, 02),
(20, 02),
(23, 02),
(24, 02),
(26, 02),
(27, 02),
(28, 02),
(36, 02),
(40, 02),
(41, 02),
(42, 02),
(43, 02),
(44, 02),
(45, 02),
(46, 02),
(47, 02),
(48, 02),
(49, 02),
(50, 02),
(51, 02),
(52, 02),
(53, 02),
(54, 02);

INSERT INTO `app_user_role_permissions`
(`permission_id`, `user_role_id`)
VALUES
(03, 03),
(04, 03),
(08, 03),
(12, 03),
(16, 03),
(17, 03),
(18, 03),
(19, 03),
(20, 03),
(22, 03),
(24, 03),
(28, 03),
(40, 03),
(45, 03);

INSERT INTO `app_user_role_permissions`
(`permission_id`, `user_role_id`)
VALUES
(45, 04);

INSERT INTO `app_users`
(`user_id`, `user_name`, `user_password`, `user_role_id`)
VALUES
(01, 'admin',    '$2a$10$OiJF.agD0/Rdx/i3MGAmceGkxdrdmacBUXNyGFgCGKWaoBxY4FaOe', 01),
(02, 'manager',  '$2a$10$sDmKrVsFaQ7O.hKwxiZQV.vnwZFZkLBY4XyKVOO9UjaupDvhRoUye', 02),
(03, 'salesperson', '$2a$10$j3fyIRTlgzANU8b2uDAsL.k2djo9Ywj3IJHeKse7TyeMT1dfcs8q2', 03),
(04, 'customer',  '$2a$10$jQDzZpBh8JE0Yi4tOrx2XuJP3vPgepZFNH0UNlrIQJJFtrzd/XO8q', 04);
-- passwords equal usernames, bcrypt logarithm cost factor of 10
