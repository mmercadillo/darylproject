<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">
	<head>
		<title>Error Occurred</title>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" />
	</head>
	<body>
	    <div class="container">
	        <h1>Error Occurred!</h1>
	        <div class="alert alert-danger" role="alert">
	            <b>
	            [<span th:text="${status}">status</span>]
	            <span th:text="${error}">error</span>
	            </b>
	            <p th:text="${message}">message</p>
	        </div>
	    </div>
	</body>

</html>