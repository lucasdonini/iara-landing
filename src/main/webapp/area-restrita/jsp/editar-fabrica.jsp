<%@ page import="com.model.Fabrica" %>
<%@ page import="com.model.Endereco" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String erro = (String) request.getAttribute("erro");
    Fabrica f = (Fabrica) request.getAttribute("fabrica");
    Endereco e = (Endereco) request.getAttribute("endereco");
    Map<Integer, String> planos = (Map<Integer, String>) request.getAttribute("planos");
    String complemento = e.getComplemento();
%>
<html lang="pt-BR">

<head>
    <title>Editar Fábrica | Área Restrita</title>
    <link rel="stylesheet" href="/styles/editar-fabrica.css">
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
            <h2>Editar Fábrica - ID: <%= f.getId() %>
            </h2>

            <form action="${pageContext.request.contextPath}/area-restrita/fabricas" method="post">
                <div class="form-container">
                    <div class="form-section">
                        <h2>Dados da Fábrica</h2>
                        <label for="nome">Nome da Fábrica:</label>
                        <input type="text" id="nome" name="nome" value="<%= f.getNomeUnidade() %>" placeholder="Nome">
                        <label for="cnpj">CNPJ:</label>
                        <input type="text" id="cnpj" name="cnpj" value="<%= f.getCnpj() %>" pattern="\d{14}" title="CNPJ inválido"
                               placeholder="XX.XXX.XXX/YYYY-ZZ">
                        <label for="email">Email Corporativo:</label>
                        <input type="email" id="email" name="email" value="<%= f.getEmailCorporativo() %>"
                               placeholder="Email para contato">
                        <label for="ramo">Ramo:</label>
                        <input type="text" id="ramo" name="ramo" value="<%= f.getRamo() %>" placeholder="Ramo">
                        <label for="nome_empresa">Nome da Empresa:</label>
                        <input type="text" id="nome_empresa" name="nome_empresa" value="<%= f.getNomeIndustria() %>"
                               placeholder="Nome da empresa">

                        <label for="status">Status:</label>
                        <select id="status" name="status">
                            <% for (Boolean b : List.of(true, false)) { %>
                            <option value="<%= b.toString() %>" <%=b ? "selected" : "" %>>
                                <%= b ? "Ativa" : "Inativa" %>
                            </option>
                            <% } %>
                        </select>

                        <label for="id_plano">Plano:</label>
                        <select id="id_plano" name="id_plano">
                            <% for (int idPlano : planos.keySet()) { %>
                            <option value="<%= idPlano %>" <%=idPlano == f.getIdPlano() ? "selected" : "" %>>
                                <%= planos.get(idPlano) %>
                            </option>
                            <% } %>
                        </select>
                    </div>

                    <div class="form-section">
                        <h2>Endereço da Fábrica</h2>

                        <label for="cep">CEP:</label>
                        <input type="text" id="cep" name="cep" value="<%= e.getCep() %>" pattern="\d{8}"
                               title="Insira um CEP válido"
                               placeholder="XXXXX-XXX">
                        <label id="logradouro">Logradouro:</label>
                        <input type="text" id="logradouro" name="logradouro" value="<%= e.getRua() %>" placeholder="Logradouro">
                        <label id="numero">Número:</label>
                        <input type="number" id="numero" name="numero" value="<%= e.getNumero() %>" placeholder="n°">
                        <label id="complemento">Complemento:</label>
                        <input type="text" id="complemento" name="complemento" value="<%= complemento != null ? complemento : "" %>"
                               placeholder="Complemento">
                        <label for="estado">Estado:</label>
                        <input type="text" id="estado" name="estado" value="<%= e.getEstado() %>" placeholder="Estado">
                        <label for="bairro">Bairro:</label>
                        <input type="text" id="bairro" name="bairro" value="<%= e.getBairro() %>" placeholder="Bairro">
                        <label for="cidade">Cidade:</label>
                        <input type="text" id="cidade" name="cidade" value="<%= e.getCidade() %>" placeholder="Cidade">
                    </div>
                </div>

                <input type="hidden" name="id_fabrica" value="<%= f.getId() %>">
                <input type="hidden" name="action" value="update">

                <button type="submit" style="display: block">Salvar</button>
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