SET SEARCH_PATH TO public;

INSERT INTO language VALUES
  (1, 'Українська', 'UA', FALSE),
  (2, 'Русский', 'RU', FALSE),
  (3, 'English', 'EN', FALSE);

INSERT INTO "user" VALUES
  (DEFAULT, 'Геннадий Петров', 'petrov@adiddas.com.ua', '456', FALSE, '(044) 466-45-46',
            '+380974563232', 'some note', FALSE, DEFAULT, 'http://www.someurl.com/someurl', 2),
  (DEFAULT, 'Админис Тратор', 'admin@adiddas.com.ua', '555', TRUE, '+380441234321',
            '+380970012332', 'first note', FALSE, DEFAULT, 'http://www.adiddas.com.ua/contacts/admin', 1),
  (DEFAULT, 'Иван Иванов', 'ivan@unknown.net', '111', TRUE, '+380443333333',
            '+380973333333', 'this note belongs Ivan', FALSE, DEFAULT, 'http://www.unknown.net', 3);

INSERT INTO company VALUES
  (DEFAULT, 'Adiddas LTD', '(044)5888-52-85', 'adiddas@adiddas.com.ua', 'Киев, ул. Малышка, 1', 1, 'http://www.adiddas.com.ua', FALSE,
   1, '2015-02-18 15:36:38'),
  (DEFAULT, 'Le Staro', '+380111111111', 'info@lestaro.com.tw', 'Somewhere in Asia, 1', 1, 'http://www.lestaro.com.tw', FALSE,
   2, '2015-08-11 05:22:12');

INSERT INTO currency VALUES
  (1, 'грн', TRUE, FALSE),
  (2, 'usd', TRUE, FALSE),
  (3, 'euro', FALSE, FALSE);

INSERT INTO contact VALUES
  (DEFAULT, 'Володимир Грицків', 1, 'junior java developer', 1, 1, '+380505656545', 'gryts.vol', 'some@ukr.net', FALSE,
            '2015-12-16 20:38:40', 1),
  (DEFAULT, 'Майкл Щур', 1, 'director', 1, 1, '+380505585957', 'michael.shur', 'michael@ukr.net',
            FALSE, '2016-02-16 15:16:44', 1),
  (DEFAULT, 'Просто Герасим', 3, 'sales manager', 3, 2, '+380503333333', 'skayp-gerasima', 'geras@idiotov.net',
            FALSE, '2016-01-02 09:00:00', 2);

INSERT INTO stage_deals VALUES
  (1, 'Рассмотрение', FALSE),
  (2, 'Оформление', FALSE),
  (3, 'В ожидании', FALSE),
  (4, 'Закрыто', FALSE);

INSERT INTO deal VALUES
  (DEFAULT, 'small deal', 3, 1, 2500.25, 1, FALSE, '2016-02-11 10:26:44', 1),
  (DEFAULT, 'deal-2 (крепеж 2 палеты)', 2, 3, 15330, 2, FALSE, '2016-03-11 00:00:40', 3),
  (DEFAULT, 'big deal', 1, 1, 12500, 1, FALSE, '2016-02-11 10:26:44', 1);

INSERT INTO note VALUES
  (DEFAULT, 'some note some some note', 1, '2016-02-11 10:26:44', FALSE, 1, NULL, NULL),
  (DEFAULT, 'some second note about the same', 1, '2016-02-15 11:46:42', FALSE, NULL, 1, NULL),
  (DEFAULT, 'some third note about the some other', 1, '2016-03-15 12:16:01', FALSE, NULL, NULL, 1);

INSERT INTO rights VALUES
  (DEFAULT, 1, 0, TRUE, TRUE, FALSE, TRUE, FALSE, FALSE),
  (DEFAULT, 2, 0, TRUE, TRUE, FALSE, TRUE, TRUE, FALSE),
  (DEFAULT, 3, 0, TRUE, FALSE, TRUE, FALSE, TRUE, FALSE),
  (DEFAULT, 1, 1, TRUE, TRUE, TRUE, TRUE, TRUE, FALSE),
  (DEFAULT, 2, 1, FALSE, TRUE, TRUE, FALSE, FALSE, FALSE),
  (DEFAULT, 3, 1, TRUE, TRUE, TRUE, TRUE, TRUE, FALSE),
  (DEFAULT, 1, 2, TRUE, TRUE, TRUE, TRUE, TRUE, FALSE),
  (DEFAULT, 2, 2, FALSE, TRUE, FALSE, TRUE, TRUE, FALSE),
  (DEFAULT, 3, 2, FALSE, TRUE, TRUE, TRUE, TRUE, FALSE);

INSERT INTO tag VALUES
  (DEFAULT, '#general-01', FALSE),
  (DEFAULT, '#general-02', FALSE),
  (DEFAULT, '#special-01', FALSE);

INSERT INTO task_type VALUES
  (1, 'Важно', FALSE),
  (2, 'Срочно', FALSE),
  (3, 'Работка', FALSE);

INSERT INTO task_status VALUES
  (1, 'В работе', FALSE),
  (2, 'Выполнено', FALSE),
  (3, 'Приостановлено', FALSE);

INSERT INTO task VALUES
  (DEFAULT, 2, 1, 2, 1, 'Выяснить ситуацию', 2, FALSE, '2016-02-11 10:26:44', 1, NULL, NULL),
  (DEFAULT, 3, 2, 1, 2, 'Выяснить ситуацию тоже', 1, FALSE, '2016-02-11 10:26:44', NULL, 1, NULL),
  (DEFAULT, 4, 1, 3, 1, 'Выяснить и эту ситуацию', 1, FALSE, '2016-02-11 10:26:44', NULL, NULL, 1);

INSERT INTO visit_history VALUES
  (DEFAULT, 1, '2016-02-11 10:26:44', '192.168.0.100',
   'Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 OPR/35.0.2066.92', FALSE),
  (DEFAULT, 1, '2016-02-11 10:28:44', '192.168.0.102',
   'Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 OPR/35.0.2066.92', FALSE);

INSERT INTO attached_file VALUES
  (DEFAULT, 1, '2016-02-11 10:36:44', 'document.png', 156882, FALSE, './files/1/document.png', DEFAULT, NULL, 1, NULL);

INSERT INTO contact_company_tag VALUES
  (DEFAULT, 1, 3, NULL, FALSE);

INSERT INTO deal_contact VALUES
  (1, 1),
  (1, 2);
