<%@ page import="com.model.Plano" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="pt-BR">
<%
  Plano plano = (Plano) request.getAttribute("plano");
  String erro = (String) request.getAttribute("erro");
%>

<head>
  <title>Landing Teste</title>
  <link rel="stylesheet" href="/styles/editar-plano.css">
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
        <h2>Editar Plano - ID: <%= plano.getId() %>
        </h2>

        <form action="${pageContext.request.contextPath}/planos?action=update" method="post">
          <input type="hidden" name="action" value="update">
          <input type="hidden" name="id" value="<%= plano.getId() %>">
          <input type="text" name="nome" value="<%= plano.getNome() %>" placeholder="Novo nome">
          <input type="number" name="valor" step="any" value="<%= plano.getValor() %>" placeholder="Novo valor">
          <input type="text" name="descricao" value="<%= plano.getDescricao() %>" placeholder="Nova descricao">
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