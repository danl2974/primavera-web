<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Home 
</h1>

<P>  The time on the server is ${serverTime}. </P>

<p>
${timeproduct}</p>

<p>
Locale: ${locale}</p>

<p>
WS response: <br/><br/> 
${wsresponse}</p>

</body>
</html>
