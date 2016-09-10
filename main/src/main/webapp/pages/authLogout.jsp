<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Вход выполнен</title>
</head>
<body>
<table style="border: none">
    <tbody>
    <tr>
        <td><h3 align="center">CRM "CRIUS"</h3></td>
    </tr>
    <tr>
        <td><h4 align="center">(страница будет заменена на Dashboard)</h4></td>
    </tr>
    <tr>
        <td><a href="/contactcreate">Создать контакт</a></td>
    </tr>
    <tr>
        <td><a href="/company">Создать компанию</a></td>
    </tr>
    <tr>
        <td><a href="/viewcompanies">Просмотр контактов и компаний</a></td>
    </tr>
    <tr>
        <td><a href="/dealFunnel">Deal Funnel</a></td>
    </tr>
    <tr>
        <td><a href="/dealList">Deal List</a></td>
    </tr>
    <tr></tr>
    <tr>
        <td>
            <form action="/logout" method="post">
                <input type="submit" value="Выйти из системы">
            </form>
        </td>
    </tr>
    </tbody>
</table>
</body>
</html>
