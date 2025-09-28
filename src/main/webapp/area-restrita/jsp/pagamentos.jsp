<%@ page import="com.dto.PagamentoDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dao.PagamentoDAO" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: ryanmoraes-ieg
  Date: 24/09/2025
  Time: 14:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    List<PagamentoDTO> pagamentos = (List<PagamentoDTO>) request.getAttribute("pagamentos");
%>
<h1>Pagamentos</h1>
<a href="${pageContext.request.contextPath}/area-restrita/index">Voltar à área restrita</a>
<br>
<form action="${pageContext.request.contextPath}/area-restrita/pagamentos?action=read" method="post">
    <label>Campo de Filtragem:</label>
    <select name="campoFiltro">
        <option value="null" selected>Nenhum selecionado</option>
    <% for (String chave:PagamentoDAO.camposFiltraveis.keySet()){ %>
        <option value="<%=PagamentoDAO.camposFiltraveis.get(chave)%>"><%=chave%></option>
    <%}%>
    </select>
    <label>Valor Filtrado:</label>
    <input type="text" name="valorFiltro">
    <label>Ordenar por:</label>
    <select name="campoSequencia">
        <option value="null" selected>Nenhum selecionado</option>
        <% for (String chave:PagamentoDAO.camposFiltraveis.keySet()){ %>
        <option value="<%=PagamentoDAO.camposFiltraveis.get(chave)%>"><%=chave%></option>
        <%}%>
    </select>
    <select name="direcaoSequencia">
        <option value="crescente" selected>Crescente</option>
        <option value="decrescente">Decrescente</option>
    </select>
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
    <% for (PagamentoDTO pagamento : pagamentos) { %>
    <tr>
        <td><%= pagamento.getId() %>
        </td>
        <td><%= pagamento.getValorPago() %>
        </td>
        <td><%= pagamento.getStatus() %>
        </td>
        <td><%= pagamento.getDataVencimento() %>
        </td>
        <td><%= pagamento.getDataPagamento() %>
        </td>
        <td><%= pagamento.getTipoPagamento() %>
        </td>
        <td><%= pagamento.getFkFabrica() %>
        </td>
        <td>
            <form action="${pageContext.request.contextPath}/area-restrita/pagamentos?action=update" method="get">
                <input type="hidden" name="id" value="<%= pagamento.getId() %>">
                <button type="submit">Editar</button>
            </form>
            <form action="${pageContext.request.contextPath}/area-restrita/pagamentos?action=delete" method="get">
                <input type="hidden" name="id" value="<%= pagamento.getId() %>">
                <button type="submit">Deletar</button>
            </form>
        </td>
    </tr>
    <% } %>
</table>
<a href="${pageContext.request.contextPath}/area-restrita/pagamentos?action=create">Cadastrar novo Plano</a>
</body>
</html>
