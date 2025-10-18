<%@ page import="com.dto.AtualizacaoUsuarioDTO" %>
<%@ page import="com.model.TipoAcesso" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  AtualizacaoUsuarioDTO usuario = (AtualizacaoUsuarioDTO) request.getAttribute("usuario");
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
  List<String> emailGerentes = (List<String>) request.getAttribute("emailGerentes");
  String erro = (String) request.getAttribute("erro");
%>
<html>
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>
  Editar usuário
</h1>
<form action="${pageContext.request.contextPath}/area-restrita/usuarios" method="post">
  <input type="hidden" name="action" value="update">
  <input type="hidden" name="id" value="<%= usuario.getId() %>">
  <input type="text" name="nome" value="<%= usuario.getNome() %>" placeholder="Novo nome">

  <select name="email_gerente" >
      <% if (emailGerentes != null) {%>
      <% for (String email:emailGerentes) { %>
      <option value="<%= email %>" <%= email.equals(usuario.getEmailGerente()) ? "selected" : "" %>>
          <%= email %>
      </option>
      <% } %>
      <% } %>
  </select>

  <select name="genero">
      <% for (String genero: List.of("masculino", "feminino", "outros")) {%>
      <option value=" <%= genero %>" <%= genero.equals(usuario.getGenero()) ? "selected" : ""%>>
          <%= genero %>
      </option>
      <%}%>
  </select>

  <input type="text" name="cargo" value="<%= usuario.getCargo() %>" placeholder="Novo cargo">
  <input type="text" name="email" value="<%= usuario.getEmail() %>" placeholder="Novo email">
  
  <select name="nivel_acesso">
    <% for (TipoAcesso t : TipoAcesso.values()) { %>
    <option value="<%= t.nivel() %>" <%= t == usuario.getTipoAcesso() ? "selected" : "" %>>
      <%= t.descricao() %>
    </option>
    <% } %>
  </select>

  <input type="text" name="desc_tipoacesso" value="<%= usuario.getDescTipoAcesso() %>" placeholder="Nova descrição do tipo de acesso">
  
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
<a href="${pageContext.request.contextPath}/area-restrita/usuarios">Cancelar</a>
<% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>
