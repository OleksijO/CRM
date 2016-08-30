
INSERT INTO language VALUES  (NULL, 'Українська', 'UA', FALSE);
INSERT INTO language VALUES  (NULL, 'Русский', 'RU', FALSE);
INSERT INTO language VALUES  (NULL, 'English', 'EN', FALSE);

INSERT INTO users  VALUES
  (NULL,'Геннадий Петров', 'petrov@adiddas.com.ua', '456', FALSE, '(044) 466-45-46',
            '+380974563232', 'some note', FALSE, NULL, 'http://www.someurl.com/someurl', 1);
INSERT INTO users VALUES
  (NULL,'Админис Тратор', 'admin@adiddas.com.ua', '555', TRUE, '+380441234321',
            '+380970012332', 'first note', FALSE, NULL, 'http://www.adiddas.com.ua/contacts/admin', 0);
INSERT INTO users  VALUES
  (NULL,'Иван Иванов', 'ivan@unknown.net', '111', TRUE, '+380443333333',
            '+380973333333', 'this note belongs Ivan', FALSE, NULL, 'http://www.unknown.net', 2);

INSERT INTO company VALUES
  (NULL, 'Adiddas LTD', '(044)5888-52-85', 'adiddas@adiddas.com.ua', 'Киев, ул. Малышка, 1', 0, 'http://www.adiddas.com.ua', FALSE,
   0, '2015-02-18 15:36:38');
INSERT INTO company VALUES
  (NULL, 'Le Staro', '+380111111111', 'info@lestaro.com.tw', 'Somewhere in Asia, 1', 0, 'http://www.lestaro.com.tw', FALSE,
   1, '2015-08-11 05:22:12');

INSERT INTO currency VALUES
  (NULL , 'грн', TRUE, FALSE);
INSERT INTO currency VALUES
  (NULL, 'usd', TRUE, FALSE);
INSERT INTO currency VALUES
  (NULL, 'euro', FALSE, FALSE);

INSERT INTO contact VALUES
  (1,'Володимир Грицків', 0, 'junior java developer', 1, 0, '+380505656545', 'gryts.vol', 'some@ukr.net', FALSE,
            '2015-12-16 20:38:40', 1);
INSERT INTO contact VALUES
  (2,'Майкл Щур', 0, 'director', 1, 0, '+380505585957', 'michael.shur', 'michael@ukr.net',
            FALSE, '2016-02-16 15:16:44', 0);
INSERT INTO contact  VALUES
  (3, 'Просто Герасим', 2, 'sales manager', 3, 1, '+380503333333', 'skayp-gerasima', 'geras@idiotov.net',
            FALSE, '2016-01-02 09:00:00', 1);

INSERT INTO stage_deals VALUES  (NULL , 'Рассмотрение', FALSE);
INSERT INTO stage_deals VALUES  (NULL , 'Оформление', FALSE);
INSERT INTO stage_deals VALUES (NULL , 'В ожидании', FALSE);
INSERT INTO stage_deals VALUES (NULL , 'Закрыто', FALSE);

INSERT INTO deal VALUES
  (NULL , 'small deal', 2, 0, 2500.25, 0, FALSE, '2016-02-11 10:26:44', 0);
INSERT INTO deal VALUES
  (NULL , 'deal-2 (крепеж 2 палеты)', 1, 2, 15330, 1, FALSE, '2016-03-11 00:00:40', 2);
INSERT INTO deal VALUES
  (NULL , 'big deal', 0, 0, 12500, 0, FALSE, '2016-02-11 10:26:44', 0);

INSERT INTO note VALUES
  (NULL , 'some note some some note', 0, '2016-02-11 10:26:44', FALSE, 0, NULL, NULL);
INSERT INTO note VALUES
  (NULL , 'some second note about the same', 0, '2016-02-15 11:46:42', FALSE, NULL, 0, NULL);
INSERT INTO note VALUES
  (NULL , 'some third note about the some other', 0, '2016-03-15 12:16:01', FALSE, NULL, NULL, 0);

INSERT INTO rights VALUES
  (NULL, 0, 0, TRUE, TRUE, FALSE, TRUE, FALSE, FALSE);
INSERT INTO rights VALUES
  (NULL, 1, 0, TRUE, TRUE, FALSE, TRUE, TRUE, FALSE);
INSERT INTO rights VALUES
  (NULL, 2, 0, TRUE, FALSE, TRUE, FALSE, TRUE, FALSE);
INSERT INTO rights VALUES
  (NULL, 0, 1, TRUE, TRUE, TRUE, TRUE, TRUE, FALSE);
INSERT INTO rights VALUES
  (NULL, 1, 1, FALSE, TRUE, TRUE, FALSE, FALSE, FALSE);
INSERT INTO rights VALUES
  (NULL, 2, 1, TRUE, TRUE, TRUE, TRUE, TRUE, FALSE);
INSERT INTO rights VALUES
  (NULL, 0, 2, TRUE, TRUE, TRUE, TRUE, TRUE, FALSE);
INSERT INTO rights VALUES
  (NULL, 1, 2, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE);
INSERT INTO rights VALUES
  (NULL, 2, 2, FALSE, TRUE, TRUE, TRUE, TRUE, FALSE);

INSERT INTO tag VALUES
  (NULL, '#general-01', FALSE);
INSERT INTO tag VALUES
  (NULL, '#general-02', FALSE);
INSERT INTO tag VALUES
  (NULL, '#special-01', FALSE);

INSERT INTO task_type VALUES
  (NULL, 'Важно', FALSE);
INSERT INTO task_type VALUES
  (NULL, 'Срочно', FALSE);
INSERT INTO task_type VALUES
  (NULL, 'Работка', FALSE);

INSERT INTO task_status VALUES
  (NULL, 'В работе', FALSE);
INSERT INTO task_status VALUES
  (NULL, 'Выполнено', FALSE);
INSERT INTO task_status VALUES
  (NULL, 'Приостановлено', FALSE);

INSERT INTO task VALUES
  (NULL, 2, 0, 1, 0, 'Выяснить ситуацию', 1, FALSE, '2016-02-11 10:26:44', 0, NULL, NULL);
INSERT INTO task VALUES
  (NULL, 3, 1, 0, 1, 'Выяснить ситуацию тоже', 0, FALSE, '2016-02-11 10:26:44', NULL, 0, NULL);
INSERT INTO task VALUES
  (NULL, 4, 0, 2, 0, 'Выяснить и эту ситуацию', 0, FALSE, '2016-02-11 10:26:44', NULL, NULL, 0);

INSERT INTO visit_history VALUES
  (NULL, 0, '2016-02-11 10:26:44', '192.168.0.100',
   'Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 OPR/35.0.2066.92', FALSE);
INSERT INTO visit_history VALUES
  (NULL, 0, '2016-02-11 10:28:44', '192.168.0.102',
   'Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 OPR/35.0.2066.92', FALSE);

INSERT INTO attached_file VALUES
  (NULL,0 , '2016-02-11 10:36:44', 'document.png', 156882, FALSE, './files/1/document.png', NULL, NULL, 0, NULL);

INSERT INTO contact_company_tag VALUES
  (NULL, 0, 2, NULL, FALSE);

INSERT INTO deal_contact VALUES
  (0, 0);
INSERT INTO deal_contact VALUES
  (0, 1);
