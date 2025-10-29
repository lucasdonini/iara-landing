<%@ page import="com.dto.UsuarioDTO" %>
<%@ page import="com.utils.DataUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.dao.UsuarioDAO.camposFiltraveis" %>
<%@ page import="com.model.DirecaoOrdenacao" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    List<UsuarioDTO> usuarios = (List<UsuarioDTO>) request.getAttribute("usuarios");
%>
<!DOCTYPE html>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Landing Teste</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/usuarios.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/crud_geral.css">
</head>

<body>
<main>
    <div id="sidebar">
        <aside>
            <div id="logout">
                <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/pagina_anterior.svg" alt="simbolo de sair">
                <form action="${pageContext.request.contextPath}/login-handler" method="post">
                    <input type="hidden" name="action" value="logout">
                    <button type="submit">Sair</button>
                </form>
            </div>

            <div id="imagem">
                <img id="logo-iara" src="${pageContext.request.contextPath}/assets/imagens gerais/iara_maior.svg" alt="Logo IARA">
            </div>

            <nav>
                <ul>
                    <li>
                        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/home.svg" alt="icone home">
                        <a href="${pageContext.request.contextPath}/area-restrita/index.jsp">Página inicial</a>
                    </li>
                    <li class="active">
                        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/usuarios_azul.svg" alt="icone usuarios">
                        <a href="${pageContext.request.contextPath}/area-restrita/usuarios">Usuários</a>
                    </li>
                    <li>
                        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/super_adm.svg" alt="icone super adm">
                        <a href="${pageContext.request.contextPath}/area-restrita/superadms">Super ADM</a>
                    </li>
                    <li>
                        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/planos.svg" alt="icone planos">
                        <a href="${pageContext.request.contextPath}/area-restrita/planos">Planos</a>
                    </li>
                    <li>
                        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/fabricas.svg" alt="icone fábricas">
                        <a href="${pageContext.request.contextPath}/area-restrita/fabricas">Fábricas</a>
                    </li>
                    <li>
                        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/pagamento.svg" alt="icone pagamentos">
                        <a href="${pageContext.request.contextPath}/area-restrita/pagamentos">Pagamentos</a>
                    </li>
                    <li>
                        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/BI.svg" alt="icone BI">
                        <a href="https://iara-area-restrita.vercel.app/home/dashboard">BI</a>
                    </li>
                </ul>
            </nav>
        </aside>
    </div>

    <div id="fundo_tela">

        <div id="topo">
            <div id="local">
                <h2>Gerenciar Usuários</h2>
                <p>Gerencie e organize seus Usuários</p>
            </div>
            <div id="foto_perfil">
                <img id="perfil" src="${pageContext.request.contextPath}/assets/crud/foto_perfil.svg" alt="foto de perfil">
            </div>
        </div>

        <div id="tela_principal">
            <div id="cabecalho">
                <h1 id="titulo">Lista de Usuários</h1>

                <div class="filtro-container">
                    <button class="btn-filtro"
                            onclick="document.querySelector('.filtro-card').classList.toggle('ativo')">
                        Filtro
                    </button>

                    <div class="filtro-card">
                        <form action="${pageContext.request.contextPath}/area-restrita/pagamentos" method="get">
                            <input type="hidden" name="action" value="read">

                            <div class="filtragem">
                                <label>
                                    Campo de Filtragem:
                                    <select id="campoFiltro" name="campo_filtro">
                                        <option value="" selected>Nenhum selecionado</option>
                                        <option value="nome" data-type="text">Nome</option>
                                        <option value="genero" data-type="select">Gênero</option>
                                        <option value="data_nascimento" data-type="date-nascimento">Data de Nascimento</option>
                                        <option value="cargo" data-type="text">Cargo</option>
                                        <option value="email" data-type="email">Email</option>
                                        <option value="tipo_acesso" data-type="select">Tipo de Acesso</option>
                                        <option value="statusU" data-type="select">Status</option>
                                        <option value="data_criacao" data-type="datetime-local">Data de Criação</option>
                                    </select>
                                </label>
                            </div>

                            <div class="filtragem">
                                <label for="valorFiltro">Valor Filtrado:</label>
                                <input type="text" id="valorFiltro" name="valor_filtro">
                            </div>

                            <div class="filtragem">
                                <label>
                                    Ordenar por:
                                    <select name="campo_sequencia">
                                        <option value="" selected>Nenhum selecionado</option>

                                        <% for (String chave : camposFiltraveis.keySet()) { %>
                                        <option value="<%= chave %>">
                                            <%= camposFiltraveis.get(chave) %>
                                        </option>
                                        <% } %>
                                    </select>
                                </label>
                            </div>

                            <div class="filtragem">
                                <label>
                                    Direção de ordenação
                                    <input type="radio" name="direcao_sequencia"
                                           value="<%= DirecaoOrdenacao.CRESCENTE.getSql() %>"
                                           checked> Crescente
                                    <input type="radio" name="direcao_sequencia"
                                           value="<%= DirecaoOrdenacao.DECRESCENTE.getSql() %>">
                                    Decrescente
                                </label>
                            </div>

                            <input type="submit" value="Filtrar" id="filtrar">
                        </form>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/area-restrita/pagamentos" method="get">
                    <input type="hidden" name="action" value="read">
                    <button id="limpaFiltro" type="submit">Limpar Filtros</button>
                </form>

                <a id="cadastrar" href="${pageContext.request.contextPath}/area-restrita/usuarios?action=create">Cadastrar</a>
            </div>

            <div id="tabela_usuarios">
                <table border="0">
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Email</th>
                        <th>Email Gerente</th>
                        <th>Tipo de Acesso</th>
                        <th>Data de Criação</th>
                        <th>Status</th>
                        <th>Fábrica</th>
                        <th>Gênero</th>
                        <th>Cargo</th>
                    </tr>

                    <% for (UsuarioDTO u : usuarios) { %>
                    <tr>
                        <td>
                            <%= u.getId() %>
                        </td>
                        <td>
                            <%= u.getNome() %>
                        </td>
                        <td>
                            <%= u.getEmail() %>
                        </td>
                        <td>
                            <%= u.getEmailGerente() == null ? u.getEmailGerente() : "Sem gerente" %>
                        </td>
                        <td>
                            <%= u.getTipoAcesso().descricao() %>
                        </td>
                        <td>
                            <%= u.getDataCriacao().format(DataUtils.DMY) %>
                        </td>
                        <td>
                            <%= u.getStatus() ? "Ativo" : "Inativo" %>
                        </td>
                        <td>
                            <%= u.getNomeFabrica() %>
                        </td>
                        <td>
                            <%= u.getGenero() %>
                        </td>
                        <td>
                            <%= u.getCargo() %>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/area-restrita/usuarios" method="get">
                                <input type="hidden" name="id" value="<%= u.getId() %>">
                                <input type="hidden" name="action" value="update">
                                <button id="editar" type="submit">Editar</button>
                            </form>

                            <form action="${pageContext.request.contextPath}/area-restrita/usuarios" method="post">
                                <input type="hidden" name="id" value="<%= u.getId() %>">
                                <input type="hidden" name="action" value="delete">
                                <button id="deletar" type="submit">Deletar</button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                </table>
            </div>

        </div>
    </div>

</main>
<script src="${pageContext.request.contextPath}/javascript/infoTrader.js"></script>
</body>

</html>