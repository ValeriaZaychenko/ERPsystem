
<spring:message code="settings" var="title" />
<template:main htmlTitle="${title}" >

    <jsp:attribute name="headContent">
        <script src="<c:url value="/resources/js/external/jquery.validate.js" />"></script>
        <script src="<c:url value="/resources/js/password_validate.js" />"></script>
        <script src="<c:url value="/resources/js/external/additional-methods.js" />"></script>
    </jsp:attribute>

    <jsp:body>

        <div class="container">
            <div class="container" style="margin-top:40px">
                <div class="row">
                    <div class="col-sm-6 col-md-4 col-md-offset-4">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <strong><spring:message code="change.password"/></strong>
                            </div>
                            <div class="panel-body">

                              <form role="form" id="changePasswordForm" action="/changePassword/" method="POST" >
                                        <div class="row">
                                            <div class="col-sm-12 col-md-10  col-md-offset-1 ">

                                                <div class="form-group">
                                                    <label for="oldPassword" class="col-sm-2 control-label">
                                                    </label>
                                                    <div class="col-sm-12">
                                                        <div class="input-group">
                                                    <span class="input-group-addon">
                                                        <i class="glyphicon glyphicon-lock"></i>
                                                    </span>
                                                            <input id="oldPassword" class="form-control" placeholder="<spring:message code='old.password'/>" name="oldPassword" type="password">
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="form-group">
                                                    <label for="newPassword" class="col-sm-2 control-label">
                                                    </label>
                                                    <div class="col-sm-12">
                                                        <div class="input-group">
                                                    <span class="input-group-addon">
                                                       <i class="glyphicon glyphicon-lock"></i>
                                                    </span>
                                                            <input id="newPassword" class="form-control" placeholder="<spring:message code='new.password'/>" name="newPassword" type="password">
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="form-group">
                                                    <label for="newPasswordConfirmed" class="col-sm-2 control-label">
                                                    </label>
                                                    <div class="col-sm-12">
                                                        <div class="input-group">
                                                    <span class="input-group-addon">
                                                       <i class="glyphicon glyphicon-lock"></i>
                                                    </span>
                                                            <input id="newPasswordConfirmed" class="form-control" placeholder="<spring:message code='confirm.new.password'/>" name="newPasswordConfirmed" type="password">
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="form-group">
                                                    <div class="col-sm-12">
                                                        <div class="input-group">
                                                            <input type="hidden" name="userId" value="${pageContext.request.userPrincipal.id}">
                                                            <button type="submit" class="btn btn-lg btn-primary btn-block" id="btnChangePassword" >
                                                                <spring:message code="change"/>
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                </form>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            var validateStrings = new Array();
            validateStrings['invalid.value.error.message']  = "<spring:message code='invalid.value.error.message' />";
            validateStrings['invalid.password.error.message']  = "<spring:message code='invalid.date.error.message' />";
            validateStrings['empty.password.error.message'] = "<spring:message code='empty.date.error.message' />";
            validateStrings['mismatch.new.password.error.message'] = "<spring:message code='empty.description.error.message' />";
        </script>
    </jsp:body>
</template:main>