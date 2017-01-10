<spring:message code="title.progress" var="progressPageTitle" />
<template:main htmlTitle="${progressPageTitle}" >

    <jsp:attribute name="headContent">
        <link rel="stylesheet" href="<c:url value="/resources/css/progress.css" />" />
        <!--<link rel="stylesheet" href="<c:url value="/resources/css/external/bootstrap-combined.min.css" />" />-->
        <link rel="stylesheet" href="<c:url value="/resources/css/external/datepicker.css" />" />
        <script src="<c:url value="/resources/js/external/bootstrap-datepicker.js" />"></script>
        <script src="<c:url value="/resources/js/bootstrap-datepicker.ru.js" />" charset="UTF-8"></script>
        <script src="<c:url value="/resources/js/custom_datepicker.js" />"></script>
    </jsp:attribute>

    <jsp:body>
        <div class="table-responsive">
            <table id="progress-table" class="table table-hover">
                <thead>
                <tr>
                    <th class="text-center"> <spring:message code="name"/> </th>
                    <th class="text-center"> <spring:message code="progress"/> </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${progress}" var="p">
                    <tr id="users-row-${p.userId}">
                        <td>${p.userName}</td>
                        <td>
                            <div class="progress">
                                <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="" aria-valuemin="0" aria-valuemax="100" style="width: ${p.progress}%">
                                    <span class="sr-only">${p.progress}% Complete (success)</span>
                                </div>
                                <span class="progress-completed">${p.progress}%</span>
                            </div>
                       </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class='col-md-6'>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <strong><spring:message code="view.progress.between"/></strong>
                </div>
                <div class="panel-body">
                    <form role="form" id="progressDatesForm" action="/progress" method="GET">
                        <fieldset>
                            <div class="row">
                                <div class="col-sm-12 col-md-10  col-md-offset-1 ">

                                    <div class="form-group">
                                        <label for="datepicker_month" class="col-sm-2 control-label">
                                        </label>
                                        <div class="col-sm-12">
                                            <div class="input-append date" id="datepicker_month" data-date-format="yyyy-mm" data-date="2017-01">
                                                <input  type="text" readonly="readonly" name="month" >
                                                <span class="add-on"><i class="fa fa-th"></i></span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="col-sm-12">
                                            <div class="input-group">
                                                <input type="submit" value="<spring:message code="view.progress"/>" name="View Progress" />
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
            <ul>
                ${monthDate} :
                <li><spring:message code="weekends"/> - ${weekends}</li>
                <li><spring:message code="holidays"/> - ${holiday}</li>
                <li><spring:message code="working.days"/> - ${workingDays}</li>
                <li><spring:message code="all.days"/> - ${allDays}</li>
            </ul>
        </div>
    </jsp:body>

</template:main>
