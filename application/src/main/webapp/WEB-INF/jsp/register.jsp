<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
		<title><spring:message code="screen.register.caption" /></title>
    </head>
	<body>
		<spring:eval expression="@applicationProps['application.version']" var="applicationVersion"/>
		<spring:url value="/resources-{applicationVersion}" var="resourceUrl">
			<spring:param name="applicationVersion" value="${applicationVersion}"/>
		</spring:url>	
	
		<script type="text/javascript" src="${resourceUrl}/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="${resourceUrl}/register.js"></script>
	    <div align="center">
		    <form:form modelAttribute="user" action="${pageContext.request.contextPath}/users/new" method="POST">
		        <table>
		            <caption><spring:message code="screen.register.caption" /></caption>
		            <tr>
		                <td><label for="userName"><spring:message code="screen.register.userName" /></label><br/><form:errors path="userName" cssClass="errors"/></td>
		                <td><input id="userName" name="userName" type="text" value="${user.userName}"/></td>
		            </tr>
		            <tr>
		                <td><label for="name"><spring:message code="screen.register.name" /></label><br/><form:errors path="name" cssClass="errors"/></td>
		                <td><input id="name" name="name" type="text" value="${user.name}"/></td>
		            </tr>
		            <tr>
		                <td><label for="surName"><spring:message code="screen.register.surName" /></label><br/><form:errors path="surName" cssClass="errors"/></td>
		                <td><input id="surName" name="surName" type="text" value="${user.surName}"/></td>
		            </tr>
		            <tr>
		                <td><label for="password"><spring:message code="screen.register.password" /></label><br/><form:errors path="password" cssClass="errors"/></td>
		                <td><input id="password" name="password" value="" type="password" /></td>
		            </tr>
		            <tr>
		                <td><label for="passwordConfirmation"><spring:message code="screen.register.passwordConfirmation" /></label></td>
		                <td><input id="passwordConfirmation" name="passwordConfirmation" value="" type="password" /></td>
		            </tr>
		            <tr>
		                <td><label for="tenantCheck"><spring:message code="screen.register.asTenantUser" /></label></td>
		                <td><input id="tenantCheck" name="tenantCheck" value="" type="checkbox" /></td>
		            </tr>
		            <tr id="tenantNameRow">
		                <td><label for="tenantName"><spring:message code="screen.register.tenantName" /></label><br/><form:errors path="tenantName" cssClass="errors"/></td>
		                <td><input id="tenantName" name="tenantName" type="text" value="${user.tenantName}" /></td>
		            </tr>
		            <tr style="display: none" id="tenantIdRow">
						<td><label for="tenantId"><spring:message code="screen.register.tenant"/></label><br/><form:errors path="tenantId" cssClass="errors"/></td>
						<td>
							<form:select path="tenantId">
								<option value=""><spring:message code="screen.tenant.input.empty"/></option>
								<form:options items="${user.tenantList}" itemValue="id" itemLabel="name" />
							</form:select>
						</td>
		            </tr>
		            <tr>
		            	<td colspan="2" align="center">
		            		<img src="${pageContext.request.contextPath}/captcha.jpg"/>
		            	</td>
		            </tr>
		            <tr>
		            	<td><label for="captcha"><spring:message code="screen.register.captcha" /></label><br/><form:errors path="captcha" cssClass="errors"/></td>
		            	<td><input type="text" name="captcha" id="captcha"/></td>
		            </tr>
		            <tr>
		                <td colspan="2"><input type="submit" id="register" value="<spring:message code="screen.register.submit" />" name="register" /></td>
		            </tr>
		        </table>
		    </form:form>
		</div>
	</body>
</html>