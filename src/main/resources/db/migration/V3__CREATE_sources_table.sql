CREATE TABLE `sources` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(150) NULL,
  `source` VARCHAR(254) NULL,
  `productId` INT NULL,
  `active` TINYINT(1) NULL DEFAULT 0,
  `created` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  INDEX `productId_idx` (`productId` ASC),
  CONSTRAINT `productId`
    FOREIGN KEY (`productId`)
    REFERENCES `products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
