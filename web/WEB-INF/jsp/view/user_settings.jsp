
<spring:message code="settings" var="title" />
<template:main htmlTitle="${title}" >

  <jsp:attribute name="headContent">
    <script src="<c:url value="/resources/js/external/jquery.validate.js" />"></script>
    <script src="<c:url value="/resources/js/password_validate.js" />"></script>
    <script src="<c:url value="/resources/js/external/additional-methods.js" />"></script>
  </jsp:attribute>

  <jsp:body>

    <!--- Change password -->
    <div class="container">
      <div class="container" style="margin-top:40px">
        <div class="row">
          <div class="col-sm-6 col-md-4 col-md-offset-4">
            <div class="panel panel-default">

              <!--- Header -->
              <div class="panel-heading">
                <strong><spring:message code="change.password"/></strong>
              </div>
              <div class="panel-body">

                <!--- Chage password form -->
                <form role="form" id="change-password-form" action="/changePassword/" method="POST" >
                  <div class="row">
                    <div class="col-sm-12 col-md-10  col-md-offset-1 ">

                      <!--- Old password -->
                      <div class="form-group">
                        <label class="col-sm-2 control-label" for="oldPassword"></label>
                        <div class="col-sm-12">
                          <div class="input-group">
                            <span class="input-group-addon">
                              <i class="glyphicon glyphicon-lock"></i>
                            </span>
                            <input class="form-control" id="oldPassword" name="oldPassword" type="password" placeholder="<spring:message code='old.password'/>">
                          </div>
                        </div>
                      </div>

                      <!--- New password -->
                      <div class="form-group">
                        <label class="col-sm-2 control-label" for="newPassword"></label>
                        <div class="col-sm-12">
                          <div class="input-group">
                            <span class="input-group-addon">
                              <i class="glyphicon glyphicon-lock"></i>
                            </span>
                            <input class="form-control" id="newPassword" name="newPassword" type="password" placeholder="<spring:message code='new.password'/>">
                          </div>
                        </div>
                      </div>

                      <!--- Confirm new password -->
                      <div class="form-group">
                        <label class="col-sm-2 control-label" for="newPasswordConfirmed"></label>
                        <div class="col-sm-12">
                          <div class="input-group">
                            <span class="input-group-addon">
                              <i class="glyphicon glyphicon-lock"></i>
                            </span>
                            <input class="form-control" id="newPasswordConfirmed" name="newPasswordConfirmed" type="password" placeholder="<spring:message code='confirm.new.password'/>">
                          </div>
                        </div>
                      </div>

                      <!--- Change password button -->
                      <div class="form-group">
                        <div class="col-sm-12">
                          <div class="input-group">
                            <input name="userId" type="hidden" value="${pageContext.request.userPrincipal.id}">
                            <button class="btn btn-lg btn-primary btn-block" id="btnChangePassword" type="submit">
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
        var validateStrings =
        {
            'invalid.value.error.message' : "<spring:message code='invalid.value.error.message' />",
            'invalid.password.error.message' : "<spring:message code='invalid.date.error.message' />",
            'empty.password.error.message' : "<spring:message code='empty.date.error.message' />",
            'mismatch.new.password.error.message' : "<spring:message code='empty.description.error.message' />"
        };
    </script>
  </jsp:body>
</template:main>