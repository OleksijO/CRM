<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title><spring:message code="message"/>:</title>
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
    <div class="row" align="center">
        <div class="row">
            <a class="hyperlink" href="?language=en">English</a> |
            <a class="hyperlink" href="?language=ru_RU">Русский</a>
        </div>
        <br>
        <br>
        <h3><spring:message code="operationResult"/>:</h3>
        <br>
        <h2>${message}</h2>

    </div>
</div>
</body>
</html>
