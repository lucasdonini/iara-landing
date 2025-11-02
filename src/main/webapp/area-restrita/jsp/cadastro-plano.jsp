<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String erro = (String) request.getAttribute("erro");
%>
<html lang="pt-BR">

<head>
    <title>Cadastrar Plano | Área Restrita</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cadastro-plano.css">
    <link rel="icon"
          href="${pageContext.request.contextPath}/assets/IARA%20-%20Imagens%20Landing/Geral/Mascote%20IARA.png">
</head>

<body>
<a href="${pageContext.request.contextPath}/area-restrita/planos" class="btn-sair">Cancelar</a>

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
            <h2>Cadastre um Plano!</h2>
            <form action="${pageContext.request.contextPath}/area-restrita/planos" method="post" class="LoginForm">
                <input type="hidden" name="action" value="create">

                <input name="nome"
                       type="text"
                       placeholder="Nome"
                       maxlength="100"
                       required
                >

                <input name="valor"
                       type="number"
                       step="0.01"
                       placeholder="Valor (R$)"
                >

                <input name="descricao"
                       type="text"
                       placeholder="Descrição"
                >

                <input name="anos_duracao"
                       type="number"
                       placeholder="Anos"
                       min="0"
                       required
                >

                <input name="meses_duracao"
                       type="number"
                       placeholder="Meses"
                       min="0"
                       required
                >

                <input name="dias_duracao"
                       type="number"
                       placeholder="Dias"
                       min="0"
                       required
                >

                <button type="submit">Cadastrar</button>
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