<%@ page import="com.dto.AtualizacaoUsuarioDTO" %>
<%@ page import="com.model.TipoAcesso" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.model.Genero" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    AtualizacaoUsuarioDTO usuario = (AtualizacaoUsuarioDTO) request.getAttribute("usuario");
    Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
    String erro = (String) request.getAttribute("erro");
%>
<html>

<head>
    <title>Editar Usuário | Área Restrita</title>
    <link rel="stylesheet" href="/styles/editar-usuario.css">
    <link rel="icon" href="../assets/IARA%20-%20Imagens%20Landing/Geral/Mascote%20IARA.png">
</head>

<body>
<a href="${pageContext.request.contextPath}/area-restrita/usuarios" class="btn-sair">Cancelar</a>

<main class="login-container">
    <img src="/assets/Cadastro/fundo-cadastro.png" alt="Fundo decorativo" class="bg-particles">

    <div class="left-side">
        <img src="/assets/Cadastro/iara-direita_1-removebg-preview%201.png" alt="Mascote IARA" class="mascote">
    </div>

    <div class="right-side">
        <div class="login-box">
            <img src="/assets/IARA%20-%20Imagens%20Landing/Logo/logo-iara.png" alt="Logo IARA" class="logo">
            <h2>Editar Usuário - ID: <%= usuario.getId() %>
            </h2>

            <form action="${pageContext.request.contextPath}/area-restrita/usuarios" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="<%= usuario.getId() %>">
                <input type="text" name="nome" value="<%= usuario.getNome() %>" placeholder="Novo nome">
                <input type="text" name="email" value="<%= usuario.getEmail() %>" placeholder="Novo email">
                <input type="email" name="email_gerente" value="<%= usuario.getEmailGerente() %>"
                       placeholder="Email do novo gerente">
                <input type="text" name="cargo" value="<%= usuario.getCargo() %>">

                <select name="nivel_acesso">
                    <% for (TipoAcesso t : TipoAcesso.values()) { %>
                    <option value="<%= t.nivel() %>" <%=t == usuario.getTipoAcesso() ? "selected" : "" %>>
                        <%= t.descricao() %>
                    </option>
                    <% } %>
                </select>

                <select name="status">
                    <% for (boolean b : List.of(true, false)) { %>
                    <option value="<%= b %>" <%=b == usuario.getStatus() ? "selected" : "" %>>
                        <%= b ? "Ativo" : "Inativo" %>
                    </option>
                    <% } %>
                </select>

                <select name="fk_fabrica">
                    <% for (int id : fabricas.keySet()) { %>
                    <option value="<%= id %>" <%=id == usuario.getFkFabrica() ? "selected" : "" %>>
                        <%= fabricas.get(id) %>
                    </option>
                    <% } %>
                </select>

                <select name="genero">
                    <% for (Genero g : Genero.values()) { %>
                    <option value="<%= g.name().toLowerCase() %>" <%= g.equals(usuario.getGenero()) ? "selected" : "" %>>
                        <%= g.toString() %>
                    </option>
                    <% } %>
                </select>

                <button type="submit">Salvar</button>
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