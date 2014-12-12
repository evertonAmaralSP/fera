CREATE TABLE `sources` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(150) NULL,
  `source` VARCHAR(120) NULL,
  `templateId` INT NULL,
  `productId` INT NULL,
  `lastUpdateDatePooling` TIMESTAMP NULL,
  `lastUpdateIdPooling` VARCHAR(120) NULL,
  `active` TINYINT(1) NULL DEFAULT 0,
  `created` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  INDEX `productId_idx` (`productId` ASC),
  INDEX `templateId_idx` (`templateId` ASC),
  CONSTRAINT `templateId`
    FOREIGN KEY (`templateId`)
    REFERENCES `templates` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `productId`
    FOREIGN KEY (`productId`)
    REFERENCES `products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
