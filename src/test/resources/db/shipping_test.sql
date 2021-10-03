-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema shipping
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `shipping_test` ;

-- -----------------------------------------------------
-- Schema shipping
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `shipping_test` ;
USE `shipping_test` ;

-- -----------------------------------------------------
-- Table `role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `role` ;

CREATE TABLE IF NOT EXISTS `role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `person`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `person` ;

CREATE TABLE IF NOT EXISTS `person` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `email` VARCHAR(45) NULL,
  `name` VARCHAR(45) NULL,
  `surname` VARCHAR(45) NULL,
  `patronomic` VARCHAR(45) NULL,
  `title` VARCHAR(45) NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
  INDEX `fk_person_role1_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `fk_person_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `locality`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `locality` ;

CREATE TABLE IF NOT EXISTS `locality` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shipping_status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shipping_status` ;

CREATE TABLE IF NOT EXISTS `shipping_status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `shipping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `shipping` ;

CREATE TABLE IF NOT EXISTS `shipping` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `creation_timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `person_id` INT NOT NULL,
  `download_datetime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `load_locality_id` INT NOT NULL,
  `shipper` VARCHAR(45) NOT NULL,
  `download_address` VARCHAR(45) NOT NULL,
  `consignee` VARCHAR(45) NOT NULL,
  `unload_locality_id` INT NOT NULL,
  `unload_address` VARCHAR(45) NOT NULL,
  `unloading_datetime` DATETIME NULL,
  `distance` DOUBLE NOT NULL DEFAULT 0,
  `weight` DOUBLE NOT NULL DEFAULT 0,
  `volume` DOUBLE NOT NULL DEFAULT 0,
  `fare` DECIMAL(8,2) NOT NULL DEFAULT 0,
  `shipping_status_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_shipping_person1_idx` (`person_id` ASC) VISIBLE,
  INDEX `fk_shipping_locality1_idx` (`load_locality_id` ASC) VISIBLE,
  INDEX `fk_shipping_locality2_idx` (`unload_locality_id` ASC) VISIBLE,
  INDEX `fk_shipping_shipping_status1_idx` (`shipping_status_id` ASC) VISIBLE,
  CONSTRAINT `fk_shipping_person1`
    FOREIGN KEY (`person_id`)
    REFERENCES `person` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_shipping_locality1`
    FOREIGN KEY (`load_locality_id`)
    REFERENCES `locality` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_shipping_locality2`
    FOREIGN KEY (`unload_locality_id`)
    REFERENCES `locality` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_shipping_shipping_status1`
    FOREIGN KEY (`shipping_status_id`)
    REFERENCES `shipping_status` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `invoice_status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `invoice_status` ;

CREATE TABLE IF NOT EXISTS `invoice_status` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL DEFAULT 'undefined',
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `invoice`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `invoice` ;

CREATE TABLE IF NOT EXISTS `invoice` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `person_id` INT NOT NULL,
  `creation_datetime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP(),
  `sum` DECIMAL(8,2) NOT NULL DEFAULT 0,
  `invoice_status_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_invoice_person1_idx` (`person_id` ASC) VISIBLE,
  INDEX `fk_invoice_invoice_status1_idx` (`invoice_status_id` ASC) VISIBLE,
  CONSTRAINT `fk_invoice_person1`
    FOREIGN KEY (`person_id`)
    REFERENCES `person` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_invoice_invoice_status1`
    FOREIGN KEY (`invoice_status_id`)
    REFERENCES `invoice_status` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `invoice_shippings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `invoice_shippings` ;

CREATE TABLE IF NOT EXISTS `invoice_shippings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sum` DECIMAL(8,2) NOT NULL DEFAULT 0,
  `invoice_id` INT NOT NULL,
  `shipping_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_invoice_shippings_invoice1_idx` (`invoice_id` ASC) VISIBLE,
  INDEX `fk_invoice_shippings_shipping1_idx` (`shipping_id` ASC) VISIBLE,
  CONSTRAINT `fk_invoice_shippings_invoice1`
    FOREIGN KEY (`invoice_id`)
    REFERENCES `invoice` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_invoice_shippings_shipping1`
    FOREIGN KEY (`shipping_id`)
    REFERENCES `shipping` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `settlement_type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `settlement_type` ;

CREATE TABLE IF NOT EXISTS `settlement_type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `vector` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `settlements`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `settlements` ;

CREATE TABLE IF NOT EXISTS `settlements` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `creation_datetime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `person_id` INT NOT NULL,
  `settlment_type_id` INT NOT NULL,
  `amount` DECIMAL(8,2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_settlements_person1_idx` (`person_id` ASC) VISIBLE,
  INDEX `fk_settlements_settlment_type1_idx` (`settlment_type_id` ASC) VISIBLE,
  CONSTRAINT `fk_settlements_person1`
    FOREIGN KEY (`person_id`)
    REFERENCES `person` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_settlements_settlment_type1`
    FOREIGN KEY (`settlment_type_id`)
    REFERENCES `settlement_type` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_details`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_details` ;

CREATE TABLE IF NOT EXISTS `user_details` (
  `person_id` INT NOT NULL,
  `balance` DECIMAL(8,2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`person_id`),
  CONSTRAINT `fk_user_balance_person1`
    FOREIGN KEY (`person_id`)
    REFERENCES `person` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `logistic_config`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistic_config` ;

CREATE TABLE IF NOT EXISTS `logistic_config` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `logistic_net`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `logistic_net` ;

CREATE TABLE IF NOT EXISTS `logistic_net` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `logistic_config_id` INT NOT NULL,
  `city_id` INT NOT NULL,
  `neighbor_id` INT NOT NULL,
  `distance` DOUBLE NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_logistic_net_locality1_idx` (`city_id` ASC) VISIBLE,
  INDEX `fk_logistic_net_locality2_idx` (`neighbor_id` ASC) VISIBLE,
  CONSTRAINT `fk_logistic_net_logistic_config1`
    FOREIGN KEY (`logistic_config_id`)
    REFERENCES `logistic_config` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_logistic_net_locality1`
    FOREIGN KEY (`city_id`)
    REFERENCES `locality` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_logistic_net_locality2`
    FOREIGN KEY (`neighbor_id`)
    REFERENCES `locality` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tariff`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tariff` ;

CREATE TABLE IF NOT EXISTS `tariff` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `creation_timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `logistic_config_id` INT NOT NULL,
  `truck_velocity` INT NOT NULL DEFAULT 50,
  `density` DOUBLE NOT NULL DEFAULT 0.5,
  `paperwork` DOUBLE NOT NULL DEFAULT 40,
  `targeted_receipt` INT NOT NULL DEFAULT 50,
  `targeted_delivery` INT NOT NULL DEFAULT 50,
  `shipping_rate` DOUBLE NOT NULL DEFAULT 0.3,
  `insurance_worth` DOUBLE NOT NULL DEFAULT 50,
  `insurance_rate` DOUBLE NOT NULL DEFAULT 0.02,
  PRIMARY KEY (`id`),
  INDEX `fk_tariff_logistic_config1_idx` (`logistic_config_id` ASC) VISIBLE,
  CONSTRAINT `fk_tariff_logistic_config1`
    FOREIGN KEY (`logistic_config_id`)
    REFERENCES `logistic_config` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `properties`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `properties` ;

CREATE TABLE IF NOT EXISTS `properties` (
  `id` VARCHAR(45) NOT NULL,
  `value` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Persistent_Logins`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Persistent_Logins` ;

CREATE TABLE IF NOT EXISTS `Persistent_Logins` (
  `username` VARCHAR(64) NOT NULL,
  `series` VARCHAR(64) NOT NULL,
  `token` VARCHAR(64) NOT NULL,
  `last_used` TIMESTAMP NOT NULL,
  PRIMARY KEY (`series`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `role`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `role` (`id`, `name`) VALUES (1, 'manager');
INSERT INTO `role` (`id`, `name`) VALUES (2, 'user');

COMMIT;


-- -----------------------------------------------------
-- Data for table `person`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `person` (`id`, `login`, `password`, `email`, `name`, `surname`, `patronomic`, `title`, `role_id`) VALUES (1, 'vasya', '$2a$10$gCiKp2j/SJSfbcSD9eNyruha5NWfQkv17bZGEtM65vMWHnaiue6Bq', NULL, 'Василий', 'Иванов', NULL, NULL, 2);
INSERT INTO `person` (`id`, `login`, `password`, `email`, `name`, `surname`, `patronomic`, `title`, `role_id`) VALUES (2, 'petya', '$2a$10$gCiKp2j/SJSfbcSD9eNyruha5NWfQkv17bZGEtM65vMWHnaiue6Bq', NULL, 'Петр', 'Григоренко', NULL, NULL, 2);
INSERT INTO `person` (`id`, `login`, `password`, `email`, `name`, `surname`, `patronomic`, `title`, `role_id`) VALUES (3, 'sasha', '$2a$10$gCiKp2j/SJSfbcSD9eNyruha5NWfQkv17bZGEtM65vMWHnaiue6Bq', NULL, 'Александр', 'Смирнов', NULL, NULL, 1);
INSERT INTO `person` (`id`, `login`, `password`, `email`, `name`, `surname`, `patronomic`, `title`, `role_id`) VALUES (4, 'admin', '$2a$10$gCiKp2j/SJSfbcSD9eNyruha5NWfQkv17bZGEtM65vMWHnaiue6Bq', NULL, 'Admin', 'Admin', NULL, NULL, 1);
INSERT INTO `person` (`id`, `login`, `password`, `email`, `name`, `surname`, `patronomic`, `title`, `role_id`) VALUES (5, 'user', '$2a$10$gCiKp2j/SJSfbcSD9eNyruha5NWfQkv17bZGEtM65vMWHnaiue6Bq', '', 'User', 'User', NULL, NULL, 2);

COMMIT;


-- -----------------------------------------------------
-- Data for table `locality`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `locality` (`id`, `name`) VALUES (1, 'Киев');
INSERT INTO `locality` (`id`, `name`) VALUES (2, 'Львов');
INSERT INTO `locality` (`id`, `name`) VALUES (3, 'Луцк');
INSERT INTO `locality` (`id`, `name`) VALUES (4, 'Ужгород');
INSERT INTO `locality` (`id`, `name`) VALUES (5, 'Ровно');
INSERT INTO `locality` (`id`, `name`) VALUES (6, 'Тернополь');
INSERT INTO `locality` (`id`, `name`) VALUES (7, 'Хмельницкий');
INSERT INTO `locality` (`id`, `name`) VALUES (8, 'Житомир');
INSERT INTO `locality` (`id`, `name`) VALUES (9, 'Черкассы');
INSERT INTO `locality` (`id`, `name`) VALUES (10, 'Чернигов');
INSERT INTO `locality` (`id`, `name`) VALUES (11, 'Кропивницкий');
INSERT INTO `locality` (`id`, `name`) VALUES (12, 'Николаев');
INSERT INTO `locality` (`id`, `name`) VALUES (13, 'Херсон');
INSERT INTO `locality` (`id`, `name`) VALUES (14, 'Одесса');
INSERT INTO `locality` (`id`, `name`) VALUES (15, 'Днепр');
INSERT INTO `locality` (`id`, `name`) VALUES (16, 'Запорожье');
INSERT INTO `locality` (`id`, `name`) VALUES (17, 'Сумы');
INSERT INTO `locality` (`id`, `name`) VALUES (18, 'Харьков');
INSERT INTO `locality` (`id`, `name`) VALUES (19, 'Полтава');
INSERT INTO `locality` (`id`, `name`) VALUES (20, 'Винница');
INSERT INTO `locality` (`id`, `name`) VALUES (21, 'Черновцы');
INSERT INTO `locality` (`id`, `name`) VALUES (22, 'Ивано-Франковск');

COMMIT;


-- -----------------------------------------------------
-- Data for table `shipping_status`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `shipping_status` (`id`, `name`) VALUES (1, 'just created');
INSERT INTO `shipping_status` (`id`, `name`) VALUES (2, 'waiting for pay');
INSERT INTO `shipping_status` (`id`, `name`) VALUES (3, 'accepted');
INSERT INTO `shipping_status` (`id`, `name`) VALUES (4, 'delivering');
INSERT INTO `shipping_status` (`id`, `name`) VALUES (5, 'delivered');

COMMIT;


-- -----------------------------------------------------
-- Data for table `shipping`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `shipping` (`id`, `creation_timestamp`, `person_id`, `download_datetime`, `load_locality_id`, `shipper`, `download_address`, `consignee`, `unload_locality_id`, `unload_address`, `unloading_datetime`, `distance`, `weight`, `volume`, `fare`, `shipping_status_id`) VALUES (1, '2021-08-22T23:26:35.000+00:00', 1, '2021-08-22T23:26:35.000+00:00', 2, 'sender1', 'address11', 'reciever1', 8, 'address12', NULL, 800, 300, 450, 1345.96, 1);
INSERT INTO `shipping` (`id`, `creation_timestamp`, `person_id`, `download_datetime`, `load_locality_id`, `shipper`, `download_address`, `consignee`, `unload_locality_id`, `unload_address`, `unloading_datetime`, `distance`, `weight`, `volume`, `fare`, `shipping_status_id`) VALUES (2, '2021-09-10T23:26:35.000+00:00', 1, '2021-09-10T23:26:35.000+00:00', 4, 'sender2', 'address22', 'reciever2', 9, 'address22', NULL, 600, 40, 40, 378.00, 2);
INSERT INTO `shipping` (`id`, `creation_timestamp`, `person_id`, `download_datetime`, `load_locality_id`, `shipper`, `download_address`, `consignee`, `unload_locality_id`, `unload_address`, `unloading_datetime`, `distance`, `weight`, `volume`, `fare`, `shipping_status_id`) VALUES (3, '2021-09-15T23:26:35.000+00:00', 2, '2021-09-15T23:26:35.000+00:00', 5, 'sender3', 'address32', 'reciever3', 15, 'address32', NULL, 450, 30, 30, 150.2, 1);
INSERT INTO `shipping` (`id`, `creation_timestamp`, `person_id`, `download_datetime`, `load_locality_id`, `shipper`, `download_address`, `consignee`, `unload_locality_id`, `unload_address`, `unloading_datetime`, `distance`, `weight`, `volume`, `fare`, `shipping_status_id`) VALUES (4, '2021-09-15T23:26:35.000+00:00', 2, '2021-09-15T23:26:35.000+00:00', 3, 'sender3', 'address32', 'reciever3', 16, 'address32', NULL, 300, 60, 50, 400, 2);
INSERT INTO `shipping` (`id`, `creation_timestamp`, `person_id`, `download_datetime`, `load_locality_id`, `shipper`, `download_address`, `consignee`, `unload_locality_id`, `unload_address`, `unloading_datetime`, `distance`, `weight`, `volume`, `fare`, `shipping_status_id`) VALUES (5, '2021-09-20T23:26:35.000+00:00', 2, '2021-09-20T23:26:35.000+00:00', 2, 'sender3', 'address32', 'reciever3', 11, 'address32', NULL, 300, 60, 50, 462, 4);
INSERT INTO `shipping` (`id`, `creation_timestamp`, `person_id`, `download_datetime`, `load_locality_id`, `shipper`, `download_address`, `consignee`, `unload_locality_id`, `unload_address`, `unloading_datetime`, `distance`, `weight`, `volume`, `fare`, `shipping_status_id`) VALUES (6, '2021-09-20T23:26:35.000+00:00', 2, '2021-09-20T23:26:35.000+00:00', 2, 'sender1', 'address11', 'reciever1', 8, 'address12', NULL, 800, 300, 450, 1345.96, 5);
INSERT INTO `shipping` (`id`, `creation_timestamp`, `person_id`, `download_datetime`, `load_locality_id`, `shipper`, `download_address`, `consignee`, `unload_locality_id`, `unload_address`, `unloading_datetime`, `distance`, `weight`, `volume`, `fare`, `shipping_status_id`) VALUES (7, '2021-09-22T23:26:35.000+00:00', 1, '2021-08-22T23:26:35.000+00:00', 2, 'sender1', 'address11', 'reciever1', 8, 'address12', NULL, 800, 300, 450, 1100.96, 1);
INSERT INTO `shipping` (`id`, `creation_timestamp`, `person_id`, `download_datetime`, `load_locality_id`, `shipper`, `download_address`, `consignee`, `unload_locality_id`, `unload_address`, `unloading_datetime`, `distance`, `weight`, `volume`, `fare`, `shipping_status_id`) VALUES (8, '2021-07-22T23:26:35.000+00:00', 1, '2021-08-22T23:26:35.000+00:00', 2, 'sender1', 'address11', 'reciever1', 8, 'address12', NULL, 800, 300, 450, 1200.96, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `invoice_status`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `invoice_status` (`id`, `name`) VALUES (1, 'waiting for pay');
INSERT INTO `invoice_status` (`id`, `name`) VALUES (2, 'paid');

COMMIT;


-- -----------------------------------------------------
-- Data for table `invoice`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `invoice` (`id`, `person_id`, `creation_datetime`, `sum`, `invoice_status_id`) VALUES (1, 1, '2021-09-22 18:08:45', 1345.96, 1);
INSERT INTO `invoice` (`id`, `person_id`, `creation_datetime`, `sum`, `invoice_status_id`) VALUES (2, 1, '2021-01-22 18:08:45', 103, 1);
INSERT INTO `invoice` (`id`, `person_id`, `creation_datetime`, `sum`, `invoice_status_id`) VALUES (3, 1, '2021-02-22 18:08:45', 240, 1);
INSERT INTO `invoice` (`id`, `person_id`, `creation_datetime`, `sum`, `invoice_status_id`) VALUES (4, 1, '2021-03-22 18:08:45', 234, 1);
INSERT INTO `invoice` (`id`, `person_id`, `creation_datetime`, `sum`, `invoice_status_id`) VALUES (5, 1, '2021-04-22 18:08:45', 643, 1);
INSERT INTO `invoice` (`id`, `person_id`, `creation_datetime`, `sum`, `invoice_status_id`) VALUES (6, 1, '2021-05-22 18:08:45', 234, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `invoice_shippings`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `invoice_shippings` (`id`, `sum`, `invoice_id`, `shipping_id`) VALUES (1, 1345.96, 1, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `settlement_type`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `settlement_type` (`id`, `name`, `vector`) VALUES (1, 'payment', 1);
INSERT INTO `settlement_type` (`id`, `name`, `vector`) VALUES (2, 'spending', -1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `settlements`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `settlements` (`id`, `creation_datetime`, `person_id`, `settlment_type_id`, `amount`) VALUES (1, DEFAULT, 1, 1, 20000);
INSERT INTO `settlements` (`id`, `creation_datetime`, `person_id`, `settlment_type_id`, `amount`) VALUES (2, DEFAULT, 2, 1, 20000);
INSERT INTO `settlements` (`id`, `creation_datetime`, `person_id`, `settlment_type_id`, `amount`) VALUES (3, DEFAULT, 1, 2, 500);
INSERT INTO `settlements` (`id`, `creation_datetime`, `person_id`, `settlment_type_id`, `amount`) VALUES (4, DEFAULT, 2, 2, 1500);

COMMIT;


-- -----------------------------------------------------
-- Data for table `user_details`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `user_details` (`person_id`, `balance`) VALUES (1, 0);
INSERT INTO `user_details` (`person_id`, `balance`) VALUES (2, 0);

COMMIT;


-- -----------------------------------------------------
-- Data for table `logistic_config`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `logistic_config` (`id`, `name`) VALUES (1, 'один распредсклад');

COMMIT;


-- -----------------------------------------------------
-- Data for table `logistic_net`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 2, 1, 550);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 3, 1, 438);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 4, 1, 809);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 5, 1, 336);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 6, 1, 420);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 7, 1, 323);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 8, 1, 140);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 9, 1, 192);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 10, 1, 148);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 11, 1, 303);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 12, 1, 481);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 13, 1, 547);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 14, 1, 475);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 15, 1, 478);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 16, 1, 556);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 17, 1, 369);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 18, 1, 482);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 19, 1, 344);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 20, 1, 263);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 21, 1, 531);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 22, 1, 605);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 2, 550);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 3, 438);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 4, 809);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 5, 336);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 6, 420);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 7, 323);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 8, 140);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 9, 192);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 10, 148);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 11, 303);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 12, 481);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 13, 547);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 14, 475);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 15, 478);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 16, 556);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 17, 369);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 18, 482);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 19, 344);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 20, 263);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 21, 531);
INSERT INTO `logistic_net` (`id`, `logistic_config_id`, `city_id`, `neighbor_id`, `distance`) VALUES (DEFAULT, 1, 1, 22, 605);

COMMIT;


-- -----------------------------------------------------
-- Data for table `tariff`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `tariff` (`id`, `creation_timestamp`, `logistic_config_id`, `truck_velocity`, `density`, `paperwork`, `targeted_receipt`, `targeted_delivery`, `shipping_rate`, `insurance_worth`, `insurance_rate`) VALUES (1, '2021-01-30 01:20:37', 1, 50, 0.5, 40, 50, 50, 0.3, 50, 0.02);
INSERT INTO `tariff` (`id`, `creation_timestamp`, `logistic_config_id`, `truck_velocity`, `density`, `paperwork`, `targeted_receipt`, `targeted_delivery`, `shipping_rate`, `insurance_worth`, `insurance_rate`) VALUES (2, '2021-02-20 01:20:37', 1, 50, 0.5, 45, 90, 66, 0.4, 50, 0.02);
INSERT INTO `tariff` (`id`, `creation_timestamp`, `logistic_config_id`, `truck_velocity`, `density`, `paperwork`, `targeted_receipt`, `targeted_delivery`, `shipping_rate`, `insurance_worth`, `insurance_rate`) VALUES (3, '2021-03-30 01:20:37', 1, 50, 0.5, 45, 90, 66, 0.5, 50, 0.02);
INSERT INTO `tariff` (`id`, `creation_timestamp`, `logistic_config_id`, `truck_velocity`, `density`, `paperwork`, `targeted_receipt`, `targeted_delivery`, `shipping_rate`, `insurance_worth`, `insurance_rate`) VALUES (4, '2021-04-30 01:20:37', 1, 50, 0.5, 45, 90, 66, 0.6, 50, 0.02);
INSERT INTO `tariff` (`id`, `creation_timestamp`, `logistic_config_id`, `truck_velocity`, `density`, `paperwork`, `targeted_receipt`, `targeted_delivery`, `shipping_rate`, `insurance_worth`, `insurance_rate`) VALUES (5, '2021-05-30 01:20:37', 1, 55, 0.5, 45, 80, 66, 0.7, 50, 0.02);
INSERT INTO `tariff` (`id`, `creation_timestamp`, `logistic_config_id`, `truck_velocity`, `density`, `paperwork`, `targeted_receipt`, `targeted_delivery`, `shipping_rate`, `insurance_worth`, `insurance_rate`) VALUES (6, '2021-06-30 01:20:37', 1, 55, 0.5, 45, 80, 77, 0.8, 50, 0.02);
INSERT INTO `tariff` (`id`, `creation_timestamp`, `logistic_config_id`, `truck_velocity`, `density`, `paperwork`, `targeted_receipt`, `targeted_delivery`, `shipping_rate`, `insurance_worth`, `insurance_rate`) VALUES (7, '2021-07-20 01:20:37', 1, 55, 0.5, 56, 80, 77, 0.9, 50, 0.02);
INSERT INTO `tariff` (`id`, `creation_timestamp`, `logistic_config_id`, `truck_velocity`, `density`, `paperwork`, `targeted_receipt`, `targeted_delivery`, `shipping_rate`, `insurance_worth`, `insurance_rate`) VALUES (8, '2021-07-21 01:20:37', 1, 66, 0.5, 56, 80, 77, 1.0, 50, 0.02);
INSERT INTO `tariff` (`id`, `creation_timestamp`, `logistic_config_id`, `truck_velocity`, `density`, `paperwork`, `targeted_receipt`, `targeted_delivery`, `shipping_rate`, `insurance_worth`, `insurance_rate`) VALUES (9, '2021-07-22 01:20:37', 1, 66, 0.5, 56, 45, 77, 1.1, 50, 0.02);
INSERT INTO `tariff` (`id`, `creation_timestamp`, `logistic_config_id`, `truck_velocity`, `density`, `paperwork`, `targeted_receipt`, `targeted_delivery`, `shipping_rate`, `insurance_worth`, `insurance_rate`) VALUES (10, '2021-07-23 01:20:37', 1, 66, 0.5, 56, 45, 77, 1.2, 50, 0.02);
INSERT INTO `tariff` (`id`, `creation_timestamp`, `logistic_config_id`, `truck_velocity`, `density`, `paperwork`, `targeted_receipt`, `targeted_delivery`, `shipping_rate`, `insurance_worth`, `insurance_rate`) VALUES (11, '2021-08-23 01:20:37', 1, 77, 0.5, 56, 45, 77, 1.3, 50, 0.02);
INSERT INTO `tariff` (`id`, `creation_timestamp`, `logistic_config_id`, `truck_velocity`, `density`, `paperwork`, `targeted_receipt`, `targeted_delivery`, `shipping_rate`, `insurance_worth`, `insurance_rate`) VALUES (12, '2021-09-23 01:20:37', 1, 77, 0.5, 56, 45, 77, 1.4, 50, 0.02);

COMMIT;


-- -----------------------------------------------------
-- Data for table `properties`
-- -----------------------------------------------------
START TRANSACTION;
USE `shipping_test`;
INSERT INTO `properties` (`id`, `value`) VALUES ('currentTariffId', '1');
INSERT INTO `properties` (`id`, `value`) VALUES ('currentLogisticConfigId', '1');

COMMIT;

