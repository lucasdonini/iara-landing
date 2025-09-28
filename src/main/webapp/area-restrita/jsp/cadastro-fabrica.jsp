<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String erro = (String) request.getAttribute("erro");
%>
<html lang="pt-BR">
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Cadastro - Fábrica</h1>
<form action="${pageContext.request.pageContext}/area-restrita/fabricas?action=create" method="post">
  <h2>Dados da fábrica</h2>
  <input type="hidden" name="action" value="create">
  <input type="text" name="nome" placeholder="Nome">
  <input type="text" name="cnpj" pattern="\d{14}" title="CNPJ inválido" placeholder="CNPJ">
  <input type="email" name="email" placeholder="Email para contato">
  <input type="text" name="ramo" placeholder="Ramo">
  <input type="text" name="empresa" placeholder="Nome da empresa">
  
  <h2>Endereço da fábrica</h2>
  <input type="text" name="cep" pattern="\d{8}" title="Insira um CEP válido" placeholder="CEP">
  <input type="text" name="logradouro" placeholder="Logradouro">
  <input type="number" name="numero" placeholder="n°">
  <input type="text" name="complemento" placeholder="Complemento">
  
  <button type="submit">Cadastrar</button>
</form>
<a href="${pageContext.request.contextPath}/area-restrita/fabricas?action=read">Cancelar</a>
<% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>
