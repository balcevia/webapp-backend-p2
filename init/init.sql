CREATE TABLE IF NOT EXISTS `USERS` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `EMAIL` VARCHAR(64) NOT NULL,
  `PASSWORD` VARCHAR(128) NOT NULL,
  `SALT` VARCHAR(128) NOT NULL,
  `INVALID_LOGIN_COUNT` INT NOT NULL DEFAULT 0,
  `ROLE` VARCHAR(16) NOT NULL,
  PRIMARY KEY (`ID`)
);

CREATE TABLE IF NOT EXISTS `USER_DETAILS` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `USER_ID` INT NOT NULL,
  `NAME` VARCHAR(64) NOT NULL,
  `SURNAME` VARCHAR(64) NOT NULL,
  `DATE_OF_BIRTH` VARCHAR(12) NOT NULL,
  `STREET_ADDRESS` VARCHAR(128) NOT NULL,
  `BUILDING_NUMBER` VARCHAR(12) NOT NULL,
  `POSTAL_CODE` VARCHAR(12) NOT NULL,
  `COUNTRY` VARCHAR(32) NOT NULL,
  `PESEL` VARCHAR(11) NOT NULL,
  PRIMARY KEY (`ID`)
);

CREATE TABLE IF NOT EXISTS `PACKAGES` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `USER_ID` INT NOT NULL,
  `COURIER_ID` INT,
  `SENDER_NAME` VARCHAR(64) NOT NULL,
  `SENDER_SURNAME` VARCHAR(64) NOT NULL,
  `SENDER_ADDRESS` VARCHAR(128) NOT NULL,
  `SENDER_PHONE_NUMBER` VARCHAR(32) NOT NULL,
  `RECEIVER_NAME` VARCHAR(64) NOT NULL,
  `RECEIVER_SURNAME` VARCHAR(64) NOT NULL,
  `RECEIVER_ADDRESS` VARCHAR(128) NOT NULL,
  `RECEIVER_PHONE_NUMBER` VARCHAR(32) NOT NULL,
  `CREATION_DATE` VARCHAR(16) NOT NULL,
  `IMAGE` LONGTEXT NOT NULL,
  `ATTACHMENT_PATH` VARCHAR(64),
  `STATUS` VARCHAR(32) NOT NULL DEFAULT 'new',
  PRIMARY KEY (`ID`)
);

INSERT INTO `USERS` (`EMAIL`, `PASSWORD`, `SALT`, `INVALID_LOGIN_COUNT`, `ROLE`) VALUES
('test.courier1@gmail.com',	'F7640A70D571DBE2A3926EB030A63E04',	'08090EDC3E6D86E3C1C4FB2E2F24C82F',	0,	'courier'),
('test.courier2@gmail.com',	'F7640A70D571DBE2A3926EB030A63E04',	'08090EDC3E6D86E3C1C4FB2E2F24C82F',	0,	'courier');

-- Password is Alamakota1@