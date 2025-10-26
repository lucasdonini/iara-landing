<%@ page import="java.util.Map" %>
<%@ page import="com.model.Pagamento" %>
<%@ page import="com.model.MetodoPagamento" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
  Map<Integer, String> planos = (Map<Integer, String>) request.getAttribute("planos");
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

<form action="${pageContext.request.contextPath}/area-restrita/pagamentos" method="post">
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
  <input type="datetime-local" name="data_pagamento" value="<%= pagamento.getDataPagamento() %>" <%= pagamento.getStatus() ? "required" : ""%> >

  <label>Data de início:</label>
  <input type="datetime-local" name="data_inicio" value="<%= pagamento.getDataInicio() %>">

  <select name="fk_metodopag">
    <% for (MetodoPagamento m : MetodoPagamento.values()) { %>
    <option value="<%= m.getNivel() %>" <%= pagamento.getMetodoPagamento().equals(m) ? "selected" : "" %>>
      <%= m.toString() %>
    </option>
    <% } %>
  </select>
  
  <select name="fk_fabrica">
    <% for (int id : fabricas.keySet()) { %>
    <option value="<%= id %>" <%= id == pagamento.getFkFabrica() ? "selected" : "" %>>
      <%= fabricas.get(id) %>
    </option>
    <% } %>
  </select>

  <select name="fk_plano">
      <% for (int id : planos.keySet()) { %>
      <option value="<%= id %>" <%= id == pagamento.getFkPlano() ? "selected" : "" %>>
          <%= planos.get(id) %>
      </option>
      <% } %>
  </select>
  <button type="submit">Salvar</button>
</form>
<a href="${pageContext.request.contextPath}/area-restrita/pagamentos">Cancelar</a>
<% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>
