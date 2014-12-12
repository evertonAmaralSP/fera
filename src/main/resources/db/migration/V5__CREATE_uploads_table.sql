CREATE TABLE `uploads` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(120) NULL,
  `productId` INT NULL,
  `type` VARCHAR(45) NULL,
  `path` VARCHAR(240) NULL,
  `created` TIMESTAMP NULL  DEFAULT CURRENT_TIMESTAMP,
  `updated` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `upload_product`
    FOREIGN KEY (`productId`)
    REFERENCES `products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);