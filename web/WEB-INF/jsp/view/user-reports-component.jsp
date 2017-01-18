<%--@elvariable id="user.reports" type="java.util.List<erp.domain.Report>"--%>

<c:choose>
    <c:when test="${fn:length(userReports) == 0}">
            <h2><spring:message code="user.have.no.reports"/> </h2>
    </c:when>

    <c:otherwise>

            <div class="row">
                <div class="[ col-xs-12 col-sm-offset-1 col-sm-10 ]">
                    <ul class="event-list">

                        <c:forEach items="${userReports}" var="report">
                            <li>
                                <time datetime="${report.date}">
                                    <span class="day">${report.day}</span>
                                    <span class="month">${report.month}</span>
                                    <span class="year">${report.year}</span>
                                </time>
                                <div class="info">
                                    <h2 class="title">${report.duration}</h2>
                                    <p class="desc">${report.description}</p>
                                    <c:if test="${report.remote}">
                                        <ul>
                                            <li style="width:33%;">Remote <span class="glyphicon glyphicon-ok"></span></li>
                                        </ul>
                                    </c:if>
                                </div>
                                <div class="social">
                                    <ul>
                                        <li class="facebook" style="width:33%;"><a href="javascript:editReport('${report.id}', '${report.date}', '${report.duration}', '${report.description}', '${report.remote}')"><span class="fa fa-pencil"></span></a></li>
                                        <li class="google-plus" style="width:33%;"><a href="javascript:deleteReport('${report.id}')"><span class="fa fa-trash"></span></a></li>
                                    </ul>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

    </c:otherwise>
</c:choose>
