<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Добавить компанию</title>
    <link rel="stylesheet" href="/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="/resources/css/bootstrapValidator.min.css"/>
    <link rel="stylesheet" href="/resources/css/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" href="/resources/css/company.css"/>
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/moment-with-locales.min.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrapValidator.min.js"></script>
    <script type="text/javascript" src="/resources/js/company.js"></script>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <form class="form-horizontal" role="form" method="post" action="/company" id="companyForm"
              enctype="multipart/form-data">
            <div class="col-md-3"></div>
            <div class="col-md-4">
                <div class="company" align="center">
                    <br>
                    <h3>Добавить компанию</h3>
                    <br>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyName"><h4>Название:</h4></label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="companyName" name="companyName"
                                   placeholder="Название компании">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyTag"><h4>Тэги:</h4></label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="companyTag" name="companyTag"
                                   placeholder="Тэги">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyResponsibleUser"><h4>Ответственный:</h4>
                        </label>
                        <div class="col-md-7">
                            <select class="form-control" id="companyResponsibleUser" name="companyResponsibleUser">
                                <c:forEach items="${responsibleUsers}" var="user">
                                    <option><c:out value="${user.getName()}"></c:out></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyPhone"><h4>Телефон:</h4></label>
                        <div class="col-md-7">
                            <input type="number" class="form-control" id="companyPhone" name="companyPhone"
                                   placeholder="Телефон">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyEmail"><h4>Email:</h4></label>
                        <div class="col-md-7">
                            <input type="email" class="form-control" id="companyEmail" name="companyEmail"
                                   placeholder="Email">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyWeb"><h4>Web-сайт:</h4></label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="companyWeb" name="companyWeb"
                                   placeholder="Web-сайт">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyAddress"><h4>Адрес:</h4></label>
                        <div class="col-md-7">
                            <textarea type="text" class="form-control" id="companyAddress"
                                      name="companyAddress" rows="3"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyNote"><h4>Примечание:</h4></label>
                        <div class="col-md-7">
                            <textarea type="text" class="form-control" id="companyNote" name="companyNote"
                                      rows="4"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12" align="right">
                            <label class="btn btn-primary" for="companyFile">
                                <input id="companyFile" name="companyFile" type="file" style="display:none;">
                                Добавить файлы
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyContact"><h4>Выбор контакта:</h4></label>
                        <div class="col-md-7">
                            <select class="form-control" id="companyContact" name="companyContact">
                                <c:forEach items="${contactList}" var="companyContact">
                                    <option><c:out value="${companyContact.getName()}"></c:out></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12" align="right">
                            <button type="button" class="btn btn-primary" id="add_contact">Добавить контакт</button>
                        </div>
                    </div>
                </div>
                <div class="contact" align="center" style="display:none;">
                    <h3>Добавить контакт</h3>
                    <br>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="contactName"><h4>Имя Фамилия:</h4></label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="contactName" name="contactName"
                                   placeholder="Имя Фамилия">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="contactPosition"><h4>Должность:</h4></label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="contactPosition" name="contactPosition"
                                   placeholder="Должность">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-5">
                            <select class="form-control col-md-4" name="typePhone">
                                <c:forEach items="${typeOfPhone}" var="companyPhone">
                                    <option><c:out value="${companyPhone}"></c:out></option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-7">
                            <input type="number" class="form-control" name="contactPhone" placeholder="Номер телефона">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="contactEmail"><h4>Email:</h4></label>
                        <div class="col-md-7">
                            <input type="email" class="form-control" id="contactEmail" name="contactEmail"
                                   placeholder="Email">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="contactSkype"><h4>Skype:</h4></label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="contactSkype" name="contactSkype"
                                   placeholder="Skype">
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="deal" align="center">
                    <br>
                    <h3>Сделки</h3>
                    <br>
                    <div class="form-group">
                        <div class="col-md-12" align="center">
                            <button type="button" class="btn btn-primary" id="add_deal">Добавить сделку</button>
                        </div>
                    </div>
                </div>
                <div class="quick_deal" align="center" style="display: none;">
                    <h3>Добавить сделку</h3>
                    <br>
                    <div class="form-group">
                        <label class="control-label col-md-4" for="dealName"><h4>Название:</h4></label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="dealName" name="dealName"
                                   placeholder="Название сделки">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4" for="dealStage"><h4>Этап:</h4></label>
                        <div class="col-md-7">
                            <select class="form-control" id="dealStage" name="dealStage">
                                <c:forEach items="${stageDeals}" var="dealStage">
                                    <option><c:out value="${dealStage.value}"></c:out></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4" for="dealBudget"><h4>Бюджет:</h4></label>
                        <div class="col-md-7">
                            <input type="number" class="form-control" id="dealBudget" name="dealBudget"
                                   placeholder="Бюджет" step="any">
                        </div>
                    </div>
                </div>
                <div class="task" align="center">
                    <h3>Запланировать действие</h3>
                    <br>
                    <div class="form-group">
                        <label class="control-label col-md-4" for="period"><h4>Когда:</h4></label>
                        <div class="col-md-7">
                            <select class="form-control" id="period" name="period">
                                <c:forEach items="${typeOfPeriod}" var="period">
                                    <option><c:out value="${period}"></c:out></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4"><h4>Дата:</h4></label>
                        <div class="col-md-7">
                            <input type="datetime" class="form-control" id="dateTask" name="dateTask"
                                   placeholder="dd/mm/yyyy"
                                   path="dateTask"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4" for="taskTime"><h4>Время:</h4></label>
                        <div class="col-md-7">
                            <select class="form-control" id="taskTime" name="taskTime">
                                <c:forEach items="${timeListForTask}" var="time">
                                    <option><c:out value="${time}"></c:out></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4" for="taskResponsibleUser"><h4>Ответственный:</h4></label>
                        <div class="col-md-7">
                            <select class="form-control" id="taskResponsibleUser" name="taskResponsibleUser">
                                <c:forEach items="${responsibleUsers}" var="user">
                                    <option><c:out value="${user.getName()}"></c:out></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4" for="taskType"><h4>Тип:</h4></label>
                        <div class="col-md-7">
                            <select class="form-control" id="taskType" name="taskType">
                                <c:forEach items="${taskType}" var="type">
                                    <option><c:out value="${type.value}"></c:out></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-12" align="center">
                        <button type="submit" class="btn btn-success" form="companyForm">Сохранить все</button>
                        <button type="reset" class="btn btn-primary">Отменить все</button>
                    </div>
                </div>
            </div>
            <div class="col-md-1"></div>
        </form>
    </div>
</div>
</body>
</html>
