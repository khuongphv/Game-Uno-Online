-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: localhost    Database: uno
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
-- Table structure for table `tblfriendship`
--

DROP TABLE IF EXISTS `tblfriendship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblfriendship` (
  `playerid1` int NOT NULL,
  `playerid2` int NOT NULL,
  `status` varchar(45) NOT NULL,
  PRIMARY KEY (`playerid1`,`playerid2`),
  KEY `player2_friend_idx` (`playerid2`),
  CONSTRAINT `player1_friend` FOREIGN KEY (`playerid1`) REFERENCES `tblplayer` (`id`),
  CONSTRAINT `player2_friend` FOREIGN KEY (`playerid2`) REFERENCES `tblplayer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblfriendship`
--

LOCK TABLES `tblfriendship` WRITE;
/*!40000 ALTER TABLE `tblfriendship` DISABLE KEYS */;
INSERT INTO `tblfriendship` VALUES (1,2,'befriend'),(1,3,'befriend'),(1,4,'befriend'),(1,5,'befriend'),(1,10,'befriend'),(3,2,'request'),(3,5,'befriend'),(3,6,'befriend'),(5,6,'befriend'),(8,1,'befriend'),(9,1,'befriend'),(10,2,'request'),(10,6,'befriend'),(10,8,'befriend'),(10,9,'befriend'),(10,12,'befriend'),(10,15,'befriend'),(11,10,'befriend'),(13,10,'befriend'),(14,10,'befriend'),(16,10,'befriend'),(17,10,'befriend'),(18,10,'befriend');
/*!40000 ALTER TABLE `tblfriendship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblgroup`
--

DROP TABLE IF EXISTS `tblgroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblgroup` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `note` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblgroup`
--

LOCK TABLES `tblgroup` WRITE;
/*!40000 ALTER TABLE `tblgroup` DISABLE KEYS */;
INSERT INTO `tblgroup` VALUES (1,'groupA',NULL),(2,'groupB',NULL),(3,'groupC',NULL),(4,'groupD',NULL),(5,'groupE',NULL),(6,'groupF',NULL),(11,'groupH',NULL);
/*!40000 ALTER TABLE `tblgroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblmatch`
--

DROP TABLE IF EXISTS `tblmatch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblmatch` (
  `id` int NOT NULL AUTO_INCREMENT,
  `begin` datetime DEFAULT NULL,
  `end` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblmatch`
--

LOCK TABLES `tblmatch` WRITE;
/*!40000 ALTER TABLE `tblmatch` DISABLE KEYS */;
INSERT INTO `tblmatch` VALUES (9,'2021-12-18 22:35:13','2021-12-18 22:36:06'),(10,'2021-12-18 22:44:55','2021-12-18 22:46:44'),(11,'2021-12-18 22:48:35','2021-12-18 22:49:02'),(13,'2021-12-19 00:54:10','2021-12-19 00:56:40'),(14,'2021-12-19 01:06:41','2021-12-19 01:09:25'),(15,'2021-12-19 01:14:39','2021-12-19 01:17:11'),(16,'2021-12-19 10:50:57','2021-12-19 10:51:24'),(17,'2021-12-19 10:55:22','2021-12-19 10:57:11'),(18,'2021-12-19 10:58:40','2021-12-19 10:59:50'),(19,'2021-12-20 12:47:40','2021-12-20 12:48:50');
/*!40000 ALTER TABLE `tblmatch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblplayer`
--

DROP TABLE IF EXISTS `tblplayer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblplayer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `nickname` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblplayer`
--

LOCK TABLES `tblplayer` WRITE;
/*!40000 ALTER TABLE `tblplayer` DISABLE KEYS */;
INSERT INTO `tblplayer` VALUES (1,'test1','1',NULL),(2,'test2','2',NULL),(3,'test3','1',NULL),(4,'test4','1',NULL),(5,'test5','1',NULL),(6,'test6','1',NULL),(7,'test7','1',NULL),(8,'test8','1',NULL),(9,'test9','1',NULL),(10,'a','a',NULL),(11,'b','b',NULL),(12,'c','c',NULL),(13,'d','d',NULL),(14,'f','f',NULL),(15,'g','g',NULL),(16,'h','h',NULL),(17,'j','j',NULL),(18,'l','l',NULL),(19,'ab','ab',NULL);
/*!40000 ALTER TABLE `tblplayer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblplayergroup`
--

DROP TABLE IF EXISTS `tblplayergroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblplayergroup` (
  `playerid` int NOT NULL,
  `groupid` int NOT NULL,
  `role` int NOT NULL,
  PRIMARY KEY (`playerid`,`groupid`),
  KEY `player_fg_idx` (`playerid`),
  KEY `group_fg_idx` (`groupid`),
  CONSTRAINT `group_fg` FOREIGN KEY (`groupid`) REFERENCES `tblgroup` (`id`),
  CONSTRAINT `player_fg` FOREIGN KEY (`playerid`) REFERENCES `tblplayer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblplayergroup`
--

LOCK TABLES `tblplayergroup` WRITE;
/*!40000 ALTER TABLE `tblplayergroup` DISABLE KEYS */;
INSERT INTO `tblplayergroup` VALUES (1,1,1),(1,2,1),(1,3,1),(2,3,2),(2,4,2),(2,6,4),(3,1,2),(3,3,2),(4,1,2),(5,1,2),(6,1,2),(7,4,1),(8,5,1),(9,1,2),(9,6,1),(10,1,2),(10,2,4),(10,4,2),(10,11,1),(12,1,3),(15,1,2),(16,1,2),(17,1,2),(18,1,2),(18,11,2);
/*!40000 ALTER TABLE `tblplayergroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblplayingplayer`
--

DROP TABLE IF EXISTS `tblplayingplayer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblplayingplayer` (
  `playerid` int NOT NULL,
  `time` datetime NOT NULL,
  `roomid` int NOT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`playerid`,`time`,`roomid`),
  CONSTRAINT `pp_player` FOREIGN KEY (`playerid`) REFERENCES `tblplayer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblplayingplayer`
--

LOCK TABLES `tblplayingplayer` WRITE;
/*!40000 ALTER TABLE `tblplayingplayer` DISABLE KEYS */;
INSERT INTO `tblplayingplayer` VALUES (1,'2021-11-25 17:43:48',5,2),(2,'2021-11-25 17:43:48',5,2),(3,'2021-11-25 17:43:48',5,1),(4,'2021-11-25 17:45:38',2,2),(6,'2021-11-25 17:45:38',2,2),(8,'2021-11-25 17:45:38',2,1),(9,'2021-11-26 17:53:56',1,1),(10,'2021-11-25 17:33:27',2,1),(10,'2021-11-25 17:36:15',3,2),(10,'2021-11-26 02:24:31',1,1),(10,'2021-11-26 02:27:01',3,1),(10,'2021-11-26 14:48:19',1,1),(10,'2021-11-26 17:53:56',1,2),(10,'2021-12-18 14:15:32',1,1),(11,'2021-11-25 17:36:15',3,1),(11,'2021-11-26 02:27:58',2,1),(11,'2021-11-26 14:48:19',1,2),(12,'2021-11-26 14:48:19',1,2),(13,'2021-11-26 02:27:01',3,2);
/*!40000 ALTER TABLE `tblplayingplayer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblpp`
--

DROP TABLE IF EXISTS `tblpp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblpp` (
  `playerid` int NOT NULL,
  `matchid` int NOT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`playerid`,`matchid`),
  KEY `playingplayer_match_idx` (`matchid`),
  CONSTRAINT `playingplayer_match` FOREIGN KEY (`matchid`) REFERENCES `tblmatch` (`id`),
  CONSTRAINT `playingplayer_player` FOREIGN KEY (`playerid`) REFERENCES `tblplayer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblpp`
--

LOCK TABLES `tblpp` WRITE;
/*!40000 ALTER TABLE `tblpp` DISABLE KEYS */;
INSERT INTO `tblpp` VALUES (10,11,1),(10,13,1),(10,14,1),(10,15,2),(10,18,1),(10,19,1),(11,13,2),(11,14,2),(11,15,2),(12,13,2),(12,15,1),(13,16,1),(14,17,1),(14,18,2);
/*!40000 ALTER TABLE `tblpp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tblturn`
--

DROP TABLE IF EXISTS `tblturn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tblturn` (
  `matchid` int NOT NULL,
  `turnid` int NOT NULL,
  `playerid` int NOT NULL,
  `card` varchar(45) NOT NULL,
  PRIMARY KEY (`matchid`,`turnid`),
  KEY `turn_player_idx` (`playerid`),
  CONSTRAINT `turn_match` FOREIGN KEY (`matchid`) REFERENCES `tblmatch` (`id`),
  CONSTRAINT `turn_player` FOREIGN KEY (`playerid`) REFERENCES `tblplayer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tblturn`
--

LOCK TABLES `tblturn` WRITE;
/*!40000 ALTER TABLE `tblturn` DISABLE KEYS */;
INSERT INTO `tblturn` VALUES (11,1,10,'302'),(11,2,10,'404'),(11,3,10,'150'),(11,4,10,'404'),(11,5,10,'010'),(11,6,10,'060'),(11,7,10,'360'),(11,8,10,'404'),(13,1,10,'290'),(13,2,11,'390'),(13,3,12,'302'),(13,4,10,'370'),(13,5,12,'404'),(13,6,11,'370'),(13,7,10,'310'),(13,8,12,'350'),(13,9,11,'050'),(13,10,10,'002'),(13,11,12,'102'),(13,12,10,'302'),(13,13,12,'404'),(13,14,10,'050'),(13,15,11,'080'),(13,16,12,'180'),(13,17,10,'100'),(13,18,11,'140'),(13,19,12,'150'),(13,20,10,'250'),(13,21,11,'240'),(13,22,12,'280'),(13,23,10,'240'),(13,24,11,'220'),(13,25,12,'201'),(13,26,11,'203'),(13,27,12,'260'),(13,28,10,'201'),(13,29,12,'230'),(13,30,10,'270'),(13,31,11,'070'),(13,32,12,'030'),(13,33,10,'020'),(13,34,11,'010'),(13,35,12,'020'),(13,36,10,'040'),(13,37,11,'405'),(13,38,12,'160'),(13,39,10,'190'),(13,40,11,'120'),(13,41,12,'130'),(13,42,10,'030'),(13,43,11,'090'),(13,44,12,'404'),(13,45,10,'390'),(13,46,11,'303'),(13,47,12,'320'),(13,48,10,'301'),(13,49,12,'360'),(13,50,10,'405'),(13,51,11,'101'),(13,52,10,'001'),(13,53,12,'060'),(13,54,10,'003'),(13,55,11,'040'),(13,56,12,'404'),(14,1,10,'302'),(14,2,11,'390'),(14,3,10,'190'),(14,4,11,'405'),(14,5,10,'160'),(14,6,11,'404'),(14,7,10,'040'),(14,8,11,'003'),(14,9,10,'203'),(14,10,11,'260'),(14,11,10,'270'),(14,12,11,'250'),(14,13,10,'201'),(14,14,10,'001'),(14,15,10,'080'),(14,16,11,'180'),(14,17,10,'140'),(14,18,11,'190'),(14,19,10,'180'),(14,20,11,'130'),(14,21,10,'103'),(14,22,11,'150'),(14,23,10,'120'),(14,24,11,'101'),(14,25,11,'301'),(14,26,11,'350'),(14,27,10,'330'),(14,28,11,'370'),(14,29,10,'310'),(14,30,11,'320'),(14,31,10,'405'),(14,32,11,'290'),(14,33,10,'202'),(14,34,11,'250'),(14,35,10,'230'),(14,36,11,'260'),(14,37,10,'230'),(14,38,11,'201'),(14,39,11,'290'),(14,40,10,'220'),(14,41,11,'120'),(14,42,10,'102'),(14,43,11,'002'),(14,44,10,'010'),(14,45,11,'090'),(14,46,10,'070'),(14,47,11,'020'),(14,48,10,'320'),(14,49,11,'303'),(14,50,10,'303'),(14,51,11,'301'),(14,52,11,'300'),(14,53,10,'100'),(14,54,11,'130'),(14,55,10,'110'),(14,56,11,'210'),(14,57,10,'200'),(14,58,11,'280'),(14,59,10,'380'),(14,60,11,'310'),(14,61,10,'302'),(14,62,11,'404'),(15,1,11,'180'),(15,2,10,'110'),(15,3,12,'101'),(15,4,10,'301'),(15,5,11,'380'),(15,6,10,'330'),(15,7,12,'370'),(15,8,11,'170'),(15,9,10,'110'),(15,10,12,'310'),(15,11,11,'210'),(15,12,10,'240'),(15,13,12,'290'),(15,14,11,'405'),(15,15,10,'000'),(15,16,12,'100'),(15,17,11,'150'),(15,18,10,'160'),(15,19,12,'102'),(15,20,11,'002'),(15,21,12,'404'),(15,22,11,'060'),(15,23,10,'001'),(15,24,11,'080'),(15,25,10,'002'),(15,26,12,'050'),(15,27,10,'090'),(15,28,11,'390'),(15,29,12,'390'),(15,30,10,'340'),(15,31,11,'350'),(15,32,12,'380'),(15,33,10,'310'),(15,34,11,'303'),(15,35,12,'303'),(15,36,10,'203'),(15,37,11,'230'),(15,38,12,'280'),(15,39,10,'202'),(15,40,11,'270'),(15,41,10,'220'),(15,42,12,'290'),(15,43,11,'260'),(15,44,10,'160'),(15,45,12,'150'),(15,46,11,'140'),(15,47,10,'190'),(15,48,12,'090'),(15,49,11,'010'),(15,50,10,'003'),(15,51,12,'030'),(15,52,11,'003'),(15,53,10,'010'),(15,54,12,'210'),(15,55,11,'260'),(15,56,10,'250'),(15,57,12,'201'),(15,58,10,'001'),(15,59,11,'070'),(15,60,10,'080'),(16,1,13,'404'),(16,2,13,'080'),(16,3,13,'404'),(16,4,13,'160'),(16,5,13,'360'),(16,6,13,'330'),(16,7,13,'320'),(16,8,13,'220'),(17,1,14,'404'),(17,2,14,'230'),(17,3,14,'201'),(17,4,14,'301'),(17,5,14,'201'),(17,6,14,'202'),(17,7,14,'002'),(17,8,14,'090'),(17,9,14,'040'),(17,10,14,'000'),(17,11,14,'020'),(17,12,14,'060'),(17,13,14,'003'),(17,14,14,'080'),(17,15,14,'380'),(17,16,14,'390'),(17,17,14,'340'),(17,18,14,'310'),(17,19,14,'303'),(17,20,14,'330'),(17,21,14,'130'),(17,22,14,'170'),(17,23,14,'101'),(17,24,14,'140'),(17,25,14,'103'),(17,26,14,'110'),(17,27,14,'190'),(17,28,14,'290'),(17,29,14,'210'),(17,30,14,'404'),(17,31,14,'030'),(17,32,14,'020'),(17,33,14,'030'),(17,34,14,'330'),(17,35,14,'380'),(18,1,14,'404'),(18,2,14,'230'),(18,3,14,'201'),(18,4,14,'301'),(18,5,14,'201'),(18,6,14,'202'),(18,7,14,'002'),(18,8,14,'090'),(18,9,14,'040'),(18,10,14,'000'),(18,11,14,'020'),(18,12,14,'060'),(18,13,14,'003'),(18,14,14,'080'),(18,15,14,'380'),(18,16,14,'390'),(18,17,14,'340'),(18,18,14,'310'),(18,19,14,'303'),(18,20,14,'330'),(18,21,14,'130'),(18,22,14,'170'),(18,23,14,'101'),(18,24,14,'140'),(18,25,14,'103'),(18,26,14,'110'),(18,27,14,'190'),(18,28,14,'290'),(18,29,14,'210'),(18,30,14,'404'),(18,31,14,'030'),(18,32,14,'020'),(18,33,14,'030'),(18,34,14,'330'),(18,35,14,'380'),(18,36,14,'340'),(18,37,10,'303'),(18,38,14,'003'),(18,39,10,'001'),(18,40,10,'030'),(18,41,14,'010'),(18,42,10,'070'),(18,43,14,'030'),(18,44,10,'405'),(18,45,14,'200'),(18,46,10,'100'),(18,47,14,'110'),(18,48,10,'150'),(18,49,14,'190'),(18,50,10,'160'),(18,51,14,'404'),(18,52,10,'360'),(18,53,14,'340'),(18,54,10,'301'),(18,55,10,'201'),(18,56,10,'260'),(18,57,14,'202'),(18,58,10,'290'),(18,59,14,'090'),(18,60,10,'390'),(18,61,14,'302'),(19,1,10,'404'),(19,2,10,'160'),(19,3,10,'110'),(19,4,10,'404'),(19,5,10,'203'),(19,6,10,'200'),(19,7,10,'270'),(19,8,10,'220'),(19,9,10,'020'),(19,10,10,'002'),(19,11,10,'050'),(19,12,10,'002'),(19,13,10,'070'),(19,14,10,'030'),(19,15,10,'330'),(19,16,10,'301'),(19,17,10,'303'),(19,18,10,'390'),(19,19,10,'090'),(19,20,10,'001'),(19,21,10,'404'),(19,22,10,'150');
/*!40000 ALTER TABLE `tblturn` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-15 22:32:51
