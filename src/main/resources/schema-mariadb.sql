-- Adminer 4.8.1 MySQL 5.5.5-10.5.9-MariaDB-1:10.5.9+maria~focal dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP TABLE IF EXISTS `addresses`;
CREATE TABLE `addresses` (
  `address_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address_city` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `address_first_line` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `address_municipality` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `address_notes` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address_postal_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address_second_line` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`address_id`),
  UNIQUE KEY `UK_ADDR` (`address_city`,`address_municipality`,`address_first_line`,`address_second_line`,`address_postal_code`,`address_notes`),
  KEY `IDX_ADDR_FIRST_LINE` (`address_first_line`),
  KEY `IDX_ADDR_SECOND_LINE` (`address_second_line`),
  KEY `IDX_ADDR_POSTAL_CODE` (`address_postal_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `app_params`;
CREATE TABLE `app_params` (
  `param_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_category` varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL,
  `param_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `param_value` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`param_id`),
  UNIQUE KEY `UK_PARAM` (`param_category`,`param_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `app_permissions`;
CREATE TABLE `app_permissions` (
  `permission_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission_code` varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL,
  `permission_description` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`permission_id`),
  UNIQUE KEY `UK_PERM_CODE` (`permission_code`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `app_user_roles`;
CREATE TABLE `app_user_roles` (
  `user_role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_role_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`user_role_id`),
  UNIQUE KEY `UK_UROLE_NAME` (`user_role_name`),
  KEY `IDX_UROLE_NAME` (`user_role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `billing_companies`;
CREATE TABLE `billing_companies` (
  `billing_company_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `billing_company_id_number` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `billing_company_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`billing_company_id`),
  UNIQUE KEY `UK_BILL_CY_ID_NO` (`billing_company_id_number`),
  UNIQUE KEY `UK_BILL_CY_NAME` (`billing_company_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `billing_types`;
CREATE TABLE `billing_types` (
  `billing_type_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `billing_type_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`billing_type_id`),
  UNIQUE KEY `UK_BILL_TYP_NAME` (`billing_type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `images`;
CREATE TABLE `images` (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `image_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `image_filename` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `image_url` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`image_id`),
  UNIQUE KEY `UK_IMG_CODE` (`image_code`),
  UNIQUE KEY `UK_IMG_FILENAME` (`image_filename`),
  UNIQUE KEY `UK_IMG_URL` (`image_url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `payment_types`;
CREATE TABLE `payment_types` (
  `payment_type_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `payment_type_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`payment_type_id`),
  UNIQUE KEY `UK_PAYMT_NAME` (`payment_type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `people`;
CREATE TABLE `people` (
  `person_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `person_email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `person_first_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `person_id_number` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `person_last_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `person_phone1` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `person_phone2` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`person_id`),
  UNIQUE KEY `UK_PERSON_ID_NO` (`person_id_number`),
  KEY `IDX_PERSON_ID_NO` (`person_id_number`),
  KEY `IDX_PERSON_FIRSTNAME` (`person_first_name`),
  KEY `IDX_PERSON_LASTNAME` (`person_last_name`),
  KEY `IDX_PERSON_EMAIL` (`person_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `product_lists`;
CREATE TABLE `product_lists` (
  `product_list_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_list_code` varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL,
  `product_list_disabled` bit(1) NOT NULL,
  `product_list_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`product_list_id`),
  UNIQUE KEY `UK_PLIST_NAME` (`product_list_name`),
  UNIQUE KEY `UK_PLIST_CODE` (`product_list_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `product_categories`;
CREATE TABLE `product_categories` (
  `product_category_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_category_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `product_category_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent_product_category_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`product_category_id`),
  UNIQUE KEY `UK_PROD_CAT_CODE` (`product_category_code`),
  UNIQUE KEY `UK_PROD_CAT_IF_CHILD` (`parent_product_category_id`,`product_category_name`),
  CONSTRAINT `FK_PROD_CAT_PARENT` FOREIGN KEY (`parent_product_category_id`) REFERENCES `product_categories` (`product_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `sell_statuses`;
CREATE TABLE `sell_statuses` (
  `sell_status_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sell_status_code` int(11) NOT NULL,
  `sell_status_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`sell_status_id`),
  UNIQUE KEY `UK_SELL_STATUS_CODE` (`sell_status_code`),
  UNIQUE KEY `UK_SELL_STATUS_NAME` (`sell_status_name`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `shippers`;
CREATE TABLE `shippers` (
  `shipper_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shipper_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`shipper_id`),
  UNIQUE KEY `UK_SHIPPER_NAME` (`shipper_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;






DROP TABLE IF EXISTS `app_users`;
CREATE TABLE `app_users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `person_id` bigint(20) DEFAULT NULL,
  `user_role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UK_USER_NAME` (`user_name`),
  KEY `IDX_USER_NAME` (`user_name`),
  CONSTRAINT `FK_USER_PERSON_ID` FOREIGN KEY (`person_id`) REFERENCES `people` (`person_id`),
  CONSTRAINT `FK_USER_UROLE_ID` FOREIGN KEY (`user_role_id`) REFERENCES `app_user_roles` (`user_role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `app_user_role_permissions`;
CREATE TABLE `app_user_role_permissions` (
  `user_role_permission_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission_id` bigint(20) NOT NULL,
  `user_role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_role_permission_id`),
  CONSTRAINT `FK_UROLEPERMS_PERM_ID` FOREIGN KEY (`permission_id`) REFERENCES `app_permissions` (`permission_id`),
  CONSTRAINT `FK_UROLEPERMS_UROLE_ID` FOREIGN KEY (`user_role_id`) REFERENCES `app_user_roles` (`user_role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `customers`;
CREATE TABLE `customers` (
  `customer_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `person_id` bigint(20) NOT NULL,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `UK_CUSTOM_PERSON_ID` (`person_id`),
  CONSTRAINT `FK_CUSTOM_PERSON_ID` FOREIGN KEY (`person_id`) REFERENCES `people` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `product_description` varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `product_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `product_price` int(11) NOT NULL,
  `product_stock_critical` int(11) NOT NULL,
  `product_stock_current` int(11) NOT NULL,
  `product_category_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `UK_PRODUCT_CODE` (`product_code`),
  UNIQUE KEY `UK_PRODUCT_NAME` (`product_name`),
  KEY `IDX_PRODUCT_NAME` (`product_name`),
  CONSTRAINT `FK_PRODUCT_CAT_ID` FOREIGN KEY (`product_category_id`) REFERENCES `product_categories` (`product_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `salespeople`;
CREATE TABLE `salespeople` (
  `salesperson_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `person_id` bigint(20) NOT NULL,
  PRIMARY KEY (`salesperson_id`),
  CONSTRAINT `FK_SPERSON_PERSON_ID` FOREIGN KEY (`person_id`) REFERENCES `people` (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;







DROP TABLE IF EXISTS `product_images`;
CREATE TABLE `product_images` (
  `product_image_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `image_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  PRIMARY KEY (`product_image_id`),
  CONSTRAINT `FK_PIMGS_IMAGE_ID` FOREIGN KEY (`image_id`) REFERENCES `images` (`image_id`),
  CONSTRAINT `FK_PIMGS_PROD_ID` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `product_list_items`;
CREATE TABLE `product_list_items` (
  `product_list_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_list_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  PRIMARY KEY (`product_list_item_id`),
  CONSTRAINT `FK_PLIST_ITEM_PARENT_ID` FOREIGN KEY (`product_list_id`) REFERENCES `product_lists` (`product_list_id`),
  CONSTRAINT `FK_PLIST_ITEM_PROD_ID` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `sales`;
CREATE TABLE `sales` (
  `sell_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sell_date` datetime(6) NOT NULL,
  `sell_net_value` int(11) NOT NULL,
  `sell_taxes_value` int(11) NOT NULL,
  `sell_total_items` int(11) NOT NULL,
  `sell_total_value` int(11) NOT NULL,
  `sell_transaction_token` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sell_transport_value` int(11) NOT NULL,
  `billing_address_id` bigint(20) DEFAULT NULL,
  `billing_company_id` bigint(20) DEFAULT NULL,
  `billing_type_id` bigint(20) NOT NULL,
  `customer_id` bigint(20) NOT NULL,
  `payment_type_id` bigint(20) NOT NULL,
  `salesperson_id` bigint(20) DEFAULT NULL,
  `shipper_id` bigint(20) DEFAULT NULL,
  `shipping_address_id` bigint(20) DEFAULT NULL,
  `sell_status_id` bigint(20) NOT NULL,
  PRIMARY KEY (`sell_id`),
  KEY `IDX_SELL_DATE` (`sell_date`),
  KEY `IDX_SELL_TOKEN` (`sell_transaction_token`),
  CONSTRAINT `FK_SELL_CUSTOM_ID` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
  CONSTRAINT `FK_SELL_BILL_TYP_ID` FOREIGN KEY (`billing_type_id`) REFERENCES `billing_types` (`billing_type_id`),
  CONSTRAINT `FK_SELL_PAYMT_TYP_ID` FOREIGN KEY (`payment_type_id`) REFERENCES `payment_types` (`payment_type_id`),
  CONSTRAINT `FK_SELL_STATUS_ID` FOREIGN KEY (`sell_status_id`) REFERENCES `sell_statuses` (`sell_status_id`),
  CONSTRAINT `FK_SELL_BILL_CY_ID` FOREIGN KEY (`billing_company_id`) REFERENCES `billing_companies` (`billing_company_id`),
  CONSTRAINT `FK_SELL_ADDR_BILL` FOREIGN KEY (`billing_address_id`) REFERENCES `addresses` (`address_id`),
  CONSTRAINT `FK_SELL_SHIPPER_ID` FOREIGN KEY (`shipper_id`) REFERENCES `shippers` (`shipper_id`),
  CONSTRAINT `FK_SELL_ADDR_SHIP` FOREIGN KEY (`shipping_address_id`) REFERENCES `addresses` (`address_id`),
  CONSTRAINT `FK_SELL_SPERSON` FOREIGN KEY (`salesperson_id`) REFERENCES `salespeople` (`salesperson_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `sell_details`;
CREATE TABLE `sell_details` (
  `sell_detail_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sell_detail_unit_value` int(11) NOT NULL,
  `sell_detail_units` int(11) NOT NULL,
  `sell_detail_description` varchar(260) COLLATE utf8mb4_unicode_ci NOT NULL,
  `product_id` bigint(20) NULL,
  `sell_id` bigint(20) NOT NULL,
  PRIMARY KEY (`sell_detail_id`),
  CONSTRAINT `FK_SELL_DETAIL_PROD_ID` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE SET NULL,
  CONSTRAINT `FK_SELL_DETAIL_PARENT_ID` FOREIGN KEY (`sell_id`) REFERENCES `sales` (`sell_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 2022-01-06 06:37:28