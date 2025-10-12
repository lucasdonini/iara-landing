<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%
  String erro = (String) request.getAttribute("erro");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Cadastro - Super Administrador</h1>
<form action="${pageContext.request.contextPath}/area-restrita/superadms" method="post">
  <input type="hidden" name="action" value="create">
  <input type="text" name="nome" placeholder="Nome">
  <input type="text" name="cargo" placeholder="Cargo">
  <input type="email" name="email" placeholder="Email">
  <input type="text" pattern=".{8,}" title="A senha deve ter 8 ou mais caracteres" name="senha" placeholder="Senha">
  <button type="submit">Cadastrar</button>
</form>
<a href="${pageContext.request.contextPath}/area-restrita/superadms">Cancelar</a>
<%
  if (erro != null && !erro.isBlank()) {
%>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>