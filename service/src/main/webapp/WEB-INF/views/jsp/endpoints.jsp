<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Webservices client for Soap requests</title>
</head>
<body>

	<h2>Endpoints</h2>
	<table id="theTable"> 
	<c:forEach items="${endpointMap}" var="endpointMap">
    <tr>
        <td><c:out value = "${endpointMap.key}"></c:out></td>
        <td/>
        <td/>
        <td><c:out value = "${endpointMap.value}"></c:out></td>
    </tr>
</c:forEach>
</table>
	
</body>
</html>