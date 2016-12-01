<spring:message code="title.delete.users" var="title" />
<template:main htmlTitle="${title}" >
    <jsp:body>
        <div class="row">
            <div class="col-md-12">
                <div class="portlet light">
                    <div class="portlet-title">
                        <div class="caption font-green-haze">
                            <i class="icon-settings font-green haze"></i>
                            <span class="caption-subject bold uppercase">
                                Title
                            </span>
                        </div>
                    </div>
                    <div class="portlet-body form">
                        <form role="form" class="form-horizontal" action="<c:url value="/users/delete"/>"
                              method="post">
                            <input type="hidden" name="id" value="${user.id}">
                            <p>
                                <spring:message code="confirm.user.delete"/> "${user.name}"
                            </p>

                            <div class="form-actions">
                                <div class="row">
                                    <div class="col-md-12">
                                        <a href="javascript:history.go(-1)" class="btn default">
                                            <spring:message code="cancel"/>
                                        </a>
                                        <button type="submit" class="btn blue">
                                            <spring:message code="submit"/>
                                        </button>
                                    </div>
                                </div>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</template:main>
