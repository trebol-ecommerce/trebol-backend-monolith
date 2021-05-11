/**
 * Author:  Benjamin La Madrid <bg.lamadrid at gmail.com>
 */

INSERT INTO `product_families`
(`product_family_id`, `product_family_name`)
VALUES
(01, 'Vestuario y Calzado');

INSERT INTO `product_types`
(`product_type_id`, `product_type_name`, `product_family_id`)
VALUES
(01, 'Zapatillas', 01);

INSERT INTO `products`
(`product_id`, `product_name`, `product_code`, `product_price`, `product_stock_current`, `product_stock_critical`, `product_type_id`)
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

INSERT INTO `product_images`
(`product_id`, `image_id`)
VALUES
(01, 01),
(02, 02),
(03, 03);

INSERT INTO `app_user_roles`
(`user_role_id`, `user_role_name`)
VALUES 
(01, 'Administrador'),
(02, 'Manager'),
(03, 'Vendedor'),
(04, 'Cliente');

INSERT INTO `people`
(`person_id`, `person_name`, `person_idcard`, `person_email`, `person_address`, `person_phone1`, `person_phone2`)
VALUES
(01, 'Test', '1111111', 'test@example.com', 'Example.com', 0, 0);

INSERT INTO `app_permissions`
(`permission_id`, `permission_code`)
VALUES
(01, 'product_families:delete'),
(02, 'product_families:create'),
(03, 'product_families:update'),
(04, 'product_families:read'),
(05, 'product_types:delete'),
(06, 'product_types:create'),
(07, 'product_types:update'),
(08, 'product_types:read'),
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

INSERT INTO `app_user_role_permissions`
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
(44, 44, 01);

INSERT INTO `app_users`
(`user_id`, `user_name`, `user_password`, `user_role_id`, `person_id`)
VALUES
(01, 'admin', '$2a$10$U4vUIIlZDBvpwT.KkQuQHOfJrbGpGBol3WT9ryASiwPfYAX1bEq0K', 01, 01);
-- password is 'admin', was generated using bcrypt cli w/ strength 10
