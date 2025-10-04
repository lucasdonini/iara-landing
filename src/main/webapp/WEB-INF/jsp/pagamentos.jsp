<%@ page import="com.model.Pagamento" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dao.PagamentoDAO" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.model.DirecaoOrdenacao" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  List<Pagamento> pagamentos = (List<Pagamento>) request.getAttribute("pagamentos");
  Map<String, String> camposFiltraveis = PagamentoDAO.camposFiltraveis;
%>

<html>
<head>
  <title>Landing Teste</title>
</head>

<body>
<h1>Pagamentos</h1>
<a href="${pageContext.request.contextPath}/area-restrita">Voltar à área restrita</a>
<br>

<form action="${pageContext.request.contextPath}/pagamentos" method="get">
  <input type="hidden" name="action" value="read">
  
  <label>Campo de Filtragem:</label>
  <select name="campoFiltro">
    <option value="" selected>Nenhum selecionado</option>
    
    <% for (String chave : camposFiltraveis.keySet()) { %>
    <option value="<%= camposFiltraveis.get(chave) %>">
      <%= chave %>
    </option>
    <% } %>
  </select>
  
  <label>Valor Filtrado:</label>
  <input type="text" name="valorFiltro">
  
  <label>Ordenar por:</label>
  <select name="campoSequencia">
    <option value="" selected>Nenhum selecionado</option>
    <% for (String chave : camposFiltraveis.keySet()) { %>
    <option value="<%= camposFiltraveis.get(chave) %>">
      <%= chave %>
    </option>
    <% } %>
  </select>
  
  <label>
    Direção de ordenação
    <input type="radio" name="direcaoSequencia" value="<%= DirecaoOrdenacao.CRESCENTE.getSql() %>" checked> Crescente
    <input type="radio" name="direcaoSequencia" value="<%= DirecaoOrdenacao.DECRESCENTE.getSql() %>"> Decrescente
  </label>
  
  <input type="submit" value="Filtrar">
</form>

<table border="1">
  <tr>
    <th>Id</th>
    <th>Valor Pago</th>
    <th>Status</th>
    <th>Data de Vencimento</th>
    <th>Data de Pagamento</th>
    <th>Tipo de Pagamento</th>
    <th>ID da Fabrica</th>
  </tr>
  <% for (Pagamento pagamento : pagamentos) { %>
  <tr>
    <td>
      <%= pagamento.getId() %>
    </td>
    <td>
      <%= pagamento.getValorPago() %>
    </td>
    <td>
      <%= pagamento.getStatus() %>
    </td>
    <td>
      <%= pagamento.getDataVencimento() %>
    </td>
    <td>
      <%= pagamento.getDataPagamento() %>
    </td>
    <td>
      <%= pagamento.getTipoPagamento() %>
    </td>
    <td>
      <%= pagamento.getFkFabrica() %>
    </td>
    <td>
      <form action="${pageContext.request.contextPath}/pagamentos" method="get">
        <input type="hidden" name="id" value="<%= pagamento.getId() %>">
        <input type="hidden" name="action" value="update">
        <button type="submit">Editar</button>
      </form>
      <form action="${pageContext.request.contextPath}/pagamentos" method="post">
        <input type="hidden" name="id" value="<%= pagamento.getId() %>">
        <input type="hidden" name="action" value="delete">
        <button type="submit">Deletar</button>
      </form>
    </td>
  </tr>
  <% } %>
</table>
<a href="${pageContext.request.contextPath}/pagamentos?action=create">Cadastrar novo Plano</a>
</body>
</html>
