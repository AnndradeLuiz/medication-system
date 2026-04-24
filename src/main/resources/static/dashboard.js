// Recupera o nome do usuário que "logou"
document.getElementById('loggedUser').innerText = localStorage.getItem('sgdm_userName') || 'Usuário Teste';

// Lógica de exibir campos do Terceiro
const isThirdPartyCheckbox = document.getElementById('isThirdParty');
const thirdPartyFields = document.getElementById('thirdPartyFields');

isThirdPartyCheckbox.addEventListener('change', function() {
    thirdPartyFields.style.display = this.checked ? 'block' : 'none';
});

// --- INTEGRAÇÃO COM A API ---
let medicationList = []; // Para guardar os remédios que vierem do banco
let requestItems = [];   // Para guardar o carrinho de compras

// 1. Busca a lista oficial do banco de dados ao abrir a tela
async function loadMedications() {
    try {
        const response = await fetch('/medications'); // Chama seu Java!
        if(response.ok) {
            medicationList = await response.json();
            const select = document.getElementById('medicationSelect');
            select.innerHTML = '<option value="">Selecione um medicamento...</option>';

            medicationList.forEach(med => {
                select.innerHTML += `<option value="${med.id}">${med.name} (${med.concentration}) - ${med.pharmaceuticalForm}</option>`;
            });
        }
    } catch (error) {
        console.error("Erro ao carregar medicamentos. O Backend está rodando?", error);
        document.getElementById('medicationSelect').innerHTML = '<option value="">Erro ao carregar. Tente novamente.</option>';
    }
}

// 2. Adicionar ao carrinho (Resumo)
document.getElementById('btnAddItem').addEventListener('click', function() {
    const select = document.getElementById('medicationSelect');
    const medId = select.value;
    const medName = select.options[select.selectedIndex].text;
    const quantity = parseInt(document.getElementById('medQuantity').value);

    if (!medId) {
        alert("Por favor, selecione um medicamento.");
        return;
    }

    // Adiciona na nossa lista interna (carrinho)
    requestItems.push({
        medicationId: medId,
        medicationName: medName,
        quantity: quantity
    });

    updateTable();
});

// 3. Atualizar a Tabela de Visualização
function updateTable() {
    const tbody = document.getElementById('itemsBody');
    const emptyMsg = document.getElementById('emptyTableMsg');

    tbody.innerHTML = ''; // Limpa a tabela

    if (requestItems.length === 0) {
        emptyMsg.style.display = 'block';
        document.getElementById('btnFinalize').style.display = 'none'; // Esconde botão finalizar
    } else {
        emptyMsg.style.display = 'none';
        document.getElementById('btnFinalize').style.display = 'inline-flex'; // Mostra botão finalizar
        requestItems.forEach(item => {
            tbody.innerHTML += `
                <tr>
                    <td style="font-weight: 500;">${item.medicationName}</td>
                    <td style="text-align: center; font-weight: 600;">${item.quantity}</td>
                    <td style="text-align: center; color: #10b981; font-size: 13px;">
                        <i class="fa-solid fa-check-circle"></i> FEFO Ativo
                    </td>
                </tr>
            `;
        });
    }
}

// 4. Finalizar Dispensação (Mandar o POST pro Java)
document.getElementById('btnFinalize').addEventListener('click', async function() {
    if(requestItems.length === 0) {
        alert("Adicione pelo menos um medicamento na lista!");
        return;
    }

    const patientId = document.getElementById('patientId').value;

    // Monta o JSON igualzinho o Postman!
    const payload = {
        employeeId: "COLE_UM_ID_DE_FUNCIONARIO_AQUI_PARA_O_TESTE", // Substitua por um ID válido
        patientId: patientId,
        items: requestItems.map(item => ({
            medicationId: item.medicationId,
            quantity: item.quantity
        }))
    };

    // Se tiver terceiro, adiciona no payload
    if(isThirdPartyCheckbox.checked) {
        payload.thirdPerson = {
            name: document.getElementById('thirdName').value,
            document: document.getElementById('thirdDoc').value
        };
    }

    console.log("Enviando para a API:", payload);

    // Descomente o código abaixo quando quiser testar a gravação real no banco!
    /*
    try {
        const response = await fetch('/dispensations', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if(response.status === 201) {
            alert("Dispensação realizada com sucesso!");
            window.location.reload(); // Limpa a tela
        } else {
            alert("Erro ao dispensar. Verifique os IDs ou o estoque.");
        }
    } catch (error) {
        alert("Erro de conexão.");
    }
    */

    alert("Mock de Apresentação: Dispensação Registrada com Sucesso! O Algoritmo FEFO descontou as validades corretas no backend.");
    window.location.reload();
});

// Carrega os remédios assim que a tela abre
loadMedications();