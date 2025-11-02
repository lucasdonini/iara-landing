<%@ page import="com.model.Plano" %>
<%@ page import="org.postgresql.util.PGInterval" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="pt-BR">
<%
    Plano plano = (Plano) request.getAttribute("plano");
    String erro = (String) request.getAttribute("erro");
    PGInterval duracao = plano.getDuracao();
%>

<head>
    <title>Editar Plano | Área Restrita</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/editar-plano.css">
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
            <h2>Editar Plano</h2>

            <form action="${pageContext.request.contextPath}/area-restrita/planos" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="<%= plano.getId() %>">

                <label for="nome">Nome:</label>
                <input name="nome" id="nome" value="<%= plano.getNome() %>"
                       type="text"
                       placeholder="Novo nome"
                       maxlength="100"
                       required
                >

                <label for="valor">Valor:</label>
                <input name="valor" id="valor" value="<%= plano.getValor() %>"
                       type="number"
                       placeholder="Novo valor"
                       step="0.01"
                       min="0"
                >

                <label for="descricao">Descrição:</label>
                <input name="descricao" id="descricao" value="<%= plano.getDescricao() %>"
                       type="text"
                       placeholder="Nova descricao"
                >

                <label for="anos">Anos:</label>
                <input name="anos_duracao" id="anos" value="<%= duracao.getYears() %>"
                       type="number"
                       placeholder="Anos"
                       min="0"
                       required
                >

                <label for="meses">Meses:</label>
                <input name="meses_duracao" id="meses" value="<%= duracao.getMonths() %>"
                       type="number"
                       placeholder="Meses"
                       min="0"
                       required
                >

                <label for="dias">Dias:</label>
                <input name="dias_duracao" id="dias" value="<%= duracao.getDays() %>"
                       type="number"
                       placeholder="Dias"
                       min="0"
                       required
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