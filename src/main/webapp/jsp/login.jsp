<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String erro = (String) request.getAttribute("erro");
%>

<!DOCTYPE html>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IARA | Cadastro</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"
          integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw=="
          crossorigin="anonymous" referrerpolicy="no-referrer" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"
            integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>
    <link rel="icon" href="../assets/IARA%20-%20Imagens%20Landing/Geral/Mascote%20IARA.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cadastro1.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
</head>

<body>
<a href="${pageContext.request.contextPath}/index.html" class="btn-sair">
    <i class="fa-solid fa-arrow-left"></i> Voltar
</a>

<main class="login-container">
    <img src="${pageContext.request.contextPath}/assets/Cadastro/fundo-cadastro.png" alt="Fundo decorativo" class="bg-particles">

    <div class="left-side">
        <img src="${pageContext.request.contextPath}/assets/Cadastro/iara-direita_1-removebg-preview%201.png" alt="Mascote IARA" class="mascote">
    </div>

    <div class="right-side">
        <div class="login-box">
            <img src="${pageContext.request.contextPath}/assets/IARA%20-%20Imagens%20Landing/Logo/logo-iara.png" alt="Logo IARA" class="logo">
            <h2>Bem-Vindo de Volta!</h2>
            <form id="loginForm" action="${pageContext.request.contextPath}/login-handler?action=login" method="post">
                <label for="email">E-mail</label>
                <input type="email" id="email" name="email" placeholder="exemplo@email.com" required>

                <label for="senha">Senha</label>
                <input type="password" id="senha" name="senha" placeholder="Senha123" required autocomplete="off">

                <button type="submit">ENTRAR</button>
            </form>
            <% if (erro != null) { %>
            <p>
                <%= erro %>
            </p>
            <% } %>
        </div>
    </div>
</main>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/javascript/script.js"></script>
</body>

</html>