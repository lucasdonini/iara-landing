<%@ page import="java.util.List" %>
<%@ page import="com.model.DirecaoOrdenacao" %>
<%@ page import="com.dto.FabricaDTO" %>
<%@ page import="static com.dao.FabricaDAO.camposFiltraveis" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  List<FabricaDTO> fabricas = (List<FabricaDTO>) request.getAttribute("fabricas");
%>

<html>
<head>
  <title>Landing Teste</title>
</head>

<body>
<h1>Fabricas</h1>
<a href="${pageContext.request.contextPath}/area-restrita">Voltar à área restrita</a>
<br>

<form action="${pageContext.request.contextPath}/area-restrita/fabricas" method="get">
  <input type="hidden" name="action" value="read">
  
  <label for="campoFiltro">Campo de Filtragem:</label>
  <select id="campoFiltro" name="campo_filtro">
    <option value="" selected>Nenhum selecionado</option>
    <option value="id" data-type="number">ID</option>
    <option value="nome_unidade" data-type="text">Nome</option>
    <option value="cnpj" data-type="number">CNPJ</option>
    <option value="statusF" data-type="select">Status</option>
    <option value="nome_industria" data-type="text">Empresa</option>
    <option value="ramo" data-type="text">Ramo</option>
    <option value="bairro" data-type="text">Bairro</option>
    <option value="cidade" data-type="text">Cidade</option>
    <option value="estado" data-type="text">Estado</option>
  </select>
  
  <div id="containerValorFiltro">
    <label for="valorFiltro">Valor Filtrado:</label>
    <input id="valorFiltro" type="text" name="valor_filtro">
  </div>
  
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

<form action="${pageContext.request.contextPath}/area-restrita/fabricas" method="get">
  <input type="hidden" name="action" value="read">
  <button type="submit">Limpar Filtros</button>
</form>

<table border="1">
  <tr>
    <th>Id</th>
    <th>Nome</th>
    <th>CNPJ</th>
    <th>Status</th>
    <th>Email</th>
    <th>Empresa</th>
    <th>Ramo</th>
    <th>Plano</th>
    <th>Endereço</th>
  </tr>
  <% for (FabricaDTO f : fabricas) { %>
  <tr>
    <td>
      <%= f.getId() %>
    </td>
    <td>
      <%= f.getNomeUnidade() %>
    </td>
    <td>
      <%= f.cnpjFormatado() %>
    </td>
    <td>
      <%= f.getStatus() ? "Ativa" : "Inativa" %>
    </td>
    <td>
      <%= f.getEmailCorporativo() %>
    </td>
    <td>
      <%= f.getNomeIndustria() %>
    </td>
    <td>
      <%= f.getRamo() %>
    </td>
    <td>
      <%= f.getPlano() %>
    </td>
    <td>
      <%= f.getEndereco() %>
    </td>
    <td>
      <form action="${pageContext.request.contextPath}/area-restrita/fabricas" method="get">
        <input type="hidden" name="id" value="<%= f.getId() %>">
        <input type="hidden" name="action" value="update">
        <button type="submit">Editar</button>
      </form>
      <form action="${pageContext.request.contextPath}/area-restrita/fabricas" method="post">
        <input type="hidden" name="id_fabrica" value="<%= f.getId() %>">
        <input type="hidden" name="action" value="delete">
        <button type="submit">Deletar</button>
      </form>
    </td>
  </tr>
  <% } %>
</table>

<a href="${pageContext.request.contextPath}/area-restrita/fabricas?action=create">Cadastrar nova Fábrica</a>
<script src="${pageContext.request.contextPath}/javascript/infoTrader.js"></script>
</body>
</html>
