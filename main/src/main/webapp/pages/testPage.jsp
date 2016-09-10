<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Добавить компанию</title>

</head>
<body>
<div class="container-fluid">
    <div class="row">
        <form:form class="form-horizontal" role="form" method="post" action="/company" id="companyForm"
                   enctype="multipart/form-data">

                <br>
                <h3>Добавить компанию</h3>
                <br>

                    <label class="control-label col-md-5" for="companyName"><h4>Название:</h4></label>

                        <form:input type="text" class="form-control" id="companyName" name="companyName"
                               placeholder="Название компании" path="model.company.name"/>




                    <h3>Добавить контакт</h3>
                    <br>

                        <label class="control-label col-md-5" for="contactName"><h4>Имя Фамилия:</h4></label>

                            <form:input type="text" class="form-control" id="contactName" name="contactName"
                                   placeholder="Имя Фамилия" path="model.company.responsibleUser"/>




                        <h3>Добавить сделку</h3>
                        <br>

                            <label class="control-label col-md-4" for="dealName"><h4>Название:</h4></label>

                                <form:input type="text" class="form-control" id="dealName" name="dealName"
                                       placeholder="Название сделки" path="model.deal.name"/>

                                <button type="submit" class="btn btn-success" form="companyForm">Сохранить все</button>

                    </form:form>
                </div>

        </div>
</body>
</html>
