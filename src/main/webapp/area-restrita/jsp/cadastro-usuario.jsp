<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
  List<String> emailGerentes = (List<String>) request.getAttribute("emailGerentes");
  String erro = (String) request.getAttribute("erro");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Cadastro - Administrador</h1>
<form action="${pageContext.request.contextPath}/area-restrita/usuarios" method="post">
  <input type="hidden" name="action" value="create">
  <input type="text" name="nome" placeholder="Nome" required>

  <select name="email_gerentes">
      <option value="" selected>-- Selecione --</option>
      <% if (emailGerentes != null) {%>
      <% for (String email:emailGerentes) {%>
      <option value="<%= email %>"><%= email %></option>
      <% } %>
      <% } %>
  </select>

  <select name="genero" required>
      <option value="" selected>-- Selecione --</option>
      <% for (String genero: List.of("Masculino", "Feminino", "Outros")) {%>
      <option value="<%= genero.toLowerCase() %>">
          <%= genero %>
      </option>
      <% } %>
  </select>

  <input type="date" name="data_nascimento" max="1999-12-31" required>
  <input type="text" name="cargo" placeholder="Cargo" required>
  <input type="email" name="email" placeholder="Email" required>
  <input type="text" pattern=".{8,}" title="A senha deve ter 8 ou mais caractéres" name="senha" placeholder="Senha" required>
  
  <select name="fk_fabrica" required>
    <option value="" selected>-- Selecione --</option>
    <% for (int id : fabricas.keySet()) { %>
    <option value="<%= id %>">
      <%= fabricas.get(id) %>
    </option>
    <% } %>
  </select>
  
  <button type="submit">Cadastrar</button>
</form>
<a href="${pageContext.request.contextPath}/area-restrita/usuarios">Cancelar</a>
<% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>