<%@ page import="com.dto.UsuarioDTO" %>
<%@ page import="com.utils.DataUtils" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.model.DirecaoOrdenacao" %>
<%@ page import="static com.dao.UsuarioDAO.camposFiltraveis" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  List<UsuarioDTO> usuarios = (List<UsuarioDTO>) request.getAttribute("usuarios");
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
  String erro = (String) request.getAttribute("erro");
%>
<html lang="pt-BR">
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Usuários</h1>
<a href="${pageContext.request.contextPath}/area-restrita"> Voltar à área restrita</a>

<form action="${pageContext.request.contextPath}/area-restrita/usuarios" method="get">
  <input type="hidden" name="action" value="read">
  
  <label>
    Campo de Filtragem:
    <select name="campo_filtro">
      <option value="" selected>Nenhum selecionado</option>
      
      <% for (String chave : camposFiltraveis.keySet()) { %>
      <option value="<%= chave %>">
        <%= camposFiltraveis.get(chave) %>
      </option>
      <% } %>
    </select>
  </label>
  
  <label>
    Valor Filtrado:
    <input type="text" name="valor_filtro">
  </label>
  
  <label>
    Ordenar por:
    <select name="campo_sequencia">
      <option value="" selected>Nenhum selecionado</option>
      
      <% for (String chave : camposFiltraveis.keySet()) { %>
      <option value="<%= chave %>">
        <%= camposFiltraveis.get(chave) %>
      </option>
      <% } %>
    </select>
  </label>
  
  <label>
    Direção de ordenação
    <input type="radio" name="direcao_sequencia" value="<%= DirecaoOrdenacao.CRESCENTE.getSql() %>" checked> Crescente
    <input type="radio" name="direcao_sequencia" value="<%= DirecaoOrdenacao.DECRESCENTE.getSql() %>"> Decrescente
  </label>
  
  <input type="submit" value="Filtrar">
</form>

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
      <%= u.getTipoAcesso().descricao() %>
    </td>
    <td>
      <%= u.getDataCriacao().format(DataUtils.DMY) %>
    </td>
    <td>
      <%= u.getStatus() ? "Ativo" : "Inativo" %>
    </td>
    <td>
      <%= fabricas.get(u.getIdFabrica()) %>
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
