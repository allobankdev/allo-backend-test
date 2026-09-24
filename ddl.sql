CREATE TABLE `persons`(
	`id` VARCHAR(255) NOT NULL PRIMARY KEY,
	`name` VARCHAR(255) NOT NULL,
	`email` VARCHAR(255) NOT NULL UNIQUE,
	`created_at` BIGINT UNSIGNED NOT NULL
);

CREATE TABLE `groups`(
	`id` VARCHAR(255) NOT NULL PRIMARY KEY,
	`name` VARCHAR(255) NOT NULL,
	`description` TEXT,
	`created_at` BIGINT UNSIGNED NOT NULL,
	`deleted` TINYINT UNSIGNED NOT NULL
);

CREATE TABLE `group_members`(
	`id` VARCHAR(255) NOT NULL PRIMARY KEY,
	`person_id` VARCHAR(255) NOT NULL, 
	`group_id` VARCHAR(255) NOT NULL,
	`added_at` BIGINT UNSIGNED NOT NULL,
	FOREIGN KEY (`person_id`) REFERENCES `persons`(`id`),
	FOREIGN KEY (`group_id`) REFERENCES `groups`(`id`),
	CONSTRAINT `uk_group_person` UNIQUE (`group_id`, `person_id`)
);

CREATE TABLE `idempotency_keys`(
	`id` VARCHAR(255) NOT NULL PRIMARY KEY,
	`idempotency_key` VARCHAR(255) UNIQUE NOT NULL,
	`hash_request` VARCHAR(255) NOT NULL, 
	`response` TEXT, 
	`created_at` BIGINT UNSIGNED NOT NULL
);

CREATE TABLE `expenses` (
	`id` VARCHAR(255) NOT NULL PRIMARY KEY,
	`group_id` VARCHAR(255) NOT NULL,
	`paid_by` VARCHAR(255) NOT NULL,
	`category_id` VARCHAR(255) NOT NULL,
	`description` TEXT NOT NULL,
	`amount` DECIMAL(19,2) NOT NULL,
	`split_type` VARCHAR(20) NOT NULL,
	`created_at` BIGINT UNSIGNED NOT NULL,
	FOREIGN KEY (`paid_by`) REFERENCES `persons`(`id`),
	FOREIGN KEY (`group_id`) REFERENCES `groups`(`id`),
	FOREIGN KEY (`category_id`) REFERENCES `expense_categories`(`id`)
);

CREATE TABLE `expense_categories` (
	`id` VARCHAR(255) NOT NULL PRIMARY KEY,
	`name` VARCHAR(255) NOT NULL,
	`created_at` BIGINT UNSIGNED NOT NULL,
	`deleted` TINYINT UNSIGNED NOT NULL
);

CREATE TABLE `expense_shares` (
	`id` VARCHAR(255) NOT NULL PRIMARY KEY,
	`expense_id` VARCHAR(255) NOT NULL,
	`person_id` VARCHAR(255) NOT NULL,
	`amount` DECIMAL(19,2) NOT NULL,
	FOREIGN KEY (`person_id`) REFERENCES `persons`(`id`),
	FOREIGN KEY (`expense_id`) REFERENCES `expenses`(`id`),
	CONSTRAINT `uk_expense_person` UNIQUE (`expense_id`, `person_id`)
);

CREATE TABLE payments (
	`id` VARCHAR(255) NOT NULL PRIMARY KEY,
	`group_id` VARCHAR(255) NOT NULL,
	`from_person_id` VARCHAR(255) NOT NULL,
	`to_person_id` VARCHAR(255) NOT NULL,
	`amount` DECIMAL(19,2) NOT NULL,
	`paid_at` BIGINT UNSIGNED NOT NULL,
	`created_at` BIGINT UNSIGNED NOT NULL,
	FOREIGN KEY (`group_id`) REFERENCES `groups`(`id`),
	FOREIGN KEY (`from_person_id`) REFERENCES `persons`(`id`),
	FOREIGN KEY (`to_person_id`) REFERENCES `persons`(`id`),
	CONSTRAINT chk_payment_amount CHECK (amount > 0),
	CONSTRAINT chk_payment_different_people CHECK (from_person_id <> to_person_id)
);
