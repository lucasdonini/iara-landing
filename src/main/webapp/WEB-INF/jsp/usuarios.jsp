<%@ page import="com.dto.UsuarioDTO" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  List<UsuarioDTO> usuarios = (List<UsuarioDTO>) request.getAttribute("usuarios");
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricasPorFk");
  DateTimeFormatter isoDmy = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  String erro = (String) request.getAttribute("erro");
%>
<html lang="pt-BR">
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Usuários</h1>
<a href="${pageContext.request.contextPath}/area-restrita/index"> Voltar à área restrita</a>
<table border="1">
  <tr>
    <th>ID</th>
    <th>Nome</th>
    <th>Email</th>
    <th>Tipo de Acesso</th>
    <th>Data de Criação</th>
    <th>Status</th>
    <th>Fábrica</th>
  </tr>
  
  <% for (UsuarioDTO u : usuarios) { %>
  <tr>
    <td>
      <%= u.getId() %>
    </td>
    <td>
      <%= u.getNome() %>
    </td>
    <td>
      <%= u.getEmail() %>
    </td>
    <td>
      <%= u.getNivelAcesso().toString() %>
    </td>
    <td>
      <%= u.getDtCriacao().format(isoDmy) %>
    </td>
    <td>
      <%= u.getStatus() ? "Ativo" : "Inativo" %>
    </td>
    <td>
      <%= fabricas.get(u.getFkFabrica()) %>
    </td>
    <td>
      <form action="${pageContext.request.contextPath}/area-restrita/usuarios" method="get">
        <input type="hidden" name="id" value="<%= u.getId() %>">
        <input type="hidden" name="action" value="update">
        <button type="submit">Editar</button>
      </form>
      
      <form action="${pageContext.request.contextPath}/area-restrita/usuarios" method="post">
        <input type="hidden" name="id" value="<%= u.getId() %>">
        <input type="hidden" name="action" value="delete">
        <button type="submit">Deletar</button>
      </form>
    </td>
  </tr>
  <% } %>
</table>
<a href="${pageContext.request.contextPath}/area-restrita/usuarios?action=create">Cadastrar novo Administrador</a>
<% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>
