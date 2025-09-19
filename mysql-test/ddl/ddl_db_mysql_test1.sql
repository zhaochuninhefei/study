CREATE DATABASE `db_mysql_test1` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;

USE `db_mysql_test1`;

DROP TABLE IF EXISTS `db_mysql_test1`.`tb_order`;
CREATE TABLE IF NOT EXISTS `db_mysql_test1`.`tb_order` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ord_number` varchar(20) NOT NULL COMMENT '订单编号',
  `custom_number` varchar(20) NOT NULL COMMENT '客户编号',
  `product_number` varchar(20) NOT NULL COMMENT '商品编号',
  `warehouse_number` varchar(20) NOT NULL COMMENT '仓库编号',
  `ord_status` tinyint NOT NULL COMMENT '订单状态',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tb_order_unique01` (`ord_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '订单表';

DROP TABLE IF EXISTS `db_mysql_test1`.`tb_custom`;
CREATE TABLE IF NOT EXISTS `db_mysql_test1`.`tb_custom` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `custom_number` varchar(20) NOT NULL COMMENT '客户编号',
  `custom_name` varchar(50) NOT NULL COMMENT '客户姓名',
  `custom_phone` varchar(20) NOT NULL COMMENT '客户手机号',
  `custom_address` varchar(200) NOT NULL COMMENT '客户地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tb_custom_unique01` (`custom_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户表';

DROP TABLE IF EXISTS `db_mysql_test1`.`tb_product`;
CREATE TABLE IF NOT EXISTS `db_mysql_test1`.`tb_product` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `product_number` varchar(20) NOT NULL COMMENT '商品编号',
  `product_name` varchar(50) NOT NULL COMMENT '商品名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '商品表';

DROP TABLE IF EXISTS `db_mysql_test1`.`tb_warehouse`;
CREATE TABLE IF NOT EXISTS `db_mysql_test1`.`tb_warehouse` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `warehouse_number` varchar(20) NOT NULL COMMENT '仓库编号',
  `warehouse_name` varchar(50) NOT NULL COMMENT '仓库名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '仓库表';
