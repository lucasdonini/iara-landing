document.addEventListener("DOMContentLoaded", () => {
    // Resgata elementos HTMl
    const campoFiltro = document.getElementById("campoFiltro");
    const containerValorFiltro = document.getElementsByClassName("filtragem")[1];

    // Chama a função quando um novo campo é selecionado
    campoFiltro.addEventListener("change", () => {
        // Tipo definido no 'data-type'
        const tipo = campoFiltro.selectedOptions[0].dataset.type;

        // limpa o container e re-insere só o label
        containerValorFiltro.innerHTML = `
            <label for="valorFiltro">Valor Filtrado:</label>
        `;

        if (tipo === "select") {
            const select = document.createElement("select");
            select.id = "valorFiltro";
            select.name = "valor_filtro";

            const value = campoFiltro.value;
            if (value === "statusU"){
                const ativo = new Option("Ativo");
                const inativo = new Option("Inativo");

                ativo.value = "true";
                inativo.value = "false";

                select.add(ativo); select.add(inativo);
            } else if(value === "statusP"){
                const pago = new Option("Pago");
                const pendente = new Option("Pendente");

                pago.value = "true";
                pendente.value = "false";

                select.add(pago); select.add(pendente);
            } else if (value === "statusF"){
                const ativa = new Option("Ativa");
                const inativa = new Option("Inativa");

                ativa.value = "true";
                inativa.value = "false";

                select.add(ativa); select.add(inativa);
            } else if(value === "tipo_acesso"){
                const geren = new Option("Gerenciamento");
                const alt = new Option("Alteração");
                const suges = new Option("Sugestão");
                const leit = new Option("Leitura");

                leit.value = "0";
                suges.value = "1";
                alt.value = "2";
                geren.value = "3";

                select.add(leit); select.add(suges); select.add(alt); select.add(geren);
            } else if (value === "genero"){
                const masculino = new Option("Masculino");
                const feminino = new Option("Feminino");
                const outros = new Option("Outros");

                masculino.value = "masculino";
                feminino.value = "feminino";
                outros.value = "outros";

                select.add(masculino); select.add(feminino); select.add(outros);
            } else if(value === "fk_metodopag"){
                const credito = new Option("Cartão de Crédito");
                const boleto = new Option("Boleto");
                const pix = new Option("PIX");
                const trans = new Option("Transferência Bancária");
                const debito = new Option("Débito Automático");

                credito.value = "1";
                boleto.value = "2";
                pix.value = "3";
                trans.value = "4";
                debito.value = "5";

                select.add(credito); select.add(boleto); select.add(pix); select.add(trans); select.add(debito);
            }
            containerValorFiltro.appendChild(select);
        } else if (tipo === "duracao"){
            const numDuracao = document.createElement("input");
            const labelDuracao = document.createElement("select");

            numDuracao.id = "valorFiltro";
            numDuracao.name = "valor_filtro";
            labelDuracao.id = "labelDuracao";
            labelDuracao.name = "label_duracao";

            numDuracao.type = "number";
            numDuracao.min = "0";
            numDuracao.placeholder = "Ex: 2 (anos, meses...)";

            const anos = new Option("Anos");
            anos.value = "years";
            const meses = new Option("Meses");
            meses.value = "months";
            const dias = new Option("Dias");
            dias.value = "days";

            labelDuracao.add(anos); labelDuracao.add(meses); labelDuracao.add(dias);

            containerValorFiltro.appendChild(numDuracao);
            containerValorFiltro.appendChild(labelDuracao);
        } else {
            const input = document.createElement("input");
            input.id = "valorFiltro";
            input.name = "valor_filtro";

            if (tipo === "decimal"){
                input.type = "number";
                input.step = "0.01";
                input.inputMode = "decimal";
                input.placeholder = "R$";
            } else if (tipo === "date-nascimento"){
                input.type = "date";
                const data = new Date();
                data.setFullYear(data.getFullYear()-16);

                input.max = data.toISOString().split("T")[0];
            } else if (tipo === "date" || tipo === "datetime-local"){
                input.type = tipo;
                const data = new Date();
                data.setFullYear(data.getFullYear()+15);

                input.max = data.toISOString().split("T")[0];
            } else{
                input.type = tipo;
                if (tipo === "date"){
                    input.max = "1999-12-31";
                }
                input.placeholder = "Digite o valor...";
            }
            containerValorFiltro.appendChild(input);
        }
    });
});


function confirmarDelete(event) {
    event.preventDefault();

    Swal.fire({
        title: "Tem certeza que deseja excluir?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#383f91",
        cancelButtonColor: "#fff",
        confirmButtonText: "Excluir",
        cancelButtonText: "Cancelar",

        didOpen: (popup) => {
            // Atributos
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
            event.target.submit();
        }
    });

    return false;
}