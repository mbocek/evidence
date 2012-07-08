<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
	</head>
	
	<body>
		<div align="center">Login here
			<form action="j_spring_security_check" method="post">
		        <c:if test="${not empty param.login_error}">
		                Can not login user!
		        </c:if>
				<table>
					<tr>
						<td>User</td>
						<td><input type="text" name="j_username" <c:if test="${not empty param.login_error}">value='<%= session.getAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY) %>'</c:if>/></td>
					</tr>
					<tr>
						<td>Password</td>
						<td><input type="password" name="j_password" /></td>
					</tr>
					<tr>
						<td><input type="submit" value="login"><td>
					</tr>
				</table>
			</form>
		</div>
	</body>
</html>