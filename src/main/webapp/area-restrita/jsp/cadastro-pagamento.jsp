<%@ page import="java.util.Map" %>
<%@ page import="com.model.MetodoPagamento" %>
<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%
    Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
    Map<Integer, String> planos = (Map<Integer, String>) request.getAttribute("planos");
    String erro = (String) request.getAttribute("erro");
%>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <title>Landing Teste</title>
    <link rel="stylesheet" href="/styles/cadastro-pagamento.css">
</head>

<body>
<a href="${pageContext.request.contextPath}/area-restrita/pagamentos" class="btn-sair">Cancelar</a>

<main class="login-container">
    <img src="/assets/Cadastro/fundo-cadastro.png" alt="Fundo decorativo" class="bg-particles">

    <div class="left-side">
        <img src="/assets/Cadastro/iara-direita_1-removebg-preview%201.png" alt="Mascote IARA" class="mascote">
    </div>

    <div class="right-side">
        <div class="login-box">
            <img src="/assets/IARA%20-%20Imagens%20Landing/Logo/logo-iara.png" alt="Logo IARA" class="logo">
            <h2>Cadastre um Pagamento!</h2>
            <form action="${pageContext.request.contextPath}/area-restrita/pagamentos" method="post" class="LoginForm">
                <input type="hidden" name="action" value="create">
                <select name="status">
                    <option value="" selected>Selecione o status do Pagamento</option>
                    <option value="true">Pagamento Realizado ✅</option>
                    <option value="false">Pagamento Pendente ❌</option>
                </select>

                <input type="date" name="data_inicio" data-placeholder="Selecione a Data de Início">
                <input type="date" name="data_vencimento" data-placeholder="Selecione a Data de Vencimento">
                <input type="date" name="data_pagamento" data-placeholder="Selecione a Data do Pagamento">
                <input type="number" name="valor" step="0.01" min="0" placeholder="Valor (em R$)">

                <select name="metodo_pagamento">
                    <option value="" select>-- Selecione --</option>

                    <% for (MetodoPagamento m : MetodoPagamento.values()) { %>
                    <option value="<%= m.getId() %>">
                        <%= m.toString() %>
                    </option>
                    <% } %>
                </select>

                <select name="fk_fabrica">
                    <option value="" selected>Selecione o ID da Fábrica que o pagamento refere-se</option>

                    <% for (int id : fabricas.keySet()) { %>
                    <option value="<%= id %>">
                        <%= fabricas.get(id) %>
                    </option>
                    <% } %>
                </select>

                <select name="fk_plano">
                    <option value="" selected>-- Selecione --</option>

                    <% for (int id : planos.keySet()) { %>
                    <option value="<%= id %>">
                        <%= planos.get(id) %>
                    </option>
                    <% } %>
                </select>
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