<%@ page import="com.dto.SuperAdmDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    SuperAdmDTO adm = (SuperAdmDTO) request.getAttribute("superAdm");
    String erro = (String) request.getAttribute("erro");
%>
<html>

<head>
    <title>Editar Super ADM | Área Restrita</title>
    <link rel="stylesheet" href="/styles/editar-superadm.css">
    <link rel="icon" href="../assets/IARA%20-%20Imagens%20Landing/Geral/Mascote%20IARA.png">
</head>

<body>
<a href="${pageContext.request.contextPath}/area-restrita/superadms" class="btn-sair">Cancelar</a>

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

            <form action="${pageContext.request.contextPath}/area-restrita/superadms" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="<%= adm.getId() %>">
                <label for="nome">Nome:</label>
                <input type="text" name="nome" id="nome" value="<%= adm.getNome() %>" placeholder="Novo nome">
                <label for="cargo">Cargo:</label>
                <input type="text" id="cargo" name="cargo" value="<%= adm.getCargo() %>" placeholder="Novo cargo">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="<%= adm.getEmail() %>" placeholder="Novo email">
                <label for="senha_atual">Senha Atual:</label>
                <input type="text" id="senha_atual" name="senha_atual" placeholder="Insira sua senha atual">
                <label for="nova_senha">Nova Senha:</label>
                <input type="text" id="nova_senha" name="nova_senha" pattern=".{8,}" title="A senha deve ter pelo menos 8 dígitos"
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