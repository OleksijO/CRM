# spring-framework

Some issues and explanations:

1. Logging added not in all modules.

2. Not all DAO moved to JdbcTemplate implementation. This caused using dao module during tests in jdbc-template module

3. On mvc moved only create company page. Added page with message showing results of adding company operation.

4. Not solved problem with clearing form 'add company' after validation error.

5. To UserDAO added method getUserById() equivalent to getById(). Result in case of absent user with specified id - EmptyResultDataAccessException.
  Method getById returned null in mentioned case.

6. DAO tests are using real but another (crm-crius-test) from work (crm-crius) postgresql database.

7. Web deployment descriptor and resources were moved to main module.

8. Used some temporary "features" to set up navigation system: rest without auth., added mainMenu.jsp, added welcome page with redirect to menu etc.

9. DAO interface moved to another module, service didn't because it has one implementation.

10. Some modules were not renamed to decrease number of changes in github repo.








