<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
	<body>
		<h2>Evidence application!</h2>
		<sec:authorize access="isAuthenticated()">
			<jsp:forward page="/app" />
		</sec:authorize>
		<a href="<c:url value="/app" />">Sign in</a><br/>
		<a href="<c:url value="/users/new" />">Register</a>
	</body>
</html>
