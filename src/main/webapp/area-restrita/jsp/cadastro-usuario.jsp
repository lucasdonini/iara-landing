<%@ page import="java.util.Map" %>
<%@ page import="com.model.Genero" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
    String erro = (String) request.getAttribute("erro");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <title>Landing Teste</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cadastro-usuario.css">
</head>
<body>
<a href="${pageContext.request.contextPath}/area-restrita/usuarios" class="btn-sair">Cancelar</a>

<main class="login-container">
    <img src="${pageContext.request.contextPath}/assets/Cadastro/fundo-cadastro.png" alt="Fundo decorativo" class="bg-particles">

    <div class="left-side">
        <img src="${pageContext.request.contextPath}/assets/Cadastro/iara-direita_1-removebg-preview%201.png" alt="Mascote IARA" class="mascote">
    </div>

    <div class="right-side">
        <div class="login-box">
            <img src="${pageContext.request.contextPath}/assets/IARA%20-%20Imagens%20Landing/Logo/logo-iara.png" alt="Logo IARA" class="logo">
            <h2>Cadastre um Usuário!</h2>
            <form action="${pageContext.request.contextPath}/area-restrita/usuarios" method="post" class="LoginForm">
                <input type="hidden" name="action" value="create">
                <input type="text" name="nome" placeholder="Nome">
                <input type="email" name="email" placeholder="Email">
                <input type="text" pattern=".{8,}" title="A senha deve ter 8 ou mais caractéres" name="senha"
                       placeholder="Senha">
                <input type="email" name="email_gerente" placeholder="Email do Gerente">
                <input type="text" name="cargo" placeholder="Cargo">

                <select name="fk_fabrica" required>
                    <option value="" selected>Selecione</option>
                    <% for (int id : fabricas.keySet()) { %>
                    <option value="<%= id %>">
                        <%= fabricas.get(id) %>
                    </option>
                    <% } %>
                </select>

                <select name="genero">
                    <option value="" selected>-- Selecione --</option>

                    <% for (Genero g : Genero.values()) { %>
                    <option value="<%= g.name().toLowerCase() %>">
                        <%= g.toString() %>
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