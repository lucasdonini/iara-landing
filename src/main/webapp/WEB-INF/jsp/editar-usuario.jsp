<%@ page import="com.dto.AtualizacaoUsuarioDTO" %>
<%@ page import="com.model.NivelAcesso" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  AtualizacaoUsuarioDTO usuario = (AtualizacaoUsuarioDTO) request.getAttribute("infosUsuario");
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricasPorFk");
  String erro = (String) request.getAttribute("erro");
%>
<html>
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>
  Editar usuário - ID: <%= usuario.getId() %>
</h1>
<form action="${pageContext.request.contextPath}/area-restrita/usuarios" method="post">
  <input type="hidden" name="action" value="update">
  <input type="hidden" name="id" value="<%= usuario.getId() %>">
  <input type="text" name="nome" value="<%= usuario.getNome() %>" placeholder="Novo nome">
  <input type="text" name="email" value="<%= usuario.getEmail() %>" placeholder="Novo email">
  
  <select name="nivel_acesso">
    <% for (NivelAcesso n : NivelAcesso.values()) { %>
    <option value="<%= n.nivel() %>" <%= n == usuario.getNivelAcesso() ? "selected" : "" %>>
      <%= n.toString() %>
    </option>
    <% } %>
  </select>
  
  <select name="status">
    <% for (boolean b : List.of(true, false)) { %>
    <option value="<%= b %>" <%= b == usuario.getStatus() ? "selected" : "" %>>
      <%= b ? "Ativo" : "Inativo" %>
    </option>
    <% } %>
  </select>
  
  <select name="fk_fabrica">
    <% for (int id : fabricas.keySet()) { %>
    <option value="<%= id %>" <%= id == usuario.getFkFabrica() ? "selected" : "" %>>
      <%= fabricas.get(id) %>
    </option>
    <% } %>
  </select>
  
  <button type="submit">Salvar</button>
</form>
<% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>
