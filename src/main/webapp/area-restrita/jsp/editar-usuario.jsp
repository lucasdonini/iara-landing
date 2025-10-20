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

  emailGerentes.add("");
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
  
  <select name="email_gerente">
    <% if (usuario.getEmailGerente() == null) {%>
    <option value="" selected>-- Selecione --</option>
    <% } %>
    <% for (String email : emailGerentes) { %>
    <option value="<%= email %>" <%= email.equals(usuario.getEmailGerente()) ? "selected" : "" %>>
      <%= email %>
    </option>
    <% } %>
  </select>
  
  <select name="genero">
    <% for (String genero : List.of("masculino", "feminino", "outros")) {%>
    <option value=" <%= genero %>" <%= genero.equals(usuario.getGenero()) ? "selected" : ""%>>
      <%= genero %>
    </option>
    <%}%>
  </select>
  
  <input type="text" name="cargo" value="<%= usuario.getCargo() %>" placeholder="Novo cargo">
  <input type="text" name="email" value="<%= usuario.getEmail() %>" placeholder="Novo email">
  
  <select name="nivel_acesso">
    <% for (TipoAcesso t : TipoAcesso.values()) { %>
    <option value="<%= t.nivel() %>" <%= usuario.getTipoAcesso().equals(t) ? "selected" : "" %>>
      <%= t.toString() %>
    </option>
    <% } %>
  </select>
  
  <select name="status">
    <option value="true" <%= usuario.getStatus() ? "selected" : "" %>>Ativo</option>
    <option value="false" <%= !usuario.getStatus() ? "selected" : "" %>>Inativo</option>
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
