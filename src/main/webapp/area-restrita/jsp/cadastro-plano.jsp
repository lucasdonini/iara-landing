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
  <input type="text" name="nome" required>
  <label>Valor:</label>
  <input type="number" step="any" placeholder="R$" name="valor" required>
  <label>Descrição:</label>
  <input type="text" name="descricao" required>
  <label>Anos:</label>
  <input type="number" name="anos_duracao" min="0">
  <label>Meses:</label>
  <input type="number" name="meses_duracao" min="0">
  <label>Dias:</label>
  <input type="number" name="dias_duracao" min="0">
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