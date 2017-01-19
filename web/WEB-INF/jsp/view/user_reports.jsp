<%--@elvariable id="user.reports" type="java.util.List<erp.domain.Report>"--%>

<spring:message code="title.user.reports" var="title" />
<template:main htmlTitle="${title}" >

  <jsp:attribute name="headContent">
    <link rel="stylesheet" href="<c:url value="/resources/css/reports.css" />" />
    <link rel="stylesheet" href="<c:url value="/resources/css/external/datepicker.css" />" />

    <script src="<c:url value="/resources/js/report_validate.js" />"></script>
    <script src="<c:url value="/resources/js/report.js" />"></script>
    <script src="<c:url value="/resources/js/external/bootstrap-datepicker.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap-datepicker.ru.js" />" charset="UTF-8"></script>
    <script src="<c:url value="/resources/js/custom_datepicker.js" />"></script>
  </jsp:attribute>

  <jsp:body>
  <div class="row">

    <!--- Filter panel -->
    <div class="collapse" id="filter-panel">
      <div class="panel panel-default">
        <div class="panel-body">
          <form class="form-inline" role="form">

            <!--- Filter -->
            <div class="form-group">
              <label class="filter-col" for="filter-select">
                <spring:message code="filter" />
              </label>
              <select class="form-control" id="filter-select">
                <option value="filter-option-current-month">
                  <spring:message code="current.month" />
                </option>
                <option value="filter-option-other-month">
                  <spring:message code="other.month" />
                </option>
                <option value="filter-option-current-day">
                  <spring:message code="today" />
                </option>
                <option value="filter-option-other-day">
                  <spring:message code="other.day" />
                </option>
              </select>
            </div>

            <!--- Datepicker for other month option -->
            <div class="form-group" id="other-month-picker">
              <label class="filter-col" for="filter-month">
                <spring:message code="pick.month" />
              </label>
              <div class="input-append date form-control" id="datepicker-month" data-date-format="yyyy-mm" data-date="2017-01">
                <input class="input-sm" id="filter-month" name="filter-month" type="text" readonly="readonly">
                <span class="add-on"><i class="fa fa-th"></i></span>
              </div>
            </div>

            <!--- Datepicker for other day option -->
            <div class="form-group" id="other-day-picker">
              <label class="col-sm-2 control-label" for="filter-date"></label>
              <div class="col-sm-12">
                <div class="input-group">
                  <span class="input-group-addon">
                    <i class="fa fa-calendar" aria-hidden="true"></i>
                  </span>
                  <input class="form-control" id="filter-date" name="filter-date" type="date" placeholder="<spring:message code='date'/>" autofocus>
                </div>
              </div>
            </div>

            <!--- Search (autocomplete in future) -->
            <div class="form-group">
              <label class="filter-col" for="search">
                <spring:message code="search" />
              </label>
              <input class="form-control input-sm" id="search" type="text">
            </div>

            <!--- Order by -->
            <div class="form-group">
              <label class="filter-col" for="orderby">
                <spring:message code="order.by" />
              </label>
              <select class="form-control" id="orderby">
                <option><spring:message code="date" /></option>
                <option><spring:message code="duration" /></option>
                <option><spring:message code="description" /></option>
                <option><spring:message code="remote" /></option>
              </select>
            </div>

            <!--- Filter button -->
            <div class="form-group">
              <button onclick="filterOrSearch()" class="btn btn-default filter-col" type="button">
                <span class="glyphicon glyphicon-record"></span>
                <spring:message code="search" />
              </button>
            </div>

          </form>
        </div>
      </div>
    </div>

    <!--- Filter collapse button -->
    <button class="btn btn-primary" type="button" data-toggle="collapse" data-target="#filter-panel">
      <span class="glyphicon glyphicon-cog"></span>
      <spring:message code="search" />
    </button>

    <!--- User reports -->
    <div class="col-lg-6 col-md-6 col-sm-6" id="user-reports">
      <jsp:include page="user-reports-component.jsp"></jsp:include>
    </div>

    <!--- Add/edit report panel -->
    <div class="col-lg-6 col-md-6 col-sm-6">
      <div class='col-md-6'>
        <div class="panel panel-default">

          <!--- Add/edit report heading -->
          <div class="panel-heading">
            <strong><spring:message code="add.report"/></strong>
          </div>

          <!--- Add/edit report form -->
          <div class="panel-body">
            <form id="report-form" role="form" action="/reports/add" method="POST">
              <fieldset>
                <div class="row">
                  <div class="col-sm-12 col-md-10  col-md-offset-1 ">

                    <!--- Report date -->
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

                    <!--- Report duration -->
                    <div class="form-group">
                      <label class="col-sm-2 control-label" for="duration"></label>
                      <div class="col-sm-12">
                        <div class="input-group">
                          <span class="input-group-addon">
                            <i class="fa fa-clock-o" aria-hidden="true"></i>
                          </span>
                          <input class="form-control" id="duration" name="duration" type="number" step="0.01" value="" placeholder="<spring:message code='duration'/>">
                        </div>
                        <span class="help-block"><spring:message code="hint.duration"/></span>
                      </div>
                    </div>

                    <!--- Report description -->
                    <div class="form-group">
                      <label class="col-sm-2 control-label" for="description"></label>
                      <div class="col-sm-12">
                        <div class="input-group">
                          <span class="input-group-addon">
                            <i class="fa fa-thumb-tack" aria-hidden="true"></i>
                          </span>
                          <input class="form-control" id="description" name="description" type="text" value="" placeholder="<spring:message code='description'/>">
                        </div>
                        <span class="help-block"><spring:message code="hint.description"/></span>
                      </div>
                    </div>

                    <!--- Report remote checkbox -->
                    <div class="form-group">
                      <label class="col-sm-2 control-label" for="remote"></label>
                      <div class="col-sm-12">
                        <div>
                          <spring:message code="remote"/>
                          <span>
                            <input id="remote" name="remote" type="checkbox" value="" placeholder="<spring:message code='remote'/>">
                          </span>
                        </div>
                      </div>
                    </div>

                    <!--- Save button -->
                    <div class="form-group">
                      <div class="col-sm-12">
                        <div class="input-group">
                          <input class="reportId" name="reportId" type="hidden" value="">
                          <button class="btn btn-lg btn-primary btn-block" id="btn-save-report" type="button" disabled="disabled">
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
    </div>

    <script type="text/javascript">
        var validateModalStrings =
        {
            'invalid.value.error.message' : "<spring:message code='invalid.value.error.message' />",
            'invalid.date.error.message' : "<spring:message code='invalid.date.error.message' />",
            'empty.date.error.message' : "<spring:message code='empty.date.error.message' />",
            'empty.duration.error.message' : "<spring:message code='empty.duration.error.message' />",
            'invalid.duration.error.message' : "<spring:message code='empty.description.error.message' />"
        };
        var strings =
        {
            'confirm.delete' : "<spring:message code='confirm.report.delete' />"
        };
    </script>
  </jsp:body>
</template:main>