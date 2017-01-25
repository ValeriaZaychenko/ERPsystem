<%--@elvariable id="user.reports" type="java.util.List<erp.domain.Report>"--%>

<c:choose>

  <c:when test="${fn:length(userReports) == 0}">
    <h2><spring:message code="user.have.no.reports" /></h2>
  </c:when>

  <c:otherwise>
    <div class="row">
      <div class="[ col-xs-12 col-sm-offset-1 col-sm-10 ]">
        <ul class="event-list">

          <c:forEach items="${userReports}" var="report">
            <li>

              <!--- Date view -->
              <time datetime="${report.date}">
                <span class="day">${report.day}</span>
                <span class="month">${report.month}</span>
                <span class="year">${report.year}</span>
              </time>

              <!--- Main info view -->
              <div class="info">
                <h2 class="title">${report.duration}</h2>
                <p class="desc">${report.description}</p>
                <c:if test="${report.remote}">
                  <ul>
                    <li style="width:33%;">Remote <span class="glyphicon glyphicon-ok"></span></li>
                  </ul>
                </c:if>
              </div>

              <!--- Action buttons -->
              <div class="action-buttons">
                <ul>
                  <li class="edit-report-btn" style="width:33%;">
                    <a href="javascript:editReport('${report.id}', '${report.date}', '${report.duration}', '${report.description}', '${report.remote}')">
                      <span class="fa fa-pencil"></span>
                    </a>
                  </li>
                  <li class="delete-report-btn" style="width:33%;">
                    <a href="javascript:deleteReport('${report.id}')">
                      <span class="fa fa-trash"></span>
                    </a>
                  </li>
                </ul>
              </div>

            </li>
          </c:forEach>
          <h1><spring:message code="total"/> : ${sumOfDurations}</h1>
        </ul>

        <!--- Progress table -->
        <div class="table-responsive">
          <table class="table table-hover">

            <thead>
            <tr>
              <th class="text-center"> <spring:message code="progress"/> </th>
            </tr>
            </thead>

            <tbody>
              <tr>
                <td>
                  <div class="progress">
                    <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="" aria-valuemin="0" aria-valuemax="100" style="width: ${userProgress}%">
                      <span class="sr-only">${userProgress}% Complete (success)</span>
                    </div>
                    <span class="progress-completed">${userProgress}%</span>
                  </div>
                </td>
              </tr>
            </tbody>

          </table>
        </div>

        <!--- View quantity of weekends, holidays, working days, all days -->
        <div class='col-md-6'>
          <ul>
            <li><spring:message code="weekends"/> - ${weekends}</li>
            <li><spring:message code="holidays"/> - ${holiday}</li>
            <li><spring:message code="working.days"/> - ${workingDays}</li>
            <li><spring:message code="all.days"/> - ${allDays}</li>
          </ul>
        </div>

      </div>
    </div>
  </c:otherwise>
</c:choose>
