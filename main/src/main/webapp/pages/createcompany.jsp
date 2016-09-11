<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title><spring:message code="addcompany.title"/></title>

    <link rel="stylesheet" href="/resources/css/bootstrap.min.css">
    <link rel="stylesheet" href="/resources/css/bootstrapValidator.min.css"/>
    <link rel="stylesheet" href="/resources/css/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" href="/resources/css/company.css"/>
<!--
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/moment-with-locales.min.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrapValidator.min.js"></script>
    <script type="text/javascript" src="/resources/js/company.js"></script>
-->
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <form:form class="form-horizontal" method="POST" action="/company" id="companyForm"
                   enctype="multipart/form-data"
                   commandName="addCompanyForm"
                   modelAttribute="addCompanyForm">
            <div class="col-md-3">
                <div class="row">
                    <a class="hyperlink" href="?language=en">English</a> |
                    <a class="hyperlink" href="?language=ru_RU">Русский</a>
                </div>
            </div>
            <div class="col-md-4">
                <div class="company" align="center">
                    <br>
                    <h3><spring:message code="addcompany.title"/></h3>
                    <br>
                    <div class="form-group">
                        <font color="red"><form:errors path="*" /></font>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyName"><h4><spring:message code="addcompany.name"/></h4></label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="companyName" name="companyName"
                                   placeholder="<spring:message code="addcompany.companyname"/>" path="companyName"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyTag"><h4><spring:message code="addcompany.tags"/>:</h4></label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="companyTag" name="companyTag"
                                   placeholder="<spring:message code="addcompany.tags"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyResponsibleUser"><h4><spring:message code="responsibleuser"/>:</h4>
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
                        <label class="control-label col-md-5" for="companyPhone"><h4><spring:message code="addcompany.phone"/>:</h4></label>
                        <div class="col-md-7">
                            <input type="number" class="form-control" id="companyPhone" name="companyPhone"
                                   placeholder="<spring:message code="addcompany.phone"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyEmail"><h4><spring:message code="email"/>:</h4></label>
                        <div class="col-md-7">
                            <input type="email" class="form-control" id="companyEmail" name="companyEmail"
                                   placeholder="<spring:message code="email"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyWeb"><h4><spring:message code="addcompany.website"/>:</h4></label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="companyWeb" name="companyWeb"
                                   placeholder="<spring:message code="addcompany.website"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyAddress"><h4><spring:message code="addcompany.address"/>:</h4></label>
                        <div class="col-md-7">
                            <textarea type="text" class="form-control" id="companyAddress"
                                      name="companyAddress" rows="3"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyNote"><h4><spring:message code="addcompany.note"/>:</h4></label>
                        <div class="col-md-7">
                            <textarea type="text" class="form-control" id="companyNote" name="companyNote"
                                      rows="4"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12" align="right">

                            <label class="btn btn-primary" for="companyFile">
                                <input id="companyFile" name="companyFile" type="file" style="display:none;">
                                <spring:message code="addcompany.addfile"/>
                            </label>

                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="companyContact"><h4><spring:message code="addcompany.selectcontact"/>:</h4></label>
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
                            <button type="button" class="btn btn-primary" id="add_contact"><spring:message code="addcompany.addcontact"/></button>
                        </div>
                    </div>
                </div>
                <div class="contact" align="center" style="display:none;">
                    <h3><spring:message code="addcontact.title"/></h3>
                    <br>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="contactName"><h4><spring:message code="addcontact.names"/>:</h4></label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="contactName" name="contactName"
                                   placeholder="<spring:message code="addcontact.names"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="contactPosition"><h4><spring:message code="addcontact.position"/>:</h4></label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="contactPosition" name="contactPosition"
                                   placeholder="<spring:message code="addcontact.position"/>">
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
                            <input type="number" class="form-control" name="contactPhone" placeholder="<spring:message code="addcompany.phone"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="contactEmail"><h4><spring:message code="email"/>:</h4></label>
                        <div class="col-md-7">
                            <input type="email" class="form-control" id="contactEmail" name="contactEmail"
                                   placeholder="<spring:message code="email"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-5" for="contactSkype"><h4><spring:message code="addcontact.skype"/>:</h4></label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="contactSkype" name="contactSkype"
                                   placeholder="<spring:message code="addcontact.skype"/>">
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="deal" align="center">
                    <br>
                    <h3><spring:message code="adddeal.title"/></h3>
                    <br>
                    <div class="form-group">
                        <div class="col-md-12" align="center">
                            <button type="button" class="btn btn-primary" id="add_deal"><spring:message code="addcompany.adddeal"/></button>
                        </div>
                    </div>
                </div>
                <div class="quick_deal" align="center" style="display: none;">
                    <h3><spring:message code="addcompany.adddeal"/></h3>
                    <br>
                    <div class="form-group">
                        <label class="control-label col-md-4" for="dealName"><h4><spring:message code="adddeal.name"/>:</h4></label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" id="dealName" name="dealName"
                                   placeholder="<spring:message code="adddeal.name"/>">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4" for="dealStage"><h4><spring:message code="adddeal.stage"/>:</h4></label>
                        <div class="col-md-7">
                            <select class="form-control" id="dealStage" name="dealStage">
                                <c:forEach items="${stageDeals}" var="dealStage">
                                    <option><c:out value="${dealStage.value}"></c:out></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4" for="dealBudget"><h4><spring:message code="adddeal.budget"/>:</h4></label>
                        <div class="col-md-7">
                            <input type="number" class="form-control" id="dealBudget" name="dealBudget"
                                   placeholder="<spring:message code="adddeal.budget"/>" step="any">
                        </div>
                    </div>
                </div>
                <div class="task" align="center">
                    <h3><spring:message code="addtask.title"/></h3>
                    <br>
                    <div class="form-group">
                        <label class="control-label col-md-4" for="period"><h4><spring:message code="addtask.period"/>:</h4></label>
                        <div class="col-md-7">
                            <select class="form-control" id="period" name="period">
                                <c:forEach items="${typeOfPeriod}" var="period">
                                    <option><c:out value="${period}"></c:out></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4"><h4><spring:message code="addtask.date"/>:</h4></label>
                        <div class="col-md-7">
                            <input type="datetime" class="form-control" id="dateTask" name="dateTask"
                                   placeholder="dd/mm/yyyy"
                                   path="dateTask"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4" for="taskTime"><h4><spring:message code="addtask.time"/>:</h4></label>
                        <div class="col-md-7">
                            <select class="form-control" id="taskTime" name="taskTime">
                                <c:forEach items="${timeListForTask}" var="time">
                                    <option><c:out value="${time}"></c:out></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4" for="taskResponsibleUser"><h4><spring:message code="responsibleuser"/>:</h4></label>
                        <div class="col-md-7">
                            <select class="form-control" id="taskResponsibleUser" name="taskResponsibleUser">
                                <c:forEach items="${responsibleUsers}" var="user">
                                    <option><c:out value="${user.getName()}"></c:out></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4" for="taskType"><h4><spring:message code="addtask.type"/>:</h4></label>
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
                        <button type="submit" class="btn btn-success" form="companyForm"><spring:message code="addcompanyform.saveall"/></button>
                        <button type="reset" class="btn btn-primary"><spring:message code="addcompanyform.cancel"/></button>
                    </div>
                </div>
            </div>
            <div class="col-md-1"></div>
            <input type="submit" value="Save">
        </form:form>
    </div>
</div>
</body>
</html>
