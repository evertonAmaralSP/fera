CREATE TABLE `templates` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `description` VARCHAR(150) NULL,
  `application_id` INT NULL,
  `type_id` INT NULL,
  `master_id` INT NULL,
  `path` VARCHAR(120) NULL,
  `document` TEXT NULL,
  `createdAt` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `type_id`
    FOREIGN KEY (`type_id`)
    REFERENCES `template_types` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `application_id`
    FOREIGN KEY (`application_id`)
    REFERENCES `applications` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
