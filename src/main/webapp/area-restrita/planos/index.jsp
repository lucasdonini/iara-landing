<%@ page import="com.dto.PlanosDTO" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: ryanmoraes-ieg
  Date: 09/09/2025
  Time: 18:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Landing Teste</title>
</head>
<body>
    <%
  List<PlanosDTO> planos = (List<PlanosDTO>) request.getAttribute("planos");
%>
<html>
<head>
    <title>Landing Teste</title>
</head>
<body>
<h1>Planos</h1>
<a href="${pageContext.request.contextPath}/area-restrita/index">Voltar à área restrita</a>
<table border="1">
    <tr>
        <th>Id</th>
        <th>Nome</th>
        <th>Valor</th>
        <th>Descrição</th>
    </tr>
    <% for (PlanosDTO plano : planos) { %>
    <tr>
        <td><%= plano.getId() %></td>
        <td><%= plano.getNome() %></td>
        <td><%= plano.getValor() %></td>
        <td><%= plano.getDescricao() %></td>
        <td>
            <form action="${pageContext.request.contextPath}/area-restrita/update-plano" method="get">
                <input type="hidden" name="id" value="<%= plano.getId() %>">
                <button type="submit">Editar</button>
            </form>
            <form action="${pageContext.request.contextPath}/area-restrita/delete-plano" method="get">
                <input type="hidden" name="id" value="<%= plano.getId() %>">
                <button type="submit">Deletar</button>
            </form>
        </td>
    </tr>
    <% } %>
</table>
<a href="${pageContext.request.contextPath}/area-restrita/planos/cadastro.html">Cadastrar novo Plano</a>
</body>
</html>
