<spring:message code="title.holiday" var="holidayPageTitle" />
<template:main htmlTitle="${holidayPageTitle}" >

    <jsp:attribute name="headContent">
        <script src="<c:url value="/resources/js/holiday_validate.js" />"></script>
        <script src="<c:url value="/resources/js/holiday.js" />"></script>
    </jsp:attribute>

    <jsp:body>
        <div class="row">
            <c:choose>
                <c:when test="${fn:length(holiday) == 0}">
                    <div class="col-lg-6 col-md-6 col-sm-6">
                        <h2><spring:message code="no.holidays.this.year"/> </h2>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="col-lg-6 col-md-6 col-sm-6">
                        <table id="holidaytable" class="table">
                            <thead>
                                <tr>
                                    <th class="text-center"> <spring:message code="date"/> </th>
                                    <th class="text-center"> <spring:message code="description"/> </th>
                                    <th><spring:message code="actions"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${holiday}" var="h">
                                    <form role="form" id="holiday-form">
                                        <tr id="holiday-row-${h.id}">
                                            <td><span class="date">${h.date}</span></td>
                                            <td><span class="description">${h.description}</span></td>
                                            <td>
                                                <input type="hidden" class="holidayId" name="holidayId" value="${h.id}">
                                                <a class='editHolidayBtnClass btn btn-info btn-xs'>
                                                    <i class="fa fa-pencil" aria-hidden="true"></i>
                                                    <spring:message code="edit" />
                                                </a>

                                                <a class="btn btn-danger btn-xs" onclick="deleteHoliday('${h.id}')">
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
                        <strong><spring:message code="add.holiday"/></strong>
                    </div>

                    <div class="panel-body">
                        <form role="form" id="holidayForm" action="/holidays/add" method="POST">
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
                                                    <input type="hidden" class="holidayId" name="holidayId" value="">
                                                    <button type="button" class="btn btn-lg btn-primary btn-block" id="btnSaveHoliday" disabled="disabled">
                                                        <spring:message code="save"/>
                                                        <input type="text" id="add-or-edit" name="add-or-edit" class="hidden">
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
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
            validateModalStrings['empty.duration.error.message']  = "<spring:message code='empty.duration.error.message' />";
            validateModalStrings['invalid.duration.error.message'] = "<spring:message code='invalid.duration.error.message' />";
            validateModalStrings['empty.description.error.message'] = "<spring:message code='empty.description.error.message' />";
            strings['confirm.delete'] = "<spring:message code='confirm.report.delete' />";
        </script>
    </jsp:body>

</template:main>