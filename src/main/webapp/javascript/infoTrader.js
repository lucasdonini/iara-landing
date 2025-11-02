
// Função que mostra popup para confirmar decisão do usuário de deletar dados
function confirmarDelete(event) {
    // Bloqueia envio do formulário
    event.preventDefault();

    // Função da biblioteca "SweetAlert" para estilização do popup
    Swal.fire({
        // Caracterização básica
        title: "Tem certeza que deseja excluir?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#383f91",
        cancelButtonColor: "#fff",
        confirmButtonText: "Excluir",
        cancelButtonText: "Cancelar",

        // Caracterização isolada dos botões para quando o popup for aberto
        didOpen: (popup) => {
            const confirmButton = popup.querySelector('.swal2-confirm');
            const cancelButton = popup.querySelector('.swal2-cancel');

            popup.style.fontFamily = "Arial";
            popup.style.color = "black";
            confirmButton.style.color = "white";
            cancelButton.style.color = "black";
            confirmButton.style.fontWeight = "bold";
            cancelButton.style.fontWeight = "bold";
        }
    }).then((result) => {
        if (result.isConfirmed) {
            // Envia o formulário com os dados presentes dentro do 'form'
            event.target.submit();
        }
    });
}

// Função que muda o tipo de campo do valor da filtragem de forma dinâmica na tabela 'usuario'
function tipoCampoUsuario() {
    // Resgatando elementos HTML
    const campoFiltro = document.getElementById("campoFiltro");
    const containerValorFiltro = document.getElementsByClassName("filtragem")[1];
    const containerEmailGerentes = document.getElementsByClassName("filtragem")[2];
    const containerFabricas = document.getElementsByClassName("filtragem")[3];

    // Resgata o tipo definido no data-type e o valor do option
    const tipo = campoFiltro.selectedOptions[0].dataset.type;
    const value = campoFiltro.value;

    // Se for tipos de FKs, mostra div que contém códigos java dentro do JSP, e omite as demais
    if (tipo === "email-gerente") {
        mudarDisplay({
            mostrar: [containerEmailGerentes],
            esconder: [containerValorFiltro, containerFabricas]
        });
    } else if (tipo === "fabrica") {
        mudarDisplay({
            mostrar: [containerFabricas],
            esconder: [containerValorFiltro, containerEmailGerentes]
        });
    } else {
        mudarDisplay({
            mostrar: [containerValorFiltro],
            esconder: [containerEmailGerentes, containerFabricas]
        });

        // Chama a função que muda o tipo do campo
        atualizarValorFiltro({
            tipo: tipo,
            value: value,
            containerValorFiltro: containerValorFiltro,
            opcoesSelect: {
                genero: [
                    ["Masculino", "masculino"],
                    ["Feminino", "feminino"],
                    ["Outros", "outros"]
                ],
                tipo_acesso: [
                    ["Gerente", 2],
                    ["Supervisor SIF", 1],
                    ["Funcionário", 0]
                ],
                status: [
                    ["Ativo", true],
                    ["Inativo", false]
                ]
            }
        })
    }
}

// Função que muda o tipo de campo do valor da filtragem de forma dinâmica na tabela 'pagamento'
function tipoCampoPagamento() {
    // Resgatando elementos HTML
    const campoFiltro = document.getElementById("campoFiltro");
    const containerValorFiltro = document.getElementsByClassName("filtragem")[1];
    const containerPlanos = document.getElementsByClassName("filtragem")[2];
    const containerFabricas = document.getElementsByClassName("filtragem")[3];

    // Resgata o tipo definido no data-type e o valor do option
    const tipo = campoFiltro.selectedOptions[0].dataset.type;
    const value = campoFiltro.value;

    // Se for tipos de FKs, mostra div que contém códigos java dentro do JSP, e omite as demais
    if (tipo === "plano") {
        mudarDisplay({
            mostrar: [containerPlanos],
            esconder: [containerValorFiltro, containerFabricas]
        })
    } else if (tipo === "fabrica") {
        mudarDisplay({
            mostrar: [containerFabricas],
            esconder: [containerValorFiltro, containerPlanos]
        })
    } else {
        mudarDisplay({
            mostrar: [containerValorFiltro],
            esconder: [containerPlanos, containerFabricas]
        })

        // Chama a função que muda o tipo do campo
        atualizarValorFiltro({
            tipo: tipo,
            value: value,
            containerValorFiltro: containerValorFiltro,
            opcoesSelect: {
                status: [
                    ["Pago", true],
                    ["Pendente", false]
                ],
                fk_metodopag : [
                    ["Cartão de Crédito", 1],
                    ["Boleto Bancário", 2],
                    ["PIX", 3],
                    ["Transferência Bancária", 4],
                    ["Débito Automático", 5]
                ]
            }
        })
    }
}

// Função que muda o tipo de campo do valor da filtragem de forma dinâmica na tabela 'fabrica'
function tipoCampoFabrica() {
    // Resgatando elementos HTML
    const campoFiltro = document.getElementById("campoFiltro");
    const containerValorFiltro = document.getElementsByClassName("filtragem")[1];
    const containerPlanos = document.getElementsByClassName("filtragem")[2];

    // Resgata o tipo definido no data-type e o valor do option
    const tipo = campoFiltro.selectedOptions[0].dataset.type;
    const value = campoFiltro.value;

    // Se for tipos de FKs, mostra div que contém códigos java dentro do JSP, e omite as demais
    if (tipo === "plano") {
        mudarDisplay({
            mostrar: [containerPlanos],
            esconder: [containerValorFiltro]
        })
    } else {
        mudarDisplay({
            mostrar: [containerValorFiltro],
            esconder: [containerPlanos]
        })

        // Chama a função que muda o tipo do campo
        atualizarValorFiltro({
            tipo: tipo,
            value: value,
            containerValorFiltro: containerValorFiltro,
            opcoesSelect: {
                status: [
                    ["Ativa", true],
                    ["Inativa", false]
                ]
            }
        })
    }
}

// Função que muda o tipo de campo do valor da filtragem de forma dinâmica na tabela 'plano' e 'super_adm'
function tipoCampoPlanoAdm() {
    // Resgatando elementos HTML
    const campoFiltro = document.getElementById("campoFiltro");
    const containerValorFiltro = document.getElementsByClassName("filtragem")[1];

    // Resgata o tipo definido no data-type e o valor do option
    const tipo = campoFiltro.selectedOptions[0].dataset.type;
    const value = campoFiltro.value;

    // Chama a função que muda o tipo do campo
    atualizarValorFiltro({
        tipo: tipo,
        value: value,
        containerValorFiltro: containerValorFiltro,
        opcoesSelect: {}
    })
}


// Função genérica para definir o tipo do campo 'valoFiltro'
function atualizarValorFiltro({
    tipo, // Tipo definido no data-type
    value, // Nome do campo definido em 'value'
    containerValorFiltro, // Div pai que será criado o 'input' ou 'select'
    opcoesSelect = {} // Options definidos para o select da tabela definida
                              }) {

    // Limpa o container
    while(containerValorFiltro.firstChild){
        containerValorFiltro.removeChild(containerValorFiltro.firstChild);
    }

    // Cria nova label dentro do container de valorFilto
    const label = document.createElement("label");
    label.textContent = "Valor Filtrado:";
    label.setAttribute("for", "valorFiltro");
    containerValorFiltro.appendChild(label);

    if (tipo === "select") {

        // Cria elemento 'select'
        const select = document.createElement("select");
        select.name = "valor_filtro";
        select.id = "valorFiltro";
        select.add(new Option("--- SELECIONE ---", "", true, false));

        // Verifica se o value tem chave definida e se há uma chave com nome do value no map 'opcoesSelect'
        if (value && opcoesSelect[value]) {

            // Percorre o map definido como chave 'value' e cria options com base na chave e valor, adicionando ao select
            opcoesSelect[value].forEach(([key, val]) =>{
                select.add(new Option(key, val));
            });
        }

        // Adiciona como elemento filho do container
        containerValorFiltro.appendChild(select);

    } else if (tipo === "duracao") {

        // Se o tipo for 'duracao' cria duas constantes, um input e um select
        const numDuracao = document.createElement("input");
        const labelDuracao = document.createElement("select");

        numDuracao.id = "valorFiltro";
        numDuracao.name = "valor_filtro";
        labelDuracao.id = "labelDuracao";
        labelDuracao.name = "label_duracao";

        // Define o type e configs adicionais do input
        numDuracao.type = "number";
        numDuracao.min = "0";
        numDuracao.placeholder = "Ex: 2 (anos, meses...)";

        // Adiciona os options do select
        labelDuracao.add(new Option("Anos", "years"));
        labelDuracao.add(new Option("Meses", "months"));
        labelDuracao.add(new Option("Dias", "days"));

        // Adiciona ambos como elementos filhos do container
        containerValorFiltro.appendChild(numDuracao);
        containerValorFiltro.appendChild(labelDuracao);

    } else {

        // Cria elemento 'input'
        const input = document.createElement("input");
        input.name = "valor_filtro";
        input.id = "valorFiltro";

        switch (tipo) {
            case "date-nascimento":

                // Se o data-type for 'date-nascimento' define data máxima como 16 anos antes da data atual, uma vez que apenas pessoas com no mínimo 16 anos podem trabalhar
                input.type = "date";
                const data = new Date();
                data.setFullYear(data.getFullYear()-16);

                input.max = data.toISOString().split("T")[0];

                break;

            case "decimal":
                // Se o data-type for 'decimal', define o step como 0.1, para que seja possível colocar números de até 2 casas decimais
                input.type = "number";
                input.min = "0";
                input.step = "0.01";
                input.placeholder = "R$";

                break;

            default:

                // Resgata o tipo definido no data-type e o placeholder
                input.type = tipo;
                input.placeholder = "Digite o valor...";

                if (tipo === "date") {
                    // Limita as datas para que não ocorra erro no parse dentro do backend
                    input.min = "1900-01-01";
                    input.max = "9999-12-31";

                }

                 break;
        }

        // Adiciona como elemento filho do container
        containerValorFiltro.appendChild(input);
    }
}


// Função que muda os displays de forma automática para a visualização das divs e dados recuperados pelo código java no JSP
function mudarDisplay({mostrar, esconder}) {

    mostrar.forEach(div => div.style.display = "block");
    esconder.forEach(div => div.style.display = "none");
}
