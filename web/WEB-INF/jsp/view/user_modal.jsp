
<div class="modal fade" id="user-modal" tabindex="-1" role="dialog" aria-labelledby="user-modal-label">
  <div class="modal-dialog" role="document">
    <div class="modal-content">

      <!--- Header -->
      <div class="modal-header">
        <button class="close" type="button" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        <h4 class="modal-title" id="user-modal-title"></h4>
      </div>

      <div class="modal-body">
        <form class="form-horizontal" id="user-modal-form">
          <input class="hidden" name="user-id" type="text">

          <!--- User name -->
          <div class="form-group">
            <label class="col-sm-2 control-label" for="user-name">
              <spring:message code="name"/>
            </label>
            <div class="col-sm-9">
              <div class="input-group">
                <span class="input-group-addon">
                  <i class="fa fa-user" aria-hidden="true"></i>
                </span>
                <input class="form-control" id="user-name" name="user-name" type="text" placeholder="<spring:message code="enter.name"/> ">
              </div>
              <span class="help-block"><spring:message code="hint.name"/></span>
            </div>
          </div>

          <!--- User email -->
          <div class="form-group">
            <label class="col-sm-2 control-label" for="user-email">
              <spring:message code="email"/>
            </label>
            <div class="col-sm-9">
              <div class="input-group">
                <span class="input-group-addon">
                  <i class="fa fa-envelope-o" aria-hidden="true"></i>
                </span>
                <input class="form-control" id="user-email" name="user-email" type="text" placeholder="<spring:message code="enter.email"/> ">
              </div>
              <span class="help-block"><spring:message code="hint.email"/></span>
            </div>
          </div>

          <!--- User role -->
          <div class="form-group">
            <label class="col-sm-2 control-label" for="user-role">
              <spring:message code="position"/>
            </label>
            <div class="col-sm-9">
              <div class="input-group">
                <span class="input-group-addon">
                  <i class="fa fa-briefcase" aria-hidden="true"></i>
                </span>
                <select class="form-control" id="user-role">
                  <c:forEach items="${possibleUserRoles}" var="possibleUserRole">
                    <option>${possibleUserRole}</option>
                  </c:forEach>
                </select>
              </div>
            </div>
          </div>

          <div class="form-group">
            <div class="col-sm-9 col-sm-offset-2">

              <!--- Error hint -->
              <div class="alert alert-danger" id="error-hint" hidden></div>

              <div class="input-group">

                <!--- Add/edit user button -->
                <button type="button" class="btn btn-primary" id="btnSaveUser">
                  <spring:message code="save"/>
                  <input type="text" id="add-or-edit" name="add-or-edit" class="hidden">
                </button>

                <!--- Close modal button -->
                <button type="button" class="btn btn-default" data-dismiss="modal">
                  <spring:message code="close"/>
                </button>

              </div>
            </div>
          </div>

        </form>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
    var validateModalStrings =
    {
        'invalid.value.error.message': "<spring:message code='invalid.value.error.message' />",
        'empty.name.error.message' : "<spring:message code='empty.name.error.message' />",
        'invalid.name.error.message' : "<spring:message code='invalid.name.error.message' />",
        'empty.email.error.message' : "<spring:message code='empty.email.error.message' />",
        'invalid.email.error.message' : "<spring:message code='invalid.email.error.message' />"
    };
</script>