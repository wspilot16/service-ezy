<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Webservices client for Soap requests</title>
<link href="<c:url value="/resources/core/css/webclient.css" />" rel="stylesheet">
</head>
<body>

	<h2>Method Elements</h2>
	<form:form method="POST" action="webMethodElements"
		modelAttribute="wsdlRequest">
		<table id="theTable">
			<c:forEach items="${wsdlRequest.webMethodMap}" var="entry">
				<tr>
					<td><form:radiobutton path="selectedMethod"
							value="${entry.key}" /> <%-- 					<td><input type="radio" name="webMethod" value="${entry.key}"> --%>
						<c:out value="${entry.key}"></c:out></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="2"><input type="submit" value="Submit" /></td>
			</tr>
		</table>
	</form:form>
</body>
</html>