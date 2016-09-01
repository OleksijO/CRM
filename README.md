# spring-framework


Task 1.

1) DataSource должен инициализироваться с помощью Spring в отдельном файле конфигурации -- \spring-framework\controller\src\main\webapp\WEB-INF\datasource.xml

2) Объявить имплементации DAO интерфейса как Spring Beans -- annotation-driven

3) DataSource инжектится в бины с помощью Spring -- in AbstractDAO injects in field dataSource. 

Also here added method protected getConnection(){return dataSource.getConnection();}. 

4) Использовать конфигурацию одновременно c помощью XML и аннотации  --\spring-framework\controller\src\main\webapp\WEB-INF\services.xml


Task 2.

Добавить Spring DI в Service слой, сделав inject DAO имплементаций. -- injected by properties 

Task 3. -- \spring-framework\controller\src\main\webapp\WEB-INF\services.xml

1) Инициализировать два Service Beans с помощью статического фабричного метода в Spring -- только xml

2) Инициализировать два Service Beans с помощью не статического фабричного метода в Spring -- только xml

3) Для двух Service Beans реализовать отложенную инициализацию --xml + annotations


Task 4.

1) Реализовать один бин с scope="singleton" -- companyService

2) Реализовать один бин с scope="prototype" -- contactService

4) Для одного бина реализовать и сконфигурировать init и destroy методы -- dealService


Task 5.

Добавить Spring DI в Controller слой -- SpringBeanAutowiringSupport used.


Task 7.

1) Реализовать внедрение зависимостей через конструктор (на свое усмотрение в DAO, Service или Controller слое) - contactDAO
(abstractDAO не трогал, так как тянет изменение конструкторов потомков)

2) Реализовать внедрение зависимостей через проперти (на свое усмотрение в DAO, Service или Controller слое) -- by XML: UserService -
Interfaces UserDaoUser and LanguageDaoUser were added to implement appropriate setters of Dao.

3) В XML конфигурации реализовать два autowire byName и два byType (на свое усмотрение в DAO, Service или Controller слое)
-- один byName dealService; два byType - companyService, contactService

4) Написать ментору в каких случаях используют @Autowired(required=false)

5) Связать два бина с помощью аннотаций Qualifier("name") -- dealService


Task 9.

Разбить Spring XML Context на несколько файлов
Сделать импорт всех файлов в главный файл конфигурации
--- импорт xml по цепочке datasource->dao->service-> application-context <-model


Task 13. Перевести CompanyDAOImpl на JdbcTemplate --готово


Task 14. Переписать Unit тесты на SpringJUnit4ClassRunner.class

--готово. Переведено на реальную тестовую базу.


Task 12. Перевести ContactDAOImpl на JdbcTemplate -- готово


Task 11. Перевести DealDAOImpl DAO на JdbcTemplate -- готово


Task 10. Кофигурация Spring JdbcTemplate

Подключить datasource из jndi:

<jee:jndi-lookup id="dataSource"
        jndi-name="java:comp/env/jdbc/datasource"/> ----- готово.

Сконфигурировать встроеную БД HSQL:

<jdbc:embedded-database id="dataSource">
        <jdbc:script location="schema.sql"/>
        <jdbc:script location="test-data.sql"/>
    </jdbc:embedded-database>

Убедить что база данных успешно создаться и наполняется тестовыми данными --наполняется, но тесты валятся


Task 16. Добавить аспекты

--done


Task 20. Подключение Spring transaction

3) Объявить все Service класса транзакционными, --done

в двух Service интерфейсах объявить все методы транзакционными --DealService, UserService


Task 6. Создать DAO для отображения информации для Dashboard
-- done, test also added.


17. Переписать на Spring MVC сервлеты по работе со Компанией и Контактами
--сделал только компанию.Добавил multipart.


 18. REST для Контактов на Spring MVC
---done. Need to test.

TO DO:



 21. Spring локализация
#23 opened 3 days ago by becomejavasenior

 8. Spring локализация
#14 opened 10 days ago by becomejavasenior

 19. REST для Сделок на Spring MVC
#21 opened 3 days ago by becomejavasenior



24. Spring transaction, аттрибут Propagation
#26 opened 5 minutes ago by MykolaBova

 23 Планировщик задач
#25 opened 5 minutes ago by MykolaBova

 22 Validation
#24 opened 6 minutes ago by MykolaBova

 15. Написать DAO для управления правами пользователей на JdbcTemplate
#3 opened 10 days ago by becomejavasenior




