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

        <br>
        <br>
        <h2><spring:message code="error.title"/>.</h2>
        <br>
        <br>
        <br>
        <h3><spring:message code="error.info"/>: </h3>
        <br>
        <br>
        <h4><spring:message code="error.requested.url"/>: ${url}</h4>
        <br>
        <h4><spring:message code="error.type"/>: ${exceptionClass}</h4>
        <br>
        <h4><spring:message code="error.message"/>: ${exceptionMessage} </h4>
        <br>
        <br>
        <h5><spring:message code="error.logged"/>. </h5>
        <br>
        <h4><spring:message code="error.inform.support"/>. </h4>


    </div>
</div>
</body>
</html>
