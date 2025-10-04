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
<form action="${pageContext.request.contextPath}/pagamentos" method="post">
  <input type="hidden" name="action" value="create">
  <label>Status:</label>
  <select name="status">
    <option value="true">Pagamento Realizado ✅</option>
    <option value="false">Pagamento Pendente ❌</option>
  </select>
  <label>Data de Vencimento:</label>
  <input type="date" name="dataVencimento">
  <label>Data de Pagamento:</label>
  <input type="date" name="dataPagamento">
  <label>Tipo de Pagamento:</label>
  <select name="tipoPagamento">
    <option value="debito">Débito</option>
    <option value="credito">Crédito</option>
    <option value="pix">PIX</option>
  </select>
  <label>ID da Fábrica que o pagamento refere-se:</label>
  <select name="fkFabrica">
    <option value="" selected> --- Selecione --- </option>
    
    <% for (int id : fabricas.keySet()) { %>
    <option value="<%= id %>">
      <%= fabricas.get(id) %>
    </option>
    <% } %>
  </select>
  <input type="submit">
</form>
<a href="${pageContext.request.contextPath}/pagamentos?action=read">Cancelar</a>
<% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>