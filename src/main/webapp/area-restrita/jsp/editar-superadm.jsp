<%@ page import="com.dto.SuperAdmDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  SuperAdmDTO adm = (SuperAdmDTO) request.getAttribute("superAdm");
  String erro = (String) request.getAttribute("erro");
%>
<html>

<head>
  <title>Landing Teste</title>
  <link rel="stylesheet" href="/styles/editar-superadm.css">
</head>

<body>
  <a href="${pageContext.request.contextPath}/superadms?action=read" class="btn-sair">Cancelar</a>

  <main class="login-container">
    <img src="/assets/Cadastro/fundo-cadastro.png" alt="Fundo decorativo" class="bg-particles">

    <div class="left-side">
      <img src="/assets/Cadastro/iara-direita_1-removebg-preview%201.png" alt="Mascote IARA" class="mascote">
    </div>

    <div class="right-side">
      <div class="login-box">
        <img src="/assets/IARA%20-%20Imagens%20Landing/Logo/logo-iara.png" alt="Logo IARA" class="logo">
        <h2>Editar Super Adm - ID: <%= adm.getId() %>
        </h2>

        <form action="${pageContext.request.contextPath}/superadms" method="post">
          <input type="hidden" name="action" value="update">
          <input type="hidden" name="id" value="<%= adm.getId() %>">
          <input type="text" name="nome" value="<%= adm.getNome() %>" placeholder="Novo nome">
          <input type="text" name="cargo" value="<%= adm.getCargo() %>" placeholder="Novo cargo">
          <input type="email" name="email" value="<%= adm.getEmail() %>" placeholder="Novo email">
          <input type="text" name="senha_atual" placeholder="Insira sua senha atual">
          <input type="text" name="nova_senha" pattern=".{8,}" title="A senha deve ter pelo menos 8 dígitos"
            placeholder="Insira sua nova senha">
          <button type="submit">Salvar</button>
        </form>
      </div>
    </div>
  </main>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script src="/javascript/script.js"></script>

  <% if (erro != null && !erro.isBlank()) { %>
<p>
  <%= erro %>
</p>
<% } %>

</body>

</html>