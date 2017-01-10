<spring:message code="title.holiday" var="holidayPageTitle" />
<template:main htmlTitle="${holidayPageTitle}" >

    <jsp:attribute name="headContent">
        <script src="<c:url value="/resources/js/holiday_validate.js" />"></script>
        <script src="<c:url value="/resources/js/holiday.js" />"></script>
        <link rel="stylesheet" href="<c:url value="/resources/css/external/datepicker.css" />" />
        <script src="<c:url value="/resources/js/external/bootstrap-datepicker.js" />"></script>
        <script src="<c:url value="/resources/js/custom_datepicker.js" />"></script>
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
                            ${holidaysYear}
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

                                                <a class="btn btn-danger btn-xs" onclick="cloneHoliday('${h.id}')">
                                                    <i class="fa fa-clone" aria-hidden="true"></i>
                                                    <spring:message code="clone" />
                                                </a>
                                            </td>
                                        </tr>
                                    </form>
                                </c:forEach>
                            </tbody>
                        </table>
                        <a class="btn btn-danger btn-xs" onclick="cloneHolidays('${holidaysYear}')">
                            <i class="fa fa-clone" aria-hidden="true"></i>
                            <spring:message code="clone.holidays.to.next.year" />
                        </a>
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
            <div class='col-md-6'>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <strong><spring:message code="pick.holidays.year"/></strong>
                    </div>
                    <div class="panel-body">
                        <form role="form" id="pickYearHolidayForm" action="/holidays" method="GET">
                            <fieldset>
                                <div class="row">
                                    <div class="col-sm-12 col-md-10  col-md-offset-1 ">

                                        <div class="form-group">
                                            <label for="datepicker_year" class="col-sm-2 control-label">
                                            </label>
                                            <div class="col-sm-12">
                                                <div class="input-append date" id="datepicker_year" data-date-format="yyyy" data-date="2017">
                                                    <input  type="text" readonly="readonly" name="year" >
                                                    <span class="add-on"><i class="fa fa-th"></i></span>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-sm-12">
                                                <div class="input-group">
                                                    <input type="submit" value="<spring:message code="view.holidays"/>" name="View Holidays" />
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
            validateModalStrings['empty.description.error.message'] = "<spring:message code='empty.description.error.message' />";
            strings['confirm.delete'] = "<spring:message code='confirm.holiday.delete' />";
            strings['confirm.clone'] = "<spring:message code='confirm.holidays.clone' />";
            strings['confirm.clone.one'] = "<spring:message code='confirm.holiday.clone' />";
        </script>
    </jsp:body>

</template:main>