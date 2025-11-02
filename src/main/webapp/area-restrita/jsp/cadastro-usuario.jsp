<%@ page import="java.util.Map" %>
<%@ page import="com.model.Genero" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
  Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
  List<String> emailGerentes = (List<String>) request.getAttribute("emailGerentes");
  String erro = (String) request.getAttribute("erro");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <title>Cadastrar Usuário | Área Restrita</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cadastro-usuario.css">
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
      <h2>Cadastre um Usuário!</h2>
      <form action="${pageContext.request.contextPath}/area-restrita/usuarios" method="post" class="LoginForm">
        <input type="hidden" name="action" value="create">
        <input type="text" name="nome" placeholder="Nome" required>
        <input type="date" name="data_nascimento" max="<%= LocalDate.now().minusYears(16) %>" required>
        <input type="email" name="email" placeholder="Email" required>
        <input type="text" pattern=".{8,}" title="A senha deve ter 8 ou mais caractéres" name="senha"
               placeholder="Senha" required>
        <input type="text" name="cargo" placeholder="Cargo" required>
        
        <select name="email_gerentes">
          <option value="" selected>Sem gerente</option>
          
          <% for (String email : emailGerentes) { %>
          <option value="<%= email %>">
            <%= email %>
          </option>
          <% } %>
        </select>
        
        <select name="genero" required>
          <option value="" selected>-- Selecione --</option>
          
          <% for (String genero : List.of("Masculino", "Feminino", "Outros")) { %>
          <option value="<%= genero.toLowerCase() %>">
            <%= genero %>
          </option>
          <% } %>
        </select>
        
        <select name="fk_fabrica" required>
          <option value="" selected>-- Selecione --</option>
          
          <% for (int id : fabricas.keySet()) { %>
          <option value="<%= id %>">
            <%= fabricas.get(id) %>
          </option>
          <% } %>
        </select>
        
        <button type="submit">Cadastrar</button>
      </form>
    
    </div>
  </div>
</main>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/javascript/script.js"></script>

<%
  if (erro != null && !erro.isBlank()) {
%>
<p>
  <%= erro %>
</p>
<% } %>
</body>
</html>