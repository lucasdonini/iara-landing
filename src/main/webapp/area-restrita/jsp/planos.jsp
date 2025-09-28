<%@ page import="java.util.List" %>
<%@ page import="com.model.Plano" %>
<%@ page import="com.dao.PagamentoDAO" %>
<%@ page import="com.dao.PlanoDAO" %>
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
<a href="${pageContext.request.contextPath}/area-restrita/index">Voltar à área restrita</a>
<br>
<form action="${pageContext.request.contextPath}/area-restrita/pagamentos?action=read" method="post">
    <label>Campo de Filtragem:</label>
    <select name="campoFiltro">
        <option value="null" selected>Nenhum selecionado</option>
        <% for (String chave:PlanoDAO.camposFiltraveis.keySet()){ %>
        <option value="<%=PlanoDAO.camposFiltraveis.get(chave)%>"><%=chave%></option>
        <%}%>
    </select>
    <label>Valor Filtrado:</label>
    <input type="text" name="valorFiltro">
    <label>Ordenar por:</label>
    <select name="campoSequencia">
        <option value="null" selected>Nenhum selecionado</option>
        <% for (String chave:PlanoDAO.camposFiltraveis.keySet()){ %>
        <option value="<%=PlanoDAO.camposFiltraveis.get(chave)%>"><%=chave%></option>
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
    <th>Nome</th>
    <th>Valor</th>
    <th>Descrição</th>
  </tr>
  <% for (Plano plano : planos) { %>
  <tr>
    <td><%= plano.getId() %>
    </td>
    <td><%= plano.getNome() %>
    </td>
    <td><%= plano.getValor() %>
    </td>
    <td><%= plano.getDescricao() %>
    </td>
    <td>
      <form action="${pageContext.request.contextPath}/area-restrita/planos?action=update" method="get">
        <input type="hidden" name="id" value="<%= plano.getId() %>">
        <input type="hidden" name="action" value="update">
        <button type="submit">Editar</button>
      </form>
      <form action="${pageContext.request.contextPath}/area-restrita/planos?action=delete" method="post">
        <input type="hidden" name="id" value="<%= plano.getId() %>">
        <input type="hidden" name="action" value="delete">
        <button type="submit">Deletar</button>
      </form>
    </td>
  </tr>
  <% } %>
</table>
<a href="${pageContext.request.contextPath}/area-restrita/planos?action=create">Cadastrar novo Plano</a>
</body>
</html>
