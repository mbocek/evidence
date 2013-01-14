<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
		<title><spring:message code="screen.login.caption" /></title>
	</head>
	
	<body>
		<div align="center">
			<form action="j_spring_security_check" method="post">
		        <c:if test="${not empty param.login_error}">
					<spring:message code="screen.login.message.canNotLogin" />
		        </c:if>
				<table>
		            <caption><spring:message code="screen.login.caption" /></caption>
					<tr>
						<td><label for="userName"><spring:message code="screen.login.userName" /></label></td>
						<td><input type="text" name="j_username" id="userName" 
						<c:if test="${not empty param.login_error}">
							value='<%= session.getAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY) %>'
						</c:if>></input></td>
					</tr>
					<tr>
						<td><label for="password"><spring:message code="screen.login.password" /></label></td>
						<td><input type="password" name="j_password" id="password"></input></td>
					</tr>
					<tr>
						<td><input type="submit" value="<spring:message code="screen.login.submit" />"></input></td>
					</tr>
				</table>
			</form>
		</div>
	</body>
</html>