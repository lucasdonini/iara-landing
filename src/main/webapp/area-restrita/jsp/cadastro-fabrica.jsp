<%@ page import="java.util.Map" %>
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
    <img src="/assets/Cadastro/fundo-cadastro.png" alt="Fundo decorativo" class="bg-particles">

    <div class="left-side">
        <img src="/assets/Cadastro/iara-direita_1-removebg-preview%201.png" alt="Mascote IARA" class="mascote">
    </div>

    <div class="right-side">
        <div class="login-box">
            <img src="/assets/IARA%20-%20Imagens%20Landing/Logo/logo-iara.png" alt="Logo IARA" class="logo">
            <h2>Cadastre uma Fábrica!</h2>

            <form action="${pageContext.request.contextPath}/area-restrita/fabricas?action=create" method="post">
                <input type="hidden" name="action" value="create">

                <div class="form-container">
                    <div class="form-section">
                        <h2>Dados</h2>
                        <input type="text" name="nome" placeholder="Nome">
                        <input type="text" name="cnpj" pattern="\d{14}" title="CNPJ inválido" placeholder="CNPJ">
                        <input type="email" name="email" placeholder="Email para contato">
                        <input type="text" name="ramo" placeholder="Ramo">
                        <input type="text" name="empresa" placeholder="Nome da empresa">
                        <select name="id_plano">
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
                        <input type="text" name="cep" pattern="\d{8}" title="Insira um CEP válido" placeholder="CEP">
                        <input type="text" name="logradouro" placeholder="Logradouro">
                        <input type="number" name="numero" placeholder="n°">
                        <input type="text" name="complemento" placeholder="Complemento">
                        <input type="text" name="estado" placeholder="Estado">
                        <input type="text" name="bairro" placeholder="Bairro">
                        <input type="text" name="cidade" placeholder="Cidade">
                    </div>
                </div>

                <button type="submit">Cadastrar</button>
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