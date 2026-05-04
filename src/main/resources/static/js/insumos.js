const API_URL = 'http://localhost:8080';
let supplyList = [];

// --- INICIALIZAÇÃO E SEGURANÇA ---
document.addEventListener("DOMContentLoaded", () => {
    const loggedEmployeeName = localStorage.getItem('sgdm_userName');
    const loggedEmployeeId = localStorage.getItem('sgdm_employeeId');

    if (!loggedEmployeeId || loggedEmployeeId === "undefined" || loggedEmployeeId === "null") {
        alert("Sessão expirada ou inválida. Por favor, faça login novamente.");
        window.location.href = '/login.html';
        return;
    }

    if (loggedEmployeeName) {
        const userEl = document.getElementById('loggedUser');
        if (userEl) userEl.innerText = loggedEmployeeName;
    }

    loadSupplies();
    setupEditInsumoAutocomplete();
});

// --- 1. CARREGAR INSUMOS ---
async function loadSupplies() {
    try {
        const response = await fetch(`${API_URL}/supplies`);
        if (response.ok) {
            supplyList = await response.json();
            populateSupplySelects();
        }
    } catch (error) {
        console.error("Erro ao carregar insumos:", error);
    }
}

function populateSupplySelects() {
    const selectLot = document.getElementById('supplyLotSelect');
    if (!selectLot) return;

    let options = '<option value="">Selecione o insumo no estoque...</option>';
    supplyList.forEach(sup => {
        options += `<option value="${sup.id}">${sup.name}</option>`;
    });
    selectLot.innerHTML = options;
}

// --- 2. CONSULTAR E EDITAR INSUMO ---
function setupEditInsumoAutocomplete() {
    const input = document.getElementById('searchEditInsumoInput');
    const list = document.getElementById('editInsumoSuggestions');
    if (!input) return;

    input.addEventListener('input', function (e) {
        const query = e.target.value.toLowerCase();
        if (query.length < 2) { list.style.display = 'none'; return; }

        const filtered = supplyList.filter(s => s.name && s.name.toLowerCase().includes(query));
        list.innerHTML = '';

        filtered.forEach(s => {
            const li = document.createElement('li');
            li.style.cssText = 'padding: 10px 15px; border-bottom: 1px solid #f3f4f6; cursor: pointer; transition: background 0.2s;';
            li.innerHTML = `<span style="font-weight: 600; color: #1f2937;">${s.name}</span> <small style="color: #6b7280;">(${s.observation || ''})</small>`;

            li.onclick = () => {
                selectInsumoToEdit(s.id);
                list.style.display = 'none';
                input.value = '';
            };
            list.appendChild(li);
        });
        list.style.display = 'block';
    });
}

async function selectInsumoToEdit(id) {
    try {
        const response = await fetch(`${API_URL}/supplies/${id}`);
        if (!response.ok) return;
        const supply = await response.json();

        document.getElementById('editInsumoId').value = supply.id;
        document.getElementById('displayInsumoName').innerText = supply.name;
        document.getElementById('editInsumoName').value = supply.name;
        document.getElementById('editInsumoObs').value = supply.observation || '';

        const tbody = document.getElementById('editInsumoLotsBody');
        tbody.innerHTML = '';

        if (supply.lots && supply.lots.length > 0) {
            supply.lots.forEach((lot, index) => {
                // Corta a data do Java (Instant) para o formato YYYY-MM-DD do input de data
                const dateValue = lot.expirationDate ? lot.expirationDate.split('T')[0] : '';

                tbody.innerHTML += `
                    <tr>
                        <td style="padding: 8px;">
                            <input type="text" id="insumoEditLotCode_${index}" value="${lot.lotCode}" 
                                class="edit-inline-input" title="Clique para editar o código">
                        </td>
                        <td style="padding: 8px;">
                            <input type="date" id="insumoEditLotExp_${index}" value="${dateValue}" 
                                class="edit-inline-input" title="Clique para editar a data de validade">
                        </td>
                        <td style="padding: 8px;">
                            <!-- AGORA É UM CAMPO EDITÁVEL: type="number" -->
                            <input type="number" id="insumoEditLotQty_${index}" value="${lot.receivedQuantity}" 
                                class="edit-inline-input" style="text-align: center; font-weight: 600;" 
                                title="Clique para editar a quantidade recebida">
                        </td>
                    </tr>
                `;
            });
        } else {
            tbody.innerHTML = '<tr><td colspan="3" style="text-align: center; padding: 20px; color: #6b7280;">Nenhum lote registrado.</td></tr>';
        }

        document.getElementById('editInsumoCard').style.display = 'block';
    } catch (e) { console.error(e); }
}

async function saveInsumoEdit() {
    const id = document.getElementById('editInsumoId').value;
    const updatedLots = [];

    document.querySelectorAll('#editInsumoLotsBody tr').forEach((row, index) => {
        const codeInput = document.getElementById(`insumoEditLotCode_${index}`);
        const expInput = document.getElementById(`insumoEditLotExp_${index}`);
        const qtyInput = document.getElementById(`insumoEditLotQty_${index}`);

        if (codeInput && expInput && qtyInput) {
            const expDateISO = expInput.value ? new Date(expInput.value + 'T12:00:00Z').toISOString() : null;

            updatedLots.push({
                lotCode: codeInput.value.trim(),
                expirationDate: expDateISO,
                quantity: parseInt(qtyInput.value) // Passando como "quantity" para o Java entender
            });
        }
    });

    const payload = {
        name: document.getElementById('editInsumoName').value.trim(),
        observation: document.getElementById('editInsumoObs').value.trim(),
        lots: updatedLots
    };

    try {
        const response = await fetch(`${API_URL}/supplies/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("Insumo e lotes atualizados com sucesso!");
            document.getElementById('editInsumoCard').style.display = 'none';
            loadSupplies(); // Atualiza a lista global
        } else {
            alert("Erro ao atualizar insumo.");
        }
    } catch (e) { alert("Erro de conexão."); }
}

function closeEditInsumo() {
    document.getElementById('editInsumoCard').style.display = 'none';
}

// --- 3. ENTRADA DE LOTE DE INSUMOS ---
async function salvarLoteInsumo() {
    const supplyId = document.getElementById('supplyLotSelect').value;
    const lote = document.getElementById('supplyLotCode').value.trim();
    const qtd = parseInt(document.getElementById('supplyLotQuantity').value);
    let validade = document.getElementById('supplyLotExpiration').value;

    if (!supplyId || !lote || !qtd || !validade) {
        alert("Preencha todos os campos do lote!");
        return;
    }

    const dataValidade = new Date(validade + 'T00:00:00');
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);

    if (dataValidade < hoje) {
        alert("Regra de Segurança: Não é permitido dar entrada em um lote com validade vencida!");
        return;
    }

    const expirationDateISO = new Date(validade + 'T12:00:00Z').toISOString();

    const lotPayload = {
        lotCode: lote,
        expirationDate: expirationDateISO,
        quantity: qtd // Passando como "quantity" para o Java (LotRequestDTO) entender
    };

    try {
        const response = await fetch(`${API_URL}/supplies/${supplyId}/lots`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify([lotPayload])
        });

        if (response.ok || response.status === 201 || response.status === 204) {
            alert(`Lote ${lote} registrado com sucesso.`);
            document.getElementById('supplyLotSelect').value = "";
            document.getElementById('supplyLotCode').value = "";
            document.getElementById('supplyLotQuantity').value = "";
            document.getElementById('supplyLotExpiration').value = "";
            loadSupplies();
        } else {
            alert("Erro ao salvar lote no servidor.");
        }
    } catch (error) {
        alert("Erro de conexão com o servidor.");
    }
}

async function salvarNovoInsumo() {
    const nome = document.getElementById('newSupplyName').value.trim();
    const observacao = document.getElementById('newSupplyObservation').value.trim();

    if (!nome) {
        alert("O Nome do insumo é obrigatório.");
        return;
    }

    const payload = {
        name: nome,
        observation: observacao || null,
        lots: []
    };

    try {
        const response = await fetch(`${API_URL}/supplies`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok || response.status === 201) {
            alert(`Insumo '${nome}' cadastrado com sucesso.`);
            document.getElementById('newSupplyName').value = "";
            document.getElementById('newSupplyObservation').value = "";
            loadSupplies();
        } else {
            alert("Erro ao cadastrar insumo.");
        }
    } catch (error) {
        alert("Erro de comunicação com o servidor.");
    }
}

// --- CONTROLE DE ABAS ---
function switchTab(tabId, element) {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    document.querySelectorAll('.tab-section').forEach(s => {
        s.classList.remove('active-section');
        s.style.display = 'none';
    });

    element.classList.add('active');
    const secaoAtiva = document.getElementById(tabId);
    if (secaoAtiva) {
        secaoAtiva.classList.add('active-section');
        secaoAtiva.style.display = 'block';
    }
}

// --- EXPORTAÇÕES PARA O HTML CONSEGUIR LER AS FUNÇÕES ---
window.saveInsumoEdit = saveInsumoEdit;
window.closeEditInsumo = closeEditInsumo;
window.salvarNovoInsumo = salvarNovoInsumo;
window.salvarLoteInsumo = salvarLoteInsumo;
window.switchTab = switchTab;
window.loadSupplies = loadSupplies;
window.populateSupplySelects = populateSupplySelects;
window.setupEditInsumoAutocomplete = setupEditInsumoAutocomplete;
window.selectInsumoToEdit = selectInsumoToEdit;