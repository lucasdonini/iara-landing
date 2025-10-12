<%@ page import="com.model.Pagamento" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.dao.PagamentoDAO.camposFiltraveis" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.model.DirecaoOrdenacao" %>
<%@ page import="java.time.LocalDate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  List<Pagamento> pagamentos = (List<Pagamento>) request.getAttribute("pagamentos");
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
  Map<String, String> tiposPagamentos = Pagamento.tiposPagamento;
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
  
  <label>Campo de Filtragem:</label>
  <select name="campo_filtro">
    <option value="" selected>Nenhum selecionado</option>
    
    <% for (String chave : camposFiltraveis.keySet()) { %>
    <option value="<%= chave %>">
      <%= camposFiltraveis.get(chave) %>
    </option>
    <% } %>
  </select>
  
  <label>Valor Filtrado:</label>
  <input type="text" name="valor_filtro">
  
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
    <th>Tipo de Pagamento</th>
    <th>Fábrica</th>
  </tr>
  <%
    for (Pagamento pagamento : pagamentos) {
      LocalDate dtPagto = pagamento.getDataPagamento();
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
      <%= tiposPagamentos.get(pagamento.getTipoPagamento()) %>
    </td>
    <td>
      <%= fabricas.get(pagamento.getIdFabrica()) %>
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
</body>
</html>
