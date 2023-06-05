/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `userdb`
--
CREATE DATABASE IF NOT EXISTS `userdb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `userdb`;

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role`
(
    `id`   int(11)      NOT NULL,
    `name` varchar(100) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role`
    DISABLE KEYS */;
INSERT INTO `role`
VALUES (1, 'User'),
       (2, 'Admin');
/*!40000 ALTER TABLE `role`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_credential`
--

DROP TABLE IF EXISTS `user_credential`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_credential`
(
    `id`       int(11)      NOT NULL AUTO_INCREMENT,
    `role_id`  int(11)      NOT NULL DEFAULT '0',
    `username` varchar(100) NOT NULL,
    `email`    varchar(100) NOT NULL,
    `password` varchar(100) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `uc_user` UNIQUE (`username`, `email`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

ALTER TABLE userdb.`user_credential`
    MODIFY COLUMN Password LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL;