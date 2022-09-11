<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="javax.naming.InitialContext,com.phoenixhell.ejbserver.FirstEjb,com.phoenixhell.ejbserver.FirstEjbLocal" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>  
<body>
<% 
InitialContext context = new InitialContext();
FirstEjbLocal firstEjbLocal = (FirstEjbLocal) context.lookup("FirstEjbBean/local");
String name = firstEjbLocal.sayHello("本地调用");
out.println(name);
%>
</body>
</html>