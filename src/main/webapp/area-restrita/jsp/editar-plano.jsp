<%@ page import="com.model.Plano" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="pt-BR">
<%
  Plano plano = (Plano) request.getAttribute("plano");
  String erro = (String) request.getAttribute("erro");
%>
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Editar Plano - ID: <%= plano.getId() %>
</h1>
<form action="${pageContext.request.contextPath}/area-restrita/planos" method="post">
  <input type="hidden" name="action" value="update">
  <input type="hidden" name="id" value="<%= plano.getId() %>">
  <input type="text" name="nome" value="<%= plano.getNome() %>" placeholder="Novo nome">
  <input type="number" name="valor" step="any" value="<%= plano.getValor() %>" placeholder="Novo valor">
  <input type="text" name="descricao" value="<%= plano.getDescricao() %>" placeholder="Nova descricao">
  <label>Anos:</label>
  <input type="number" name="anos_duracao" value="<%= plano.getDuracao().getYears() %>" min="0">
  <label>Meses:</label>
  <input type="number" name="meses_duracao" value="<%= plano.getDuracao().getMonths() %>" min="0">
  <label>Dias:</label>
  <input type="number" name="dias_duracao" value="<%= plano.getDuracao().getDays() %>" min="0">
  <button type="submit">Salvar</button>
</form>
<a href="${pageContext.request.contextPath}/area-restrita/planos">Cancelar</a>
<% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>
