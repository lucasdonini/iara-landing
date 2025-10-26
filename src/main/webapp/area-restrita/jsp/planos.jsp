<%@ page import="java.util.List" %>
<%@ page import="com.model.Plano" %>
<%@ page import="static com.dao.PlanoDAO.camposFiltraveis" %>
<%@ page import="com.model.DirecaoOrdenacao" %>
<%@ page import="com.utils.NumerosUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  List<Plano> planos = (List<Plano>) request.getAttribute("planos");
%>
<html lang="pt-BR">
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Planos</h1>
<a href="${pageContext.request.contextPath}/area-restrita">Voltar à área restrita</a>
<br>
<form action="${pageContext.request.contextPath}/area-restrita/planos" method="get">
  <input type="hidden" name="action" value="read">

  <label for="campoFiltro" >Campo de Filtragem:</label>
  <select id="campoFiltro" name="campo_filtro">
    <option value="" selected>Nenhum selecionado</option>
    <option value="id" data-type="number">ID</option>
    <option value="nome" data-type="text">Nome</option>
    <option value="valor" data-type="decimal" placeholder="R$">Valor</option>
    <option value="descricao" data-type="text-area">Descrição</option>
    <option value="duracao" data-type="duracao">Duração</option>
  </select>

  <div id="containerValorFiltro">
      <label for="valorFiltro" >Valor Filtrado:</label>
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
<form action="${pageContext.request.contextPath}/area-restrita/planos" method="get">
  <input type="hidden" name="action" value="read">
  <button type="submit">Limpar Filtros</button>
</form>
<table border="1">
  <tr>
    <th>Id</th>
    <th>Nome</th>
    <th>Valor</th>
    <th>Descrição</th>
    <th>Duração</th>
  </tr>
  <% for (Plano plano : planos) { %>
  <tr>
    <td><%= plano.getId() %>
    </td>
    <td><%= plano.getNome() %>
    </td>
    <td><%= NumerosUtils.reais.format(plano.getValor()) %>
    </td>
    <td><%= plano.getDescricao() %>
    </td>
    <td>
       <% if (plano.getDuracao().getYears() != 0) {%>
            <p><%= plano.getDuracao().getYears() %> Anos</p>
        <%} if (plano.getDuracao().getMonths() != 0) {%>
            <p><%= plano.getDuracao().getMonths() %> Meses</p>
        <%} if (plano.getDuracao().getDays() != 0) {%>
            <p><%= plano.getDuracao().getDays() %> Dias</p>
        <% } %>
    </td>
    <td>
      <form action="${pageContext.request.contextPath}/area-restrita/planos" method="get">
        <input type="hidden" name="id" value="<%= plano.getId() %>">
        <input type="hidden" name="action" value="update">
        <button type="submit">Editar</button>
      </form>
      <form action="${pageContext.request.contextPath}/area-restrita/planos" method="post">
        <input type="hidden" name="id" value="<%= plano.getId() %>">
        <input type="hidden" name="action" value="delete">
        <button type="submit">Deletar</button>
      </form>
    </td>
  </tr>
  <% } %>
</table>
<a href="${pageContext.request.contextPath}/area-restrita/planos?action=create">Cadastrar novo Plano</a>
<script src="${pageContext.request.contextPath}/javascript/infoTrader.js"></script>
</body>
</html>
