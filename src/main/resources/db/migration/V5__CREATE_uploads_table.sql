CREATE TABLE `uploads` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(120) NULL,
  `applicationId` INT NULL,
  `type` VARCHAR(45) NULL,
  `path` VARCHAR(240) NULL,
  `created` TIMESTAMP NULL  DEFAULT CURRENT_TIMESTAMP,
  `updated` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `upload_application`
    FOREIGN KEY (`applicationId`)
    REFERENCES `applications` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);