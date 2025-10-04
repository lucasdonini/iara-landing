<%@ page import="com.dto.SuperAdmDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.model.DirecaoOrdenacao" %>
<%@ page import="static com.dao.SuperAdmDAO.camposFiltraveis" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  List<SuperAdmDTO> adms = (List<SuperAdmDTO>) request.getAttribute("superAdms");
%>
<html lang="pt-BR">
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Super Administradores</h1>
<a href="${pageContext.request.contextPath}/area-restrita">Voltar à área restrita</a>

<form action="${pageContext.request.contextPath}/superadms" method="get">
  <input type="hidden" name="action" value="read">
  
  <label>
    Campo de Filtragem:
    <select name="campoFiltro">
      <option value="" selected>Nenhum selecionado</option>
      
      <% for (String chave : camposFiltraveis.keySet()) { %>
      <option value="<%= camposFiltraveis.get(chave) %>">
        <%= chave %>
      </option>
      <% } %>
    </select>
  </label>
  
  <label>
    Valor Filtrado:
    <input type="text" name="valorFiltro">
  </label>
  
  <label>
    Ordenar por:
    <select name="campoSequencia">
      <option value="" selected>Nenhum selecionado</option>
      
      <% for (String chave : camposFiltraveis.keySet()) { %>
      <option value="<%= camposFiltraveis.get(chave) %>">
        <%= chave %>
      </option>
      <% } %>
    </select>
  </label>
  
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
    <th>Nome</th>
    <th>Cargo</th>
    <th>Email</th>
  </tr>
  <% for (SuperAdmDTO adm : adms) { %>
  <tr>
    <td>
      <%= adm.getId() %>
    </td>
    <td>
      <%= adm.getNome() %>
    </td>
    <td>
      <%= adm.getCargo() %>
    </td>
    <td>
      <%= adm.getEmail() %>
    </td>
    <td>
      <form action="${pageContext.request.contextPath}/superadms" method="get">
        <input type="hidden" name="id" value="<%= adm.getId() %>">
        <input type="hidden" name="action" value="update">
        <button type="submit">Editar</button>
      </form>
      <form action="${pageContext.request.contextPath}/superadms" method="post">
        <input type="hidden" name="id" value="<%= adm.getId() %>">
        <input type="hidden" name="action" value="delete">
        <button type="submit">Deletar</button>
      </form>
    </td>
  </tr>
  <% } %>
</table>
<a href="${pageContext.request.contextPath}/superadms?action=create">Cadastrar novo Super Administrador</a>
</body>
</html>
