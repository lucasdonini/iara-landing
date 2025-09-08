<%@ page import="com.dto.UsuarioDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.io.IOException" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  List<UsuarioDTO> usuarios = (List<UsuarioDTO>) request.getAttribute("usuarios");
  DateTimeFormatter isoDmy = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>

<%!
  private void editar(HttpServletRequest req, HttpServletResponse resp, int id) throws IOException {
    String destino = req.getContextPath() + "/usuario/editar?id=" + id;
    resp.sendRedirect(destino);
  }
%>
<html>
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Usuários</h1>
<%-- TODO: implementar a visualização de fábrica --%>
<a href="${pageContext.request.contextPath}/area-restrita/index"> Voltar à área restrita</a>
<table border="1">
  <tr>
    <th>ID</th>
    <th>Nome</th>
    <th>Email</th>
    <th>Tipo de Acesso</th>
    <th>Data de Criação</th>
    <th>Status</th>
    <th>Código da Fábrica</th>
  </tr>
  <% for (UsuarioDTO u : usuarios) { %>
  <tr>
    <td><%= u.getId() %>
    </td>
    <td><%= u.getNome() %>
    </td>
    <td><%= u.getEmail() %>
    </td>
    <td><%= u.getNivelAcesso().toString() %>
    </td>
    <td><%= u.getDtCriacao().format(isoDmy) %>
    </td>
    <td><%= u.getStatus() ? "Ativo" : "Inativo" %>
    </td>
    <td><%= u.getFkFabrica() %>
    </td>
    <td>
      <form action="${pageContext.request.contextPath}/area-restrita/update-usuario" method="get">
        <input type="hidden" name="id" value="<%= u.getId() %>">
        <button type="submit">Editar</button>
      </form>
      <form action="${pageContext.request.contextPath}/area-restrita/delete-usuario" method="post">
        <input type="hidden" name="id" value="<%= u.getId() %>">
        <button type="submit">Deletar</button>
      </form>
    </td>
  </tr>
  <% } %>
</table>
<a href="${pageContext.request.contextPath}/area-restrita/usuario/cadastro.html">Cadastrar novo Administrador</a>
</body>
</html>
