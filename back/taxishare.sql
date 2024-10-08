-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema taxishare
-- -----------------------------------------------------
-- taxishare

-- -----------------------------------------------------
-- Schema taxishare
--
-- taxishare
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `taxishare` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
USE `taxishare` ;

-- -----------------------------------------------------
-- Table `taxishare`.`USER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `taxishare`.`USER` (
  `USER_NO` INT NOT NULL AUTO_INCREMENT,
  `USER_ID` VARCHAR(45) NOT NULL,
  `USER_PASSWORD` VARCHAR(255) NOT NULL,
  `USER_NAME` VARCHAR(45) NOT NULL,
  `USER_TYPE` INT NOT NULL DEFAULT 1,
  `PHONE_NUM` VARCHAR(45) NOT NULL,
  `USER_SEX` CHAR NOT NULL,
  PRIMARY KEY (`USER_NO`),
  UNIQUE INDEX `USER_ID_UNIQUE` (`USER_ID` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `taxishare`.`USER_TOKEN`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `taxishare`.`USER_TOKEN` (
  `TOKEN_NO` INT NOT NULL AUTO_INCREMENT,
  `USER_NO` INT NOT NULL,
  `TOKEN` VARCHAR(255) NULL,
  `TOKEN_TYPE` VARCHAR(45) NOT NULL,
  `CREATED_AT` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `EXPIRES_AT` TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP + INTERVAL 1 DAY),
  `REVOKED` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`TOKEN_NO`),
  INDEX `USER_NO_idx` (`USER_NO` ASC) VISIBLE,
  CONSTRAINT `USER_NO`
    FOREIGN KEY (`USER_NO`)
    REFERENCES `taxishare`.`USER` (`USER_NO`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `taxishare`.`ROUTES`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `taxishare`.`ROUTES` (
  `ROUTE_NO` INT NOT NULL AUTO_INCREMENT,
  `RT_USER_NO` INT NOT NULL,
  `ORIGIN` VARCHAR(255) NOT NULL,
  `ORIGIN_LATITUDE` DOUBLE NULL,
  `ORIGIN_LONGITUDE` DOUBLE NULL,
  `DESTINATION` VARCHAR(255) NOT NULL,
  `DESTINATION_LATITUDE` DOUBLE NULL,
  `DESTINATION_LONGITUDE` DOUBLE NULL,
  `DISTANCE_M` INT NULL,
  `START_TIME` TIMESTAMP NULL,
  `END_TIME` TIMESTAMP NULL,
  `FARE` INT NULL,
  `TOLL` INT NULL,
  `PAID_FARE` INT NULL,
  `STATUS` INT NOT NULL DEFAULT 0,
  `PERSON_CNT` INT NOT NULL DEFAULT 1,
  PRIMARY KEY (`ROUTE_NO`),
  INDEX `RT_USER_NO_idx` (`RT_USER_NO` ASC) VISIBLE,
  CONSTRAINT `RT_USER_NO`
    FOREIGN KEY (`RT_USER_NO`)
    REFERENCES `taxishare`.`USER` (`USER_NO`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `taxishare`.`ROUTE_HISTORY`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `taxishare`.`ROUTE_HISTORY` (
  `ROUTE_HISTORY_NO` INT NOT NULL AUTO_INCREMENT,
  `ROUTE_NO1` INT NOT NULL,
  `ROUTE_NO2` INT NOT NULL,
  `START_TIME1` TIMESTAMP NULL,
  `START_TIME2` TIMESTAMP NULL,
  `END_TIME1` TIMESTAMP NULL,
  `END_TIME2` TIMESTAMP NULL,
  `ORIGIN1` VARCHAR(255) NOT NULL,
  `ORIGIN2` VARCHAR(255) NOT NULL,
  `DESTINATION1` VARCHAR(255) NOT NULL,
  `DESTINATION2` VARCHAR(255) NOT NULL,
  `TOTAL_M` INT NULL,
  `TOTAL_FARE` INT NULL,
  `STATUS` INT NOT NULL DEFAULT 0,
  `DRIVER_NO` INT NULL,
  PRIMARY KEY (`ROUTE_HISTORY_NO`),
  INDEX `ROUTE_NO_idx` (`ROUTE_NO1` ASC) VISIBLE,
  INDEX `ROUTE_NO2_idx` (`ROUTE_NO2` ASC) VISIBLE,
  INDEX `DRIVER_NO_idx` (`DRIVER_NO` ASC) VISIBLE,
  CONSTRAINT `ROUTE_NO1`
    FOREIGN KEY (`ROUTE_NO1`)
    REFERENCES `taxishare`.`ROUTES` (`ROUTE_NO`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `ROUTE_NO2`
    FOREIGN KEY (`ROUTE_NO2`)
    REFERENCES `taxishare`.`ROUTES` (`ROUTE_NO`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `DRIVER_NO`
    FOREIGN KEY (`DRIVER_NO`)
    REFERENCES `taxishare`.`USER` (`USER_NO`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
