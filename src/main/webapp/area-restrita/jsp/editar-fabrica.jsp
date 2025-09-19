<%@ page import="com.model.Fabrica" %>
<%@ page import="com.model.Endereco" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: lucasdonini-ieg
  Date: 10/09/2025
  Time: 18:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  Fabrica f = (Fabrica) request.getAttribute("infosFabrica");
  Endereco e = f.getEndereco();
  String complemento = e.getComplemento();
%>
<html>
<head>
  <title>Title</title>
</head>
<body>
<h1>Editar Fábrica - ID: <%= f.getId() %>
</h1>
<form action="${pageContext.request.contextPath}/area-restrita/fabricas" method="post">
  <h2>Dados da fábrica</h2>
  <input type="text" name="nome" value="<%= f.getNome() %>" placeholder="Nome">
  <input type="text" name="cnpj" value="<%= f.getCnpj() %>" pattern="\d{14}" title="CNPJ inválido" placeholder="CNPJ">
  <input type="email" name="email" value="<%= f.getEmail() %>" placeholder="Email para contato">
  <input type="text" name="ramo" value="<%= f.getRamo() %>" placeholder="Ramo">
  <input type="text" name="nome_empresa" value="<%= f.getNomeEmpresa() %>" placeholder="Nome da empresa">
  <select name="status">
    <% for (Boolean b : List.of(true, false)) { %>
    <option value="<%= b.toString() %>" <%= b ? "selected" : "" %>><%= b ? "Ativa" : "Inativa" %>
    </option>
    <% } %>
  </select>
  
  <h2>Endereço da fábrica</h2>
  <input type="text" name="cep" value="<%= e.getCep() %>" pattern="\d{8}" title="Insira um CEP válido"
         placeholder="CEP">
  <input type="text" name="logradouro" value="<%= e.getRua() %>" placeholder="Logradouro">
  <input type="number" name="numero" value="<%= e.getNumero() %>" placeholder="n°">
  <input type="text" name="complemento" value="<%= complemento != null ? complemento : "" %>" placeholder="Complemento">
  
  <input type="hidden" name="id_fabrica" value="<%= f.getId() %>">
  <input type="hidden" name="id_endereco" value="<%= e.getId() %>">
  <input type="hidden" name="action" value="update">
  
  <button type="submit" style="display: block">Salvar</button>
</form>
</body>
</html>
