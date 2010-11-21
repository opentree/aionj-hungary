CREATE  TABLE `spawn` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `world` INT NOT NULL ,
  `templateId` INT NOT NULL ,
  `x` FLOAT NOT NULL ,
  `y` FLOAT NOT NULL ,
  `z` FLOAT NOT NULL ,
  `staticId` INT NOT NULL DEFAULT 0 ,
  `heading` INT NOT NULL DEFAULT 0 ,
  `interval` INT NOT NULL DEFAULT 180 ,
  `nextrespawnTime` VARCHAR(128) NULL DEFAULT '' ,
  `spawnTime` VARCHAR(45) NULL DEFAULT 'ALL' ,
  PRIMARY KEY (`id`) ,
  INDEX `templateId` (`templateId` ASC) );