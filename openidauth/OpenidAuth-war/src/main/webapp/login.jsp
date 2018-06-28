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
  background: url(data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTAyNCIgaGVpZ2h0PSI3NjgiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgeG1sbnM6eGxpbms9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkveGxpbmsiPg0KIDxnPg0KICA8dGl0bGU+TGF5ZXIgMTwvdGl0bGU+DQogIDxyZWN0IHg9IjAiIHk9Ii0xIiBpZD0ic3ZnXzEiIHdpZHRoPSIxMDI0IiBoZWlnaHQ9Ijc2OCIgZmlsbD0iIzMyNWM4MCIvPg0KICA8ZyBpZD0ic3ZnXzIiPg0KICAgPGcgaWQ9InN2Z18zIj4NCiAgICA8ZGVmcz4NCiAgICAgPHJlY3QgaGVpZ2h0PSI3NjgiIHdpZHRoPSIxMDI0IiBpZD0iU1ZHSURfMV8iLz4NCiAgICA8L2RlZnM+DQogICAgPGNsaXBQYXRoIGlkPSJTVkdJRF8yXyI+DQogICAgIDx1c2UgaWQ9InN2Z180IiB4bGluazpocmVmPSIjU1ZHSURfMV8iLz4NCiAgICA8L2NsaXBQYXRoPg0KICAgIDxwYXRoIGNsaXAtcGF0aD0idXJsKCNTVkdJRF8yXykiIGQ9Im02NzIuNzc4OTkyLDk4Ny4zODIwMTljLTIyMy44MzIwMDEsMC4wMTU5OTEgLTQwNS41NjU5NzksLTIxMi43NTcwMTkgLTQwNS41NjU5NzksLTIxMi43NTcwMTljLTYuMDc5MDEsLTguMjM3OTc2IC05LjE1MDAyNCwtMTIuNjMwOTgxIC0yMy4wMDAwMTUsLTM3LjYyNWMtNjUuNDU1OTk0LC0zMS4wNTkwMjEgLTEyMi45NjA5OTksLTgwLjg1OTk4NSAtMTc0LjQyNjk5NCwtMTY0LjA3NzAyNmMtMTg4LjczMDAwMywtMzA1LjA1ODk2IDQxLjM5MDk5OSwtNTM0LjgzNDk3MiAxMTYuNDA3OTk3LC01OTcuOTgyOTczYzUuMTk3OTk4LC00LjU3IDI1LjQxMjAwMywtMTYuMzgzMDAxIDU1LjU0MSwtMjkuOTk3YzMyLjI0NTk4NywtNzguNDA1MDA2IDkwLjA2NTk5NCwtMTQzLjc3NTAwMiAxNzEuOTgxMDAzLC0xOTQuMzcwOTk1YzE2OC44NjkwMTksLTEwNC4zNDcgMzA5LjA4NTAyMiwtODcuNjkwOTk0IDM5Ny4wMDA5NzcsLTU1LjM3NTk5MmMxMjUuODQxMDAzLDQ2LjI5OTk4OCAyMTguMTgzMDQ0LDE1MS44ODU5ODYgMjQ0LjM2Mjk3NiwyMzIuMjY5OTg5YzAuMzMwMDc4LDAuOTY1OTk2IDAuNjUwMDI0LDEuOTYyOTk3IDAuOTcwMDkzLDIuOTI4OTkzYzkuMjY5ODk3LDUuMDg1MDA3IDE4LjUxMDAxLDEwLjcwMjAwMyAyNy42MTk5OTUsMTYuODk4MDAzYzI0Mi40Mzk5NDEsMTY0LjY4IDI2Mi40Njk5NzEsMzg0LjYwNTk4OCAyMDAuNDA5OTEyLDUzNS41MjYwMDFjLTM3Ljc4OTkxNyw5MS44NDI5ODcgLTk5LjgxOTk0NiwyMDQuMDkwOTczIC0yMjguMDI5OTA3LDI2My41NTUwMjNjLTM5LjQ0MDA2Myw4MC44NjcwMDQgLTExMS4zNDIwNDEsMTU3LjQ1Mzk3OSAtMjM2LjQ3NzA1MSwyMTAuNjkwMDAyYy01MC40MDY5ODIsMjEuNDE3OTY5IC05OS42OTIwMTcsMzAuMzE3OTkzIC0xNDYuNzk0MDA2LDMwLjMxNzk5M3ptLTMxOC4zNTgwMDIsLTIxNC45MzgwNDljNzYuMDEyMDI0LDcwLjExNzAwNCAyNTMuNjM5OTg0LDIwMi45ODEwMTggNDM5LjQ4MjAyNSwxMjMuOTMyMDA3Yzc1LjE3Nzk3OSwtMzEuOTc2OTkgMTI4LjE1MTk3OCwtNzMuMzE5OTQ2IDE2NC44OTAwMTUsLTExOC42ODU5NzRjLTM3LjQ0NDAzMSw3LjQ2Njk4IC03OC43NzEwNTcsMTEuMzQ2MDA4IC0xMjUuMjY0MDM4LDEwLjEyMjAwOWMtNjQuNDYwOTk5LC0xLjQzMjAwNyAtMTI0LjgxNTAwMiwtMTkuNDQwMDAyIC0xNzkuNDU2OTcsLTQ2LjE1NTAyOWMtNDguMTkyMDE3LDE3LjUyNjAwMSAtMTcwLjE4MzAxNCw1NC4wMDkwMzMgLTI5OS42NTEwMzEsMzAuNzg2OTg3em0zNjMuMjc5MDIyLC03NC45NzY5OWMzNy4xMjM5NjIsMTQuNDM0OTk4IDc2LjQ2MDk5OSwyMy41NDQwMDYgMTE3LjMwNzAwNywyNC40NjA5OTljNjguNjY0MDAxLDEuNDQ3OTk4IDEyNS43NDQ5OTUsLTguNzIxOTg1IDE3Mi44ODI5OTYsLTI3LjAwMzk2N2M1MS4yMDk5NjEsLTEyNC44MDEwMjUgMTcuNjQwMDE1LC0yNTUuOTQzMDI0IDIuNzg5OTc4LC0zMDEuOTM3MDEyYy0xMC41ODk5NjYsMTUuMDQ2OTk3IC0yMi4yMDUwMTcsMjkuNjkxOTg2IC0zNC44NDYwMDgsNDMuOTAyMDA4Yy0xMDMuMjg2OTg3LDExNS45NjU5NzMgLTIwNi40NDM5NywyMTMuMzYwOTkyIC0yNTguMTMzOTcyLDI2MC41Nzc5NzJ6bS00MTQuNDg4MDA3LC02LjA5OTk3NmMxMDEuOTY4OTk0LDM5LjQyNzk3OSAyMDcuODg0OTc5LDI0LjAxMDk4NiAyNzUuMjAxOTk2LDUuOTIxOTk3Yy0xMjkuODg0MDAzLC04OC45NDUwMDcgLTIxMy4xNzk5OTMsLTIxMi40NDI5OTMgLTIxOC40NzM5OTksLTIyMC40MjQ5ODhsLTY1LjI2MywtOTIuMTgxYy0wLjMyMTAxNCwwLjk4MTk5NSAtMC42MTAwMTYsMS45NjI5ODIgLTAuODk4MDEsMi45NjA5OTljLTM1LjM1ODk3OCwxMTYuMzY4OTg4IC0xMC4zMzIwMDEsMjM3LjY3ODAwOSA5LjQzMzAxNCwzMDMuNzIyOTkyem0yMy4wMDU5ODEsLTM3NS45NjM5ODlsODcuODUyMDIsMTI0LjA5Mjk4N2MxLjcwMDk4OSwyLjU1ODk5IDk4LjM0Mzk5NCwxNDUuNTI5MDIyIDIzOS42ODI5ODMsMjI3LjA1NmMzOC40NzEwMDgsLTM0LjQyMjk3NCAxNTUuNjQ4OTg3LC0xNDEuNzMxMDE4IDI3My4wNTIwMDIsLTI3My41ODA5OTRjMTUuNzg2MDExLC0xNy43MDE5OTYgMjkuNjE0OTksLTM2LjIwOTAxNSA0MS41ODMwMDgsLTU1LjUzNzAxOGMtNjYuMDk2OTg1LC03NS4wMjYwMDEgLTE5Ny44MDk5OTgsLTIyMS4wMDQ5OSAtMzAzLjk1MDk4OSwtMzE2LjkwMjk5Yy01NC4zMjIwMjEsMjguMzIzOTk5IC04OS44NDEwMDMsNTUuNDU1OTk5IC05MC44MDMwNCw1Ni4yMTMwMDNjLTQuNTU2OTQ2LDIuOTYwOTk5IC0xNjcuNjQ5OTYzLDEwMy41NDE5OTIgLTI0Ny40MTU5ODUsMjM4LjY1OTAxMnptNzIyLjAzMjAxMywxMy4zNDA5NzNjMjMuMjUsMjEuNTI3MDA4IDAsMCAwLDBjMjcuMTgwMDU0LDI3Ljk2NzAxIDY1LjUsMTczLjk4MDAxMSA0MC42NTAwMjQsMzIwLjc4MjAxM2M2Ny44Mjk5NTYsLTUyLjAxMyAxMDYuNzE5OTcxLC0xMjQuNDc5MDA0IDEzNC40MTAwMzQsLTE5MS44NDUwMDFjNDkuMjc5OTA3LC0xMTkuNzc5OTk5IDM1LjE5OTk1MSwtMjkyLjQ5MDAwNSAtMTQyLjc4MDAyOSwtNDMxLjI5MmMyMC43NjAwMSwxMTIuNTA2MDA0IDkuOTM5OTQxLDIxMy42MDE5OTcgLTMyLjI4MDAyOSwzMDIuMzU0OTg4em0tODMxLjU0MjAwNywtMjkzLjExNzk4OWMtODkuMDA2OTk2LDc5LjkzMzk5OCAtMjM5LjI5Nzk5MywyNjIuOTU5OTk5IC05MS4wOTE5OTUsNTAyLjUzNTk4OGMyOS40NTUwMDIsNDcuNjAzMDI3IDYzLjA0ODk5Niw4My4zMzAwMTcgOTguNzI5MDA0LDEwOS43NzAwMmMtMTUuNjkwMDAyLC03NS43OTgwMzUgLTI0LjIyNTAwNiwtMTc4LjEzMjk5NiA2LjU0NjAwNSwtMjc5LjUwMzAyMWM1LjgzOTk5NiwtMTkuMTUwOTcgMTMuMzc5OTksLTM3Ljg1MDk4MyAyMi4yNjc5OSwtNTUuOTcwOTc4Yy0yMS4wNTAwMDMsLTUzLjE4ODAxOSAtNTQuMjU5MDAzLC0xNjAuNzUzMDA2IC0zNi40NTEwMDQsLTI3Ni44MzIwMDhsMCwwem01MTIuNjcxMDM2LC00NC44MzQ5OTljOTUuOTY4OTk0LDg5LjQ3Njk5NyAyMDQuNjEyOTc2LDIwOC41NjUwMDIgMjcwLjUxNjk2OCwyODIuODAxOTg3YzMyLjQ0Mzk3LC04NS44ODc5ODUgMzMuMDUzOTU1LC0xODUuMDA0OTkgMS42NjQwMDEsLTI5Ni4zMDM5ODhjLTkxLjIxNjAwMywtMzguMjY4OTk5IC0xOTEuNTgwOTk0LC0xOC4yODIgLTI3Mi4xODA5NjksMTMuNTAyMDVsMCwtMC4wMDAwM2wwLC0wLjAwMDAyem0tNDM1LjM0NDAyNSwyLjM2NmMtMjkuMjMwMDExLDkwLjU3MDk5NyAtMTQuNDA3MDEzLDE4My4yMzQ5OTggMy43MjE5ODUsMjQ2LjA2MTk5NGM5MS44OTQwMTIsLTEyNi40MjY5OTQgMjMxLjA4NDAxNSwtMjEyLjA4OTk5NCAyMzguNzIwMDAxLC0yMTYuNzM5OTk0YzIuNzI2OTksLTIuMTczIDI4LjgxMjk4OCwtMjIuMTYgNzAuMjM2MDIzLC00NS45NzgwMDFjLTk3LjA1OTAyMSwtNTEuMDk2MDAxIC0yMzMuMjMyMDI1LC0xNC43MDk5OTkgLTMxMi42NzgwMDksMTYuNjU2MDExbDAsLTAuMDAwMDF6bTE5Ny45NzEwMDgsLTEwNy43NTg5OTdjNjQuMzAwMDE4LDAgMTI4LjY5Njk5MSwxNC44Mzc5OTcgMTgyLjE1MjAwOCw1Ni41MTg5OTdjODAuNzYwOTg2LC0zNi40MzQ5OTggMTg3LjM4MTk1OCwtNjYuNzg2MDAzIDI5Mi41OTI5NTcsLTQ0LjUxM2MtMzMuMDgwOTk0LC00OS42NDY5OTYgLTkzLjM2OTk5NSwtMTA4Ljk1MDAwNSAtMTc4LjY1NDk2OCwtMTQwLjMxNDk5NWMtMTA2LjQ2MDk5OSwtMzkuMTY5OTk4IC0yMjMuOTkyOTgxLC0yMi4wMTQ5OTkgLTMzOS45MTkwMDYsNDkuNjE0OTljLTQ3LjI5NTAxMywyOS4yMjUwMDYgLTg1LjA1OTk5OCw2My44NTcwMSAtMTEyLjgxNTAwMiwxMDMuNDc4MDA0YzQ3LjU4NDAxNSwtMTQuMDk4IDEwMi4wNjYwMSwtMjQuNzgzOTk3IDE1Ni42NDQwMTIsLTI0Ljc4Mzk5N3oiIGlkPSJYTUxJRF85NjY1XyIgZmlsbD0iIzM4NjU4NyIvPg0KICAgPC9nPg0KICA8L2c+DQogPC9nPg0KPC9zdmc+) no-repeat center center fixed;
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
.logo {
  text-align: center;
  padding-bottom: 20px;
}
.logo img {
  height: 42px;
  width: 42px;
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

</style><title>Login page</title></head><body class="logon otherstuff"><div class="topContainer"><div id="app-banner" class="bannerRow" role="banner" aria-label="Banner" /><div class="bannerCell"><div class="topSpacer"> </div><div class="logo"><img src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMTI4IiBoZWlnaHQ9IjEyOCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KIDxnPgogIDx0aXRsZT5MYXllciAxPC90aXRsZT4KICA8ZyBpZD0iR3VpZGUiIGRpc3BsYXk9Im5vbmUiLz4KICA8ZyBpZD0iTGF5ZXJfMSIvPgogIDxnIGlkPSJMYXllcl8yIi8+CiAgPGcgaWQ9IkxheWVyXzMiLz4KICA8ZyBpZD0iTGF5ZXJfNCI+CiAgIDxnIGlkPSJYTUxJRF81Njc1XyI+CiAgICA8cGF0aCBpZD0iWE1MSURfNTY3Nl8iIGQ9Im02NS41NjgsMTIzLjgxNGMtMjAuMzk4LDAuMDAxIC0zNi4yODksLTE4LjI0MDAxIC0zNy4wOSwtMTkuMTc1bC0wLjQ2NSwtMC43NDFjLTAuMDcsLTAuMTYxIC0wLjQ3NywtMS4wOTQgLTEuMDE4LC0yLjYzNGMtNS45NjUsLTIuODIyIC0xMS42ODcsLTcuNTQxIC0xNi4zNzcsLTE1LjEwMWMtMTcuMTk5LC0yNy43NjMgMy43ODIsLTQ4LjU2MyAxMC41ODIsLTU0LjM2M2MwLjQ3NCwtMC40MTUgMi4zMTYsLTEuNDg4IDUuMDYxLC0yLjcyNWMyLjkzOSwtNy4xMjMgOC4yMDgsLTEzLjA2MSAxNS42NzIsLTE3LjY1OGMxNS4zODksLTkuNDc5IDI4LjE2NywtNy45NjYgMzYuMTc4LC01LjAzMWMxMS40NjgsNC4yIDE5LjksMTMuOCAyMi4zLDIxLjEwMWMwLjAyOSwwLjEgMC4xLDAuMiAwLjEsMC4yNjZjMC44NDUsMC41IDEuNywxIDIuNSwxLjVjMjIuMDkyOTksMTUgMjMuOSwzNC45IDE4LjMsNDguNjVjLTMuNDQ0LDguMzQ0IC05LjA5NiwxOC41NDEgLTIwLjc4MSwyMy45NDNjLTMuNTk0LDcuMzQ2IC0xMC4xNDYsMTQuMzA0IC0yMS41NSwxOS4xYy00LjYyODAxLDIuMDU0IC05LjA4LDIuODU0IC0xMy4zOCwyLjg2OGwtMC4wMzIsMGwwLDB6bS0yOS4wMTIsLTE5LjUyN2M2LjkyNyw2LjM5OTk5IDIzLjEsMTguMzk5OTkgNDAsMTEuM2M2Ljg1MSwtMi45MDUgMTEuNjc4LC02LjY2MSAxNS4wMjYsLTEwLjc4MmMtMy40MTIsMC42NzggLTcuMTc4LDEuMDMxIC0xMS40MTUsMC45MmMtNS44NzQsLTAuMTMgLTExLjM3NCwtMS43NjYgLTE2LjM1NCwtNC4xOTNjLTQuMzQyLDEuNTY4IC0xNS40MTMsNC44NjggLTI3LjIxMywyLjc1NWwtMC4wNDQsMHptMzMuMTA1LC02LjgxMWMzLjM4MywxLjMgNywyLjEgMTAuNywyLjJjNi4yNTcsMC4xIDExLjQ1OSwtMC43OTIgMTUuNzU0LC0yLjQ1M2M0LjY2NywtMTEuMzM4IDEuNjA4LC0yMy4yNTEgMC4yNTQwMSwtMjcuNDNjLTAuOTY1LDEuMzY3IC0yLjAyMywyLjY5NyAtMy4xNzUsNGMtOS40MjEsMTAuNTA3IC0xOC43OTQsMTkuNDA3IC0yMy40OTQsMjMuNjgzbC0wLjAzODk5LDBsMCwwem0tMzcuNzcxLC0wLjU1NGM5LjI5MiwzLjYwMDAxIDE4LjksMi4yIDI1LjEsMC41Yy0xMS44MzYsLTguMDc5OTkgLTE5LjQyNywtMTkuMyAtMTkuOTA5LC0yMC4wMjQ5OWwtNS45NDcsLTguMzc0Yy0wLjAyOSwwLjA4OSAtMC4wNTYsMC4xNzc5OSAtMC4wODIsMC4zYy0zLjI0NCwxMC41NzcgLTAuOTUyLDIxLjU3NyAwLjg0OCwyNy41OTlsLTAuMDEsMGwwLDB6bTIuMDk2LC0zNC4xNTVsOC4wMDYsMTEuMjczYzAuMTU1LDAuMiA5LDEzLjIgMjEuOCwyMC42YzMuNTA2LC0zLjEyNyAxNC4xODQsLTEyLjg3NiAyNC44ODMsLTI0Ljg1NGMxLjQzOSwtMS42MDggMi42OTksLTMuMjg5IDMuNzg4OTksLTUuMDQ1MDFjLTYuMDIyOTksLTYuODE2IC0xOC4wMjU5OSwtMjAuMDc3IC0yNy42OTksLTI4Ljc4OWMtNC45NSwyLjU3MyAtOC4xODcsNS4wMzggLTguMjc1LDUuMTA3Yy0wLjM3MiwwLjM0MSAtMTUuMTksOS40NDEgLTIyLjQ5LDIxLjcwOGwtMC4wMTQsMGwwLDB6bTY1Ljc5OCwxLjIzM2MwLjYxNDAxLDAuNyAxLDEuMiAxLjEwMDAxLDEuMjcybDAuNTEyLDAuODQ1OTljMC4wODgsMC4yIDUsMTMuMTAwMDEgMi4xLDI3LjAyM2M2LjE4MSwtNC43MjUgOS43MjUsLTExLjMwOCAxMi4yNDgsLTE3LjQyOGM0LjQ5MSwtMTAuODgyIDMuMjA4LC0yNi41NzIgLTEzLjAxMiwtMzkuMTgxYzEuODg0OTksMTAuMTY4IDAuODY4LDE5LjM2OCAtMi45MzIsMjcuNDQ3bC0wLjAxNjAxLDAuMDIxem0tNzUuNzc3LC0yNi42Yy04LjExMSw3LjIgLTIxLjgwNywyMy44IC04LjMwNyw0NS42MDRjMi42ODQsNC4zIDUuNyw3LjYgOSw5Ljk3MmMtMS40MywtNi44ODYgLTIuMjA4LC0xNi4xODMgMC41OTYsLTI1LjM5MmMwLjUzMiwtMS43NCAxLjIxOSwtMy40MzkgMi4wMjksLTUuMDg1Yy0xLjkxNSwtNC43OTkgLTQuOTI1LC0xNC41OTkgLTMuMzI1LC0yNS4xNDhsMC4wMDcsMC4wNDl6bTQ2LjcxOSwtNC4xYzguNzQ2LDguMSAxOC42LDE4LjkgMjQuNywyNS42OTFjMi45NTYsLTcuODAzIDMuMDEyLC0xNi44MDcgMC4xNTIsLTI2LjkxOGMtOC4zNjEsLTMuNDczIC0xNy40NzgsLTEuNjczIC0yNC44NzgwMSwxLjIwNWwwLjAyNiwwLjAyMmwwLDB6bS0zOS42NzMsMC4xOTJjLTIuNjY0LDguMjI4IC0xLjMxMywxNi42IDAuMywyMi4zNTRjOC4zNzQsLTExLjQ4NSAyMS4wNTgsLTE5LjI2NyAyMS43NTQsLTE5LjY5YzAuMjQ5LC0wLjE5NyAyLjYyNiwtMi4wMTMgNi40MDEsLTQuMTc3Yy04LjgwNiwtNC42NzkgLTIxLjIwOCwtMS4zNzkgLTI4LjQwOCwxLjUxM2wtMC4wNDcsMHptMTguMDQxLC05Ljc5MmM1Ljg2LDAgMTEuNywxLjMgMTYuNiw1LjEzNGM3LjM2LC0zLjMxIDE3LjA3NiwtNi4wNjcgMjYuNjY0LC00LjA0NGMtMy4wMTUsLTQuNTEgLTguNTA5LC05Ljg5OCAtMTYuMjgxLC0xMi43NDdjLTkuNzAyLC0zLjU1OCAtMjAuNDEyLC0yIC0zMC45NzYsNC41MDdjLTQuMzEsMi42NTUgLTcuNzUxLDUuODAxIC0xMC4yODEsOS40MDFjNC4zMzYsLTEuMjUxIDkuMjgsLTIuMjUxIDE0LjI4LC0yLjI0OGwtMC4wMDYsLTAuMDAzeiIgZmlsbD0id2hpdGUiLz4KICAgPC9nPgogIDwvZz4KICA8ZyBpZD0iTGF5ZXJfNSIvPgogPC9nPgo8L3N2Zz4="/></div><div class="title"><span class="text">Welcome to </span><span class="IBM">IBM</span>
  <fmt:message key="product.name.${clientId}" var="productName"/>
  <c:set var="unresolvedName" value="???product.name.${clientId}???"/>
  <c:if test = "${pageScope.productName eq unresolvedName}">
    <c:set var="productName"><fmt:message key="product.name.default"/></c:set>
  </c:if>
  <span class="text"><c:out value="${productName}"/></span>
</div>
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
          <input type="hidden" value="<%=request.getAttribute("stateCookie")%>" name="stateCookie"/>
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
          </div></div></div></body></html>