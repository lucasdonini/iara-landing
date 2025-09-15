<%@ page import="com.dto.PagamentoDTO" %><%--
  Created by IntelliJ IDEA.
  User: ryanmoraes-ieg
  Date: 11/09/2025
  Time: 20:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    PagamentoDTO pagamento = (PagamentoDTO) request.getAttribute("infosPagamento");
%>
<head>
    <title>Landing Teste</title>
</head>
<body>
<h1>Editar Pagamento - ID: <%= pagamento.getId() %>
</h1>
<form action="${pageContext.request.contextPath}/area-restrita/update-pagamento" method="post">
    <input type="hidden" value="<%=pagamento.getId()%>" name="id">
    <label>Valor pago: R$</label>
    <input type="number" placeholder="Novo valor" name="valorPago" value="<%= pagamento.getValorPago() %>">
    <label>Status:</label>
    <select name="status">
        <option value="true" <%= pagamento.getStatus() ? "selected" : "" %>>Pagamento realizado ✅</option>
        <option value="false" <%= !pagamento.getStatus() ? "selected" : "" %>>Pagamento pendente ❌</option>
    </select>
    <label>Nova data de vencimento:</label>
    <input type="date" name="dataVencimento" value="<%= pagamento.getDataVencimento() %>">
    <label>Nova data de pagamento:</label>
    <input type="date" name="dataPagamento" value="<%= pagamento.getDataPagamento() %>">
    <select name="tipoPagamento">
        <option value="credito" <%= pagamento.getTipoPagamento().equals("credito") ? "selected" : ""%>>Crédito</option>
        <option value="debito" <%= pagamento.getTipoPagamento().equals("debito") ? "selected" : ""%>>Débito</option>
        <option value="pix" <%= pagamento.getTipoPagamento().equals("pix") ? "selected" : ""%>>PIX</option>
    </select>
    <input type="number" name="fkFabrica" value="<%= pagamento.getFkFabrica() %>" placeholder="Novo ID de Fábrica">
    <button type="submit">Salvar</button>
</form>
</body>
</html>
