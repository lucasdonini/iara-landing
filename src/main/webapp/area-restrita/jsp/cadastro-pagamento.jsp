<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%
    String erro = (String) request.getAttribute("erro");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <title>Landing Teste</title>
</head>
<body>
<h1>Cadastro - Super Administrador</h1>
<form action="${pageContext.request.contextPath}/area-restrita/pagamentos?action=create" method="post">
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
    <input type="number" name="fkFabrica">
    <input type="submit">
</form>
<a href="${pageContext.request.contextPath}/area-restrita/pagamentos?action=read">Cancelar</a>
<%
    if (erro != null && !erro.isBlank()) {
%>
<p>
    <%= erro %>
</p>
<% } %>
</body>
</html>