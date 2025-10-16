document.addEventListener("DOMContentLoaded", () => {
    const campoFiltro = document.getElementById("campoFiltro");
    const containerValorFiltro = document.getElementById("containerValorFiltro");

    campoFiltro.addEventListener("change", () => {
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
            } else if (value === "tipo_pagamento"){
                const debito = new Option("Débito");
                const credito = new Option("Crédito");
                const pix = new Option("PIX");

                debito.value = "debito";
                credito.value = "credito";
                pix.value = "pix";

                select.add(debito); select.add(credito); select.add(pix);
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
            }
            containerValorFiltro.appendChild(select);
        } else {
            const input = document.createElement("input");
            input.id = "valorFiltro";
            input.name = "valor_filtro";

            if (tipo === "decimal"){
                input.type = "number";
                input.step = "0.01";
                input.inputMode = "decimal";
                input.placeholder = "R$";
            }
            else{
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
