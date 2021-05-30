<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 27/5/2021
  Time: 3:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Details</title>
    <style>
        table{
            margin: 0;
            border: 1px solid black;
            padding: 5px;
        }
        td,th{
            border: 1px solid black;
        }
    </style>
</head>
<body>
    <center>
        <h1>User Details </h1>
        <p>
            <a href="/users">Back to user list</a>
        </p>
    </center>
    <fieldset>
        <legend>Search list is</legend>
        <table>
            <tr>
                <th>Name: </th>
                <th>Email: </th>
                <th>Country</th>
            </tr>
            <c:forEach items='${requestScope["users"]}' var="user">
                <tr>
                    <td>${user.getNameUser()}</td>
                    <td>${user.getEmail()}</td>
                    <td>${user.getCountry()}</td>
                </tr>
            </c:forEach>
        </table>
    </fieldset>
</body>
</html>
