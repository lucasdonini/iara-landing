<%@ page import="java.util.Map" %>
<%@page contentType="text/html; charset=UTF-8" language="java" %>
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
<h1>Cadastro - Pagamento</h1>
<form action="${pageContext.request.contextPath}/area-restrita/pagamentos" method="post">
  <input type="hidden" name="action" value="create">
  <label>Status:</label>
  <select name="status">
    <option value="true">Pagamento Realizado ✅</option>
    <option value="false">Pagamento Pendente ❌</option>
  </select>
  <label>Valor Pago:</label>
  <input type="number" step="any" placeholder="R$" name="valor">
  <label>Data de Vencimento:</label>
  <input type="date" name="data_vencimento">
  <label>Data de Pagamento:</label>
  <input type="date" name="data_pagamento">
  <label>Tipo de Pagamento:</label>
  <select name="tipo_pagamento">
    <option value="debito">Débito</option>
    <option value="credito">Crédito</option>
    <option value="pix">PIX</option>
  </select>
  <label>Fábrica referente:</label>
  <select name="id_fabrica">
    <option value="" selected> --- Selecione --- </option>
    
    <% for (int id : fabricas.keySet()) { %>
    <option value="<%= id %>">
      <%= fabricas.get(id) %>
    </option>
    <% } %>
  </select>
  <input type="submit">
</form>
<a href="${pageContext.request.contextPath}/area-restrita/pagamentos?action=read">Cancelar</a>
<% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>