<%@ page import="com.dto.SuperAdmDTO" %><%--
  Created by IntelliJ IDEA.
  User: lucasdonini-ieg
  Date: 08/09/2025
  Time: 13:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  SuperAdmDTO adm = (SuperAdmDTO) request.getAttribute("infosAdm");
%>
<html>
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Editar Super Adm - ID: <%= adm.getId() %>
</h1>
<form action="${pageContext.request.contextPath}/area-restrita/update-superadm" method="post">
  <input type="hidden" name="id" value="<%= adm.getId() %>">
  <input type="text" name="nome" value="<%= adm.getNome() %>" placeholder="Novo nome">
  <input type="text" name="cargo" value="<%= adm.getCargo() %>" placeholder="Novo cargo">
  <input type="email" name="email" value="<%= adm.getEmail() %>" placeholder="Novo email">
  <input type="text" name="senha_atual" placeholder="Insira sua senha atual">
  <input type="text" name="nova_senha" pattern=".{8,}" title="A senha deve ter 8 caractéres ou mais"
         placeholder="Insira sua nova senha">
  <button type="submit">Salvar</button>
</form>
</body>
</html>
