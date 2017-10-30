<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="<c:url value="/resources/core/css/webclient.css" />" rel="stylesheet">
</head>
<body>
	<table id="soapResponseTable">
		<tr> 
			<td> 
				<pre> 
					<h3> 
						<c:out value="${responseXml}"></c:out>
					</h3> 
				</pre> 
			</td>
		</tr>
	</table>
</body>
</html>