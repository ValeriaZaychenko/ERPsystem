<%--@elvariable id="users" type="java.util.List<erp.domain.User>"--%>

<spring:message code="title.users" var="title" />
<template:main htmlTitle="${title}" >

    <jsp:body>
        <c:choose>

            <c:when test="${fn:length(users) == 0}">
                <h2><spring:message code="no.users.in.system"/> </h2>
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addUser" data-whatever="@mdo">
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
                                    <tr>
                                        <th scope="row"><input type="checkbox" class="checkthis" /></th>
                                        <td>${user.name}</td>
                                        <td>${user.email}</td>
                                        <td>${user.userRole}</td>
                                        <td class="text-center">
                                            <a class='btn btn-info btn-xs' href="#">
                                                <i class="fa fa-pencil" aria-hidden="true"></i>
                                                <spring:message code="edit" />
                                            </a>
                                            <a href="/users/${user.id}/delete" class="btn btn-danger btn-xs">
                                                <i class="fa fa-trash" aria-hidden="true"></i>
                                                <spring:message code="delete" />
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                         <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#addUser" data-whatever="@mdo">
                             <spring:message code="add.user"/>
                         </button>
                     </div>
                </div>
            </c:otherwise>

        </c:choose>

        <!--MODAL FOR ADD EDIT USER---------------------------------------------------------->
        <div class="modal fade" id="addUserModal" tabindex="-1" role="dialog" aria-labelledby="addUserModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title" id="addUserModalLabel">
                            <spring:message code="add.user"/>
                        </h4>
                    </div>
                    <div class="modal-body">
                        <form>
                            <div class="form-group">
                                <label for="recipient-name" class="control-label">
                                    <spring:message code="name"/>
                                </label>
                                <input type="text" class="form-control" id="recipient-name">
                            </div>
                            <div class="form-group">
                                <label for="message-text" class="control-label">
                                    <spring:message code="email"/>
                                </label>
                                <textarea class="form-control" id="message-text"></textarea>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            <spring:message code="close"/>
                        </button>
                        <button type="button" class="btn btn-primary">
                            <spring:message code="add"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>


    </jsp:body>
</template:main>