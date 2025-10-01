<%@ page import="java.util.List" %>
<%@ page import="com.model.Fabrica" %>
<%@ page import="com.dao.FabricaDAO" %>
<%@ page import="com.dao.EnderecoDAO" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  List<Fabrica> fabricas = (List<Fabrica>) request.getAttribute("fabricas");
  Map<String, String> camposFiltraveisFabrica = FabricaDAO.camposFiltraveis;
  Map<String, String> camposFiltraveisEndereco = EnderecoDAO.camposFiltraveis;
%>
<html>
<head>
  <title>Landing Teste</title>
</head>
<body>
<h1>Fabricas</h1>
<a href="${pageContext.request.contextPath}/area-restrita/index">Voltar à área restrita</a>
<br>
<form action="${pageContext.request.contextPath}/area-restrita/pagamentos?action=read" method="get">
    <label>Campo de Filtragem:</label>
    <select name="campoFiltro">
        <option value="" selected>Nenhum selecionado</option>
        <% for (String chave:camposFiltraveisFabrica.keySet()){ %>
        <option value="<%=camposFiltraveisFabrica.get(chave)%>"><%=chave%></option>
        <%}%>
        <% for (String chave: camposFiltraveisEndereco.keySet()){ %>
        <option value="<%=camposFiltraveisEndereco.get(chave)%>"><%=chave%></option>
        <%}%>
    </select>
    <label>Valor Filtrado:</label>
    <input type="text" name="valorFiltro">
    <label>Ordenar por:</label>
    <select name="campoSequencia">
        <option value="" selected>Nenhum selecionado</option>
        <% for (String chave: camposFiltraveisFabrica.keySet()){ %>
        <option value="<%=camposFiltraveisFabrica.get(chave)%>"><%=chave%></option>
        <%}%>
        <% for (String chave: camposFiltraveisEndereco.keySet()){ %>
        <option value="<%=camposFiltraveisEndereco.get(chave)%>"><%=chave%></option>
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
    <th>CNPJ</th>
    <th>Status</th>
    <th>Email</th>
    <th>Empresa</th>
    <th>Ramo</th>
    <th>Endereço</th>
  </tr>
  <% for (Fabrica f : fabricas) { %>
  <tr>
    <td>
      <%= f.getId() %>
    </td>
    <td>
      <%= f.getNome() %>
    </td>
    <td>
      <%= f.getCnpjFormatado() %>
    </td>
    <td>
      <%= f.getStatus() ? "Ativa" : "Inativa" %>
    </td>
    <td>
      <%= f.getEmail() %>
    </td>
    <td>
      <%= f.getNomeEmpresa() %>
    </td>
    <td>
      <%= f.getRamo() %>
    </td>
    <td>
      <%= f.getEndereco().toString() %>
    </td>
    <td>
      <form action="${pageContext.request.contextPath}/area-restrita/fabricas?action=update" method="get">
        <input type="hidden" name="id" value="<%= f.getId() %>">
        <input type="hidden" name="action" value="update">
        <button type="submit">Editar</button>
      </form>
      <form action="${pageContext.request.contextPath}/area-restrita/fabricas?action=delete" method="post">
        <input type="hidden" name="id_fabrica" value="<%= f.getId() %>">
        <input type="hidden" name="id_endereco" value="<%= f.getEndereco().getId() %>">
        <input type="hidden" name="action" value="delete">
        <button type="submit">Deletar</button>
      </form>
    </td>
  </tr>
  <% } %>
</table>
<a href="${pageContext.request.contextPath}/area-restrita/fabtricas?action=create">Cadastrar nova Fábrica</a>
</body>
</html>
