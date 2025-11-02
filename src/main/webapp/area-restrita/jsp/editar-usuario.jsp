<%@ page import="com.dto.AtualizacaoUsuarioDTO" %>
<%@ page import="com.model.TipoAcesso" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.model.Genero" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  AtualizacaoUsuarioDTO usuario = (AtualizacaoUsuarioDTO) request.getAttribute("usuario");
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
  List<String> emailGerentes = (List<String>) request.getAttribute("emailGerentes");
  String erro = (String) request.getAttribute("erro");
%>
<html>

<head>
  <title>Editar Usuário | Área Restrita</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/editar-usuario.css">
  <link rel="icon"
        href="${pageContext.request.contextPath}/assets/IARA%20-%20Imagens%20Landing/Geral/Mascote%20IARA.png">
</head>

<body>
<a href="${pageContext.request.contextPath}/area-restrita/usuarios" class="btn-sair">Cancelar</a>

<main class="login-container">
  <img src="${pageContext.request.contextPath}/assets/Cadastro/fundo-cadastro.png" alt="Fundo decorativo"
       class="bg-particles">
  
  <div class="left-side">
    <img src="${pageContext.request.contextPath}/assets/Cadastro/iara-direita_1-removebg-preview%201.png"
         alt="Mascote IARA" class="mascote">
  </div>
  
  <div class="right-side">
    <div class="login-box">
      <img src="${pageContext.request.contextPath}/assets/IARA%20-%20Imagens%20Landing/Logo/logo-iara.png"
           alt="Logo IARA" class="logo">
      <h2>Editar Usuário</h2>
      
      <form action="${pageContext.request.contextPath}/area-restrita/usuarios" method="post">
        <div id="divisao">
          <div id="coluna1">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="<%= usuario.getId() %>">
            <label for="nome">Nome:</label>
            <input type="text" id="nome" name="nome" value="<%= usuario.getNome() %>" placeholder="Novo nome">
            <label for="email">Email:</label>
            <input type="text" id="email" name="email" value="<%= usuario.getEmail() %>" placeholder="Novo email">
            <label for="email_gerente">Email do Gerente:</label>
            <input type="email" id="email_gerente" name="email_gerente" value="<%= usuario.getEmailGerente() %>"
                   placeholder="Email do novo gerente">
            <label for="cargo">Cargo:</label>
            <input type="text" id="cargo" name="cargo" value="<%= usuario.getCargo() %>">
          </div>
          <div id="coluna2">
            <label for="nivel_acesso">Nível de Acesso:</label>
            <select name="nivel_acesso" id="nivel_acesso">
              <% for (TipoAcesso t : TipoAcesso.values()) { %>
              <option value="<%= t.nivel() %>" <%=t == usuario.getTipoAcesso() ? "selected" : "" %>>
                <%= t.descricao() %>
              </option>
              <% } %>
            </select>
            
            <label id="status">Status:</label>
            <select name="status" id="status">
              <% for (boolean b : List.of(true, false)) { %>
              <option value="<%= b %>" <%=b == usuario.getStatus() ? "selected" : "" %>>
                <%= b ? "Ativo" : "Inativo" %>
              </option>
              <% } %>
            </select>
            
            <label for="fk_fabrica">Fábrica:</label>
            <select name="fk_fabrica" id="fk_fabrica">
              <% for (int id : fabricas.keySet()) { %>
              <option value="<%= id %>" <%=id == usuario.getFkFabrica() ? "selected" : "" %>>
                <%= fabricas.get(id) %>
              </option>
              <% } %>
            </select>
            
            <label for="genero">Gênero:</label>
            <select name="genero" id="genero">
              <% for (Genero g : Genero.values()) { %>
              <option value="<%= g.name().toLowerCase() %>" <%= g.equals(usuario.getGenero()) ? "selected" : "" %>>
                <%= g.toString() %>
              </option>
              <% } %>
            </select>
          </div>
        </div>
        <button type="submit">Salvar</button>
      </form>
    </div>
  </div>
</main>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/javascript/script.js"></script>

<% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>

</body>

</html>