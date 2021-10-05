-- MySQL dump 10.13  Distrib 8.0.26, for Linux (x86_64)
--
-- Host: localhost    Database: shipping
-- ------------------------------------------------------
-- Server version	8.0.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `person_id` int NOT NULL,
  `creation_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `sum` decimal(8,2) NOT NULL DEFAULT '0.00',
  `invoice_status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_invoice_person1_idx` (`person_id`),
  KEY `fk_invoice_invoice_status1_idx` (`invoice_status_id`),
  CONSTRAINT `fk_invoice_invoice_status1` FOREIGN KEY (`invoice_status_id`) REFERENCES `invoice_status` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_invoice_person1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (1,1,'2021-09-22 18:08:45',1345.96,1),(2,1,'2021-01-22 18:08:45',103.00,1),(3,1,'2021-02-22 18:08:45',240.00,1),(4,1,'2021-03-22 18:08:45',234.00,1),(5,1,'2021-04-22 18:08:45',643.00,1),(6,1,'2021-05-22 18:08:45',234.00,1),(7,1,'2021-10-05 11:57:07',1345.96,1),(8,2,'2021-10-05 11:57:07',150.20,1),(9,5,'2021-10-05 12:00:57',651.68,2),(10,5,'2021-10-05 12:13:09',42.88,1),(11,1,'2021-10-05 12:13:20',1859.24,1),(12,5,'2021-10-05 12:13:33',352.00,1);
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_shippings`
--

DROP TABLE IF EXISTS `invoice_shippings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_shippings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sum` decimal(8,2) NOT NULL DEFAULT '0.00',
  `invoice_id` int NOT NULL,
  `shipping_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_invoice_shippings_invoice1_idx` (`invoice_id`),
  KEY `fk_invoice_shippings_shipping1_idx` (`shipping_id`),
  CONSTRAINT `fk_invoice_shippings_invoice1` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_invoice_shippings_shipping1` FOREIGN KEY (`shipping_id`) REFERENCES `shipping` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_shippings`
--

LOCK TABLES `invoice_shippings` WRITE;
/*!40000 ALTER TABLE `invoice_shippings` DISABLE KEYS */;
INSERT INTO `invoice_shippings` VALUES (1,1345.96,1,1),(2,0.00,7,1),(3,0.00,8,3),(4,0.00,9,8),(5,0.00,10,7),(6,0.00,11,12),(7,0.00,12,11);
/*!40000 ALTER TABLE `invoice_shippings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_status`
--

DROP TABLE IF EXISTS `invoice_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_status` (
  `id` int NOT NULL,
  `name` varchar(45) NOT NULL DEFAULT 'undefined',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_status`
--

LOCK TABLES `invoice_status` WRITE;
/*!40000 ALTER TABLE `invoice_status` DISABLE KEYS */;
INSERT INTO `invoice_status` VALUES (1,'waiting for pay'),(2,'paid');
/*!40000 ALTER TABLE `invoice_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locality`
--

DROP TABLE IF EXISTS `locality`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `locality` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locality`
--

LOCK TABLES `locality` WRITE;
/*!40000 ALTER TABLE `locality` DISABLE KEYS */;
INSERT INTO `locality` VALUES (1,'Киев'),(2,'Львов'),(3,'Луцк'),(4,'Ужгород'),(5,'Ровно'),(6,'Тернополь'),(7,'Хмельницкий'),(8,'Житомир'),(9,'Черкассы'),(10,'Чернигов'),(11,'Кропивницкий'),(12,'Николаев'),(13,'Херсон'),(14,'Одесса'),(15,'Днепр'),(16,'Запорожье'),(17,'Сумы'),(18,'Харьков'),(19,'Полтава'),(20,'Винница'),(21,'Черновцы'),(22,'Ивано-Франковск');
/*!40000 ALTER TABLE `locality` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logistic_config`
--

DROP TABLE IF EXISTS `logistic_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `logistic_config` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logistic_config`
--

LOCK TABLES `logistic_config` WRITE;
/*!40000 ALTER TABLE `logistic_config` DISABLE KEYS */;
INSERT INTO `logistic_config` VALUES (1,'один распредсклад');
/*!40000 ALTER TABLE `logistic_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logistic_net`
--

DROP TABLE IF EXISTS `logistic_net`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `logistic_net` (
  `id` int NOT NULL AUTO_INCREMENT,
  `logistic_config_id` int NOT NULL,
  `city_id` int NOT NULL,
  `neighbor_id` int NOT NULL,
  `distance` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_logistic_net_locality1_idx` (`city_id`),
  KEY `fk_logistic_net_locality2_idx` (`neighbor_id`),
  KEY `fk_logistic_net_logistic_config1` (`logistic_config_id`),
  CONSTRAINT `fk_logistic_net_locality1` FOREIGN KEY (`city_id`) REFERENCES `locality` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_logistic_net_locality2` FOREIGN KEY (`neighbor_id`) REFERENCES `locality` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_logistic_net_logistic_config1` FOREIGN KEY (`logistic_config_id`) REFERENCES `logistic_config` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logistic_net`
--

LOCK TABLES `logistic_net` WRITE;
/*!40000 ALTER TABLE `logistic_net` DISABLE KEYS */;
INSERT INTO `logistic_net` VALUES (1,1,2,1,550),(2,1,3,1,438),(3,1,4,1,809),(4,1,5,1,336),(5,1,6,1,420),(6,1,7,1,323),(7,1,8,1,140),(8,1,9,1,192),(9,1,10,1,148),(10,1,11,1,303),(11,1,12,1,481),(12,1,13,1,547),(13,1,14,1,475),(14,1,15,1,478),(15,1,16,1,556),(16,1,17,1,369),(17,1,18,1,482),(18,1,19,1,344),(19,1,20,1,263),(20,1,21,1,531),(21,1,22,1,605),(22,1,1,2,550),(23,1,1,3,438),(24,1,1,4,809),(25,1,1,5,336),(26,1,1,6,420),(27,1,1,7,323),(28,1,1,8,140),(29,1,1,9,192),(30,1,1,10,148),(31,1,1,11,303),(32,1,1,12,481),(33,1,1,13,547),(34,1,1,14,475),(35,1,1,15,478),(36,1,1,16,556),(37,1,1,17,369),(38,1,1,18,482),(39,1,1,19,344),(40,1,1,20,263),(41,1,1,21,531),(42,1,1,22,605);
/*!40000 ALTER TABLE `logistic_net` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person` (
  `id` int NOT NULL AUTO_INCREMENT,
  `login` varchar(45) NOT NULL,
  `password` varchar(128) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `surname` varchar(45) DEFAULT NULL,
  `patronomic` varchar(45) DEFAULT NULL,
  `title` varchar(45) DEFAULT NULL,
  `role_id` int NOT NULL,
  `balance` decimal(8,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  KEY `fk_person_role1_idx` (`role_id`),
  CONSTRAINT `fk_person_role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,'vasya','$2a$10$gCiKp2j/SJSfbcSD9eNyruha5NWfQkv17bZGEtM65vMWHnaiue6Bq',NULL,'Василий','Иванов',NULL,NULL,2,20000.00),(2,'petya','$2a$10$gCiKp2j/SJSfbcSD9eNyruha5NWfQkv17bZGEtM65vMWHnaiue6Bq',NULL,'Петр','Григоренко',NULL,NULL,2,0.00),(3,'sasha','$2a$10$gCiKp2j/SJSfbcSD9eNyruha5NWfQkv17bZGEtM65vMWHnaiue6Bq',NULL,'Александр','Смирнов',NULL,NULL,1,0.00),(4,'admin','$2a$10$gCiKp2j/SJSfbcSD9eNyruha5NWfQkv17bZGEtM65vMWHnaiue6Bq',NULL,'Admin','Admin',NULL,NULL,1,0.00),(5,'user','$2a$10$gCiKp2j/SJSfbcSD9eNyruha5NWfQkv17bZGEtM65vMWHnaiue6Bq','','User','User',NULL,NULL,2,14348.32);
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `properties`
--

DROP TABLE IF EXISTS `properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `properties` (
  `id` varchar(45) NOT NULL,
  `value` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `properties`
--

LOCK TABLES `properties` WRITE;
/*!40000 ALTER TABLE `properties` DISABLE KEYS */;
INSERT INTO `properties` VALUES ('currentLogisticConfigId','1'),('currentTariffId','1');
/*!40000 ALTER TABLE `properties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'manager'),(2,'user');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settlement_type`
--

DROP TABLE IF EXISTS `settlement_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settlement_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `vector` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settlement_type`
--

LOCK TABLES `settlement_type` WRITE;
/*!40000 ALTER TABLE `settlement_type` DISABLE KEYS */;
INSERT INTO `settlement_type` VALUES (1,'payment',1),(2,'spending',-1);
/*!40000 ALTER TABLE `settlement_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settlements`
--

DROP TABLE IF EXISTS `settlements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `settlements` (
  `id` int NOT NULL AUTO_INCREMENT,
  `creation_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `person_id` int NOT NULL,
  `settlment_type_id` int NOT NULL,
  `amount` decimal(8,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`),
  KEY `fk_settlements_person1_idx` (`person_id`),
  KEY `fk_settlements_settlment_type1_idx` (`settlment_type_id`),
  CONSTRAINT `fk_settlements_person1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_settlements_settlment_type1` FOREIGN KEY (`settlment_type_id`) REFERENCES `settlement_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settlements`
--

LOCK TABLES `settlements` WRITE;
/*!40000 ALTER TABLE `settlements` DISABLE KEYS */;
INSERT INTO `settlements` VALUES (1,'2021-10-05 14:21:37',1,1,20000.00),(2,'2021-10-05 14:21:37',2,1,20000.00),(3,'2021-10-05 11:57:00',1,1,12000.00),(4,'2021-09-27 12:03:00',5,1,15000.00),(5,'2021-10-05 12:04:33',5,2,651.68);
/*!40000 ALTER TABLE `settlements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shipping`
--

DROP TABLE IF EXISTS `shipping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipping` (
  `id` int NOT NULL AUTO_INCREMENT,
  `creation_timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `person_id` int NOT NULL,
  `download_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `load_locality_id` int NOT NULL,
  `shipper` varchar(45) NOT NULL,
  `download_address` varchar(45) NOT NULL,
  `consignee` varchar(45) NOT NULL,
  `unload_locality_id` int NOT NULL,
  `unload_address` varchar(45) NOT NULL,
  `unloading_datetime` datetime DEFAULT NULL,
  `distance` double NOT NULL DEFAULT '0',
  `weight` double NOT NULL DEFAULT '0',
  `volume` double NOT NULL DEFAULT '0',
  `fare` decimal(8,2) NOT NULL DEFAULT '0.00',
  `shipping_status_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_shipping_person1_idx` (`person_id`),
  KEY `fk_shipping_locality1_idx` (`load_locality_id`),
  KEY `fk_shipping_locality2_idx` (`unload_locality_id`),
  KEY `fk_shipping_shipping_status1_idx` (`shipping_status_id`),
  CONSTRAINT `fk_shipping_locality1` FOREIGN KEY (`load_locality_id`) REFERENCES `locality` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_shipping_locality2` FOREIGN KEY (`unload_locality_id`) REFERENCES `locality` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_shipping_person1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_shipping_shipping_status1` FOREIGN KEY (`shipping_status_id`) REFERENCES `shipping_status` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipping`
--

LOCK TABLES `shipping` WRITE;
/*!40000 ALTER TABLE `shipping` DISABLE KEYS */;
INSERT INTO `shipping` VALUES (1,'2021-08-23 02:26:35',1,'2021-08-23 02:26:35',2,'sender1','address11','reciever1',8,'address12',NULL,800,300,450,1345.96,2),(2,'2021-09-11 02:26:35',1,'2021-09-11 02:26:35',4,'sender2','address22','reciever2',9,'address22',NULL,600,40,40,378.00,2),(3,'2021-09-16 02:26:35',2,'2021-09-16 02:26:35',5,'sender3','address32','reciever3',15,'address32',NULL,450,30,30,150.20,2),(4,'2021-09-16 02:26:35',2,'2021-09-16 02:26:35',3,'sender3','address32','reciever3',16,'address32',NULL,300,60,50,400.00,2),(5,'2021-09-21 02:26:35',2,'2021-09-21 02:26:35',2,'sender3','address32','reciever3',11,'address32','2021-10-05 21:00:00',300,60,50,462.00,5),(6,'2021-09-21 02:26:35',2,'2021-09-21 02:26:35',2,'sender1','address11','reciever1',8,'address12',NULL,800,300,450,1345.96,5),(7,'2021-10-05 11:41:47',5,'2021-10-05 21:00:00',9,'shipper','22222222','Степан Петрович',5,'ул.Ленина 125',NULL,528,1,1,42.88,2),(8,'2021-10-05 11:42:16',5,'2021-10-04 21:00:00',3,'shipper','ул.Львовская, 2а','consignee',1,'местность2',NULL,438,234,37,651.68,4),(9,'2021-10-05 11:42:47',5,'2021-10-04 21:00:00',7,'Иванов','addreess1','consignee',10,'местность2',NULL,471,176,248,517.49,1),(10,'2021-10-05 11:43:17',5,'2021-10-03 21:00:00',15,'Отправительный И.И.','адрес','Степан Петрович',5,'местность2',NULL,814,636,243,2419.91,1),(11,'2021-10-05 11:43:58',5,'2021-10-06 21:00:00',1,'Михал Иваныч','22222222','consignee',1,'ул.Ленина 125',NULL,0,240,213,352.00,2),(12,'2021-10-05 11:44:56',1,'2021-10-12 21:00:00',5,'Иванов','ул.Львовская, 2а','Степан Петрович',12,'addrwss2',NULL,817,485,440,1859.24,2),(13,'2021-10-05 11:46:00',1,'2021-10-19 21:00:00',9,'Михал Иваныч','адрес','consignee',6,'с.Хутор',NULL,612,301,123,983.94,1);
/*!40000 ALTER TABLE `shipping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shipping_status`
--

DROP TABLE IF EXISTS `shipping_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipping_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipping_status`
--

LOCK TABLES `shipping_status` WRITE;
/*!40000 ALTER TABLE `shipping_status` DISABLE KEYS */;
INSERT INTO `shipping_status` VALUES (3,'accepted'),(5,'delivered'),(4,'delivering'),(1,'just created'),(2,'waiting for pay');
/*!40000 ALTER TABLE `shipping_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tariff`
--

DROP TABLE IF EXISTS `tariff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tariff` (
  `id` int NOT NULL AUTO_INCREMENT,
  `creation_timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `logistic_config_id` int NOT NULL,
  `truck_velocity` int NOT NULL DEFAULT '50',
  `density` double NOT NULL DEFAULT '0.5',
  `paperwork` double NOT NULL DEFAULT '40',
  `targeted_receipt` int NOT NULL DEFAULT '50',
  `targeted_delivery` int NOT NULL DEFAULT '50',
  `shipping_rate` double NOT NULL DEFAULT '0.3',
  `insurance_worth` double NOT NULL DEFAULT '50',
  `insurance_rate` double NOT NULL DEFAULT '0.02',
  PRIMARY KEY (`id`),
  KEY `fk_tariff_logistic_config1_idx` (`logistic_config_id`),
  CONSTRAINT `fk_tariff_logistic_config1` FOREIGN KEY (`logistic_config_id`) REFERENCES `logistic_config` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tariff`
--

LOCK TABLES `tariff` WRITE;
/*!40000 ALTER TABLE `tariff` DISABLE KEYS */;
INSERT INTO `tariff` VALUES (1,'2021-01-30 01:20:37',1,50,0.5,40,50,50,0.3,50,0.02),(2,'2021-02-20 01:20:37',1,50,0.5,45,90,66,0.4,50,0.02),(3,'2021-03-30 01:20:37',1,50,0.5,45,90,66,0.5,50,0.02),(4,'2021-04-30 01:20:37',1,50,0.5,45,90,66,0.6,50,0.02),(5,'2021-05-30 01:20:37',1,55,0.5,45,80,66,0.7,50,0.02),(6,'2021-06-30 01:20:37',1,55,0.5,45,80,77,0.8,50,0.02),(7,'2021-07-20 01:20:37',1,55,0.5,56,80,77,0.9,50,0.02),(8,'2021-07-21 01:20:37',1,66,0.5,56,80,77,1,50,0.02),(9,'2021-07-22 01:20:37',1,66,0.5,56,45,77,1.1,50,0.02),(10,'2021-07-23 01:20:37',1,66,0.5,56,45,77,1.2,50,0.02),(11,'2021-08-23 01:20:37',1,77,0.5,56,45,77,1.3,50,0.02),(12,'2021-09-23 01:20:37',1,77,0.5,56,45,77,1.4,50,0.02);
/*!40000 ALTER TABLE `tariff` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-10-05 15:15:41
