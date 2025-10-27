<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%
  String erro = (String) request.getAttribute("erro");
%>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
  <title>Landing Teste</title>
  <link rel="stylesheet" href="/styles/cadastro-superadm.css">
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
        <h2>Cadastre um Super ADM!</h2>
        <form action="${pageContext.request.contextPath}/superadms" method="post" class="loginForm">
          <input type="hidden" name="action" value="create">
          <input type="text" name="nome" placeholder="Nome">
          <input type="text" name="cargo" placeholder="Cargo">
          <input type="email" name="email" placeholder="Email">
          <input type="text" pattern=".{8,}" title="A senha deve ter 8 ou mais caracteres" name="senha"
            placeholder="Senha">
          <button type="submit">Cadastrar</button>
        </form>

      </div>
    </div>
  </main>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script src="/javascript/script.js"></script>

  <%
  if (erro != null && !erro.isBlank()) {
%>
<p>
  <%= erro %>
</p>
<% } %>
</body>

</html>