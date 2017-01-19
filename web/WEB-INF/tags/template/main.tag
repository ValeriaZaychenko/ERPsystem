<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="htmlTitle" type="java.lang.String" rtexprvalue="true" required="true" %>
<%@ attribute name="headContent" fragment="true" required="false" %>
<%@ attribute name="activeLink" type="java.lang.String" rtexprvalue="true" required="false"  %>
<%@ include file="/WEB-INF/jsp/base.jspf" %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title><c:out value="${htmlTitle}" /></title>

    <link rel="stylesheet" href="<c:url value="/resources/css/external/bootstrap.min.css" />" />
    <link rel="stylesheet" href="<c:url value="/resources/css/external/bootstrapValidator.css" />" />

    <link rel="stylesheet" href="<c:url value="/resources/css/style.css" />" />
    <link rel="stylesheet" href="<c:url value="/resources/css/login-logout-style.css" />" />

    <link rel="stylesheet" href="<c:url value="/resources/fonts/font-awesome/css/font-awesome.css" />" />
    <link rel="stylesheet" href="<c:url value="/resources/css/external/languages.min.css" />" />

    <script src="<c:url value="/resources/js/external/jquery-3.1.1.js" />"></script>
    <script src="<c:url value="/resources/js/external/bootstrap.min.js" />"></script>
    <script src="<c:url value="/resources/js/external/ztoast.min.js" />"></script>
    <script src="<c:url value="/resources/js/external/bootstrapValidator.min.js" />"></script>

    <script src="<c:url value="/resources/js/set_locale.js" />"></script>
    <script src="<c:url value="/resources/js/select_checkbox.js" />"></script>

    <jsp:invoke fragment="headContent" />

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>
    <!--- Main navbar -->
    <nav class="navbar navbar-default">
      <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">
            <img class="img-responsive" src="/resources/images/aldec_logo_dont_you_go_down.png" alt="Brand">
            <div class="navbar-collapse collapse navbar-responsive-collapse"></div>
          </a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          <ul class="nav navbar-nav navbar-right">
            <sec:authorize access="hasAuthority('AUTH_ADMIN')">
              <li>
                <a href="/progress">
                  <spring:message code="progress"/>
                </a>
              </li>
            </sec:authorize>
            <sec:authorize access="hasAuthority('AUTH_ADMIN')">
              <li>
                <a href="/holidays">
                  <spring:message code="holidays"/>
                </a>
              </li>
            </sec:authorize>
            <sec:authorize access="hasAuthority('AUTH_ADMIN')">
              <li>
                <a href="/users">
                  <spring:message code="users"/>
                </a>
              </li>
            </sec:authorize>

            <!--- Language pref -->
            <li class="dropdown">
              <c:set var="languageCode" value="${pageContext.response.locale.language}" />
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button">
                <span class="lang-sm lang-lbl-full" lang="${languageCode}"></span>
                <span class="caret"></span></a>
              <ul class="dropdown-menu" role="menu">
                <li><button type="button" class="btn btn-link" onclick="setLocale('en');">
                  <span class="lang-sm lang-lbl-full" lang="en"></span></button>
                </li>
                <li><button type="button" class="btn btn-link" onclick="setLocale('ru');">
                  <span class="lang-sm lang-lbl-full" lang="ru"></span></button>
                </li>
              </ul>
            </li>

            <!--- Notifications -->
            <li>
              <a href="#">
                <i class="fa fa-bell-o" aria-hidden="true"></i>
              </a>
            </li>

            <!---User menu -->
            <c:if test="${pageContext.request.userPrincipal != null}">
              <li class="dropdown">
                <a href="#" class="dropdown-toggle  " data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                  ${pageContext.request.userPrincipal.name}
                  <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                  <li><a onclick="document.getElementById('go-change-password-form').submit();"><spring:message code="change.password"/></a></li>
                  <li role="separator" class="divider"></li>
                  <li><a onclick="document.getElementById('logoutForm').submit();"><spring:message code="log.out"/></a></li>
                </ul>
              </li>
            </c:if>

          </ul>
        </div>
      </div>
    </nav>

    <form action="/logout" id="logoutForm" method="post" ></form>
    <form action="/changePassword/" id="go-change-password-form" method="get" ></form>

    <script type="text/javascript">
        var locale =
        {
            languageCode : '${languageCode}'
        }
    </script>

    <!--- Body Page -->
    <div class="container-fluid">
      <jsp:doBody />
    </div>

  </body>
</html>