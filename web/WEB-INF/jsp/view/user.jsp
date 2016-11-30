<%--@elvariable id="users" type="java.util.List<erp.domain.User>"--%>

<template:main htmlTitle="Users">

    <jsp:body>

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

    </jsp:body>

</template:main>