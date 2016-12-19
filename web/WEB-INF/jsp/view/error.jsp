<%--@elvariable id="errorTitle" type="java.lang.String"--%>
<%--@elvariable id="errorMessage" type="java.lang.String"--%>
<%--@elvariable id="errorArguments" type="java.lang.String"--%>
<spring:message code="title.error" var="errorPageTitle" />
<template:main htmlTitle="${errorPageTitle}" >

    <jsp:body>

        <div class="container content-wrap">

        <div class="row">
            <div class="col-xs-10 col-xs-offset-1" >
                <h2>${errorMessage}</h2>
            </div>
        </div>

    </jsp:body>

</template:main>