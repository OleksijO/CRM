<%@ page contentType="text/html; charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Вход в систему</title>
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js" type="text/javascript"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" type="text/javascript"></script>
</head>
<body style="padding: 25px;">
<div class="container">
    <form class="form-signin" action="/login" method="post"
          style="max-width: 320px; margin: 0 auto; font-size: larger;">
        <h3 class="form-signin-heading" align="center">CRM "CRIUS"</h3>
        <div class="form-group">
            <label for="inputEmail" class="sr-only">Email</label>
            <input type="email" id="inputEmail" class="form-control" placeholder="Email адрес" name="email"
                   required autofocus style="z-index: 2;">
        </div>
        <div class="form-group">
            <label for="inputPassword" class="sr-only">Пароль</label>
            <input type="password" id="inputPassword" class="form-control" placeholder="Пароль" name="password"
                   required>
            <h5 style="color: red" align="center">${authMessage}</h5>
            <c:remove var="authMessage" scope="session"/>
        </div>
        <div class="form-group">
            <input type="hidden" name="fromPage" title="" value="${requestScope.fromPage}">
        </div>
        <div class="form-group">
            <button class="btn btn-primary btn-block" type="submit">Вход</button>
        </div>
        <div class="form-group">
            <div class="row">
                <div class="col-md-6">
                    <a class="hyperlink" href="/register">Регистрация</a>
                </div>
                <div class="col-md-6" align="right">
                    <a class="hyperlink" href="/logout">Страница выхода</a>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
