<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
 <link href="<c:url value="/resources/core/css/webclient.css" />" rel="stylesheet">
<title>Webservices client for Soap requests</title>
</head>
<body>

	<h2>Webservices Client</h2>
	<form:form method="POST" action="webMethodList" modelAttribute="wsdlRequest">

		<table>
			<tr>
				<td><form:label path="wsdlUrl">WSDL url</form:label></td>
				<td><form:input path="wsdlUrl" size="50" required="required"/></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="Submit" /></td>
			</tr>
		</table>
	</form:form>
</body>
</html>