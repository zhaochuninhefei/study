CREATE DATABASE IF NOT EXISTS `db_jdk11_test` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;

USE `db_jdk11_test`;

DROP TABLE IF EXISTS `db_jdk11_test`.`tb_org`;
CREATE TABLE IF NOT EXISTS `db_jdk11_test`.`tb_org` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `org_name` VARCHAR(200) NOT NULL  COMMENT '组织名',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `tb_org_unique_001` (`org_name`)
  )
 ENGINE = InnoDB
 CHARSET = utf8mb4
 COMMENT = '组织表';

DROP TABLE IF EXISTS `db_jdk11_test`.`tb_employee`;
CREATE TABLE IF NOT EXISTS `db_jdk11_test`.`tb_employee` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `employee_name` VARCHAR(200) NOT NULL  COMMENT '员工姓名',
  `employee_sex` TINYINT DEFAULT 0 NOT NULL  COMMENT '员工性别(0:女;1:男)',
  `employee_entry_ymd` DATE NOT NULL  COMMENT '员工入职年月日',
  `employee_level` TINYINT DEFAULT 0 NOT NULL  COMMENT '员工级别(0:初级;1:中级;2:高级)',
  `employee_org_id` INT UNSIGNED DEFAULT 0 NOT NULL  COMMENT '员工所属组织',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `tb_employee_unique_001` (`employee_name`)
  )
 ENGINE = InnoDB
 CHARSET = utf8mb4
 COMMENT = '员工表';
