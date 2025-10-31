<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
  <title>Landing Teste</title>
  <link rel="stylesheet" href="/styles/area-restrita.css">
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
          <li class="active">
            <img class="imagem" src="/assets/crud/home_azul.svg" alt="icone home">
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
          <li>
            <img class="imagem" src="/assets/crud/fabricas.svg" alt="icone fábricas">
            <a href="${pageContext.request.contextPath}/area-restrita/fabricas">Fábricas</a>
          </li>
          <li>
            <img class="imagem" src="/assets/crud/pagamento.svg" alt="icone pagamentos">
            <a href="${pageContext.request.contextPath}/area-restrita/pagamentos">Pagamentos</a>
          </li>
        </ul>
      </nav>
    </aside>
  </div>

  <div id="fundo_tela">

    <div id="topo">
      <div id="local">
        <h2>Página Inicial</h2>
      </div>
    </div>


    <div id="tela_principal">
      <div id="conteudo">
        <h1 id="titulo">BEM-VINDO</h1>
        <h2 id="subtitulo">AO IARA</h2>
        <p class="texto">Tudo que você precisa está aqui. <br>
          Registre, altere, delete e veja todos os dados.</p>
        <hr>
        <div id="slogan">
          <img id="iarazinha" src="/assets/boneca/iara-direita.svg" alt="Iarazinha">
          <p id="frase">Quem conta com IARA<br> não perde a conta</p>
        </div>
      </div>
    </div>

  </div>

</main>
</body>

</html>