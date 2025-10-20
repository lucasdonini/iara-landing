<%@ page import="java.util.Map" %>
<%@ page import="com.model.MetodoPagamento" %>
<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
  Map<Integer, String> planos = (Map<Integer, String>) request.getAttribute("planos");
  String erro = (String) request.getAttribute("erro");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Cadastro - Pagamento</h1>
<form action="${pageContext.request.contextPath}/area-restrita/pagamentos" method="post">
  <input type="hidden" name="action" value="create">
  <label>Status:</label>
  <select name="status" required>
    <option value="true">Pagamento Realizado ✅</option>
    <option value="false" selected>Pagamento Pendente ❌</option>
  </select>
  <label>Valor Pago:</label>
  <input type="number" step="any" placeholder="R$" name="valor" required>
  <label>Data de Vencimento:</label>
  <input type="date" name="data_vencimento" required>
  <label>Data de Pagamento:</label>
  <input type="datetime-local" name="data_pagamento">
  <label>Data de Início:</label>
  <input type="datetime-local" name="data_inicio">
  <label>Tipo de Pagamento:</label>
  <select name="metodo_pagamento" required>
    <option value="" selected> --- Selecione ---</option>

    <% for (MetodoPagamento m : MetodoPagamento.values()) {%>
    <option value="<%= m.getNivel() %>"><%= m.toString() %></option>
    <% } %>
  </select>
  <label>Fábrica referente:</label>
  <select name="fk_fabrica" required>
    <option value="" selected> --- Selecione --- </option>
    
    <% for (int id : fabricas.keySet()) { %>
    <option value="<%= id %>">
      <%= fabricas.get(id) %>
    </option>
    <% } %>
  </select>
  <select name="fk_plano" required>
      <option value="" selected> --- Selecione ---</option>

      <% for (int id : planos.keySet()) {%>
      <option value="<%= id %>"><%= planos.get(id) %></option>
      <% } %>
  </select>
  <input type="submit">
</form>
<a href="${pageContext.request.contextPath}/area-restrita/pagamentos">Cancelar</a>
<% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>