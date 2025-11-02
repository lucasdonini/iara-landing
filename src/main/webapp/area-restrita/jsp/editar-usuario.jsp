<%@ page import="com.dto.AtualizacaoUsuarioDTO" %>
<%@ page import="com.model.TipoAcesso" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.model.Genero" %>
<%@ page import="com.utils.RegexUtils" %>
<%@ page import="java.util.Objects" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    AtualizacaoUsuarioDTO usuario = (AtualizacaoUsuarioDTO) request.getAttribute("usuario");
    Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
    List<String> emailGerentes = (List<String>) request.getAttribute("emailGerentes");
    String erro = (String) request.getAttribute("erro");
%>
<html>

<head>
    <title>Editar Usuário | Área Restrita</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/editar-usuario.css">
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
            <h2>Editar Usuário</h2>

            <form action="${pageContext.request.contextPath}/area-restrita/usuarios" method="post">
                <div id="divisao">
                    <div id="coluna1">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="<%= usuario.getId() %>">

                        <label for="nome">Nome:</label>
                        <input name="nome" id="nome" value="<%= usuario.getNome() %>"
                               type="text"
                               placeholder="Novo nome"
                               maxlength="100"
                               required
                        >

                        <label for="email">Email:</label>
                        <input name="email" id="email" value="<%= usuario.getEmail() %>"
                               type="text"
                               placeholder="Novo email"
                               pattern="<%= RegexUtils.REGEX_EMAIL %>"
                               title="Email inválido"
                               maxlength="100"
                               required
                        >

                        <label for="cargo">Cargo:</label>
                        <input name="cargo" id="cargo" value="<%= usuario.getCargo() %>"
                               type="text"
                               placeholder="Novo cargo"
                               maxlength="50"
                               required
                        >

                        <label for="email_gerente">Email do Gerente:</label>
                        <select name="email_gerente" id="email_gerente">
                            <% String emailGerente = usuario.getEmailGerente(); %>
                            <option value="" <%= emailGerente == null || emailGerente.isBlank() ? "selected" : "" %>>
                                Sem gerente
                            </option>

                            <% for (String emailG : emailGerentes) { %>
                            <option value="<%= emailG %>" <%= Objects.equals(emailG, emailGerente) ? "selected" : "" %>>
                                <%= emailG %>
                            </option>
                            <% } %>
                        </select>
                    </div>

                    <div id="coluna2">
                        <label for="nivel_acesso">Nível de Acesso:</label>
                        <select name="nivel_acesso" id="nivel_acesso" required>
                            <% for (TipoAcesso t : TipoAcesso.values()) { %>
                            <option value="<%= t.nivel() %>" <%=t == usuario.getTipoAcesso() ? "selected" : "" %>>
                                <%= t.descricao() %>
                            </option>
                            <% } %>
                        </select>

                        <label id="status">Status:</label>
                        <select name="status" id="status" required>
                            <% for (boolean b : List.of(true, false)) { %>
                            <option value="<%= b %>" <%=b == usuario.getStatus() ? "selected" : "" %>>
                                <%= b ? "Ativo" : "Inativo" %>
                            </option>
                            <% } %>
                        </select>

                        <label for="fk_fabrica">Fábrica:</label>
                        <select name="fk_fabrica" id="fk_fabrica" required>
                            <% for (int id : fabricas.keySet()) { %>
                            <option value="<%= id %>" <%=id == usuario.getFkFabrica() ? "selected" : "" %>>
                                <%= fabricas.get(id) %>
                            </option>
                            <% } %>
                        </select>

                        <label for="genero">Gênero:</label>
                        <select name="genero" id="genero" required>
                            <% for (Genero g : Genero.values()) { %>
                            <option value="<%= g.name().toLowerCase() %>" <%= g.equals(usuario.getGenero()) ? "selected" : "" %>>
                                <%= g.toString() %>
                            </option>
                            <% } %>
                        </select>
                    </div>
                </div>
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