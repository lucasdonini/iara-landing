<%@ page import="static com.model.Pagamento.tiposPagamento" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.model.Pagamento" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
  Pagamento pagamento = (Pagamento) request.getAttribute("pagamento");
  String erro = (String) request.getAttribute("erro");
%>

<html lang="pt-BR">
<head>
  <title>Landing Teste</title>
</head>

<body>
<h1>
  Editar Pagamento - ID: <%= pagamento.getId() %>
</h1>

<form action="${pageContext.request.contextPath}/pagamentos" method="post">
  <input type="hidden" name="id" value="<%= pagamento.getId() %>">
  <input type="hidden" name="action" value="update">
  
  <label>Valor pago: R$</label>
  <input type="number" placeholder="Novo valor" name="valor_pago" value="<%= pagamento.getValor() %>">
  
  <label>Status:</label>
  <select name="status">
    <option value="true" <%= pagamento.getStatus() ? "selected" : "" %>>Pagamento realizado ✅</option>
    <option value="false" <%= !pagamento.getStatus() ? "selected" : "" %>>Pagamento pendente ❌</option>
  </select>
  
  <label>Data de vencimento:</label>
  <input type="date" name="data_vencimento" value="<%= pagamento.getDataVencimento() %>">
  
  <label>Data de pagamento:</label>
  <input type="date" name="data_pagamento" value="<%= pagamento.getDataPagamento() %>">
  
  <select name="tipo_pagamento">
    <% for (String key : tiposPagamento.keySet()) { %>
    <option value="<%= key %>" <%= pagamento.getTipoPagamento().equals(key) ? "selected" : "" %>>
      <%= tiposPagamento.get(key) %>
    </option>
    <% } %>
  </select>
  
  <select name="id_fabrica">
    <% for (int id : fabricas.keySet()) { %>
    <option value="<%= id %>" <%= id == pagamento.getIdFabrica() ? "selected" : "" %>>
      <%= fabricas.get(id) %>
    </option>
    <% } %>
  </select>
  <button type="submit">Salvar</button>
</form>
<% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>
