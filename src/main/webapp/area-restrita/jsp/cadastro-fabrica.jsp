<%@ page import="java.util.Map" %>
<%@ page import="com.utils.RegexUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String erro = (String) request.getAttribute("erro");
    Map<Integer, String> planos = (Map<Integer, String>) request.getAttribute("planos");
%>
<html lang="pt-BR">

<head>
    <title>Cadastrar Fábrica | Área Restrita</title>
    <link rel="stylesheet" href="/styles/cadastro-fabrica.css">
    <link rel="icon" href="../assets/IARA%20-%20Imagens%20Landing/Geral/Mascote%20IARA.png">
</head>

<body>
<a href="${pageContext.request.contextPath}/area-restrita/fabricas" class="btn-sair">Cancelar</a>

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
            <h2>Cadastre uma Fábrica!</h2>

            <form action="${pageContext.request.contextPath}/area-restrita/fabricas?action=create" method="post">
                <input type="hidden" name="action" value="create">

                <div class="form-container">
                    <div class="form-section">
                        <h2>Dados</h2>
                        <input name="nome"
                               type="text"
                               placeholder="Nome"
                               maxlength="100"
                               required
                        >

                        <input name="cnpj"
                               type="text"
                               placeholder="CNPJ"
                               pattern="<%= RegexUtils.REGEX_CNPJ %>"
                               title="CNPJ inválido"
                               maxlength="14"
                               required
                        >

                        <input name="email"
                               type="email"
                               placeholder="Email para contato"
                               pattern="<%= RegexUtils.REGEX_EMAIL %>"
                               title="Email inválido"
                               maxlength="100"
                               required
                        >

                        <input name="ramo"
                               type="text"
                               placeholder="Ramo"
                               maxlength="100"
                               required
                        >

                        <input name="empresa"
                               type="text"
                               placeholder="Nome da empresa"
                               maxlength="150"
                        >

                        <select name="id_plano" required>
                            <option value="">Selecione o Plano</option>
                            <% for (int id : planos.keySet()) { %>
                            <option value="<%= id %>">
                                <%= planos.get(id) %>
                            </option>
                            <% } %>
                        </select>
                    </div>


                    <div class="form-section">
                        <h2>Endereço</h2>
                        <input name="cep"
                               type="text"
                               placeholder="CEP"
                               pattern="<%= RegexUtils.REGEX_CEP %>"
                               title="Insira um CEP válido"
                               maxlength="8"
                               required
                        >

                        <input name="logradouro"
                               type="text"
                               placeholder="Logradouro"
                               maxlength="100"
                               required
                        >

                        <input name="numero"
                               type="number"
                               placeholder="n°"
                               required
                        >

                        <input name="complemento"
                               type="text"
                               placeholder="Complemento"
                        >

                        <input name="estado"
                               type="text"
                               placeholder="Estado"
                               required
                        >

                        <input name="bairro"
                               type="text"
                               placeholder="Bairro"
                               required
                        >

                        <input name="cidade"
                               type="text"
                               placeholder="Cidade"
                               required
                        >
                    </div>
                </div>

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