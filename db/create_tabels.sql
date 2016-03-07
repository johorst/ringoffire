CREATE TABLE `mgm_schema`.`transaktionen_geberkonten2nehmerkonten` (
  `sender` TEXT(32) NULL,
  `empfaenger` TEXT(32) NULL,
  `zeitpunkt` DATETIME NOT NULL DEFAULT NOW(),
  `betrag` INT NULL,
  `transaktions_id` INT ZEROFILL NOT NULL DEFAULT 0,
  `valid` BINARY(1) NULL DEFAULT 1,
  UNIQUE INDEX `transaktions_id_UNIQUE` (`transaktions_id` ASC),
  PRIMARY KEY (`transaktions_id`));

  
CREATE TABLE `mgm_schema`.`geberkonten` (
  `nr` TEXT(32) NOT NULL,
  `stand` INT NULL);

