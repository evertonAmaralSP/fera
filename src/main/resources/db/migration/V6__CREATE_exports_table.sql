CREATE TABLE `exports` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(120) NULL,
  `productId` INT NULL,
  `type` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `user` VARCHAR(45) NULL,
  `url` VARCHAR(120) NULL,
  `accesskey` VARCHAR(120) NULL,
  `secretKey` VARCHAR(120) NULL,
  `active` TINYINT(1) NULL DEFAULT 0,
  `path` VARCHAR(240) NULL,
  `created` TIMESTAMP NULL  DEFAULT CURRENT_TIMESTAMP,
  `updated` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `export_product`
    FOREIGN KEY (`productId`)
    REFERENCES `products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);