<%-- 
    Document   : CreateTable-BloodBank
    Created on : Apr. 10, 2021, 2:24:18 a.m.
    Author     : Jing Zhao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html><head><title>Create Blood Bank</title>
</head>
<body>
<div style="text-align: center;">
<div style="display: inline-block; text-align: left;">
<form method="post">
Owner:<br>
<select name="owner_id">
    <c:forEach var="person" items="${personList}">
        <option value="${person.id}">${person.name}</option>
    </c:forEach>
</select>
<input type="text" name="owner_id" value=""><br><br>
Name:<br>
<input type="text" name="name" value=""><br><br>
Privately_owned:<br>
<input type="text" name="privately_owned" value=""><br><br>
Established:<br>
<input type="text" name="established" value=""><br><br>
Employee_Count:<br>
<input type="text" name="employee_count" value=""><br><br>
<input type="submit" name="view" value="Add and View">
<input type="submit" name="add" value="Add">
</form>

</div>
</div>


</body></html>
