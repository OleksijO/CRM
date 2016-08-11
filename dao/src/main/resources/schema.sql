-- #development #IDEA
-- Make sure your connection now
-- is to database "crm-crius" !

CREATE SCHEMA IF NOT EXISTS public;
SET SEARCH_PATH TO public;

CREATE TABLE IF NOT EXISTS stage_deals (
  id INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  deleted BOOLEAN,
  PRIMARY KEY (id));



CREATE TABLE IF NOT EXISTS "language" (
  id INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  code CHAR(2) NOT NULL,
  deleted BOOLEAN,
  PRIMARY KEY (id));



CREATE TABLE IF NOT EXISTS "user" (
  id SERIAL NOT NULL,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(320) NOT NULL UNIQUE,
  password VARCHAR(32) NOT NULL,
  is_admin BOOLEAN,
  phone VARCHAR(45),
  mobile_phone VARCHAR(45),
  note VARCHAR(300),
  deleted BOOLEAN,
  image BYTEA,
  url VARCHAR(255),
  language_id INT NOT NULL,
  PRIMARY KEY (id),
    FOREIGN KEY (language_id)
    REFERENCES language (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE IF NOT EXISTS company (
  id SERIAL NOT NULL,
  name VARCHAR(200) NOT NULL,
  phone VARCHAR(45) NOT NULL,
  email VARCHAR(320) NOT NULL,
  address VARCHAR(200) NOT NULL,
  responsible_user_id INT NOT NULL,
  web VARCHAR(255),
  deleted BOOLEAN,
  created_by_id INT NOT NULL,
  date_create TIMESTAMP,
  PRIMARY KEY (id),
    FOREIGN KEY (responsible_user_id)
    REFERENCES "user" (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (created_by_id)
    REFERENCES "user" (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE IF NOT EXISTS contact (
  id SERIAL NOT NULL,
  name VARCHAR(300) NOT NULL,
  responsible_user_id INT NOT NULL,
  position VARCHAR(100),
  type_of_phone INT NOT NULL,
  company_id INT,
  phone VARCHAR(45),
  skype VARCHAR(32),
  email VARCHAR(320),
  deleted BOOLEAN,
  date_create TIMESTAMP,
  created_by_id INT NOT NULL,
  PRIMARY KEY (id),
    FOREIGN KEY (responsible_user_id)
    REFERENCES "user" (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (created_by_id)
    REFERENCES "user" (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (company_id)
    REFERENCES company (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE IF NOT EXISTS deal (
  id SERIAL NOT NULL,
  name VARCHAR(200) NOT NULL,
  stage_id INT NOT NULL,
  responsible_user_id INT,
  amount DECIMAL(20,2),
  company_id INT NOT NULL,
  deleted BOOLEAN,
  date_create TIMESTAMP,
  created_by_id INT NOT NULL,
  PRIMARY KEY (id),
    FOREIGN KEY (stage_id)
    REFERENCES stage_deals (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (responsible_user_id)
    REFERENCES "user" (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (company_id)
    REFERENCES company (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (created_by_id)
    REFERENCES "user" (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE IF NOT EXISTS task_type (
  id INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  deleted BOOLEAN,
  PRIMARY KEY (id));



CREATE TABLE IF NOT EXISTS task_status (
  id INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  deleted BOOLEAN,
  PRIMARY KEY (id));



CREATE TABLE IF NOT EXISTS task (
  id SERIAL NOT NULL,
  period INT NOT NULL,
  responsible_user_id INT NOT NULL,
  task_type_id INT NOT NULL,
  created_by_id INT NOT NULL,
  name VARCHAR(500),
  status_id INT NOT NULL,
  deleted BOOLEAN,
  date_create TIMESTAMP NOT NULL,
  company_id INT,
  contact_id INT,
  deal_id INT,
  PRIMARY KEY (id),
    FOREIGN KEY (responsible_user_id)
    REFERENCES "user" (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (created_by_id)
    REFERENCES "user" (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (task_type_id)
    REFERENCES task_type (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (status_id)
    REFERENCES task_status (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (company_id)
    REFERENCES company (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (contact_id)
    REFERENCES contact (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (deal_id)
    REFERENCES deal (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE IF NOT EXISTS note (
  id SERIAL NOT NULL,
  note VARCHAR(500),
  created_by_id INT NOT NULL,
  date_create TIMESTAMP NOT NULL,
  deleted BOOLEAN,
  deal_id INT,
  company_id INT,
  contact_id INT,
  PRIMARY KEY (id),
    FOREIGN KEY (created_by_id)
    REFERENCES "user" (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (deal_id)
    REFERENCES deal (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (company_id)
    REFERENCES company (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (contact_id)
    REFERENCES contact (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE IF NOT EXISTS attached_file (
  id SERIAL NOT NULL,
  created_by_id INT NOT NULL,
  date_create TIMESTAMP NOT NULL,
  filename VARCHAR(100),
  filesize INT,
  deleted BOOLEAN,
  url_file VARCHAR(255) NULL,
  file BYTEA,
  contact_id INT,
  company_id INT,
  deal_id INT,
  PRIMARY KEY (id),
    FOREIGN KEY (contact_id)
    REFERENCES contact (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (company_id)
    REFERENCES company (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (deal_id)
    REFERENCES deal (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE IF NOT EXISTS rights (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  subject_type INT NOT NULL,
  subject_type_create BOOLEAN,
  subject_type_read BOOLEAN,
  subject_type_delete BOOLEAN,
  subject_type_change BOOLEAN,
  subject_type_export BOOLEAN,
  deleted BOOLEAN,
  PRIMARY KEY (id),
    FOREIGN KEY (user_id)
    REFERENCES "user" (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



-- Table company_contact is not needed, relation can be made with field company_id
-- in table 'contact'. Contact may be assigned to company, and may be not.
-- But contact can't be assigned to many companies.



CREATE TABLE IF NOT EXISTS deal_contact (
  deal_id INT NOT NULL,
  contact_id INT NOT NULL,
  PRIMARY KEY (deal_id, contact_id),
    FOREIGN KEY (deal_id)
    REFERENCES deal (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (contact_id)
    REFERENCES contact (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE IF NOT EXISTS visit_history (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  date_create TIMESTAMP NOT NULL,
  ip_address VARCHAR(45),
  browser VARCHAR(255),
  deleted BOOLEAN,
  PRIMARY KEY (id),
    FOREIGN KEY (user_id)
    REFERENCES "user" (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE IF NOT EXISTS tag (
  id SERIAL NOT NULL,
  name VARCHAR(1000) NOT NULL,
  deleted BOOLEAN,
  PRIMARY KEY (id));



CREATE TABLE IF NOT EXISTS contact_company_tag (
  id SERIAL NOT NULL,
  contact_id INT,
  tag_id INT NOT NULL,
  company_id INT,
  deleted BOOLEAN,
  PRIMARY KEY (id),
    FOREIGN KEY (contact_id)
    REFERENCES contact (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (tag_id)
    REFERENCES tag (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (company_id)
    REFERENCES company (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE IF NOT EXISTS deal_tag (
  deal_id INT NOT NULL,
  tag_id INT NOT NULL,
  PRIMARY KEY (deal_id, tag_id),
    FOREIGN KEY (deal_id)
    REFERENCES deal (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (tag_id)
    REFERENCES tag (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE IF NOT EXISTS currency (
  id INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  active BOOLEAN,
  deleted BOOLEAN,
  PRIMARY KEY (id));
