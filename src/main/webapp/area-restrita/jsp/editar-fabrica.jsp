<%@ page import="com.model.Fabrica" %>
<%@ page import="com.model.Endereco" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.utils.RegexUtils" %>
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/editar-fabrica.css">
    <link rel="icon"
          href="${pageContext.request.contextPath}/assets/IARA%20-%20Imagens%20Landing/Geral/Mascote%20IARA.png">
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
            <div id="container_logo">
                <img src="${pageContext.request.contextPath}/assets/IARA%20-%20Imagens%20Landing/Logo/logo-iara.png"
                     alt="Logo IARA" class="logo">
            </div>
            <h2 id="titulo">Editar Fábrica</h2>

            <form action="${pageContext.request.contextPath}/area-restrita/fabricas" method="post">
                <div class="form-container">
                    <div class="form-section">
                        <h2>Dados da Fábrica</h2>
                        <div id="divisao1">
                            <div id="divisao1_1">
                                <label for="nome">Nome da Fábrica:</label>
                                <input name="nome" id="nome" value="<%= f.getNomeUnidade() %>"
                                       type="text"
                                       placeholder="Nome"
                                       maxlength="100"
                                       required
                                >

                                <label for="cnpj">CNPJ:</label>
                                <input name="cnpj" id="cnpj" value="<%= f.getCnpj() %>"
                                       type="text"
                                       placeholder="XXXXXXXXXXXXXX"
                                       pattern="<%= RegexUtils.REGEX_CNPJ %>"
                                       title="CNPJ inválido"
                                       maxlength="14"
                                       required
                                >

                                <label for="email">Email Corporativo:</label>
                                <input name="email" id="email" value="<%= f.getEmailCorporativo() %>"
                                       type="email"
                                       placeholder="Email para contato"
                                       pattern="<%= RegexUtils.REGEX_EMAIL %>"
                                       title="Email inválido"
                                       maxlength="100"
                                       required
                                >

                                <label for="ramo">Ramo:</label>
                                <input name="ramo" id="ramo" value="<%= f.getRamo() %>"
                                       type="text"
                                       placeholder="Ramo"
                                       maxlength="50"
                                       required
                                >
                            </div>

                            <div id="coluna1_2">
                                <label for="nome_empresa">Nome da Empresa:</label>
                                <input name="nome_empresa" id="nome_empresa" value="<%= f.getNomeIndustria() %>"
                                       type="text"
                                       placeholder="Nome da empresa"
                                       maxlength="150"
                                >

                                <label for="status">Status:</label>
                                <select id="status" name="status" required>
                                    <% for (Boolean b : List.of(true, false)) { %>
                                    <option value="<%= b.toString() %>" <%=b ? "selected" : "" %>>
                                        <%= b ? "Ativa" : "Inativa" %>
                                    </option>
                                    <% } %>
                                </select>

                                <label for="id_plano">Plano:</label>
                                <select id="id_plano" name="id_plano" required>
                                    <% for (int idPlano : planos.keySet()) { %>
                                    <option value="<%= idPlano %>" <%=idPlano == f.getIdPlano() ? "selected" : "" %>>
                                        <%= planos.get(idPlano) %>
                                    </option>
                                    <% } %>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="form-section">
                        <h2>Endereço da Fábrica</h2>
                        <div id="divisao2">
                            <div id="coluna2_1">
                                <label for="cep">CEP:</label>
                                <input name="cep" id="cep" value="<%= e.getCep() %>"
                                       type="text"
                                       placeholder="XXXXXXXX"
                                       pattern="<%= RegexUtils.REGEX_CEP %>"
                                       title="Insira um CEP válido"
                                       maxlength="8"
                                       required
                                >

                                <label for="logradouro">Logradouro:</label>
                                <input name="logradouro" id="logradouro" value="<%= e.getRua() %>"
                                       type="text"
                                       placeholder="Logradouro"
                                       maxlength="100"
                                       required
                                >

                                <label for="numero">Número:</label>
                                <input name="numero" id="numero" value="<%= e.getNumero() %>"
                                       type="number"
                                       placeholder="n°"
                                       min="0"
                                       required
                                >

                                <label for="complemento">Complemento:</label>
                                <input name="complemento" id="complemento"
                                       value="<%= complemento != null ? complemento : "" %>"
                                       type="text"
                                       placeholder="Complemento"
                                       maxlength="100"
                                >
                            </div>

                            <div id="coluna2_2">
                                <label for="estado">Estado:</label>
                                <input id="estado" name="estado" value="<%= e.getEstado() %>"
                                       type="text"
                                       placeholder="Estado"
                                       maxlength="50"
                                       required
                                >

                                <label for="bairro">Bairro:</label>
                                <input id="bairro" name="bairro" value="<%= e.getBairro() %>"
                                       type="text"
                                       placeholder="Bairro"
                                       maxlength="50"
                                       required
                                >

                                <label for="cidade">Cidade:</label>
                                <input id="cidade" name="cidade" value="<%= e.getCidade() %>"
                                       type="text"
                                       placeholder="Cidade"
                                       maxlength="50"
                                       required
                                >
                            </div>
                        </div>
                    </div>
                </div>

                <input type="hidden" name="id_fabrica" value="<%= f.getId() %>">
                <input type="hidden" name="action" value="update">

                <button type="submit" style="display: block">Salvar</button>
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