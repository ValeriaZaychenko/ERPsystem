<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<template:basic htmlTitle="Groups" bodyTitle="Groups">
    <c:choose>
        <c:when test="${fn:length(users) == 0}">
            <i>There are no users in the system.</i>
        </c:when>
        <c:otherwise>
            <c:forEach items="${users}" var="user">
                User ${user.name}
                <br>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</template:basic>