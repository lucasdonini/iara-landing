<%@ page import="java.util.Map" %>
<%@ page import="com.model.Genero" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="com.utils.RegexUtils" %>
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

                <input name="nome"
                       type="text"
                       placeholder="Nome"
                       maxlength="100"
                       required
                >

                <input name="data_nascimento"
                       type="date"
                       data-placeholder="Data de nascimento"
                       max="<%= LocalDate.now().minusYears(16) %>"
                       required
                >

                <input name="email"
                       type="email"
                       placeholder="Email"
                       pattern="<%= RegexUtils.REGEX_EMAIL %>"
                       title="Email inválido"
                       maxlength="100"
                       required
                >

                <input name="senha"
                       type="text"
                       pattern=".{8,}"
                       title="A senha deve ter 8 ou mais caractéres"
                       placeholder="Senha"
                       required
                >

                <input name="cargo"
                       type="text"
                       placeholder="Cargo"
                       maxlength="50"
                       required
                >

                <select name="email_gerentes">
                    <option value="">Selecione seu gerente</option>
                    <option value="">Não tenho gerente</option>

                    <% for (String email : emailGerentes) { %>
                    <option value="<%= email %>">
                        <%= email %>
                    </option>
                    <% } %>
                </select>

                <select name="genero" required>
                    <option value="" selected>Selecione seu gênero</option>

                    <% for (Genero genero : Genero.values()) { %>
                    <option value="<%= genero.toString().toLowerCase() %>">
                        <%= genero.toString() %>
                    </option>
                    <% } %>
                </select>

                <select name="fk_fabrica" required>
                    <option value="" selected>Selecione a fábrica na qual você trabalha</option>

                    <% for (int id : fabricas.keySet()) { %>
                    <option value="<%= id %>">
                        <%= fabricas.get(id) %>
                    </option>
                    <% } %>
                </select>

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