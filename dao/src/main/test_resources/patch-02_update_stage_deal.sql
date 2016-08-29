UPDATE stage_deals SET name ='Первичный контакт' WHERE name='Рассмотрение';
UPDATE stage_deals SET name ='Переговоры' WHERE name='Оформление';
UPDATE stage_deals SET name ='Принимают решение' WHERE name='В ожидании';
UPDATE stage_deals SET name ='Согласование договора' WHERE name='Закрыто';