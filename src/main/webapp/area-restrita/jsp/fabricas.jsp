<%@ page import="java.util.List" %>
<%@ page import="static com.dao.FabricaDAO.camposFiltraveis" %>
<%@ page import="com.model.DirecaoOrdenacao" %>
<%@ page import="com.dto.FabricaDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<FabricaDTO> fabricas = (List<FabricaDTO>) request.getAttribute("fabricas");
%>

<!DOCTYPE html>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="/styles/fabricas.css">
    <link rel="stylesheet" href="/styles/crud_geral.css">
</head>

<body>
<main>
    <div id="sidebar">
        <aside>
            <div id="logout">
                <img class="imagem" src="/assets/crud/pagina_anterior.svg" alt="simbolo de sair">
                <form action="${pageContext.request.contextPath}/login-handler" method="post">
                    <input type="hidden" name="action" value="logout">
                    <button type="submit">Sair</button>
                </form>
            </div>

            <div id="imagem">
                <img id="logo-iara" src="/assets/imagens gerais/iara_maior.svg" alt="Logo IARA">
            </div>

            <nav>
                <ul>
                    <li>
                        <img class="imagem" src="/assets/crud/home.svg" alt="icone home">
                        <a href="${pageContext.request.contextPath}/area-restrita/index.jsp">Página inicial</a>
                    </li>
                    <li>
                        <img class="imagem" src="/assets/crud/usuario.svg" alt="icone usuarios">
                        <a href="${pageContext.request.contextPath}/area-restrita/usuarios">Usuários</a>
                    </li>
                    <li>
                        <img class="imagem" src="/assets/crud/super_adm.svg" alt="icone super adm">
                        <a href="${pageContext.request.contextPath}/area-restrita/superadms">Super ADM</a>
                    </li>
                    <li>
                        <img class="imagem" src="/assets/crud/planos.svg" alt="icone planos">
                        <a href="${pageContext.request.contextPath}/area-restrita/planos">Planos</a>
                    </li>
                    <li class="active">
                        <img class="imagem" src="/assets/crud/fabricas_azul.svg" alt="icone fábricas">
                        <a href="${pageContext.request.contextPath}/area-restrita/fabricas">Fábricas</a>
                    </li>
                    <li>
                        <img class="imagem" src="/assets/crud/pagamento.svg" alt="icone pagamentos">
                        <a href="${pageContext.request.contextPath}/area-restrita/pagamentos">Pagamentos</a>
                    </li>
                    <li>
                        <img class="imagem" src="/assets/crud/BI.svg" alt="icone BI">
                        <a href="https://iara-area-restrita.vercel.app/home/dashboard">BI</a>
                    </li>
                </ul>
            </nav>
        </aside>
    </div>

    <div id="fundo_tela">

        <div id="topo">
            <div id="local">
                <h2>Gerenciar Fábricas</h2>
                <p>Gerencie e organize suas Fábricas</p>
            </div>
            <div id="foto_perfil">
                <img id="perfil" src="/assets/crud/foto_perfil.svg" alt="foto de perfil">
            </div>
        </div>

        <div id="tela_principal">
            <div id="cabecalho">
                <h1 id="titulo">Lista de Fábricas</h1>

                <div class="filtro-container">
                    <button class="btn-filtro"
                            onclick="document.querySelector('.filtro-card').classList.toggle('ativo')">
                        Filtro
                    </button>

                    <div class="filtro-card">
                        <form action="${pageContext.request.contextPath}/area-restrita/fabricas" method="get">
                            <input type="hidden" name="action" value="read">

                            <div class="filtragem">
                                <label>
                                    Campo de Filtragem:
                                    <select name="campo_filtro">
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
                                    Valor Filtrado:
                                    <input type="text" name="valor_filtro">
                                </label>
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
                                           value="<%= DirecaoOrdenacao.CRESCENTE.getSql() %>" checked> Crescente
                                    <input type="radio" name="direcao_sequencia"
                                           value="<%= DirecaoOrdenacao.DECRESCENTE.getSql() %>"> Decrescente
                                </label>
                            </div>

                            <input type="submit" value="Filtrar" id="filtrar">
                        </form>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/area-restrita/fabricas" method="get">
                    <input type="hidden" name="action" value="read">
                    <button id="limpaFiltro" type="submit">Limpar Filtros</button>
                </form>


                <a id="cadastrar" href="${pageContext.request.contextPath}/area-restrita/fabricas?action=create">Cadastrar</a>
            </div>

            <div id="tabela_usuarios">
                <table border="0">
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>CNPJ</th>
                        <th>Status</th>
                        <th>Email</th>
                        <th>Empresa</th>
                        <th>Plano</th>
                        <th>Ramo</th>
                    </tr>
                    <% for (FabricaDTO f : fabricas) { %>
                    <tr>
                        <td>
                            <%= f.getId() %>
                        </td>
                        <td>
                            <%= f.getNomeUnidade() %>
                        </td>
                        <td>
                            <%= f.cnpjFormatado() %>
                        </td>
                        <td>
                            <%= f.getStatus() ? "Ativa" : "Inativa" %>
                        </td>
                        <td>
                            <%= f.getEmailCorporativo() %>
                        </td>
                        <td>
                            <%= f.getNomeIndustria() %>
                        </td>
                        <td>
                            <%= f.getPlano() %>
                        </td>
                        <td>
                            <%= f.getRamo() %>
                        </td>
                        <td>
                            <%= f.getEndereco() %>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/area-restrita/fabricas" method="get">
                                <input type="hidden" name="id" value="<%= f.getId() %>">
                                <input type="hidden" name="action" value="update">
                                <button id="editar" type="submit">Editar</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/area-restrita/fabricas" method="post">
                                <input type="hidden" name="id_fabrica" value="<%= f.getId() %>">
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
</body>

</html>