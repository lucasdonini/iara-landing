<%@ page import="com.model.Plano" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="pt-BR">
<%
  Plano plano = (Plano) request.getAttribute("infosPlano");
  String erro = (String) request.getAttribute("erro");
%>
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Editar Plano - ID: <%= plano.getId() %>
</h1>
<form action="${pageContext.request.contextPath}/area-restrita/planos?action=update" method="post">
  <input type="hidden" name="action" value="update">
  <input type="hidden" name="id" value="<%= plano.getId() %>">
  <input type="text" name="nome" value="<%= plano.getNome() %>" placeholder="Novo nome">
  <input type="number" name="valor" value="<%= plano.getValor() %>" placeholder="Novo valor">
  <input type="text" name="descricao" value="<%= plano.getDescricao() %>" placeholder="Nova descricao">
  <button type="submit">Salvar</button>
</form>
<%
  if (erro != null && !erro.isBlank()) {
%>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>
