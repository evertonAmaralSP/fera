CREATE TABLE `templates` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(150) NULL,
  `product_id` INT NULL,
  `type_id` INT NULL,
  `source_id` INT NULL,
  `master_id` INT NULL,
  `path` VARCHAR(120) NULL,
  `document` TEXT NULL,
  `document_draft` TEXT NULL,
  `lastUpdateDateAvailablePooling` TIMESTAMP NULL,
  `lastUpdateDateUpdatePooling` TIMESTAMP NULL,
  `createdAt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `type_id`
    FOREIGN KEY (`type_id`)
    REFERENCES `template_types` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `product_id`
    FOREIGN KEY (`product_id`)
    REFERENCES `products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `sourceTemplateId`
    FOREIGN KEY (`source_id`)
    REFERENCES `sources` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);