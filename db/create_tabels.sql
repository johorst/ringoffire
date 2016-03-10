CREATE TABLE `mgm_schema`.`transaktionen_geberkonten2nehmerkonten` (
  `sender` TEXT(32) NULL,
  `empfaenger` TEXT(32) NULL,
  `zeitpunkt` DATETIME NOT NULL DEFAULT NOW(),
  `betrag` INT NULL,
  `transaktions_id` INT NOT NULL AUTO_INCREMENT,
  `toggle` BIT(1) NULL DEFAULT 1,
  UNIQUE INDEX `transaktions_id_UNIQUE` (`transaktions_id` ASC),
  PRIMARY KEY (`transaktions_id`));

CREATE TABLE `mgm_schema`.`geberkonten` (
  `nr` TEXT(32) NOT NULL,
  `stand` INT NULL);
  
insert into geberkonten values ('00000000-0000-0000-0000-00000025', 25);
insert into geberkonten values ('12345678-1234-1234-1234-12345678', 2500);
