
<spring:message code="settings" var="title" />
<template:main htmlTitle="${title}" >

    <jsp:attribute name="headContent">
        <script src="<c:url value="/resources/js/user.js" />"></script>
        <script src="<c:url value="/resources/js/user_validate.js" />"></script>
        <jsp:include page="/WEB-INF/jsp/view/user_modal.jsp" />
    </jsp:attribute>

    <jsp:body>
        <div class="container">
            <div class="container" style="margin-top:40px">
                <div class="row">
                    <div class="col-sm-6 col-md-4 col-md-offset-4">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <strong><spring:message code="sign.in.to.continue"/></strong>
                            </div>
                            <div class="panel-body">
                                <form role="form" action="/changePassword/" method="POST">
                                    <fieldset>
                                        <div class="row">
                                            <div class="col-sm-12 col-md-10  col-md-offset-1 ">
                                                <div class="form-group">
                                                    <div class="input-group">
                                                    <span class="input-group-addon">
                                                        <i class="glyphicon glyphicon-lock"></i>
                                                    </span>
                                                        <input class="form-control" placeholder="Old password" name="oldPassword" type="password" autofocus>
                                                    </div>
                                                </div>
                                                <input type="hidden" name="userId" value="${sessionScope.USER.id}">
                                                <div class="form-group">
                                                    <div class="input-group">
                                                    <span class="input-group-addon">
                                                        <i class="glyphicon glyphicon-lock"></i>
                                                    </span>
                                                        <input class="form-control" placeholder="New password" name="newPassword" type="password" value="">
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <input type="submit" class="btn btn-lg btn-primary btn-block" value="Change password">
                                                </div>
                                            </div>
                                        </div>
                                    </fieldset>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</template:main>