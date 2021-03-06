<%--@elvariable id="users" type="java.util.List<erp.domain.User>"--%>

<spring:message code="title.users" var="title" />
<template:main htmlTitle="${title}" >

  <jsp:attribute name="headContent">
    <script src="<c:url value="/resources/js/user.js" />"></script>
    <script src="<c:url value="/resources/js/user_validate.js" />"></script>

    <jsp:include page="/WEB-INF/jsp/view/user_modal.jsp" />
  </jsp:attribute>

  <jsp:body>
    <div class="col-md-6">
      <c:choose>

        <c:when test="${fn:length(users) == 0}">
          <!--- Empty users view -->
          <h2><spring:message code="no.users.in.system"/> </h2>
        </c:when>

        <c:otherwise>
           <!--- Users table -->
           <div class="table-responsive">
            <table class="table table-hover" id="users-table">

              <thead>
                <tr>
                  <th><input id="checkall" type="checkbox" /></th>
                  <th class="text-center"> <spring:message code="name"/> </th>
                  <th class="text-center"> <spring:message code="email"/> </th>
                  <th class="text-center"> <spring:message code="position"/> </th>
                  <th class="text-center"> <spring:message code="actions"/> </th>
                </tr>
              </thead>

              <tbody>
                <c:forEach items="${users}" var="user">
                  <tr id="users-row-${user.id}">
                    <th scope="row"><input class="checkthis" type="checkbox" /></th>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.userRole}</td>
                    <td class="text-center">
                      <a class='edit-user-btn btn btn-info btn-xs'>
                        <i class="fa fa-pencil" aria-hidden="true"></i>
                        <spring:message code="edit" />
                        <div class="hidden user-id">${user.id}</div>
                        <div class="hidden user-name">${user.name}</div>
                        <div class="hidden user-email">${user.email}</div>
                        <div class="hidden user-role">${user.userRole}</div>
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
           </div>
        </c:otherwise>
      </c:choose>

      <!-- Add user button invoke user_modal -->
      <button class="btn btn-primary" id="add-user-btn" type="button">
        <spring:message code="add.user"/>
      </button>
    </div>

    <script type="text/javascript">
        var strings =
        {
            'title.add' : "<spring:message code='add.user' />",
            'title.edit' : "<spring:message code='edit.user' />",
            'confirm.delete' : "<spring:message code='confirm.user.delete' />"
        };
    </script>

  </jsp:body>
</template:main>