<%--@elvariable id="users" type="java.util.List<erp.domain.User>"--%>

<spring:message code="title.users" var="title" />
<template:main htmlTitle="${title}" >

    <jsp:attribute name="headContent">
        <script src="<c:url value="/resources/js/user.js" />"></script>
    </jsp:attribute>

    <jsp:body>
        <c:choose>

            <c:when test="${fn:length(users) == 0}">
                <h2><spring:message code="no.users.in.system"/> </h2>
                <button type="button" class="btn btn-primary" id="addUserBtn">
                    <spring:message code="add.user"/>
                </button>
            </c:when>

            <c:otherwise>
                <div class="col-md-6">
                     <div class="table-responsive">
                        <table id="users-table" class="table table-hover">
                            <thead>
                                <tr>
                                    <th><input type="checkbox" id="checkall" /></th>
                                    <th class="text-center"> <spring:message code="name"/> </th>
                                    <th class="text-center"> <spring:message code="email"/> </th>
                                    <th class="text-center"> <spring:message code="position"/> </th>
                                    <th class="text-center"> <spring:message code="actions"/> </th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${users}" var="user">
                                    <tr id="users-row-${user.id}">
                                        <th scope="row"><input type="checkbox" class="checkthis" /></th>
                                        <td>${user.name}</td>
                                        <td>${user.email}</td>
                                        <td>${user.userRole}</td>
                                        <td class="text-center">
                                            <a class='editUserBtnClass btn btn-info btn-xs'>
                                                <i class="fa fa-pencil" aria-hidden="true"></i>
                                                <spring:message code="edit" />
                                            </a>
                                            <a class="btn btn-danger btn-xs" onclick="deleteUser('${user.id}')">
                                                <i class="fa fa-trash" aria-hidden="true"></i>
                                                <spring:message code="delete" />
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                         <button type="button" class="btn btn-primary" id="addUserBtn2">
                             <spring:message code="add.user"/>
                         </button>
                     </div>
                </div>
            </c:otherwise>

        </c:choose>

        <!--MODAL FOR ADD EDIT USER---------------------------------------------------------->
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
                        <form>
                            <div class="form-group">
                                <label for="user-name" class="control-label">
                                    <spring:message code="name"/>
                                </label>
                                <input type="text" class="form-control" id="user-name" name="user-name" placeholder="<spring:message code="enter.name"/> ">
                            </div>
                            <div class="form-group">
                                <label for="user-email" class="control-label">
                                    <spring:message code="email"/>
                                </label>
                                <input type="text" class="form-control" id="user-email" name="user-email" placeholder="<spring:message code="enter.email"/> ">
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            <spring:message code="close"/>
                        </button>
                        <button type="button" class="btn btn-primary">
                            <spring:message code="save"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <script type="text/javascript">
            var strings = new Array();
            strings['title.add']  = "<spring:message code='add.user' />";
            strings['title.edit'] = "<spring:message code='edit.user' />";
            strings['confirm.delete'] = "<spring:message code='confirm.user.delete' />";
        </script>


    </jsp:body>
</template:main>