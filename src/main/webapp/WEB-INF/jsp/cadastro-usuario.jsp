<%@ page import="java.util.Map" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
  String erro = (String) request.getAttribute("erro");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Cadastro - Administrador</h1>
<form action="${pageContext.request.contextPath}/usuarios" method="post">
  <input type="hidden" name="action" value="create">
  <input type="text" name="nome" placeholder="Nome">
  <input type="email" name="email" placeholder="Email">
  <input type="text" pattern=".{8,}" title="A senha deve ter 8 ou mais caractéres" name="senha" placeholder="Senha">
  
  <select name="id_fabrica" required>
    <option value="" selected>-- Selecione --</option>
    <% for (int id : fabricas.keySet()) { %>
    <option value="<%= id %>">
      <%= fabricas.get(id) %>
    </option>
    <% } %>
  </select>
  
  <button type="submit">Cadastrar</button>
</form>
<a href="${pageContext.request.contextPath}/usuarios?action=read">Cancelar</a>
<% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>