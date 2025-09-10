<%@ page import="com.dto.SuperAdmDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.model.Fabrica" %><%--
  Created by IntelliJ IDEA.
  User: lucasdonini-ieg
  Date: 08/09/2025
  Time: 17:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  List<Fabrica> fabricas = (List<Fabrica>) request.getAttribute("fabricas");
%>
<html>
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Fabricas</h1>
<a href="${pageContext.request.contextPath}/area-restrita/index">Voltar à área restrita</a>
<table border="1">
  <tr>
    <th>Id</th>
    <th>Nome</th>
    <th>CNPJ</th>
    <th>Status</th>
    <th>Email</th>
    <th>Empresa</th>
    <th>Ramo</th>
    <th>Endereço</th>
  </tr>
  <% for (Fabrica f : fabricas) { %>
  <tr>
    <td>
      <%= f.getId() %>
    </td>
    <td>
      <%= f.getNome() %>
    </td>
    <td>
      <%= f.cnpjFormatado() %>
    </td>
    <td>
      <%= f.getStatus() ? "Ativa" : "Inativa" %>
    </td>
    <td>
      <%= f.getEmail() %>
    </td>
    <td>
      <%= f.getNomeEmpresa() %>
    </td>
    <td>
      <%= f.getRamo() %>
    </td>
    <td>
      <%= f.getEndereco().toString() %>
    </td>
    <td>
      <form action="${pageContext.request.contextPath}/area-restrita/update-fabrica" method="get">
        <input type="hidden" name="id" value="<%= f.getId() %>">
        <button type="submit">Editar</button>
      </form>
      <form action="${pageContext.request.contextPath}/area-restrita/delete-fabrica" method="post">
        <input type="hidden" name="id_fabrica" value="<%= f.getId() %>">
        <input type="hidden" name="id_endereco" value="<%= f.getEndereco().getId() %>">
        <button type="submit">Deletar</button>
      </form>
    </td>
  </tr>
  <% } %>
</table>
<a href="${pageContext.request.contextPath}/area-restrita/fabrica/cadastro.html">Cadastrar nova Fábrica</a>
</body>
</html>
