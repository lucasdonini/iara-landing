<%@ page import="com.dto.UsuarioDTO" %>
<%@ page import="com.utils.DataUtils" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.model.DirecaoOrdenacao" %>
<%@ page import="static com.dao.UsuarioDAO.camposFiltraveis" %>
<%@ page import="com.model.Usuario" %>
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
  
    <label for="campoFiltro">Campo de Filtragem:</label>
    <select id="campoFiltro" name="campo_filtro">
      <option value="" selected>Nenhum selecionado</option>
      <option value="nome" data-type="text">Nome</option>
      <option value="genero" data-type="select">Gênero</option>
      <option value="data_nascimento" data-type="date">Data de Nascimento</option>
      <option value="cargo" data-type="text">Cargo</option>
      <option value="email" data-type="email">Email</option>
      <option value="tipo_acesso" data-type="select">Tipo de Acesso</option>
      <option value="desc_tipoacesso" data-type="text">Descrição do Tipo de Acesso</option>
      <option value="statusU" data-type="select">Status</option>
      <option value="data_criacao" data-type="datetime-local">Data de Criação</option>
    </select>

  <div id="containerValorFiltro">
      <label for="valorFiltro">Valor Filtrado:</label>
      <input id="valorFiltro" type="text" name="valor_filtro">
  </div>
  
  <label>
    Ordenar por:
    <select name="campo_sequencia">
      <% for(String chave:camposFiltraveis.keySet()){%>
      <option value="<%=chave%>">
          <%=camposFiltraveis.get(chave)%>
      </option>
      <%}%>
    </select>
  </label>
  
  <label>
    Direção de ordenação
    <input type="radio" name="direcao_sequencia" value="<%= DirecaoOrdenacao.CRESCENTE.getSql() %>" checked> Crescente
    <input type="radio" name="direcao_sequencia" value="<%= DirecaoOrdenacao.DECRESCENTE.getSql() %>"> Decrescente
  </label>
  
  <input type="submit" value="Filtrar">
</form>
<form action="${pageContext.request.contextPath}/area-restrita/usuarios" method="get">
  <input type="hidden" name="action" value="read">
  <button type="submit">Limpar Filtros</button>
</form>

<table border="1">
  <tr>
    <th>Nome</th>
    <th>Email do Gerente</th>
    <th>Gênero</th>
    <th>Data de Nascimento</th>
    <th>Cargo</th>
    <th>Email</th>
    <th>Tipo de Acesso</th>
    <th>Descrição do Tipo de Acesso</th>
    <th>Data de Criação</th>
    <th>Status</th>
    <th>Fábrica</th>
  </tr>
  
  <% for (UsuarioDTO u : usuarios) { %>
  <tr>
    <td>
      <%= u.getNome() %>
    </td>
    <td>
      <%= u.getEmailGerente() %>
    </td>
    <td>
      <%= u.getGenero() %>
    </td>
    <td>
      <%= u.getDataNascimento() %>
    </td>
    <td>
      <%= u.getCargo() %>
    </td>
    <td>
      <%= u.getEmail() %>
    </td>
    <td>
      <%= u.getTipoAcesso().descricao() %>
    </td>
    <td>
      <%= u.getDescTipoAcesso() %>
    </td>
    <td>
      <%= u.getDataCriacao().format(DataUtils.DMY) %>
    </td>
    <td>
      <%= u.getStatus() ? "Ativo" : "Inativo" %>
    </td>
    <td>
      <%= u.getNomeFabrica() %>
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
<script src="${pageContext.request.contextPath}/javascript/infoTrader.js"></script>
</body>
</html>
