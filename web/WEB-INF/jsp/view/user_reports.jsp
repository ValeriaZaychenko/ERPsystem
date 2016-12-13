<%--@elvariable id="user.reports" type="java.util.List<erp.domain.Report>"--%>

<spring:message code="title.user.reports" var="title" />
<template:main htmlTitle="${title}" >

    <jsp:attribute name="headContent">
        <script src="<c:url value="/resources/js/report_validate.js" />"></script>
        <script src="<c:url value="/resources/js/report.js" />"></script>
    </jsp:attribute>

    <jsp:body>
        <div class="row">
            <c:choose>
                <c:when test="${fn:length(userReports) == 0}">
                    <div class="col-lg-6 col-md-6 col-sm-6">
                        <h2><spring:message code="user.have.no.reports"/> </h2>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="col-lg-6 col-md-6 col-sm-6">
                        <table id="reporttable" class="table">
                            <thead>
                            <tr>
                                <th><spring:message code="date"/></th>
                                <th><spring:message code="time"/></th>
                                <th><spring:message code="description"/></th>
                                <th><spring:message code="actions"/></th>
                            </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${userReports}" var="report">
                                    <form role="form" id="report-form">
                                        <tr id="reports-row-${report.id}">
                                            <td><span class="date">${report.date}</span></td>
                                            <td><span class="time">${report.workingTime}</span></td>
                                            <td><span class="description">${report.description}</span></td>
                                            <td>
                                                <input type="hidden" class="reportId" name="reportId" value="${report.id}">
                                                <a class='editReportBtnClass btn btn-info btn-xs'>
                                                    <i class="fa fa-pencil" aria-hidden="true"></i>
                                                        <spring:message code="edit" />
                                                </a>

                                                <a class="btn btn-danger btn-xs" onclick="deleteReport('${report.id}')">
                                                    <i class="fa fa-trash" aria-hidden="true"></i>
                                                    <spring:message code="delete" />
                                                </a>
                                            </td>
                                        </tr>
                                    </form>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>

            <div class='col-md-6'>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <strong><spring:message code="add.report"/></strong>
                    </div>

                    <div class="panel-body">
                        <form role="form" id="reportForm" action="/reports/add" method="POST">
                            <fieldset>
                                <div class="row">
                                    <div class="col-sm-12 col-md-10  col-md-offset-1 ">

                                        <div class="form-group">
                                            <label for="date" class="col-sm-2 control-label">
                                            </label>
                                            <div class="col-sm-12">
                                                <div class="input-group">
                                                    <span class="input-group-addon">
                                                       <i class="fa fa-calendar" aria-hidden="true"></i>
                                                    </span>
                                                    <input id="date" class="form-control" placeholder="<spring:message code='date'/>" name="date" type="date" autofocus>
                                                </div>
                                                <span class="help-block"><spring:message code="hint.date"/></span>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="time" class="col-sm-2 control-label">
                                            </label>
                                            <div class="col-sm-12">
                                                <div class="input-group">
                                                    <span class="input-group-addon">
                                                        <i class="fa fa-clock-o" aria-hidden="true"></i>
                                                    </span>
                                                    <input id="time" class="form-control" placeholder="<spring:message code='time'/>" name="time" type="number" value="">
                                                </div>
                                                <span class="help-block"><spring:message code="hint.time"/></span>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="description" class="col-sm-2 control-label">
                                            </label>
                                            <div class="col-sm-12">
                                                <div class="input-group">
                                                    <span class="input-group-addon">
                                                        <i class="fa fa-thumb-tack" aria-hidden="true"></i>
                                                    </span>
                                                    <input id="description" class="form-control" placeholder="<spring:message code='description'/>" name="description" type="text" value="">
                                                </div>
                                                <span class="help-block"><spring:message code="hint.description"/></span>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-sm-12">
                                                <div class="input-group">
                                                    <input type="hidden" class="reportId" name="reportId" value="">
                                                    <button type="button" class="btn btn-lg btn-primary btn-block" id="btnSaveReport">
                                                        <spring:message code="save"/>
                                                        <input type="text" id="add-or-edit" name="add-or-edit" class="hidden">
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                        <!--

                                        <div class="form-group">
                                            <div class="col-sm-12">
                                                <input type="submit" id="btnSaveReport2" class="btn btn-lg btn-primary btn-block" value="<spring:message code='add'/>">
                                            </div>
                                        </div>
                                        -->
                                    </div>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            var validateModalStrings = new Array();
            var strings = new Array();
            validateModalStrings['invalid.value.error.message']  = "<spring:message code='invalid.value.error.message' />";
            validateModalStrings['invalid.date.error.message']  = "<spring:message code='invalid.date.error.message' />";
            validateModalStrings['empty.date.error.message'] = "<spring:message code='empty.date.error.message' />";
            validateModalStrings['empty.time.error.message']  = "<spring:message code='empty.time.error.message' />";
            validateModalStrings['invalid.time.error.message'] = "<spring:message code='invalid.time.error.message' />";
            validateModalStrings['empty.description.error.message'] = "<spring:message code='empty.description.error.message' />";
            strings['confirm.delete'] = "<spring:message code='confirm.report.delete' />";
        </script>
    </jsp:body>
</template:main>