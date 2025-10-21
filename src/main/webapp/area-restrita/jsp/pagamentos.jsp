<%@ page import="com.model.Pagamento" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.dao.PagamentoDAO.camposFiltraveis" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.model.DirecaoOrdenacao" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="com.utils.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  List<Pagamento> pagamentos = (List<Pagamento>) request.getAttribute("pagamentos");
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
%>

<html>
<head>
  <title>Landing Teste</title>
</head>

<body>
<h1>Pagamentos</h1>
<a href="${pageContext.request.contextPath}/area-restrita">Voltar à área restrita</a>
<br>

<form action="${pageContext.request.contextPath}/area-restrita/pagamentos" method="get">
  <input type="hidden" name="action" value="read">

  <label for="campoFiltro" >Campo de Filtragem:</label>
  <select id="campoFiltro" name="campo_filtro">
      <option value="" selected>Nenhum selecionado</option>
      <option value="id" data-type="number">ID</option>
      <option value="valor" data-type="decimal">Valor Pago</option>
      <option value="statusP" data-type="select">Status</option>
      <option value="data_vencimento" data-type="date">Data Vencimento</option>
      <option value="data_pagamento" data-type="datetime-local">Data Pagamento</option>
      <option value="data_inicio" data-type="datetime-local">Data de Início</option>
      <option value="fk_metodopag" data-type="select">Metodo de Pagamento</option>
      <!-- POR O FILTRO POR FÁBRICA -->
  </select>

  <div id="containerValorFiltro">
      <label for="valorFiltro" >Valor Filtrado:</label>
      <input id="valorFiltro" type="text" name="valor_filtro">
  </div>
  
  <label>Ordenar por:</label>
  <select name="campo_sequencia">
    <option value="" selected>Nenhum selecionado</option>
    <% for (String chave : camposFiltraveis.keySet()) { %>
    <option value="<%= chave %>">
      <%= camposFiltraveis.get(chave) %>
    </option>
    <% } %>
  </select>
  
  <label>
    Direção de ordenação
    <input type="radio" name="direcao_sequencia" value="<%= DirecaoOrdenacao.CRESCENTE.getSql() %>" checked> Crescente
    <input type="radio" name="direcao_sequencia" value="<%= DirecaoOrdenacao.DECRESCENTE.getSql() %>"> Decrescente
  </label>
  
  <input type="submit" value="Filtrar">
</form>

<form action="${pageContext.request.contextPath}/area-restrita/pagamentos" method="get">
  <input type="hidden" name="action" value="read">
  <button type="submit">Limpar Filtros</button>
</form>

<table border="1">
  <tr>
    <th>Id</th>
    <th>Valor Pago</th>
    <th>Status</th>
    <th>Data de Vencimento</th>
    <th>Data de Pagamento</th>
    <th>Data de Início</th>
    <th>Método de Pagamento</th>
    <th>Fábrica</th>
    <th>Plano</th>
  </tr>
  <%
    for (Pagamento pagamento : pagamentos) {
      LocalDateTime dtPagto = pagamento.getDataPagamento();
      LocalDateTime dtInicio = pagamento.getDataInicio();
  %>
  <tr>
    <td>
      <%= pagamento.getId() %>
    </td>
    <td>
      <%= pagamento.getValor() %>
    </td>
    <td>
      <%= pagamento.getStatus() ? "Pago" : "Pendente" %>
    </td>
    <td>
      <%= pagamento.getDataVencimento() %>
    </td>
    <td>
      <%= dtPagto == null ? "Pagamento Pendente" : dtPagto %>
    </td>
    <td>
      <%= StringUtils.capitalize(pagamento.getTipoPagamento()) %>
    </td>
    <td>
      <%= pagamento.getMetodoPagamento().toString() %>
    </td>
    <td>
      <%= fabricas.get(pagamento.getFkFabrica()) %>
    </td>
    <td>
        <%= planos.get(pagamento.getFkPlano())%>
    </td>
    <td>
      <form action="${pageContext.request.contextPath}/area-restrita/pagamentos" method="get">
        <input type="hidden" name="id" value="<%= pagamento.getId() %>">
        <input type="hidden" name="action" value="update">
        <button type="submit">Editar</button>
      </form>
      <form action="${pageContext.request.contextPath}/area-restrita/pagamentos" method="post">
        <input type="hidden" name="id" value="<%= pagamento.getId() %>">
        <input type="hidden" name="action" value="delete">
        <button type="submit">Deletar</button>
      </form>
    </td>
  </tr>
  <% } %>
</table>
<a href="${pageContext.request.contextPath}/area-restrita/pagamentos?action=create">Cadastrar novo Pagamento</a>
<script src="${pageContext.request.contextPath}/javascript/infoTrader.js"></script>
</body>
</html>
