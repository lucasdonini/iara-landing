<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String erro = (String) request.getAttribute("erro");
%>
<html lang="pt-BR">
<head>
  <title>Página de cadastro - Planos</title>
</head>
<body>
<h1>Página de Teste para Cadastro</h1>
<form action="${pageContext.request.contextPath}/area-restrita/planos" method="post">
  <input type="hidden" name="action" value="create">
  <label>Nome:</label>
  <input type="text" name="nome">
  <label>Valor:</label>
  <input type="number" step="any" placeholder="R$" name="valor">
  <label>Descrição:</label>
  <input type="text" name="descricao">
  <input type="submit">
</form>
<a href="${pageContext.request.contextPath}/area-restrita/planos">Cancelar</a>
<%
  if (erro != null && !erro.isBlank()) {
%>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>