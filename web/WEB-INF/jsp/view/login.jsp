<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<spring:message code="title.login" var="title" />
<template:main htmlTitle="${title}" >

<jsp:body>
  <div class="container">

    <!--- Startup page design -->
    <hr class="prettyline">
    <br>
    <h1 class="text-center"><b><spring:message code="homepage.slogan"/></b></h1>
    <h3 class="text-center"><spring:message code="application.name"/></h3>
    <br>
    <hr class="prettyline">

    <!--- Login -->
    <div class="container" style="margin-top:40px">
      <div class="row">
        <div class="col-sm-6 col-md-4 col-md-offset-4">
          <div class="panel panel-default">

            <!--- Header -->
            <div class="panel-heading">
              <strong><spring:message code="sign.in.to.continue"/></strong>
            </div>

            <!--- Login form -->
            <div class="panel-body">
              <form role="form" method="POST">
                <fieldset>

                  <!--- User picture -->
                  <div class="row">
                    <div class="center-block">
                      <img class="profile-img"
                        src="https://lh5.googleusercontent.com/-b0-k99FZlyE/AAAAAAAAAAI/AAAAAAAAAAA/eu7opA4byxI/photo.jpg?sz=120" alt="">
                    </div>
                  </div>

                  <!--- User name -->
                  <div class="row">
                    <div class="col-sm-12 col-md-10  col-md-offset-1 ">
                      <div class="form-group">
                        <div class="input-group">
                          <span class="input-group-addon">
                            <i class="glyphicon glyphicon-user"></i>
                          </span>
                          <input class="form-control" name="userLogin" type="text" placeholder="<spring:message code="username" />" autofocus>
                        </div>
                      </div>

                      <!--- User password -->
                      <div class="form-group">
                        <div class="input-group">
						  <span class="input-group-addon">
                            <i class="glyphicon glyphicon-lock"></i>
						  </span>
                          <input class="form-control" name="password" type="password" placeholder="<spring:message code="password"/>" value="">
                        </div>
                      </div>

                      <!--- Sign in button -->
                      <div class="form-group">
                        <input type="submit" class="btn btn-lg btn-primary btn-block" value="<spring:message code="sign.in"/>">
                      </div>

                    </div>
                  </div>
                </fieldset>
              </form>
            </div>

            <!--- Link to forget password page (doesn't work now) -->
            <div class="panel-footer ">
              <spring:message code="forgot.password"/> <a href="#" onClick=""> <spring:message code="remind"/> </a>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</jsp:body>
</template:main>
