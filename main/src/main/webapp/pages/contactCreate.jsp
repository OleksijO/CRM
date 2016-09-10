<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Contact create</title>
    <link rel="stylesheet" href="/resources/css/bootstrap.min.css" type="text/css">
    <link rel="stylesheet" href="/resources/css/contactCreate.css" type="text/css">
    <script src="/resources/js/jquery.min.js" type="text/javascript"></script>
    <script src="/resources/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="/resources/js/contactCreate.js" type="text/javascript"></script>
</head>
<body onload="onBodyLoad()">
<div class="container-fluid">
    <form class="form-horizontal" name="form_contact" role="form" method="post" enctype="multipart/form-data">
        <fieldset id="fieldset_contact">
            <legend>Новый контакт</legend>
            <div class="form-group">
                <label class="control-label col-xs-3" for="contact_name">Имя, Фамилия:</label>
                <div class="col-xs-9">
                    <input class="form-control" type="text" id="contact_name" name="contact_name" required
                           placeholder="Имя Фамилия">
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-3" for="contact_tag">Теги:</label>
                <div class="col-xs-9">
                    <input class="form-control" type="text" id="contact_tag" name="contact_tag"
                           placeholder="Теги через #">
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-3" for="contact_user_id">Ответственный:</label>
                <div class="col-xs-9">
                    <select class="form-control" id="contact_user_id" name="contact_user_id" title="Ответственный"
                            required>
                        <option disabled selected value style="display: none"></option>
                        <c:forEach var="user" items="${requestScope.userList}">
                            <option value="<c:out value="${user.id}"/>"><c:out value="${user.name}"/></option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-3" for="contact_position">Должность:</label>
                <div class="col-xs-9">
                    <input class="form-control" type="text" id="contact_position" name="contact_position"
                           placeholder="Должность">
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-3">
                    <select class="form-control" name="contact_type_of_phone" title="Тип телефона" required
                            style="font-weight: bold; text-align-last: right;">
                        <c:forEach var="typeOfPhone" items="${requestScope.typeOfPhoneArray}">
                            <option value="<c:out value="${typeOfPhone.id}"/>"><c:out value="${typeOfPhone}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-xs-9">
                    <input class="form-control" type="tel" name="contact_phone" title="Телефон" placeholder="Телефон">
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-3" for="contact_email">Email:</label>
                <div class="col-xs-9">
                    <input class="form-control" type="email" id="contact_email" name="contact_email"
                           placeholder="Email">
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-3" for="contact_skype">Skype:</label>
                <div class="col-xs-9">
                    <input class="form-control" type="text" id="contact_skype" name="contact_skype" placeholder="Skype">
                </div>
            </div>
            <div class="form-group" align="right">
                <input class="btn btn-default fieldReset" type="button" value="Сброс" title="Сбросить изменения">
            </div>
        </fieldset>
        <fieldset id="fieldset_note">
            <legend>Примечание</legend>
            <div class="form-group">
                <div class="col-xs-3"></div>
                <div class="col-xs-9">
                    <textarea class="form-control" id="note_text" name="note_text" placeholder="Примечание"
                              rows="2"></textarea>
                </div>
            </div>
            <div class="form-group" align="right">
                <input class="btn btn-default fieldReset" type="button" value="Сброс" title="Сбросить изменения">
            </div>
        </fieldset>
        <fieldset id="fieldset_file">
            <legend>Файл</legend>
            <div class="form-group">
                <div class="col-xs-3"></div>
                <div class="col-xs-9">
                    <input class="form-control" type="file" id="file_file" name="file_file">
                </div>
            </div>
            <div class="form-group" align="right">
                <input class="btn btn-default fieldReset" type="button" value="Сброс" title="Сбросить изменения">
            </div>
        </fieldset>
        <fieldset id="fieldset_company">
            <legend>Компания</legend>
            <div class="form-group" align="center">
                <div class="col-xs-1"></div>
                <div class="col-xs-5">
                    <label class="radio-inline">
                        <input type="radio" name="company_new" value="0"
                               onchange="companyRadioChange(false)">Выбрать существующую</label>
                </div>
                <div class="col-xs-5">
                    <label class="radio-inline">
                        <input type="radio" name="company_new" value="1"
                               onchange="companyRadioChange(true)" checked>Создать новую компанию</label>
                </div>
            </div>
            <div class="form-group companySelect" hidden>
                <label class="control-label col-xs-3" for="company_id">Компания:</label>
                <div class="col-xs-9">
                    <select class="form-control" id="company_id" name="company_id" title="Выбор компании">
                        <option disabled selected value style="display: none"></option>
                        <c:forEach var="company" items="${requestScope.companyList}">
                            <option value="<c:out value="${company.id}"/>"><c:out value="${company.name}"/></option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group companyNew">
                <label class="control-label col-xs-3" for="company_name">Название:</label>
                <div class="col-xs-9">
                    <input class="form-control" type="text" id="company_name" name="company_name" title="Название"
                           placeholder="Название компании">
                </div>
            </div>
            <div class="form-group companyNew">
                <label class="control-label col-xs-3" for="company_phone">Телефон:</label>
                <div class="col-xs-9">
                    <input class="form-control" type="tel" id="company_phone" name="company_phone" title="Телефон"
                           placeholder="Телефон">
                </div>
            </div>
            <div class="form-group companyNew">
                <label class="control-label col-xs-3" for="company_email">Email:</label>
                <div class="col-xs-9">
                    <input class="form-control" type="email" id="company_email" name="company_email"
                           placeholder="Email">
                </div>
            </div>
            <div class="form-group companyNew">
                <label class="control-label col-xs-3" for="company_web">Web-адрес:</label>
                <div class="col-xs-9">
                    <input class="form-control" type="url" id="company_web" name="company_web" placeholder="Web-адрес">
                </div>
            </div>
            <div class="form-group companyNew">
                <label class="control-label col-xs-3" for="company_address">Адрес:</label>
                <div class="col-xs-9">
                    <textarea class="form-control" id="company_address" name="company_address" rows="2"
                              placeholder="Адрес"></textarea>
                </div>
            </div>
            <div class="form-group" align="right">
                <input class="btn btn-default fieldReset" type="button" value="Сброс" title="Сбросить изменения">
            </div>
        </fieldset>
        <fieldset id="fieldset_deal">
            <legend>Сделка</legend>
            <div class="form-group">
                <label class="control-label col-xs-3" for="deal_name">Название:</label>
                <div class="col-xs-9">
                    <input class="form-control" type="text" id="deal_name" name="deal_name" title="Название"
                           placeholder="Название сделки">
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-3" for="deal_stage_name">Этап:</label>
                <div class="col-xs-9">
                    <input class="form-control" id="deal_stage_name" name="deal_stage_name"
                           title="Выбор этапа сделки" list="deal_stage_options" placeholder="Этап сделки">
                    <datalist id="deal_stage_options">
                        <c:forEach var="stage" items="${requestScope.stageList}">
                            <option value="<c:out value="${stage.name}"/>"></option>
                        </c:forEach>
                    </datalist>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-3" for="deal_budget">Бюджет:</label>
                <div class="col-xs-9">
                    <input class="form-control" type="number" min="0" step="0.01" id="deal_budget" name="deal_budget"
                           title="Сумма сделки" placeholder="Бюджет">
                </div>
            </div>
            <div class="form-group" align="right">
                <input class="btn btn-default fieldReset" type="button" value="Сброс" title="Сбросить изменения">
            </div>
        </fieldset>
        <fieldset id="fieldset_task">
            <legend>Действие (задача)</legend>
            <div class="form-group">
                <label class="control-label col-xs-3" for="task_period">Дата, время:</label>
                <div class="col-xs-3">
                    <select class="form-control" id="task_period" title="Выбор периода" onchange="taskPeriodChange()">
                        <option disabled selected value style="display: none"></option>
                        <c:forEach var="period" items="${requestScope.typeOfPeriodArray}">
                            <option value="<c:out value="${period.id}"/>"><c:out value="${period}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-xs-3" style="padding-left: 0; padding-right: 0;">
                    <input class="form-control" type="date" id="task_date" name="task_date" title="Выбор даты"
                           style="font-weight: bold; text-align: center; padding-left: 6px; padding-right: 6px;"/>
                </div>
                <div class="col-xs-3">
                    <select class="form-control" id="task_time" name="task_time" title="Выбор времени"
                            style="font-weight: bold; text-align-last: center; padding-left: 6px; padding-right: 6px;">
                        <c:forEach var="taskTime" varStatus="status" items="${requestScope.taskTimeList}">
                            <option value="<c:out value="${status.index}"/>"><c:out value="${taskTime}"/></option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-3" for="task_responsible_user_id">Ответственный:</label>
                <div class="col-xs-9">
                    <select class="form-control" id="task_responsible_user_id" name="task_responsible_user_id"
                            title="Ответственный">
                        <option disabled selected value style="display: none"></option>
                        <c:forEach var="user" items="${requestScope.userList}">
                            <option value="<c:out value="${user.id}"/>"><c:out value="${user.name}"/></option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-3" for="task_type">Тип задачи:</label>
                <div class="col-xs-9">
                    <select class="form-control" id="task_type" name="task_type" title="Тип задачи">
                        <option disabled selected value style="display: none"></option>
                        <c:forEach var="taskType" items="${requestScope.taskTypeList}">
                            <option><c:out value="${taskType}"/></option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-3" for="task_text">Текст задачи:</label>
                <div class="col-xs-9">
                    <textarea class="form-control" id="task_text" name="task_text" placeholder="Текст задачи"
                              rows="2"></textarea>
                </div>
            </div>
            <div class="form-group" align="right">
                <input class="btn btn-default fieldReset" type="button" value="Сброс" title="Сбросить изменения">
            </div>
        </fieldset>
        <div class="form-group" align="right">
            <div class="btn-group">
                <input class="btn btn-default" type="submit" value="Сохранить" title="Добавить контакт и все данные">
                <input class="btn btn-default" type="reset" value="Сброс всех полей"
                       title="Сбросить все изменения во всех полях" onclick="onResetAllClick()">
                <input class="btn btn-default" type="reset" value="Отмена (возврат)"
                       title="Отмена (возврат на предыдущую страницу)" onclick="history.go(-1);">
            </div>
        </div>
    </form>
</div>
</body>
</html>
