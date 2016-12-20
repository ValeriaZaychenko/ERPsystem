<spring:message code="title.progress" var="progressPageTitle" />
<template:main htmlTitle="${progressPageTitle}" >

    <jsp:attribute name="headContent">
        <link rel="stylesheet" href="<c:url value="/resources/css/progress.css" />" />
    </jsp:attribute>

    <jsp:body>
        <div class="table-responsive">
            <table id="progress-table" class="table table-hover">
                <thead>
                <tr>
                    <th class="text-center"> <spring:message code="name"/> </th>
                    <th class="text-center"> <spring:message code="progress"/> </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${progress}" var="p">
                    <tr id="users-row-${p.userId}">
                        <td>${p.userName}</td>
                        <td>
                            <div class="progress">
                                <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="" aria-valuemin="0" aria-valuemax="100" style="width: ${p.progress}%">
                                    <span class="sr-only">${p.progress}% Complete (success)</span>
                                </div>
                                <span class="progress-completed">${p.progress}%</span>
                            </div>
                       </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </jsp:body>

</template:main>
