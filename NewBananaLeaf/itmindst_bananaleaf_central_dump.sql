-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: itmindst_bananaleaf_central
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `branches`
--

DROP TABLE IF EXISTS `branches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `branches` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `status` tinyint DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branches`
--

LOCK TABLES `branches` WRITE;
/*!40000 ALTER TABLE `branches` DISABLE KEYS */;
INSERT INTO `branches` VALUES (1,'Nugegoda','Nugegoda',1),(2,'Bambalapitiya','Bambalapitiya',1),(3,'Maharagama','Maharagama',1),(4,'New Banana Leaf 3 Private Limited','Wellatte ',1);
/*!40000 ALTER TABLE `branches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grn`
--

DROP TABLE IF EXISTS `grn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grn` (
  `idgrn` int NOT NULL,
  `branch_id` int NOT NULL,
  `date` date DEFAULT NULL,
  `warranty` varchar(45) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `supplier_idsupplier` int NOT NULL,
  `user_iduser` int NOT NULL,
  `invoice_no` varchar(45) DEFAULT NULL,
  `image_path` varchar(245) DEFAULT NULL,
  `image` longblob,
  `storeskey` int DEFAULT '1',
  PRIMARY KEY (`idgrn`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grn`
--

LOCK TABLES `grn` WRITE;
/*!40000 ALTER TABLE `grn` DISABLE KEYS */;
/*!40000 ALTER TABLE `grn` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grn_main_item`
--

DROP TABLE IF EXISTS `grn_main_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grn_main_item` (
  `idgrn_main_item` int NOT NULL,
  `Main_Item_idmain_item` int NOT NULL,
  `grn_idgrn` int NOT NULL,
  `branch_id` int NOT NULL,
  `qty` double DEFAULT NULL,
  `up` double DEFAULT NULL,
  `sp` double DEFAULT NULL,
  `qty_bonus` double DEFAULT '0',
  `mfd` date DEFAULT NULL,
  `exp` date DEFAULT NULL,
  PRIMARY KEY (`idgrn_main_item`,`grn_idgrn`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grn_main_item`
--

LOCK TABLES `grn_main_item` WRITE;
/*!40000 ALTER TABLE `grn_main_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `grn_main_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `idinvoice` int NOT NULL,
  `branch_id` int NOT NULL,
  `date` varchar(45) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `s_charge` double DEFAULT '0',
  `total` double DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `warranty` varchar(45) DEFAULT NULL,
  `completed` int DEFAULT '0',
  `payment` int DEFAULT '0',
  `customer_idcustomer` int DEFAULT '1',
  `user_iduser` int NOT NULL,
  `commition` double DEFAULT '0',
  `note` varchar(45) DEFAULT NULL,
  `in_time` varchar(45) DEFAULT NULL,
  `out_time` varchar(45) DEFAULT NULL,
  `meal_type` varchar(45) DEFAULT NULL,
  `table_no` varchar(45) DEFAULT NULL,
  `payment_type` varchar(45) DEFAULT NULL,
  `steward` varchar(45) DEFAULT NULL,
  `log` int DEFAULT '0',
  `tableid` int DEFAULT NULL,
  `groupid` int DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `discount` double DEFAULT '0',
  `payment_id` int DEFAULT NULL,
  `ptype` int DEFAULT '1',
  `paid` int DEFAULT '1',
  `rep` int DEFAULT '1',
  `idtill` int DEFAULT '1',
  `pc` int DEFAULT '1',
  `gift` int DEFAULT '0',
  `prn` int DEFAULT '0',
  `idprn` int DEFAULT '1',
  `cash` double DEFAULT '0',
  `card` double DEFAULT '0',
  `type` int DEFAULT '1',
  `empty_count` varchar(45) DEFAULT NULL,
  `invoice_type` int DEFAULT '1',
  PRIMARY KEY (`idinvoice`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (32658,1,'2024-06-05',0,2087,0,'1','1',1,NULL,1,11,NULL,'Void',NULL,NULL,'DINNER','0',NULL,'-',0,NULL,NULL,'2024-06-06 03:01:33',0,1,0,41740,0,2,0,0,0,0,41740,0,0,'0',NULL),(32659,1,'2024-06-05',14790,739.5,14790,'1','1',1,NULL,1,11,NULL,NULL,NULL,NULL,'DINNER','0',NULL,'-',NULL,NULL,NULL,'2024-06-06 03:06:41',0,1,0,14790,0,2,0,0,0,0,14790,0,0,'0',NULL),(32660,2,'2024-06-05',12520,626,12520,'1','1',1,NULL,1,11,NULL,NULL,NULL,NULL,'DINNER','0',NULL,'-',NULL,NULL,NULL,'2024-06-06 03:07:24',0,1,0,12520,0,2,0,0,0,0,12520,0,0,'0',NULL),(32661,2,'2024-06-05',21980,953,21980,'1','1',1,NULL,1,11,NULL,NULL,NULL,NULL,'DINNER','0',NULL,'-',NULL,NULL,NULL,'2024-06-06 03:09:43',0,1,0,21980,0,2,0,0,0,0,21980,0,0,'0',NULL),(32662,3,'2024-06-05',6660,333,6660,'1','1',1,NULL,1,11,NULL,NULL,NULL,NULL,'DINNER','0',NULL,'-',NULL,NULL,NULL,'2024-06-06 03:11:20',0,1,0,6660,0,2,0,0,0,0,6660,0,0,'0',NULL),(32663,3,'2024-06-05',24920,1116,24920,'1','1',1,NULL,1,11,NULL,NULL,NULL,NULL,'DINNER','0',NULL,'-',NULL,NULL,NULL,'2024-06-06 03:13:14',0,1,0,24920,0,2,0,0,0,0,24920,0,0,'0',NULL),(32664,1,'2024-06-05',43060,2153,43060,'1','1',1,NULL,1,11,NULL,NULL,NULL,NULL,'DINNER','0',NULL,'-',NULL,NULL,NULL,'2024-06-06 03:23:10',0,1,0,50000,0,2,0,0,0,0,43060,0,0,'0',NULL),(32665,2,'2024-06-05',4600,230,4600,'1','1',1,NULL,1,11,NULL,NULL,NULL,NULL,'DINNER','0',NULL,'-',NULL,NULL,NULL,'2024-06-06 03:56:11',0,1,0,4600,0,2,0,0,0,0,4600,0,0,'0',NULL),(32666,1,'2025-12-05',0,0,0,'1','1',1,0,1,11,NULL,'-','2025-12-05 02:18:19','2025-12-05 02:18:19','LUNCH','1','CASH','-',NULL,NULL,NULL,'2025-12-05 07:18:19',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(32667,3,'2025-12-05',0,0,0,'1','1',1,0,1,11,NULL,'-','2025-12-05 02:24:56','2025-12-05 02:24:56','LUNCH','0','CASH','-',NULL,NULL,NULL,'2025-12-05 07:24:56',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(32668,2,'2025-12-05',0,0,0,'1','1',1,0,1,11,NULL,'-','2025-12-05 02:29:44','2025-12-05 02:29:44','LUNCH','1','CASH','-',NULL,NULL,NULL,'2025-12-05 07:29:44',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(32669,1,'2025-12-05',0,0,0,'1','1',1,0,1,11,NULL,'-','2025-12-05 02:30:56','2025-12-05 02:30:56','LUNCH','1','CASH','-',NULL,NULL,NULL,'2025-12-05 07:30:56',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(32670,0,'2025-12-05',0,0,0,'1','1',1,0,1,11,NULL,'-','2025-12-05 02:31:35','2025-12-05 02:31:35','LUNCH','1','CASH','-',NULL,NULL,NULL,'2025-12-05 07:31:35',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(32671,0,'2025-12-05',600,60,660,'1','1',1,0,1,11,NULL,'-','2025-12-05 02:32:17','2025-12-05 02:32:17','LUNCH','1','CASH','-',NULL,NULL,NULL,'2025-12-05 07:32:17',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(32672,0,'2025-12-05',520,52,572,'1','1',1,0,1,11,NULL,'-','2025-12-05 02:32:35','2025-12-05 02:32:35','LUNCH','1','CASH','-',NULL,NULL,NULL,'2025-12-05 07:32:35',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(32673,0,'2025-12-05',480,48,528,'1','1',1,0,1,11,NULL,'-','2025-12-05 02:33:16','2025-12-05 02:33:16','LUNCH','1','CASH','-',NULL,NULL,NULL,'2025-12-05 07:33:16',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(32674,0,'2025-12-05',600,60,660,'1','1',1,0,1,11,NULL,'-','2025-12-05 02:39:27','2025-12-05 02:39:27','LUNCH','1','CASH','-',NULL,NULL,NULL,'2025-12-05 07:39:27',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_has_main_item`
--

DROP TABLE IF EXISTS `invoice_has_main_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_has_main_item` (
  `idMain_Item_has_invoice` int NOT NULL,
  `Main_Item_idmain_item` int NOT NULL,
  `invoice_idinvoice` int NOT NULL,
  `branch_id` int NOT NULL,
  `qty` double DEFAULT NULL,
  `up` double DEFAULT NULL,
  `sp` double DEFAULT NULL,
  `warranty` varchar(45) DEFAULT NULL,
  `des` varchar(145) DEFAULT NULL,
  `mfd` date DEFAULT NULL,
  `exp` date DEFAULT NULL,
  `discount` double DEFAULT '0',
  `wsp` double DEFAULT '0',
  `cloud` int DEFAULT '0',
  `serv_chg` double DEFAULT '0',
  `lastqty` double DEFAULT '0',
  `kotstatus` int DEFAULT '0',
  `tabletake` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idMain_Item_has_invoice`,`invoice_idinvoice`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_has_main_item`
--

LOCK TABLES `invoice_has_main_item` WRITE;
/*!40000 ALTER TABLE `invoice_has_main_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_has_main_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `main_category`
--

DROP TABLE IF EXISTS `main_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `main_category` (
  `idMain_category` int NOT NULL AUTO_INCREMENT,
  `name` varchar(145) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idMain_category`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `main_category`
--

LOCK TABLES `main_category` WRITE;
/*!40000 ALTER TABLE `main_category` DISABLE KEYS */;
INSERT INTO `main_category` VALUES (1,'Uncategorized',NULL);
/*!40000 ALTER TABLE `main_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `main_item`
--

DROP TABLE IF EXISTS `main_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `main_item` (
  `idmain_item` int NOT NULL AUTO_INCREMENT,
  `Main_category_idMain_category` int NOT NULL,
  `name` varchar(145) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `description` varchar(145) DEFAULT NULL,
  `up` double DEFAULT NULL,
  `sp` double DEFAULT NULL,
  `wsp` double DEFAULT NULL,
  `qty` double DEFAULT '0',
  `code` varchar(145) DEFAULT NULL,
  `low` double DEFAULT '0',
  `cat1` varchar(45) DEFAULT NULL,
  `cat2` varchar(45) DEFAULT NULL,
  `withserial` int DEFAULT '0',
  `warranty` varchar(45) DEFAULT NULL,
  `discount` double DEFAULT '0',
  `discount_percentage` double DEFAULT '0',
  `short_code` varchar(45) DEFAULT NULL,
  `mfd` date DEFAULT NULL,
  `exp` date DEFAULT NULL,
  `multi_price` int DEFAULT '0',
  `cloud` int DEFAULT '0',
  `active` varchar(45) DEFAULT NULL,
  `image_path` varchar(245) DEFAULT NULL,
  `cat3` varchar(45) DEFAULT NULL,
  `parent` int DEFAULT '0',
  `image` longblob,
  `type` int DEFAULT '1',
  PRIMARY KEY (`idmain_item`)
) ENGINE=InnoDB AUTO_INCREMENT=435 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `main_item`
--

LOCK TABLES `main_item` WRITE;
/*!40000 ALTER TABLE `main_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `main_item` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-13 17:37:38
