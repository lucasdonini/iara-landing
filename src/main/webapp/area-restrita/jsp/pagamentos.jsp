<%@ page import="com.model.Pagamento" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.dao.PagamentoDAO.camposFiltraveis" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.model.DirecaoOrdenacao" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="com.model.MetodoPagamento" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<Pagamento> pagamentos = (List<Pagamento>) request.getAttribute("pagamentos");
    Map<Integer, String> fabricas = (Map<Integer, String>) request.getAttribute("fabricas");
    Map<Integer, String> planos = (Map<Integer, String>) request.getAttribute("planos");
%>

<!DOCTYPE html>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/planos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/crud_geral.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
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
                    <li>
                        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/usuario.svg" alt="icone usuarios">
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
                    <li class="active">
                        <img class="imagem" src="${pageContext.request.contextPath}/assets/crud/pagamento_azul.svg" alt="icone pagamentos">
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
                <h2>Gerenciar Pagamentos</h2>
                <p>Gerencie e organize seus Pagamentos</p>
            </div>
            <div id="foto_perfil">
                <img id="perfil" src="${pageContext.request.contextPath}/assets/crud/foto_perfil.svg" alt="foto de perfil">
            </div>
        </div>

        <div id="tela_principal">
            <div id="cabecalho">
                <h1 id="titulo">Lista de Pagamentos</h1>

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
                                        <option value="id" data-type="number">ID</option>
                                        <option value="valor" data-type="decimal">Valor Pago</option>
                                        <option value="statusP" data-type="select">Status</option>
                                        <option value="data_vencimento" data-type="date">Data Vencimento</option>
                                        <option value="data_pagamento" data-type="datetime-local">Data Pagamento</option>
                                        <option value="data_inicio" data-type="datetime-local">Data de Início</option>
                                        <option value="fk_metodopag" data-type="select">Metodo de Pagamento</option>
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

                <a id="cadastrar" href="${pageContext.request.contextPath}/area-restrita/pagamentos?action=create">Cadastrar</a>
            </div>

            <div id="tabela_usuarios">
                <table border="0">
                    <tr>
                        <th>ID</th>
                        <th>Valor Pago</th>
                        <th>Status</th>
                        <th>Data de Vencimento</th>
                        <th>Data de Pgto.</th>
                        <th>Tipo de Pgto.</th>
                        <th>Fábrica</th>
                        <th>Plano</th>
                    </tr>
                    <%
                        for (Pagamento pagamento : pagamentos) {
                            LocalDateTime dtPagto = pagamento.getDataPagamento();
                    %>
                    <tr>
                        <td>
                            <%= pagamento.getId() %>
                        </td>
                        <td>
                            <%= pagamento.getValor() %>
                        </td>
                        <td>
                            <%= pagamento.getStatus() ? "Pago" : "Pendente" %>
                        </td>
                        <td>
                            <%= pagamento.getDataVencimento() %>
                        </td>
                        <td>
                            <%= dtPagto == null ? "Pagamento Pendente" : dtPagto %>
                        </td>
                        <td>
                            <%= MetodoPagamento.deId(pagamento.getMetodoPagamento().getId()) %>
                        </td>
                        <td>
                            <%= fabricas.get(pagamento.getFkFabrica()) %>
                        </td>
                        <td>
                            <%= planos.get(pagamento.getFkPlano()) %>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/area-restrita/pagamentos" method="get">
                                <input type="hidden" name="id" value="<%= pagamento.getId() %>">
                                <input type="hidden" name="action" value="update">
                                <button id="editar" type="submit">Editar</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/area-restrita/pagamentos" method="post" onsubmit="confirmarDelete(event)">
                                <input type="hidden" name="id" value="<%= pagamento.getId() %>">
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