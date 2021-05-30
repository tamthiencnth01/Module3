<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 27/5/2021
  Time: 8:47 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Management Application</title>
</head>
<body>
<center>
    <h1>User Management</h1>
    <h2>
        <a href="/users">Back to User List</a>
    </h2>
</center>
<div align="center">
    <form method="post">
        <table border="1" cellpadding="5">
            <caption>
                <h2>Edit User</h2>
            </caption>
            <c:if test="${user!=null}">
                <input type="hidden" id="id" value="<c:out value='${user.id}'/>"/>
            </c:if>
            <tr>
                <th>User Name: </th>
                <td>
                    <input type="text" name="nameUser" size="45" value="<c:out value='${user.nameUser}'/> "/>
                </td>
            </tr>
            <tr>
                <th>User Email: </th>
                <td>
                    <input type="text" name="email" size="45" value="<c:out value='${user.email}'/> "/>
                </td>
            </tr>
            <tr>
                <th>Country: </th>
                <td>
                    <input type="text" name="country" size="15" value="<c:out value='${user.country}'/> "/>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="Edit">
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
