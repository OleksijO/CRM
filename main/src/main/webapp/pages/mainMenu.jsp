<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 11.09.2016
  Time: 15:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>CRM Crius :: Main menu</title>
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
