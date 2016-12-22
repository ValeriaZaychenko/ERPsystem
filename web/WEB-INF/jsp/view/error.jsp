<%--@elvariable id="errorTitle" type="java.lang.String"--%>
<%--@elvariable id="errorMessage" type="java.lang.String"--%>
<%--@elvariable id="errorAttribute" type="java.lang.String"--%>
<spring:message code="title.error" var="errorPageTitle" />
<template:main htmlTitle="${errorPageTitle}" >

    <jsp:body>

        <div class="container content-wrap">

        <div class="row">
            <div class="col-xs-10 col-xs-offset-1" >
                <h1><spring:message code="something.went.wrong"/></h1>
                <h2><spring:message code="${errorMessage}"/></h2>
                ${errorAttribute}
            </div>
        </div>

    </jsp:body>

</template:main>