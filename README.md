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

1) Реализовать один бин с scope="singleton" -- CompanyServiceImpl

2) Реализовать один бин с scope="prototype" -- ContactServiceImpl

4) Для одного бина реализовать и сконфигурировать init и destroy методы -- DealServiceImpl

Task 5. 

Добавить Spring DI в Controller слой -- SpringBeanAutowiringSupport used.

