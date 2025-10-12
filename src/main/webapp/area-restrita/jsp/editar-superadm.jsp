<%@ page import="com.dto.SuperAdmDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  SuperAdmDTO adm = (SuperAdmDTO) request.getAttribute("superAdm");
  String erro = (String) request.getAttribute("erro");
%>
<html>
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>
  Editar Super Adm - ID: <%= adm.getId() %>
</h1>

<form action="${pageContext.request.contextPath}/area-restrita/superadms" method="post">
  <input type="hidden" name="action" value="update">
  <input type="hidden" name="id" value="<%= adm.getId() %>">
  <input type="text" name="nome" value="<%= adm.getNome() %>" placeholder="Novo nome">
  <input type="text" name="cargo" value="<%= adm.getCargo() %>" placeholder="Novo cargo">
  <input type="email" name="email" value="<%= adm.getEmail() %>" placeholder="Novo email">
  <input type="text" name="senha_atual" placeholder="Insira sua senha atual">
  <input type="text" name="nova_senha" pattern=".{8,}" title="A senha deve ter pelo menos 8 dígitos"
         placeholder="Insira sua nova senha">
  <button type="submit">Salvar</button>
</form>

<a href="${pageContext.request.contextPath}/area-restrita/superadms?action=read">Cancelar</a>
<% if (erro != null) { %>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>
