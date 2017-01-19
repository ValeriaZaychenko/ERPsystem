<spring:message code="title.holiday" var="holidayPageTitle" />
<template:main htmlTitle="${holidayPageTitle}" >

  <jsp:attribute name="headContent">
    <link rel="stylesheet" href="<c:url value="/resources/css/external/datepicker.css" />" />

    <script src="<c:url value="/resources/js/holiday_validate.js" />"></script>
    <script src="<c:url value="/resources/js/holiday.js" />"></script>
    <script src="<c:url value="/resources/js/external/bootstrap-datepicker.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap-datepicker.ru.js" />" charset="UTF-8"></script>
    <script src="<c:url value="/resources/js/custom_datepicker.js" />"></script>
  </jsp:attribute>

  <jsp:body>
    <div class="row">

      <!--- Empty holidays view -->
      <c:choose>
        <c:when test="${fn:length(holiday) == 0}">
          <div class="col-lg-6 col-md-6 col-sm-6">
            <h2><spring:message code="no.holidays.this.year"/> </h2>
          </div>
        </c:when>

        <c:otherwise>
          <!--- Holidays table -->
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
                  <form role="form">
                    <tr id="holiday-row-${h.id}">
                      <td><span class="date">${h.date}</span></td>
                      <td><span class="description">${h.description}</span></td>
                      <td>
                        <input class="holidayId" name="holidayId" type="hidden" value="${h.id}">
                        <a class='edit-holiday-btn btn btn-info btn-xs'>
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

      <!--- Add/edit holiday form -->
      <div class='col-md-6'>
        <div class="panel panel-default">

          <div class="panel-heading">
            <strong><spring:message code="add.holiday"/></strong>
          </div>

          <div class="panel-body">
            <form role="form" id="holiday-form" action="/holidays/add" method="POST">
              <fieldset>
                <div class="row">
                  <div class="col-sm-12 col-md-10  col-md-offset-1 ">

                    <!--- Holiday date -->
                    <div class="form-group">
                      <label class="col-sm-2 control-label" for="date"></label>
                      <div class="col-sm-12">
                        <div class="input-group">
                          <span class="input-group-addon">
                             <i class="fa fa-calendar" aria-hidden="true"></i>
                          </span>
                          <input class="form-control" id="date" name="date" type="date" placeholder="<spring:message code='date'/>" autofocus>
                        </div>
                        <span class="help-block"><spring:message code="hint.date"/></span>
                      </div>
                    </div>

                    <!--- Holiday description -->
                    <div class="form-group">
                      <label class="col-sm-2 control-label" for="description"></label>
                      <div class="col-sm-12">
                        <div class="input-group">
                          <span class="input-group-addon">
                            <i class="fa fa-thumb-tack" aria-hidden="true"></i>
                          </span>
                          <input  class="form-control" id="description" name="description" type="text" value="" placeholder="<spring:message code='description'/>">
                        </div>
                        <span class="help-block"><spring:message code="hint.description"/></span>
                      </div>
                    </div>

                    <!--- Add/edir button -->
                    <div class="form-group">
                      <div class="col-sm-12">
                        <div class="input-group">
                          <input class="holidayId" name="holidayId" type="hidden" value="">
                          <button class="btn btn-lg btn-primary btn-block" id="save-holiday-btn" type="button" disabled="disabled">
                            <spring:message code="save"/>
                            <input class="hidden" id="add-or-edit" name="add-or-edit" type="text">
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

      <!--- Pick year to view holidays panel -->
      <div class='col-md-6'>
        <div class="panel panel-default">

          <div class="panel-heading">
            <strong><spring:message code="pick.holidays.year"/></strong>
          </div>

          <div class="panel-body">
            <form role="form" action="/holidays" method="GET">
              <fieldset>
                <div class="row">
                  <div class="col-sm-12 col-md-10  col-md-offset-1 ">

                    <div class="form-group">
                      <label class="col-sm-2 control-label" for="datepicker-year"></label>
                      <div class="col-sm-12">
                        <div class="input-append date" id="datepicker-year" data-date-format="yyyy" data-date="2017" >
                          <input name="year" type="text" readonly="readonly">
                          <span class="add-on"><i class="fa fa-th"></i></span>
                        </div>
                      </div>
                    </div>

                    <div class="form-group">
                      <div class="col-sm-12">
                        <div class="input-group">
                          <input name="View Holidays" type="submit" value="<spring:message code="view.holidays"/>" />
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
        var validateModalStrings =
        {
            'invalid.value.error.message' : "<spring:message code='invalid.value.error.message' />",
            'invalid.date.error.message' : "<spring:message code='invalid.date.error.message' />",
            'empty.date.error.message' : "<spring:message code='empty.date.error.message' />",
            'empty.description.error.message' : "<spring:message code='empty.description.error.message' />"
        };

        var strings =
        {
            'confirm.delete' : "<spring:message code='confirm.holiday.delete' />",
            'confirm.clone' : "<spring:message code='confirm.holidays.clone' />",
            'confirm.clone.one' : "<spring:message code='confirm.holiday.clone' />"
        };
    </script>

  </jsp:body>
</template:main>