<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  String erro = (String) request.getAttribute("erro");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
  <title>Landing Teste</title>
</head>

<body>
<h1>Landing page para Teste</h1>
<a href="${pageContext.request.contextPath}/index.html">Página inicial</a>

<form action="${pageContext.request.contextPath}/login-handler" method="post">
  <input type="hidden" name="action" value="login">
  <input type="text" name="email" placeholder="Insira seu email">
  <input type="password" name="senha" placeholder="Insira a sua senha">
  <button type="submit">Entrar</button>
</form>
<% if (erro != null) { %>
<p>
  <%= erro %>
</p>
<% } %>
</body>

</html>
