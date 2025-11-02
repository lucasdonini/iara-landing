<%@ page import="com.dto.SuperAdmDTO" %>
<%@ page import="com.utils.RegexUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    SuperAdmDTO adm = (SuperAdmDTO) request.getAttribute("superAdm");
    String erro = (String) request.getAttribute("erro");
%>
<html>

<head>
    <title>Editar Super ADM | Área Restrita</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/editar-superadm.css">
    <link rel="icon"
          href="${pageContext.request.contextPath}/assets/IARA%20-%20Imagens%20Landing/Geral/Mascote%20IARA.png">
</head>

<body>
<a href="${pageContext.request.contextPath}/area-restrita/superadms" class="btn-sair">Cancelar</a>

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
            <h2>Editar Super Adm</h2>

            <form action="${pageContext.request.contextPath}/area-restrita/superadms" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="<%= adm.getId() %>">

                <label for="nome">Nome:</label>
                <input name="nome" id="nome" value="<%= adm.getNome() %>"
                       type="text"
                       placeholder="Novo nome"
                       maxlength="100"
                       required
                >

                <label for="cargo">Cargo:</label>
                <input name="cargo" id="cargo" value="<%= adm.getCargo() %>"
                       type="text"
                       placeholder="Novo cargo"
                       maxlength="50"
                       required
                >

                <label for="email">Email:</label>
                <input name="email" id="email" value="<%= adm.getEmail() %>"
                       type="email"
                       placeholder="Novo email"
                       pattern="<%= RegexUtils.REGEX_EMAIL %>"
                       title="Email inválido"
                       maxlength="100"
                       required
                >

                <label for="senha_atual">Senha Atual:</label>
                <input name="senha_atual" id="senha_atual"
                       type="text"
                       placeholder="Insira sua senha atual"
                >

                <label for="nova_senha">Nova Senha:</label>
                <input name="nova_senha" id="nova_senha"
                       type="text"
                       placeholder="Insira sua nova senha"
                       pattern=".{8,}"
                       title="A senha deve ter pelo menos 8 dígitos"
                >

                <button type="submit">Salvar</button>
            </form>

            <% if (erro != null && !erro.isBlank()) { %>
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