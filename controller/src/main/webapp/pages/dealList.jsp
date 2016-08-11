<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Deal's list</title>

    <link href="/resources/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="/resources/css/bootstrap-datetimepicker.min.css" />
    <script type="text/javascript" src="/resources/js/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/resources/js/moment-with-locales.min.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrap-datetimepicker.min.js"></script>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-3">
            <div class="row">
                <div class="col-md-12">
                    <h3>Фильтр</h3>
                    <div class="form-group">
                        <div class="col-md-12">
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" checked value="">Открытые сделки
                                </label>
                            </div>
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" value="">Только мои сделки
                                </label>
                            </div>
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" value="">Успешно завершенные
                                </label>
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" value="">Нереализованные сделки
                                    </label>
                                </div>
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" value="">Сделки без задач
                                    </label>
                                </div>
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" value="">Сделки c просроченными задачами
                                    </label>
                                </div>
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" value="">Удаленные
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-9">
                    <h3>Период</h3>
                    <form class="form-inline" role="form">
                        <div class="form-group">
                            <div class="radio">
                                <label><input type="radio" for="period_id">
                                    Когда
                                    <select class="form-control" id="period_id">
                                        <option>00:00</option>
                                        <option>00:15</option>
                                        <option>00:30</option>
                                        <option>00:45</option>
                                    </select>
                                </label>
                            </div>
                        </div>
                    </form>
                    <div class="radio">
                        <label><input type="radio" name=""></label>
                        <form role="form">
                            <div class="form-group">
                                <div class="form-group">
                                    <label class="pull-left">От</label>
                                    <div class="input-group date" id="datetimepicker1">
                                        <input type="text" class="form-control" />
                                        <span class="input-group-addon">
                                          <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="pull-left">До </label>
                                    <div class="input-group date" id="datetimepicker2">
                                        <input type="text" class="form-control" />
                                        <span class="input-group-addon">
                                          <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="col-md-3"></div>
            </div>
            <script type="text/javascript">
                $(function () {
                    $('#datetimepicker1').datetimepicker(
                            {pickTime: false}
                    );
                });
            </script>
            <script type="text/javascript">
                $(function () {
                    $('#datetimepicker2').datetimepicker(
                            {pickTime: false}
                    );
                });
            </script>
            <div class="row">
                <div class="col-md-12">
                    <br>
                    <form class="form-inline" role="form">
                        <div class="row">
                            <div class="col-md-6">
                                <label for="stage">Этапы</label>
                            </div>
                            <div class="col-md-6">
                                <select class="form-control" id="stage">
                                    <option>1</option>
                                    <option>2</option>
                                    <option>3</option>
                                    <option>4</option>
                                </select>
                            </div>
                        </div>
                    </form>
                    <form class="form-inline" role="form">
                        <div class="row">
                            <div class="col-md-6">
                                <label for="manager">Менеджеры</label>
                            </div>
                            <div class="col-md-6">
                                <select class="form-control" id="manager">
                                    <option>1</option>
                                    <option>2</option>
                                    <option>3</option>
                                    <option>4</option>
                                </select>
                            </div>
                        </div>
                    </form>
                    <div class="row">
                        <form class="form-inline" role="form">
                            <div class="col-md-6">
                                <label for="task">Задачи</label>
                            </div>
                            <div class="col-md-6">
                                <select class="form-control" id="task">
                                    <option>1</option>
                                    <option>2</option>
                                    <option>3</option>
                                    <option>4</option>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="row">
                        <form class="form-inline" role="form">
                            <div class="col-md-6">
                                <label for="tag">Теги</label>
                            </div>
                            <div class="col-md-6">
                                <select class="form-control" id="tag">
                                    <option>1</option>
                                    <option>2</option>
                                    <option>3</option>
                                    <option>4</option>
                                </select>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-9">
            <div class="row">
                <a href="/dealFunnel" class="btn btn-primary">Funnel</a>
                <a href="/dealList" class="btn btn-primary">List</a>
                <a href="/addDeals" class="btn btn-primary pull-center">Add deal</a>
            </div>
            <br><br>
            <div class="row">
                <div class="col-md-9" align="center">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>Название сделки</th>
                            <th>Основной контакт</th>
                            <th>Компания контакта</th>
                            <th>Этап сделки</th>
                            <th>Бюджет</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="deal" items="${dealList}">
                            <tr>
                                <td><c:out value="${deal.name}"/></td>
                                <td><c:out value="${deal.getPrimaryContact().getName()}"/></td>
                                <td><c:out value="${deal.getPrimaryContact().getCompany().getName()}"/></td>
                                <td><c:out value="${deal.getStage().getName()}"/></td>
                                <td><c:out value="${deal.amount}"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                </div>
                <div class="col-md-3"></div>
            </div>
        </div>
    </div>
</div>
</body>
</html>