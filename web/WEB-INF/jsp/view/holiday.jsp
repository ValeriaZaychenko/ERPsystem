<spring:message code="title.holiday" var="holidayPageTitle" />
<template:main htmlTitle="${holidayPageTitle}" >

    <jsp:body>
            <div class="table-responsive">
                <c:if test="${holiday != null}">
                    <table id="holiday-table" class="table table-hover">
                        <thead>
                        <tr>
                            <th class="text-center"> <spring:message code="date"/> </th>
                            <th class="text-center"> <spring:message code="description"/> </th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${holiday}" var="h">
                            <tr>
                                <td>${h.date}</td>
                                <td>${h.description}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                 </c:if>
            </div>

            <div class='col-md-6'>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <strong><spring:message code="add.holiday"/></strong>
                    </div>

                    <div class="panel-body">
                        <form role="form" id="holidayForm" action="holidays/add" method="POST">
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
                                                    <input type="submit" value="<spring:message code="save"/>" name="Save" />
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
    </jsp:body>

</template:main>