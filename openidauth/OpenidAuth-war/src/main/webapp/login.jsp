<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="com.ibm.openid.authentication.text.Login" />
<!DOCTYPE html>
<html dir="ltr" lang="en"><head>
  <script type="text/javascript">
	  function showResetForm() {
		  document.getElementById("loginform").style.display="none";
		  document.getElementById("passwordResetForm").style.display="";
	  }
	  function showLoginForm() {
		  document.getElementById("passwordResetForm").style.display="none";
		  document.getElementById("loginform").style.display="";
		}
   </script>
  <meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/><style>
/**
 * File imports all the general base styling needed for styling 
 */
a:link { color: #ffffff; } /* visited link */ a:visited { color: #ffffff; }
html {
  height: 100%;
}
body {
  height: 100%;
  margin: 0;
  background-color: #325c80;
  background-size: cover;
  font-family: "Helvetica Neue for IBM", "Helvetica Neue", "Segoe UI", Segoe, Calibri, Roboto, "Droid Sans", Helvetica, Arial, sans-serif;
}
.topContainer {
  height: 100%;
  width: 100%;
  display: table;
}
.bannerRow {
  display: table-row;
  height: 42%;
}
.bannerCell {
  display: table-cell;
  vertical-align: bottom;
  padding-bottom: 17px;
}
.formContainerRow {
  display: table-row;
  width: 100%;
  /* fallback */
  background-color: #264a60;
  background-color: rgba(38, 74, 96, 0.7);
}
.formContainerCell {
  display: table-cell;
  vertical-align: middle;
}
.errorFormContainerCell {
  display: table-cell;
  vertical-align: top;
}
.errorDivContainerTable {
  display: table;
  width: 100%;
}
form input:-webkit-autofill {
  -webkit-box-shadow: 0 0 0 1000px white inset;
  box-shadow: 0 0 0 1000px white inset;
}
form input {
  width: 100%;
  padding-top: 5px;
  padding-bottom: 5px;
  background-color: white;
  border-style: solid;
  border-width: 1px;
  border-color: #41d6c3;
  text-align: center;
}
form ::-webkit-input-placeholder {
  text-align: center;
}
form :-moz-placeholder {
  /* Firefox 18- */
  text-align: center;
}
form ::-moz-placeholder {
  /* Firefox 19+ */
  text-align: center;
}
form :-ms-input-placeholder {
  text-align: center;
}
form .submitInput {
  font-size: 14px;
  font-weight: 400;
  color: #5a5a5a;
  color: #ffffff;
  background-color: #41d6c3;
  border-color: #41d6c3;
}
form img {
  position: absolute;
  height: 20px;
  width: 20px;
}
.footerMessageRow {
  display: table-row;
  height: 42%;
  font-size: 11px;
  font-weight: 400;
  color: #5a5a5a;
  color: #ffffff;
}
.footerMessageRow .footerMessageCell {
  display: table-cell;
  vertical-align: bottom;
}
.footerMessageRow .footerMessageCell .footerMessageContent {
  text-align: center;
  padding-bottom: 10px;
}
.title {
  text-align: center;
  font-size: 26px;
  font-weight: 700;
  color: #ffffff;
  line-height: 1.42857143;
  padding-top: 10px;
  padding-bottom: 10px;
}
.title .IBM {
  font-weight: 300;
}
.trialMessage {
  font-size: 12px;
  font-weight: 300;
  color: #5a5a5a;
  color: #ffffff;
  text-align: center;
  padding-bottom: 10px;
}
.loginErrrorMessageDiv {
  display: table;
  text-align: center;
  font-size: 12px;
  font-weight: 300;
  color: #5a5a5a;
  color: #ffffff;
  padding-bottom: 20px;
  padding-top: 20px;
  margin-left: auto;
  margin-right: auto;
}
.loginErrrorMessageDiv .logonErrorDivRow {
  display: table-row;
}
.loginErrrorMessageDiv .logonErrorDivRow .logonErrorDivCell {
  display: table-cell;
  vertical-align: middle;
  text-align: left;
  padding-left: 10px;
}
.loginErrrorMessageDiv .logonErrorDivRow .logonErrorDivCell img {
  padding-left: 10px;
  padding-right: 10px;
  height: 20px;
  width: 20px;
}
.loginErrrorMessageDiv .logonErrorDivRow .logonErrorDivCell.loginErrorMsgCell {
  width: 100%;
  font-size: 12px;
  font-weight: 300;
  color: #5a5a5a;
  line-height: 1.42857143;
  color: #ffffff;
}
input,
textarea {
  -webkit-appearance: none;
  -webkit-border-radius: 0;
  border-radius: 0;
}
input {
  box-sizing: border-box;
}
@media screen and (max-width: 9999px) {
  .login-content {
    display: table;
    margin-left: auto;
    margin-right: auto;
    width: 100%;
    max-width: 850px;
  }
  .login-content .username {
    display: table-cell;
    width: 33%;
    padding-left: 10px;
    padding-right: 10px;
  }
  .login-content .password {
    display: table-cell;
    width: 33%;
    padding-left: 10px;
    padding-right: 10px;
  }
  .login-content .submit {
    display: table-cell;
    width: 33%;
    padding-left: 10px;
    padding-right: 10px;
  }
  .loginErrrorMessageDiv {
    width: 100%;
    max-width: 850px;
  }
  .loginErrrorMessageDiv .logonErrorDivRow .logonErrorDivCell.loginErrorMsgCell {
    width: 100%;
  }
}
@media screen and (max-width: 1024px) {
  .login-content {
    width: 33%;
    margin-left: auto;
    margin-right: auto;
    padding-bottom: 20px;
  }
  .login-content .username {
    display: table-row;
    width: 100%;
  }
  .login-content .password {
    display: table-row;
    width: 100%;
  }
  .login-content .password input {
    margin-top: 10px;
    margin-bottom: 20px;
  }
  .login-content .submit {
    display: table-row;
    width: 100%;
    padding-bottom: 10px;
  }
  .loginErrrorMessageDiv {
    width: auto;
  }
  .loginErrrorMessageDiv .logonErrorDivRow .logonErrorDivCell {
    text-align: right;
  }
  .loginErrrorMessageDiv .logonErrorDivRow .logonErrorDivCell.loginErrorMsgCell {
    width: auto;
    text-align: left;
  }
  .formContainerCell {
    padding-top: 20px;
  }
}
@media screen and (max-width: 480px) {
  .bannerRow {
    height: 25%;
  }
  .footerMessageRow {
    height: 25%;
  }
  .footerMessageRow .footerMessageCell {
    vertical-align: middle;
  }
  .logo {
    padding-bottom: 17px;
  }
  .login-content {
    width: 100%;
    margin-left: auto;
    margin-right: auto;
    padding-bottom: 17px;
  }
  .login-content .username {
    width: 100%;
    display: table-row;
  }
  .login-content .password {
    width: 100%;
    display: table-row;
  }
  .login-content .submit {
    width: 100%;
    display: table-row;
    padding-bottom: 10px;
  }
  .formContainerCell {
    padding-left: 20px;
    padding-right: 20px;
  }
  .errorFormContainerCell {
    padding-left: 20px;
    padding-right: 20px;
  }
}

</style>
<title>Login page</title>
</head>
<body class="logon otherstuff"><div class="topContainer"><div id="app-banner" class="bannerRow" role="banner" aria-label="Banner" /><div class="bannerCell">
<div class="title"><span class="text">Welcome to </span><span class="IBM">IBM
  <fmt:message key="product.name.${clientId}" var="productName"/>
  <c:set var="unresolvedName" value="???product.name.${clientId}???"/>
  <c:if test = "${pageScope.productName eq unresolvedName}">
    <c:set var="productName"><fmt:message key="product.name.default"/></c:set>
  </c:if>
  <c:out value="${productName}"/>
</span></div>
  <div class="trialMessage"><fmt:message key="environment.info.${environment}" /></div>
  </div></div><div class="formContainerRow"><div class="formContainerCell">
        <% if (request.getAttribute("loginMessage") != null) { %>
          <div style="color:#ffffff;text-align:center;padding-bottom:20px;"><%= request.getAttribute("loginMessage") %></div>
        <% } %>
	      <form method="POST" action="<%=request.getContextPath()%>/<%=request.getAttribute("tenantId")%>/authorization" id="loginform" method="post" name="loginform"><div class="login-content" role="main" aria-label="Banner" />
	        <div class="username">
	          <input class="usernameInput" placeholder="yourname@example.com" title="Enter Your Username" name="email" type="text" id="j_username" tabindex="1" />
	        </div>
	        <div class="password">
	          <input class="passwordInput" placeholder="Password" title="Enter Your Password" name="password" type="password" id="j_password" tabindex="2" autocomplete="off"/>
	        </div>
	        <input type="hidden" value="<%=request.getAttribute("clientId")%>" name="clientId"/>
          <input type="hidden" value="<%=request.getAttribute("redirectUri")%>" name="redirectUri"/>
          <input type="hidden" value="<%=request.getAttribute("state")%>" name="state"/>
	        <div class="submit"><input tabindex="-1" title="Sign In"  value="Sign In" name="action" type="submit" class="submitInput" /></div>
	        </div>
	        <div style="text-align:center;"><a href="javascript:showResetForm();void(0);">Forgot password?</a></div>
	      </form>
        <form id="passwordResetForm" style="display:none;" method="POST" action="<%=request.getContextPath()%>/<%=request.getAttribute("tenantId")%>/password" method="post" name="loginform" id="loginForm"><div class="login-content" role="main" aria-label="Banner" />
          <div class="username">
            <input class="usernameInput" placeholder="yourname@example.com" title="Enter Your Username" name="email" type="text" id="j_username" tabindex="1" />
          </div>
          <input type="hidden" value="true" name="resetPassword"/>
          <input type="hidden" value="<%=request.getAttribute("clientId")%>" name="clientId"/>
          <input type="hidden" value="<%=request.getAttribute("redirectUri")%>" name="redirectUri"/>
          <input type="hidden" value="<%=request.getAttribute("state")%>" name="state"/>
          <div class="submit"><input tabindex="-1" title="Sign In"  value="Reset Password" name="action" type="submit" class="submitInput" /></div>
          </div>
          <div style="text-align:center;"><a href="javascript:showLoginForm();void(0);">Back to login</a></div>
        </form>	      
        
      </div></div><div class="footerMessageRow"><div class="footerMessageCell">
      <div class="footerMessageContent"><fmt:message key="hipaa.warning.${environment}" /></div>
    </div></div></div>
</body></html>