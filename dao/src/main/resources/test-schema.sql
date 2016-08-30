-- #development #IDEA
-- Make sure your connection now
-- is to database "crm-crius" !

CREATE TABLE stage_deals (
  id INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  deleted BOOLEAN,
  PRIMARY KEY (id));



CREATE TABLE language (
  id INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  code CHAR(2) NOT NULL,
  deleted BOOLEAN,
  PRIMARY KEY (id));



CREATE TABLE user (
  id IDENTITY NOT NULL,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(320) NOT NULL,
  password VARCHAR(32) NOT NULL,
  is_admin BOOLEAN,
  phone VARCHAR(45),
  mobile_phone VARCHAR(45),
  note VARCHAR(300),
  deleted BOOLEAN,
  image VARBINARY(256000),
  url VARCHAR(255),
  language_id INT NOT NULL,
    FOREIGN KEY (language_id)
    REFERENCES language (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE company (
  id IDENTITY NOT NULL,
  name VARCHAR(200) NOT NULL,
  phone VARCHAR(45) NOT NULL,
  email VARCHAR(320) NOT NULL,
  address VARCHAR(200) NOT NULL,
  responsible_user_id INT NOT NULL,
  web VARCHAR(255),
  deleted BOOLEAN,
  created_by_id INT NOT NULL,
  date_create TIMESTAMP,
    FOREIGN KEY (responsible_user_id)
    REFERENCES user (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (created_by_id)
    REFERENCES user (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE contact (
  id IDENTITY NOT NULL,
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
    FOREIGN KEY (responsible_user_id)
    REFERENCES user (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (created_by_id)
    REFERENCES user (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (company_id)
    REFERENCES company (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE deal (
  id IDENTITY NOT NULL,
  name VARCHAR(200) NOT NULL,
  stage_id INT NOT NULL,
  responsible_user_id INT,
  amount DECIMAL(20,2),
  company_id INT NOT NULL,
  deleted BOOLEAN,
  date_create TIMESTAMP,
  created_by_id INT NOT NULL,
    FOREIGN KEY (stage_id)
    REFERENCES stage_deals (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (responsible_user_id)
    REFERENCES user (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (company_id)
    REFERENCES company (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (created_by_id)
    REFERENCES user (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE task_type (
  id INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  deleted BOOLEAN,
  PRIMARY KEY (id));



CREATE TABLE task_status (
  id INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  deleted BOOLEAN,
  PRIMARY KEY (id));



CREATE TABLE task (
  id IDENTITY NOT NULL,
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
    FOREIGN KEY (responsible_user_id)
    REFERENCES user (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (created_by_id)
    REFERENCES user (id)
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



CREATE TABLE note (
  id IDENTITY NOT NULL,
  note VARCHAR(500),
  created_by_id INT NOT NULL,
  date_create TIMESTAMP NOT NULL,
  deleted BOOLEAN,
  deal_id INT,
  company_id INT,
  contact_id INT,
    FOREIGN KEY (created_by_id)
    REFERENCES user (id)
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



CREATE TABLE attached_file (
  id IDENTITY NOT NULL,
  created_by_id INT NOT NULL,
  date_create TIMESTAMP NOT NULL,
  filename VARCHAR(100),
  filesize INT,
  deleted BOOLEAN,
  url_file VARCHAR(255) NULL,
  file VARBINARY(20480000),
  contact_id INT,
  company_id INT,
  deal_id INT,
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



CREATE TABLE rights (
  id IDENTITY NOT NULL,
  user_id INT NOT NULL,
  subject_type INT NOT NULL,
  subject_type_create BOOLEAN,
  subject_type_read BOOLEAN,
  subject_type_delete BOOLEAN,
  subject_type_change BOOLEAN,
  subject_type_export BOOLEAN,
  deleted BOOLEAN,
    FOREIGN KEY (user_id)
    REFERENCES user (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



-- Table company_contact is not needed, relation can be made with field company_id
-- in table 'contact'. Contact may be assigned to company, and may be not.
-- But contact can't be assigned to many companies.



CREATE TABLE deal_contact (
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



CREATE TABLE visit_history (
  id IDENTITY NOT NULL,
  user_id INT NOT NULL,
  date_create TIMESTAMP NOT NULL,
  ip_address VARCHAR(45),
  browser VARCHAR(255),
  deleted BOOLEAN,
    FOREIGN KEY (user_id)
    REFERENCES user (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



CREATE TABLE tag (
  id IDENTITY NOT NULL,
  name VARCHAR(1000) NOT NULL,
  deleted BOOLEAN
  );



CREATE TABLE contact_company_tag (
  id IDENTITY NOT NULL,
  contact_id INT,
  tag_id INT NOT NULL,
  company_id INT,
  deleted BOOLEAN,
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



CREATE TABLE deal_tag (
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



CREATE TABLE currency (
  id INT NOT NULL,
  name VARCHAR(45) NOT NULL,
  active BOOLEAN,
  deleted BOOLEAN,
  PRIMARY KEY (id));
