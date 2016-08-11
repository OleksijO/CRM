<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Contacts and Companies view</title>
    <link rel="stylesheet" href="/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="/resources/css/contactCompanyView.css">
    <script src="/resources/js/jquery.min.js" type="text/javascript"></script>
    <script src="/resources/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="/resources/js/contactCompanyView.js" type="text/javascript"></script>
</head>
<body onload="onBodyLoad()">
<div class="container-fluid">
    <div class="row">
        <div class="col-md-4">
            <ul class="nav nav-tabs" id="listSelectionTab">
                <li class="active"><a data-toggle="tab" href="#allEntity" id="allEntityTab">Все</a></li>
                <li><a data-toggle="tab" href="#contacts" id="contactTab">Контакты</a></li>
                <li><a data-toggle="tab" href="#companies" id="companyTab">Компании</a></li>
            </ul>
            <div class="tab-content col-md-11">
                <div id="allEntity" class="tab-pane fade in active">
                    <form class="form-horizontal" id="formAll">
                        <fieldset>
                            <legend>... фильтры общие</legend>
                        </fieldset>
                    </form>
                </div>
                <div id="contacts" class="tab-pane fade">
                    <form class="form-horizontal" id="formContact">
                        <div class="form-group">
                            <div class="radio">
                                <label class="radio"><input type="radio" name="contactStd"
                                                            value="0" checked>Полный список контактов</label>
                            </div>
                            <div class="radio">
                                <label class="radio"><input type="radio" name="contactStd"
                                                            value="1">Контакты без задач</label>
                            </div>
                            <div class="radio">
                                <label class="radio"><input type="radio" name="contactStd"
                                                            value="2">Контакты с просроченными задачами</label>
                            </div>
                            <div class="radio">
                                <label class="radio"><input type="radio" name="contactStd"
                                                            value="3">Удаленные</label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-4" for="contactWhen">Когда:</label>
                            <div class="col-md-8">
                                <select class="form-control" id="contactWhen" onchange="contactWhenChange()">
                                    <option value="0">за все время</option>
                                    <option value="1">за сегодня</option>
                                    <option value="2">за 3 дня</option>
                                    <option value="3">за неделю</option>
                                    <option value="4">за месяц</option>
                                    <option value="5">за квартал</option>
                                    <option value="6">за период</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group" id="groupContactCalendar" hidden>
                            <label class="control-label col-md-1" for="contactCalendarFrom"
                                   style="padding-right: 6px">c</label>
                            <div class="col-md-5" style="padding-left: 6px">
                                <input type="date" class="form-control" id="contactCalendarFrom"
                                       name="contactCalendarFrom"
                                       title="Выберите начало периода" style="text-align: center">
                            </div>
                            <label class="control-label col-md-1" for="contactCalendarTo"
                                   style="padding-right: 6px">по</label>
                            <div class="col-md-5" style="padding-left: 6px">
                                <input type="date" class="form-control" id="contactCalendarTo" name="contactCalendarTo"
                                       title="Выберите конец периода" style="text-align: center">
                            </div>
                        </div>
                        <div class="form-group" align="center">
                            <div class="radio-inline">
                                <label class="btn btn-primary active" id="contactWhenCreateBtn">
                                    <input type="radio" class="invisible" name="contactWhenMode" value="0"
                                           onchange="contactWhenModeClick(false)" checked>Созданные</label>
                            </div>
                            <div class="radio-inline">
                                <label class="btn btn-default" id="contactWhenChangeBtn">
                                    <input type="radio" class="invisible" name="contactWhenMode" value="1"
                                           onchange="contactWhenModeClick(true)">Измененные</label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-4" for="contactStageSelect">Этапы:</label>
                            <div class="checkbox col-md-8" id="contactStageSelect">
                                <label class="checkbox"><input type="checkbox" class="checkbox" name="contactStages"
                                                               value="0">Без сделок</label>
                                <label class="checkbox"><input type="checkbox" class="checkbox" name="contactStages"
                                                               value="1">Без открытых сделок</label>
                                <label class="checkbox"><input type="checkbox" class="checkbox" name="contactStages"
                                                               value="2">Первичный контакт</label>
                                <label class="checkbox"><input type="checkbox" class="checkbox" name="contactStages"
                                                               value="3">Переговоры</label>
                                <label class="checkbox"><input type="checkbox" class="checkbox" name="contactStages"
                                                               value="4">Принимают решение</label>
                                <label class="checkbox"><input type="checkbox" class="checkbox" name="contactStages"
                                                               value="5">Согласование договора</label>
                                <label class="checkbox"><input type="checkbox" class="checkbox" name="contactStages"
                                                               value="6">Успешно реализован</label>
                                <label class="checkbox"><input type="checkbox" class="checkbox" name="contactStages"
                                                               value="7">Закрыто и не реализовано</label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-4" for="contactManager">Менеджер:</label>
                            <div class="col-md-8">
                                <select class="form-control" id="contactManager" name="contactManager">
                                    <c:forEach var="manager" items="${requestScope.userList}">
                                        <option value="${manager.id}">${manager.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-4" for="contactTask">Задачи:</label>
                            <div class="col-md-8">
                                <select class="form-control" id="contactTask" name="contactTask">
                                    <option value="0">не учитывать</option>
                                    <option value="1">на сегодня</option>
                                    <option value="2">на завтра</option>
                                    <option value="3">на этой неделе</option>
                                    <option value="4">в этом месяце</option>
                                    <option value="5">в этом квартале</option>
                                    <option value="6">нет задач</option>
                                    <option value="7">просрочены</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-4" for="contactTag">Теги:</label>
                            <div class="col-md-8">
                                <select class="form-control" id="contactTag" name="contactTag">
                                    <c:forEach var="tag" items="${requestScope.tagList}">
                                        <option value="${tag.id}">${tag.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group" align="center">
                            <input type="submit" class="btn btn-default" value="Применить" title="Применить фильтр">
                            <input type="reset" class="btn btn-default" value="Очистить" title="сбросить изменения">
                        </div>
                    </form>
                </div>
                <div id="companies" class="tab-pane fade">
                    <form class="form-horizontal" id="formCompany">
                        <fieldset>
                            <legend>... фильтры для компаний</legend>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-md-8">
            <div class="row rowHideable" id="companyRowHeader" hidden>
                <div class="col-md-8" align="left">
                    <h3>Отображение компаний</h3>
                </div>
                <div class="col-md-4" align="right">
                    <br>
                    <button type="button" class="btn btn-primary" onclick="location.href='/company'">Добавить компанию
                    </button>
                </div>
            </div>
            <div class="row rowHideable" id="companyRowTable" hidden>
                <br>
                <div class="col-md-10" align="center">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th>Наименование</th>
                            <th>Телефон</th>
                            <th>Email</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${requestScope.companyList}" var="company">
                            <tr>
                                <td><c:out value="${company.getName()}"></c:out></td>
                                <td><c:out value="${company.getPhone()}"></c:out></td>
                                <td><c:out value="${company.getEmail()}"></c:out></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="col-md-2"></div>
            </div>
            <div class="row rowHideable" id="contactRowHeader" hidden>
                <div class="col-md-8" align="left">
                    <h3>Список контактов</h3>
                </div>
                <div class="col-md-4" align="right">
                    <br>
                    <input type="button" class="btn btn-primary" onclick="location.href='/contactcreate'"
                           value="Добавить контакт">
                </div>
            </div>
            <div class="row rowHideable" id="contactRowTable" hidden>
                <div class="col-md-10" align="center">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>Наименование</th>
                            <th>Компания</th>
                            <th>Телефон</th>
                            <th>Email</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${requestScope.contactList}" var="contact">
                            <tr>
                                <td><c:out value="${contact.name}"/></td>
                                <td><c:out value="${contact.company.name}"/></td>
                                <td><c:out value="${contact.phone}"/></td>
                                <td><c:out value="${contact.email}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="row rowHideable" id="allEntityRowHeader">
                <div class="col-md-12" align="left">
                    <h3>Список контактов и компаний</h3>
                </div>
            </div>
            <div class="row rowHideable" id="allEntityRowTable">
                <br>
                <div class="col-md-10" align="center">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>Наименование</th>
                            <th>Компания</th>
                            <th>Телефон</th>
                            <th>Email</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${requestScope.contactList}" var="contact">
                            <tr>
                                <td><c:out value="${contact.name}"/></td>
                                <td><c:out value="${contact.company.name}"/></td>
                                <td><c:out value="${contact.phone}"/></td>
                                <td><c:out value="${contact.email}"/></td>
                            </tr>
                        </c:forEach>
                        <c:forEach items="${requestScope.companyList}" var="company">
                            <tr>
                                <td><c:out value="${company.name}"/></td>
                                <td></td>
                                <td><c:out value="${company.phone}"/></td>
                                <td><c:out value="${company.email}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>
</div>
</body>
</html>