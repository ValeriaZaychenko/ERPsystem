
<div class="modal fade" id="userModal" tabindex="-1" role="dialog" aria-labelledby="userModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="userModal-title">
                </h4>
            </div>

            <div class="modal-body">
                <form id="userModalForm" class="form-horizontal">
                    <input type="text" name="user-id" class="hidden">

                    <div class="form-group">
                        <label for="user-name" class="col-sm-2 control-label">
                            <spring:message code="name"/>
                        </label>
                        <div class="col-sm-9">
                            <div class="input-group">
                                <span class="input-group-addon">
                                    <i class="fa fa-user" aria-hidden="true"></i>
                                </span>
                                <input type="text" class="form-control" id="user-name" name="user-name" placeholder="<spring:message code="enter.name"/> ">
                            </div>
                            <span class="help-block"><spring:message code="hint.name"/></span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="user-email" class="col-sm-2 control-label">
                            <spring:message code="email"/>
                        </label>
                        <div class="col-sm-9">
                            <div class="input-group">
                                <span class="input-group-addon">
                                    <i class="fa fa-envelope-o" aria-hidden="true"></i>
                                </span>
                                <input type="text" class="form-control" id="user-email" name="user-email" placeholder="<spring:message code="enter.email"/> ">
                            </div>
                            <span class="help-block"><spring:message code="hint.email"/></span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="user-role" class="col-sm-2 control-label">
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

                            <div id="errorHint" class="alert alert-danger" hidden></div>

                            <div class="input-group">
                                <button type="button" class="btn btn-primary" id="btnSaveUser">
                                    <spring:message code="save"/>
                                    <input type="text" id="add-or-edit" name="add-or-edit" class="hidden">
                                </button>
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
    var validateModalStrings = new Array();
    validateModalStrings['invalid.value.error.message']  = "<spring:message code='invalid.value.error.message' />";
    validateModalStrings['empty.name.error.message']  = "<spring:message code='empty.name.error.message' />";
    validateModalStrings['invalid.name.error.message'] = "<spring:message code='invalid.name.error.message' />";
    validateModalStrings['empty.email.error.message']  = "<spring:message code='empty.email.error.message' />";
    validateModalStrings['invalid.email.error.message'] = "<spring:message code='invalid.email.error.message' />";
</script>