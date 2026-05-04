const API_URL = 'http://localhost:8080';
let loggedEmployeeName = '';
let loggedEmployeeId = '';
let searchTimeout = null;
let medicationList = [];
let requestItems = [];
let dispensationList = [];
let currentEditDispItems = [];
let currentEditDispPatientId = "";
let currentEditDispEmployeeId = "";

let selectedPatientIsExternal = false;

function applyCpfMask(value) {
    if (!value) return '';
    return value
        .replace(/\D/g, '')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d{1,2})/, '$1-$2')
        .replace(/(-\d{2})\d+?$/, '$1');
}

async function loadMedications() {
    try {
        const response = await fetch(`${API_URL}/medications`);
        if (response.ok) {
            medicationList = await response.json();
            populateSelects();
            console.log("Estoque atualizado.");
        }
    } catch (error) {
        console.error("Erro ao carregar medicamentos:", error);
    }
}

async function loadDispensations() {
    try {
        const response = await fetch(`${API_URL}/dispensations`);
        if (response.ok) {
            dispensationList = await response.json();
            console.log("Histórico de dispensações atualizado.");
            setupEditDispensationAutocomplete();
        }
    } catch (error) {
        console.error("Erro ao carregar dispensações:", error);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    loggedEmployeeName = localStorage.getItem('sgdm_userName');
    loggedEmployeeId = localStorage.getItem('sgdm_employeeId');

    if (!loggedEmployeeId || loggedEmployeeId === "undefined" || loggedEmployeeId === "null") {
        alert("Sessão expirada ou inválida. Por favor, faça login novamente.");
        window.location.href = '/login.html';
        return;
    }

    const loggedUserEl = document.getElementById('loggedUser');
    if (loggedUserEl) loggedUserEl.innerText = loggedEmployeeName;

    loadMedications();
    loadDispensations();

    const patientInput = document.getElementById('dispensePatientInput');
    if (patientInput) {
        patientInput.addEventListener('input', function (e) {
            let val = e.target.value;
            if (/^\d/.test(val)) {
                e.target.value = applyCpfMask(val);
            }
            clearTimeout(searchTimeout);
            if (val.length >= 3) {
                searchTimeout = setTimeout(() => fetchPatientSuggestions(val), 400);
            } else {
                document.getElementById('patientSuggestions').style.display = 'none';
            }
        });
    }

    const tpDocInput = document.getElementById('tpDocument');
    if (tpDocInput) {
        tpDocInput.addEventListener('input', e => e.target.value = applyCpfMask(e.target.value));
    }

    const searchEditMedInput = document.getElementById('searchEditMedInput');
    if (searchEditMedInput) {
        searchEditMedInput.addEventListener('input', function (e) {
            const query = e.target.value.toLowerCase();
            const list = document.getElementById('editMedSuggestions');

            if (query.length < 2) {
                list.style.display = 'none';
                return;
            }

            const filtered = medicationList.filter(m =>
                m.activeIngredient && m.activeIngredient.toLowerCase().includes(query)
            );

            list.innerHTML = '';
            if (filtered.length === 0) {
                list.innerHTML = '<li style="padding: 10px; color: #ef4444;">Nenhum medicamento encontrado.</li>';
            } else {
                filtered.forEach(m => {
                    const li = document.createElement('li');
                    li.style.cssText = 'padding: 10px; border-bottom: 1px solid #f3f4f6; cursor: pointer;';
                    li.innerHTML = `<strong>${m.activeIngredient}</strong> (${m.concentration})`;
                    li.onclick = () => selectMedicationToEdit(m.id);
                    list.appendChild(li);
                });
            }
            list.style.display = 'block';
        });
    }
    async function loadDispensations() {
        try {
            const response = await fetch(`${API_URL}/dispensations`);
            if (response.ok) {
                dispensationList = await response.json();

                console.log("Dispensações carregadas do banco:", dispensationList);

                setupEditDispensationAutocomplete();
            } else {
                console.error("Erro na resposta do servidor:", response.status);
            }
        } catch (error) {
            console.error("Erro ao carregar histórico de dispensações:", error);
        }
    }

    loadDispensations();
});

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

async function fetchPatientSuggestions(query) {
    const list = document.getElementById('patientSuggestions');
    try {
        const response = await fetch(`${API_URL}/patients?query=${encodeURIComponent(query)}&status=ativos`);
        if (response.ok) {
            const patients = await response.json();
            list.innerHTML = '';

            if (patients.length === 0) {
                list.innerHTML = '<li style="padding: 12px 15px; color: #ef4444; font-size: 14px;">Nenhum paciente ativo encontrado.</li>';
            } else {
                patients.forEach(p => {
                    const li = document.createElement('li');
                    li.style.cssText = 'padding: 12px 15px; border-bottom: 1px solid #f3f4f6; cursor: pointer; display: flex; justify-content: space-between; align-items: center; transition: background 0.2s;';

                    const cpfBonito = p.cpf ? applyCpfMask(p.cpf) : 'Sem CPF';

                    li.innerHTML = `
                        <span style="font-weight: 600; color: #1f2937;">${p.name}</span>
                        <span style="font-size: 13px; color: #6b7280; background: #f3f4f6; padding: 2px 8px; border-radius: 4px;">CPF: ${cpfBonito}</span>
                    `;

                    li.onmouseover = () => li.style.backgroundColor = '#f0fdf4';
                    li.onmouseout = () => li.style.backgroundColor = 'transparent';

                    li.onclick = () => {
                        document.getElementById('dispensePatientId').value = p.id;
                        document.getElementById('selectedPatientInfo').innerHTML = `
                            <span style="font-weight: 600; color: #1f2937; font-size: 15px;">${p.name}</span>
                            <span style="font-size: 13px; color: #6b7280; background: #e5e7eb; padding: 3px 8px; border-radius: 4px;">CPF: ${cpfBonito}</span>
                        `;
                        document.getElementById('dispensePatientInput').style.display = 'none';
                        list.style.display = 'none';
                        document.getElementById('selectedPatientCard').style.display = 'flex';

                        // Memoriza se o paciente é externo para barrarmos a medicação depois!
                        selectedPatientIsExternal = p.external === true;
                    };
                    list.appendChild(li);
                });
            }
            list.style.display = 'block';
        }
    } catch (error) {
        console.error("Erro no autocompletar:", error);
    }
}

document.addEventListener('click', function (e) {
    if (e.target.id !== 'dispensePatientInput') {
        const list = document.getElementById('patientSuggestions');
        if (list) list.style.display = 'none';
    }
});

function clearPatientSelection() {
    document.getElementById('dispensePatientId').value = '';
    document.getElementById('dispensePatientInput').value = '';
    document.getElementById('selectedPatientCard').style.display = 'none';
    document.getElementById('dispensePatientInput').style.display = 'block';
    document.getElementById('dispensePatientInput').focus();
    selectedPatientIsExternal = false; // Reseta a memória de segurança
}

function toggleThirdPartySection() {
    const isChecked = document.getElementById('isThirdParty').checked;
    const section = document.getElementById('thirdPartySection');

    if (isChecked) {
        section.style.display = 'block';
    } else {
        section.style.display = 'none';
        document.getElementById('tpName').value = '';
        document.getElementById('tpDocument').value = '';
        document.getElementById('tpObservation').value = '';
    }
}

function populateSelects() {
    const selectLot = document.getElementById('medicationSelectLot');
    let options = '<option value="">Selecione o medicamento no estoque...</option>';

    medicationList.forEach(med => {
        options += `<option value="${med.id}">${med.activeIngredient} (${med.concentration})</option>`;
    });

    if (selectLot) selectLot.innerHTML = options;
}

const medInput = document.getElementById('dispenseMedInput');
if (medInput) {
    medInput.addEventListener('input', function (e) {
        const query = e.target.value.toLowerCase();
        const list = document.getElementById('medSuggestions');

        if (query.length < 2) {
            list.style.display = 'none';
            return;
        }

        // CORREÇÃO: Filtrar apenas pelo activeIngredient
        const filtered = medicationList.filter(m =>
            m.activeIngredient && m.activeIngredient.toLowerCase().includes(query)
        );

        list.innerHTML = '';

        if (filtered.length === 0) {
            list.innerHTML = '<li style="padding: 10px 15px; color: #ef4444; font-size: 14px;">Medicamento não encontrado.</li>';
        } else {
            filtered.forEach(m => {
                const li = document.createElement('li');
                li.style.cssText = 'padding: 10px 15px; border-bottom: 1px solid #f3f4f6; cursor: pointer; transition: background 0.2s;';

                // CORREÇÃO: Exibir activeIngredient na lista
                li.innerHTML = `<span style="font-weight: 600; color: #1f2937;">${m.activeIngredient}</span> <span style="font-size: 13px; color: #6b7280;">(${m.concentration})</span>`;

                li.onmouseover = () => li.style.backgroundColor = '#e0e7ff';
                li.onmouseout = () => li.style.backgroundColor = 'transparent';

                li.onclick = () => {
                    // CORREÇÃO: Preencher o input com o nome correto
                    document.getElementById('dispenseMedInput').value = `${m.activeIngredient} (${m.concentration})`;
                    document.getElementById('dispenseMedId').value = m.id;
                    list.style.display = 'none';
                };
                list.appendChild(li);
            });
        }
        list.style.display = 'block';
    });
}

document.addEventListener('click', function (e) {
    if (e.target.id !== 'dispenseMedInput') {
        const medList = document.getElementById('medSuggestions');
        if (medList) medList.style.display = 'none';
    }
});

async function fetchMedicationDetails(id) {
    try {
        const response = await fetch(`${API_URL}/medications/${id}`);
        if (response.ok) {
            const med = await response.json();
            const index = medicationList.findIndex(m => m.id === id);
            if (index !== -1) {
                medicationList[index] = med;
            } else {
                medicationList.push(med);
            }
            return med;
        }
    } catch (error) {
        console.error("Erro ao procurar detalhes no banco:", error);
    }
    return null;
}

function calculateFEFOPreview(med, quantity) {
    let amountNeeded = quantity;
    let selectedLots = [];
    let totalStock = 0;

    if (!med.lots || med.lots.length === 0) {
        return { success: false, stock: 0 };
    }

    const sortedLots = [...med.lots].sort((a, b) => new Date(a.expirationDate) - new Date(b.expirationDate));

    for (let lot of sortedLots) {
        let qtdNoLote = lot.currentQuantity || 0;
        totalStock += qtdNoLote;

        if (amountNeeded <= 0) continue;
        if (qtdNoLote === 0) continue;

        let taken = Math.min(qtdNoLote, amountNeeded);
        selectedLots.push(`${lot.lotCode} (${taken} un)`);
        amountNeeded -= taken;
    }

    if (amountNeeded > 0) {
        return { success: false, stock: totalStock };
    }

    return { success: true, previewString: selectedLots.join(" + ") };
}

async function addItemDispensation() {
    const medId = document.getElementById('dispenseMedId').value;
    const quantity = parseInt(document.getElementById('medQuantity').value);

    // 1. Validações Iniciais
    if (!medId) {
        alert("Pesquise e selecione um medicamento na lista sugerida.");
        return;
    }
    if (isNaN(quantity) || quantity <= 0) {
        alert("Informe uma quantidade maior que zero.");
        return;
    }

    // 2. Busca os detalhes reais do medicamento (onde moram o Ingrediente e a Concentração)
    const med = await fetchMedicationDetails(medId) || medicationList.find(m => m.id === medId);

    if (!med) {
        alert("Erro: Medicamento não encontrado no servidor.");
        return;
    }

    // 3. TRAVA DE SEGURANÇA: Paciente de Fora (UBS Externa)
    if (selectedPatientIsExternal && med.programCategoryEnum !== 'FARMACIA_BASICA') {
        alert(`BLOQUEADO: Este paciente é de outra UBS.\nEle só tem permissão para retirar itens da Farmácia Básica.\n\nO medicamento '${med.activeIngredient} ${med.concentration}' pertence à categoria: ${med.programCategoryEnum}.`);
        return;
    }

    // 4. Verificação de Estoque (FEFO)
    const fefoResult = calculateFEFOPreview(med, quantity);

    if (!fefoResult.success) {
        alert(`Estoque insuficiente! Temos apenas ${fefoResult.stock} unidades de ${med.activeIngredient} ${med.concentration}.`);
        return;
    }

    // 5. Adiciona à lista de dispensação usando os dados confirmados do objeto 'med'
    requestItems.push({
        medicationId: medId,
        medicationActiveIngredient: med.activeIngredient, // Corrigido typo e pegando do objeto
        medicationConcentration: med.concentration,       // Agora a variável existe!
        quantity: quantity,
        previewLots: fefoResult.previewString
    });

    // 6. Limpa os campos para a próxima inclusão
    document.getElementById('dispenseMedId').value = "";
    document.getElementById('dispenseMedInput').value = "";
    document.getElementById('medQuantity').value = "0";

    updateTable();
}

function updateTable() {
    const tbody = document.getElementById('itemsBody');
    const emptyMsg = document.getElementById('emptyTableMsg');
    const btnFinalize = document.getElementById('btnFinalize');

    if (!tbody) return;

    tbody.innerHTML = '';

    if (requestItems.length === 0) {
        if (emptyMsg) emptyMsg.style.display = 'block';
        if (btnFinalize) btnFinalize.style.display = 'none';
    } else {
        if (emptyMsg) emptyMsg.style.display = 'none';
        if (btnFinalize) btnFinalize.style.display = 'inline-flex';

        requestItems.forEach((item, index) => {
            // A CORREÇÃO ESTÁ AQUI: Usando activeIngredient e concentration
            tbody.innerHTML += `
                <tr>
                    <td style="padding: 12px; font-weight: 500; color: #1f2937;">
                        ${item.medicationActiveIngredient} <span style="color: #6b7280; font-size: 0.9em;">(${item.medicationConcentration})</span>
                    </td>
                    <td style="padding: 12px; text-align: center; font-weight: 600;">
                        ${item.quantity}
                    </td>
                    <td style="padding: 12px; text-align: center; color: #0f766e; font-size: 0.85em; font-weight: 600;">
                        ${item.previewLots}
                    </td>
                    <td style="padding: 12px; text-align: center;">
                        <button type="button" class="btn-danger-icon" onclick="removeItem(${index})" title="Remover item">
                            <i class="fa-solid fa-trash"></i>
                        </button>
                    </td>
                </tr>
            `;
        });
    }
}

window.removeItem = function (index) {
    requestItems.splice(index, 1);
    updateTable();
}

async function finalizeDispensation() {
    const patientId = document.getElementById('dispensePatientId').value;
    const isThirdParty = document.getElementById('isThirdParty').checked;

    if (!patientId) {
        alert("Por favor, pesquise e clique no nome do paciente na lista de sugestões.");
        return;
    }

    if (requestItems.length === 0) {
        alert("Adicione pelo menos um medicamento à lista de dispensação.");
        return;
    }

    let thirdPersonData = null;
    if (isThirdParty) {
        const tpName = document.getElementById('tpName').value.trim();
        const tpDoc = document.getElementById('tpDocument').value.replace(/\D/g, '');

        if (!tpName || !tpDoc) {
            alert("Preencha o Nome e o Documento (CPF) do terceiro responsável.");
            return;
        }

        thirdPersonData = {
            name: tpName,
            document: tpDoc,
            observation: document.getElementById('tpObservation').value.trim()
        };
    }

    const itemsForBackend = requestItems.map(item => {
        return {
            medicationId: item.medicationId,
            quantity: item.quantity
        };
    });

    const payload = {
        employeeId: loggedEmployeeId,
        patientId: patientId,
        thirdPerson: thirdPersonData,
        items: itemsForBackend
    };

    try {
        const response = await fetch(`${API_URL}/dispensations`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok || response.status === 201) {
            alert("Sucesso! Dispensação registrada e estoques atualizados.");
            clearPatientSelection();
            document.getElementById('isThirdParty').checked = false;
            toggleThirdPartySection();
            requestItems = [];
            updateTable();
            loadMedications();
            loadDispensations();
        } else {
            alert("Erro ao registrar no servidor. Verifique o console para mais detalhes.");
        }
    } catch (error) {
        alert("Erro de comunicação com o servidor. O Java está rodando?");
    }
}

async function salvarLote() {
    const medId = document.getElementById('medicationSelectLot').value;
    const lote = document.getElementById('lotCode').value;
    const qtd = parseInt(document.getElementById('lotQuantity').value);
    let validade = document.getElementById('lotExpiration').value;

    if (!medId || !lote || !qtd || !validade) {
        alert("Preencha todos os campos para registrar a entrada do lote!");
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
        quantity: qtd
    };

    try {
        const response = await fetch(`${API_URL}/medications/${medId}/lots`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify([lotPayload])
        });

        if (response.ok || response.status === 201 || response.status === 204) {
            alert(`Sucesso! Entrada do Lote ${lote} registrada no sistema.`);
            document.getElementById('medicationSelectLot').value = "";
            document.getElementById('lotCode').value = "";
            document.getElementById('lotQuantity').value = "";
            document.getElementById('lotExpiration').value = "";
            loadMedications();
        } else {
            const errorData = await response.json();
            alert(errorData.message || "Erro ao salvar lote no servidor.");
        }
    } catch (error) {
        alert("Não foi possível conectar ao servidor para salvar o lote.");
    }
}

async function salvarNovoMedicamento() {
    const activePrinciple = document.getElementById('newMedActive').value.trim();
    const concentration = document.getElementById('newMedConcentration').value.trim();
    const pharmaceuticalForm = document.getElementById('newMedForm').value;
    const administrationRoute = document.getElementById('newMedRoute').value;
    const programCategoryEnum = document.getElementById('newMedProgram').value;

    if (!activePrinciple || !concentration || !pharmaceuticalForm || !administrationRoute) {
        alert("Os campos: Nome, Concentração, Forma Farmacêutica e Via de Administração são obrigatórios.");
        return;
    }

    const payload = {
        activeIngredient: activePrinciple,
        concentration: concentration,
        pharmaceuticalForm: pharmaceuticalForm,
        administrationRoute: administrationRoute,
        programCategoryEnum: programCategoryEnum,
        lots: []
    };

    try {
        const response = await fetch(`${API_URL}/medications`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.ok || response.status === 201) {
            alert(`Sucesso! O medicamento '${activePrinciple} ${concentration}' foi adicionado.`);
            document.getElementById('newMedActive').value = "";
            document.getElementById('newMedConcentration').value = "";
            document.getElementById('newMedForm').value = "";
            document.getElementById('newMedRoute').value = "";
            document.getElementById('newMedProgram').value = "";
            loadMedications();
        } else {
            const errorData = await response.json();
            alert(errorData.message || "Erro ao salvar.");
        }

    } catch (error) {
        alert("Não foi possível conectar ao servidor. O Java está rodando?");
    }
}

const searchEditMedInput = document.getElementById('searchEditMedInput');
if (searchEditMedInput) {
    searchEditMedInput.addEventListener('input', function (e) {
        const query = e.target.value.toLowerCase();
        const list = document.getElementById('editMedSuggestions');

        if (query.length < 2) {
            list.style.display = 'none';
            return;
        }

        const filtered = medicationList.filter(m =>
            m.activeIngredient && m.activeIngredient.toLowerCase().includes(query)
        );

        list.innerHTML = '';

        if (filtered.length === 0) {
            list.innerHTML = '<li style="padding: 10px 15px; color: #ef4444; font-size: 14px;">Medicamento não encontrado.</li>';
        } else {
            filtered.forEach(m => {
                const li = document.createElement('li');
                li.style.cssText = 'padding: 10px 15px; border-bottom: 1px solid #f3f4f6; cursor: pointer; transition: background 0.2s;';

                li.innerHTML = `<span style="font-weight: 600; color: #1f2937;">${m.activeIngredient}</span> <span style="font-size: 13px; color: #6b7280;">(${m.concentration})</span>`;

                li.onmouseover = () => li.style.backgroundColor = '#e0e7ff';
                li.onmouseout = () => li.style.backgroundColor = 'transparent';

                li.onclick = () => {
                    selectMedicationToEdit(m.id);
                    list.style.display = 'none';
                };
                list.appendChild(li);
            });
        }
        list.style.display = 'block';
    });
}

async function selectMedicationToEdit(id) {
    const med = await fetchMedicationDetails(id);
    if (!med) return;

    document.getElementById('editMedSuggestions').style.display = 'none';
    document.getElementById('searchEditMedInput').value = '';

    document.getElementById('editMedId').value = med.id;
    document.getElementById('displayMedName').innerText = med.activeIngredient;
    document.getElementById('editMedActive').value = med.activeIngredient;
    document.getElementById('editMedConcentration').value = med.concentration;
    document.getElementById('editMedForm').value = med.pharmaceuticalForm;
    document.getElementById('editMedRoute').value = med.administrationRoute;
    document.getElementById('editMedProgram').value = med.programCategoryEnum;

    // Renderização dos Lotes
    const lotsBody = document.getElementById('editLotsBody');
    lotsBody.innerHTML = '';

    if (med.lots && med.lots.length > 0) {
        med.lots.forEach((lot, index) => {
            const dateValue = lot.expirationDate ? lot.expirationDate.split('T')[0] : '';
            const qtdInicial = lot.initialQuantity !== undefined ? lot.initialQuantity : lot.quantity;

            lotsBody.innerHTML += `
                <tr>
                    <td style="padding: 8px;">
                        <input type="text" id="editLotCode_${index}" value="${lot.lotCode}" 
                            class="edit-inline-input" title="Clique para editar o código do lote">
                    </td>
                    <td style="padding: 8px;">
                        <input type="date" id="editLotExp_${index}" value="${dateValue}" 
                            class="edit-inline-input" title="Clique para editar a data de validade">
                    </td>
                    <td style="padding: 8px; font-weight: 600; text-align: center; color: #4b5563;">
                        ${qtdInicial} un
                       <input type="hidden" id="editLotQty_${index}" value="${qtdInicial}">
                    </td>
                    <td style="padding: 8px; font-weight: bold; color: #0f766e; text-align: center;">
                        ${lot.currentQuantity || 0} un
                    </td>
                </tr>
            `;
        });
    } else {
        lotsBody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: #6b7280; padding: 20px;">Sem lotes registrados para este item.</td></tr>';
    }

    document.getElementById('editMedCard').style.display = 'block';
}

function closeEditMed() {
    document.getElementById('editMedCard').style.display = 'none';
    document.getElementById('searchEditMedInput').focus();
}

async function saveMedicationEdit() {
    const id = document.getElementById('editMedId').value;

    const updatedLots = [];
    const rows = document.querySelectorAll('#editLotsBody tr');

    rows.forEach((row, index) => {
        const codeInput = document.getElementById(`editLotCode_${index}`);
        const expInput = document.getElementById(`editLotExp_${index}`);
        const qtyInput = document.getElementById(`editLotQty_${index}`); // Agora ele vai achar este campo!

        if (codeInput && expInput && qtyInput) {
            const expDateISO = expInput.value ? new Date(expInput.value + 'T12:00:00Z').toISOString() : null;

            updatedLots.push({
                lotCode: codeInput.value.trim(),
                expirationDate: expDateISO,
                quantity: parseInt(qtyInput.value)
            });
        }
    });

    const payload = {
        activeIngredient: document.getElementById('editMedActive').value.trim(),
        concentration: document.getElementById('editMedConcentration').value.trim(),
        pharmaceuticalForm: document.getElementById('editMedForm').value,
        administrationRoute: document.getElementById('editMedRoute').value,
        programCategoryEnum: document.getElementById('editMedProgram').value,
        lots: updatedLots
    };

    try {
        const response = await fetch(`${API_URL}/medications/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("Medicamento e lotes atualizados com sucesso!");
            closeEditMed();
            loadMedications();
        } else {
            const err = await response.json();
            alert(err.message || "Não foi possível atualizar os dados.");
        }
    } catch (error) {
        alert("Erro de conexão com o servidor.");
    }
}

function setupEditDispensationAutocomplete() {
    const input = document.getElementById('searchEditDispensationInput');
    const list = document.getElementById('editDispensationSuggestions');
    if (!input || !list) return;

    input.addEventListener('input', function (e) {
        const query = e.target.value.toLowerCase();
        if (query.length < 2) { list.style.display = 'none'; return; }

        const filtered = dispensationList.filter(d => {
            const dateStr = new Date(d.moment).toLocaleDateString('pt-BR');
            const patientName = d.targetPatient ? d.targetPatient.name.toLowerCase() : "";
            const employeeName = d.employee ? d.employee.name.toLowerCase() : "";

            return dateStr.includes(query) || patientName.includes(query) || employeeName.includes(query);
        });

        list.innerHTML = '';
        filtered.forEach(d => {
            const dateStr = new Date(d.moment).toLocaleDateString('pt-BR');
            const patientName = d.targetPatient ? d.targetPatient.name : "Paciente Desconhecido";
            const employeeName = d.employee ? d.employee.name : "N/A";

            const li = document.createElement('li');
            li.style.cssText = 'padding: 10px 15px; border-bottom: 1px solid #f3f4f6; cursor: pointer; transition: background 0.2s;';
            li.innerHTML = `
                <span style="font-weight: 600; color: #0f766e;">${dateStr}</span> - 
                <span style="font-weight: 600; color: #1f2937;">${patientName}</span> 
                <small style="color: #6b7280;">(Resp: ${employeeName})</small>
            `;

            li.onclick = () => {
                selectDispensationToEdit(d.id);
                list.style.display = 'none';
                input.value = '';
            };
            list.appendChild(li);
        });
        list.style.display = filtered.length > 0 ? 'block' : 'none';
    });
}

async function selectDispensationToEdit(id) {
    try {
        const response = await fetch(`${API_URL}/dispensations/${id}`);
        if (!response.ok) return;
        const disp = await response.json();

        document.getElementById('editDispensationId').value = disp.id;
        currentEditDispPatientId = disp.targetPatient ? (disp.targetPatient.patientId || disp.targetPatient.id) : null;
        currentEditDispEmployeeId = disp.employee ? (disp.employee.employeeId || disp.employee.id) : null;

        const patientName = disp.targetPatient ? disp.targetPatient.name : "N/A";
        document.getElementById('displayDispensationPatient').innerText = patientName;
        document.getElementById('editDispEmployee').value = disp.employee ? disp.employee.name : "N/A";
        document.getElementById('editDispDate').value = new Date(disp.moment).toLocaleString('pt-BR');

        let thirdPartyText = "Não";
        if (disp.thirdParty) {
            thirdPartyText = disp.thirdParty.name || (typeof disp.thirdParty === 'string' ? disp.thirdParty : "Sim");
        }

        const thirdPartyInput = document.getElementById('editDispThirdParty');
        if (thirdPartyInput) {
            thirdPartyInput.value = thirdPartyText;
        }

        const itemsResponse = await fetch(`${API_URL}/dispensations/${id}/items`);
        let itemsData = [];
        if (itemsResponse.ok) {
            itemsData = await itemsResponse.json();
        }

        currentEditDispItems = [];
        itemsData.forEach(item => {
            let existing = currentEditDispItems.find(i => i.medicationId === item.medicationId);

            if (existing) {
                existing.quantity += item.quantity;
                existing.lotsDisplay += ` + ${item.lotCode} (${item.quantity} un)`;
            } else {
                currentEditDispItems.push({
                    medicationId: item.medicationId,
                    name: `${item.medicationName || item.activeIngredient || 'Medicamento'} ${item.concentration ? '(' + item.concentration + ')' : ''}`,
                    quantity: item.quantity,
                    lotsDisplay: `${item.lotCode} (${item.quantity} un)`
                });
            }
        });

        updateEditDispensationTable();
        document.getElementById('editDispensationCard').style.display = 'block';
        setupEditDispAddMedSearch();
    } catch (e) {
        console.error(e);
        alert("Erro ao buscar detalhes da dispensação.");
    }
}

function updateEditDispensationTable() {
    const tbody = document.getElementById('editDispensationItemsBody');
    tbody.innerHTML = '';

    if (currentEditDispItems.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: #6b7280; padding: 20px;">Nenhum medicamento nesta dispensação.</td></tr>';
        return;
    }

    currentEditDispItems.forEach((item, index) => {
        const isRecalculado = item.lotsDisplay.includes("Automático") || item.lotsDisplay.includes("recalculado");

        const loteFormatado = isRecalculado
            ? `<span style="color: #6b7280; font-style: italic; font-size: 13px;">${item.lotsDisplay}</span>`
            : `<span style="color: #0f766e; font-weight: 500; font-size: 14px;">${item.lotsDisplay}</span>`;

        tbody.innerHTML += `
            <tr style="border-bottom: 1px solid #f3f4f6;">
                <td style="padding: 12px 8px; font-weight: 500; color: #1f2937;">${item.name}</td>
                <td style="padding: 12px 8px; text-align: center; font-weight: 600;">${item.quantity}</td>
                <td style="padding: 12px 8px; text-align: center;">${loteFormatado}</td>
                <td style="padding: 12px 8px; text-align: center;">
                    <button type="button" class="btn-danger-icon" onclick="removeEditDispItem(${index})" title="Remover item">
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </td>
            </tr>
        `;
    });
}

function removeEditDispItem(index) {
    currentEditDispItems.splice(index, 1);
    updateEditDispensationTable();
}

function setupEditDispAddMedSearch() {
    const input = document.getElementById('inputBuscaMedEdicao'); // Novo ID
    const list = document.getElementById('editDispAddMedSuggestions');
    if (!input || !list) return;

    input.addEventListener('input', function (e) {
        // TRAVA: Limpa o ID oculto sempre que digitar algo novo
        document.getElementById('inputOcultoMedIdEdicao').value = "";

        const query = e.target.value.toLowerCase();
        if (query.length < 2) { list.style.display = 'none'; return; }

        const filtered = medicationList.filter(m => m.activeIngredient && m.activeIngredient.toLowerCase().includes(query));
        list.innerHTML = '';

        filtered.forEach(m => {
            const li = document.createElement('li');
            li.style.cssText = 'padding: 10px 15px; border-bottom: 1px solid #f3f4f6; cursor: pointer; transition: background 0.2s;';
            li.innerHTML = `<span style="font-weight: 600;">${m.activeIngredient}</span> <small>(${m.concentration})</small>`;

            li.onclick = () => {
                document.getElementById('inputOcultoMedIdEdicao').value = m.id; // Novo ID
                input.value = `${m.activeIngredient} ${m.concentration}`;
                list.style.display = 'none';
            };
            list.appendChild(li);
        });
        list.style.display = filtered.length > 0 ? 'block' : 'none';
    });
}

function addMedToEditDispensation() {
    const medId = document.getElementById('inputOcultoMedIdEdicao').value;
    const medName = document.getElementById('inputBuscaMedEdicao').value;
    const qtyToAdd = parseInt(document.getElementById('inputNovaQtdEdicao').value);

    if (!medId || !medName) { alert("Selecione um medicamento válido."); return; }
    if (isNaN(qtyToAdd) || qtyToAdd <= 0) { alert("Informe uma quantidade válida."); return; }

    const selectedMed = medicationList.find(m => m.id === medId);
    if (!selectedMed) {
        alert("Medicamento não encontrado no estoque atual.");
        return;
    }

    const availableLots = (selectedMed.lots || [])
        .filter(lot => lot.currentQuantity > 0)
        .sort((a, b) => new Date(a.expirationDate) - new Date(b.expirationDate));

    const totalAvailable = availableLots.reduce((sum, lot) => sum + lot.currentQuantity, 0);

    if (qtyToAdd > totalAvailable) {
        alert(`Estoque insuficiente! Você tentou adicionar mais ${qtyToAdd} un, mas só restam ${totalAvailable} un disponíveis no estoque.`);
        return;
    }

    let remainingToFulfill = qtyToAdd;
    let newLotsUsed = [];

    for (const lot of availableLots) {
        if (remainingToFulfill <= 0) break;
        const takeFromLot = Math.min(lot.currentQuantity, remainingToFulfill);
        newLotsUsed.push(`${lot.lotCode} (${takeFromLot} un)`);
        remainingToFulfill -= takeFromLot;
    }

    const newLotsDisplay = newLotsUsed.join(' + ');
    const existingIndex = currentEditDispItems.findIndex(i => i.medicationId === medId);

    if (existingIndex >= 0) {
        currentEditDispItems[existingIndex].quantity += qtyToAdd;
        currentEditDispItems[existingIndex].lotsDisplay += ` + ${newLotsDisplay}`;
    } else {
        currentEditDispItems.push({
            medicationId: medId,
            name: medName,
            quantity: qtyToAdd,
            lotsDisplay: newLotsDisplay
        });
    }

    document.getElementById('inputOcultoMedIdEdicao').value = "";
    document.getElementById('inputBuscaMedEdicao').value = "";
    document.getElementById('inputNovaQtdEdicao').value = "1";

    updateEditDispensationTable();
}
async function saveDispensationEdit() {
    const id = document.getElementById('editDispensationId').value;

    const obsInput = document.getElementById('editDispObservation');
    const obs = obsInput ? obsInput.value.trim() : "";

    if (currentEditDispItems.length === 0) {
        alert("A dispensação precisa ter pelo menos um medicamento.");
        return;
    }

    const payload = {
        employeeId: currentEditDispEmployeeId,
        patientId: currentEditDispPatientId,
        observation: obs,
        items: currentEditDispItems.map(item => ({
            medicationId: item.medicationId,
            quantity: item.quantity
        }))
    };

    try {
        const response = await fetch(`${API_URL}/dispensations/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert("Dispensação atualizada com sucesso! O estoque foi recalculado.");
            closeEditDispensation();

            if (typeof loadDispensations === "function") {
                loadDispensations();
            }

            if (typeof loadMedications === "function") {
                loadMedications();
            } else {
                console.warn("Lembre-se de recarregar a lista de dispensações ou a pesquisa não achará a nova edição!");
            }
        } else {
            const errorText = await response.text();
            console.error("Erro recebido do Spring Boot:", errorText);

            try {
                const errorJson = JSON.parse(errorText);
                alert("O Servidor recusou a atualização: " + (errorJson.message || errorJson.error));
            } catch (e) {
                alert("Erro no backend! Verifique o terminal do seu Java. Status: " + response.status);
            }
        }
    } catch (error) {
        console.error("Erro interno do Front-end:", error);
        alert("Falha ao tentar se comunicar com o servidor. O Spring Boot está ligado?");
    }
}

function closeEditDispensation() {
    document.getElementById('editDispensationCard').style.display = 'none';
}

window.switchTab = switchTab;
window.clearPatientSelection = clearPatientSelection;
window.toggleThirdPartySection = toggleThirdPartySection;
window.addItemDispensation = addItemDispensation;
window.finalizeDispensation = finalizeDispensation;
window.salvarLote = salvarLote;
window.salvarNovoMedicamento = salvarNovoMedicamento;
window.saveMedicationEdit = saveMedicationEdit;
window.closeEditMed = closeEditMed;