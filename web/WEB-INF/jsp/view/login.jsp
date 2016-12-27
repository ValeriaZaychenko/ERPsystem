<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<spring:message code="title.login" var="title" />
<template:main htmlTitle="${title}" >

    <jsp:body>
        <div class="container">

            <hr class="prettyline">
            <br>
            <h1 class="text-center"><b><spring:message code="homepage.slogan"/></b></h1>
            <h3 class="text-center"><spring:message code="application.name"/></h3>
            <br>
            <hr class="prettyline">

            <div class="container" style="margin-top:40px">
                <div class="row">
                    <div class="col-sm-6 col-md-4 col-md-offset-4">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <strong><spring:message code="sign.in.to.continue"/></strong>
                            </div>
                            <div class="panel-body">
                                <form role="form" method="POST">
                                    <fieldset>
                                        <div class="row">
                                            <div class="center-block">
                                                <img class="profile-img"
                                                     src="https://lh5.googleusercontent.com/-b0-k99FZlyE/AAAAAAAAAAI/AAAAAAAAAAA/eu7opA4byxI/photo.jpg?sz=120" alt="">
                                            </div>
                                        </div>

                                        <div class="row">
                                            <div class="col-sm-12 col-md-10  col-md-offset-1 ">
                                                <div class="form-group">
                                                    <div class="input-group">
                                                        <span class="input-group-addon">
                                                            <i class="glyphicon glyphicon-user"></i>
                                                        </span>
                                                        <input class="form-control" placeholder="<spring:message code="username"/>" name="userLogin" type="text" autofocus>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <div class="input-group">
												<span class="input-group-addon">
													<i class="glyphicon glyphicon-lock"></i>
												</span>
                                                        <input class="form-control" placeholder="<spring:message code="password"/>" name="password" type="password" value="">
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <input type="submit" class="btn btn-lg btn-primary btn-block" value="<spring:message code="sign.in"/>">
                                                </div>
                                            </div>
                                        </div>
                                    </fieldset>
                                </form>
                            </div>
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
