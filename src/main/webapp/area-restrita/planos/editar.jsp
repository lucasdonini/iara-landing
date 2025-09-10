<%@ page import="com.dto.PlanosDTO" %><%--
  Created by IntelliJ IDEA.
  User: ryanmoraes-ieg
  Date: 09/09/2025
  Time: 20:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
  PlanosDTO plano = (PlanosDTO) request.getAttribute("infosPlano");
%>
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Editar Plano - ID: <%= plano.getId() %>
</h1>
<form action="${pageContext.request.contextPath}/area-restrita/update-plano" method="post">
  <input type="hidden" name="id" value="<%= plano.getId() %>">
  <input type="text" name="nome" value="<%= plano.getNome() %>" placeholder="Novo nome">
  <input type="number" name="valor" value="<%= plano.getValor() %>" placeholder="Novo valor">
  <input type="text" name="descricao" value="<%= plano.getDescricao() %>" placeholder="Nova descricao">
  <button type="submit">Salvar</button>
</form>
</body>
</html>
