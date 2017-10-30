<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Webservices client for Soap requests</title>
<link href="<c:url value="/resources/core/css/webclient.css" />" rel="stylesheet">
</head>
<body>

	<h2>Method Elements</h2>
	<form:form method="POST" action="webResponse"
		modelAttribute="wsdlRequest">
		<table id="webMethodElementsTable">
			<%-- 		<c:forEach items="${methodElementList}" var="element"> --%>
			<!-- 			<tr> -->
			<%-- 				<td><c:out value="${element.elmentName}"></c:out> --%>
			<%-- 				<input type="text" value="${element.elmentValue}"/></td> --%>
			<!-- 			</tr> -->
			<!-- 			<tr /> -->
			<%-- 		</c:forEach> --%>
			<c:set var="selMethod" value="${wsdlRequest.selectedMethod}"/>
			<c:forEach items="${wsdlRequest.webMethodMap[selMethod]}" var="wsdlRequestElement"
				varStatus="i" begin="0">
				<tr>
<%-- 				<form:hidden path="webRequestElementList[${i.index}].elementName" /> --%>
					<td><form:label path="webRequestElementList[${i.index}].elementName">${wsdlRequestElement.elementName}</form:label></td>
<%-- 					<td><c:out value="${wsdlRequestElement.elementName}"></c:out> --%>
					<td><form:input path="webRequestElementList[${i.index}].elementValue" id="elementValue${i.index}" /></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="2"><input type="submit" value="Submit" /></td>
			</tr>
		</table>
	</form:form>
</body>
</html>