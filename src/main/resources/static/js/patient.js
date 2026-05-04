const API_URL = 'http://localhost:8080';
let currentPatients = []; // Guarda os pacientes carregados

// --- FUNÇÕES DE SEGURANÇA E UTILIDADE ---

// Previne ataques XSS (Cross-Site Scripting) injetados via banco de dados
function escapeHTML(str) {
    if (!str) return '';
    return String(str).replace(/[&<>'"]/g,
        tag => ({
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            "'": '&#39;',
            '"': '&quot;'
        }[tag] || tag)
    );
}

function clearNewPatientForm() {
    document.getElementById('newName').value = '';
    document.getElementById('newCpf').value = '';
    document.getElementById('newCns').value = '';
    document.getElementById('newBirthDate').value = '';
    document.getElementById('newPhones').value = '';
    document.getElementById('newIsExternal').checked = false;

    // Limpa os programas
    const programs = ['progDiabeticos', 'progHipertensao', 'progSaudeMulher', 'progSaudeMental'];
    programs.forEach(id => {
        const el = document.getElementById(id);
        if (el) el.checked = false;
    });

    // Garante que a seção de programas volte a aparecer para o próximo cadastro
    const secaoProgramas = document.getElementById('secaoProgramasCadastro');
    if (secaoProgramas) secaoProgramas.style.display = 'block';
}

// --- CONTROLE DE ABAS ---
function switchTab(tabId, element) {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    document.querySelectorAll('.tab-section').forEach(s => s.classList.remove('active-section'));

    element.classList.add('active');

    const targetSection = document.getElementById(tabId);
    if (targetSection) {
        targetSection.classList.add('active-section');
    }
}

// --- ESCONDER/MOSTRAR PROGRAMAS NO CADASTRO (Estava faltando!) ---
function toggleProgramasCadastro() {
    const isExternal = document.getElementById('newIsExternal').checked;
    const secaoProgramas = document.getElementById('secaoProgramasCadastro');

    if (!secaoProgramas) return;

    if (isExternal) {
        // Esconde a área
        secaoProgramas.style.display = 'none';

        // Desmarca tudo se for marcado como externo
        const programs = ['progDiabeticos', 'progHipertensao', 'progSaudeMulher', 'progSaudeMental'];
        programs.forEach(id => {
            const el = document.getElementById(id);
            if (el) el.checked = false;
        });
    } else {
        // Mostra a área novamente
        secaoProgramas.style.display = 'block';
    }
}

// --- ESCONDER/MOSTRAR PROGRAMAS NA EDIÇÃO ---
function toggleProgramasEdit() {
    const isExternal = document.getElementById('editExternal').checked;
    const secaoProgramas = document.getElementById('secaoProgramasEdit');

    if (!secaoProgramas) return;

    if (isExternal) {
        secaoProgramas.style.display = 'none';

        const programs = ['editProgFarmaciaBasica', 'editProgDiabeticos', 'editProgHipertensao', 'editProgSaudeMulher', 'editProgSaudeMental'];
        programs.forEach(id => {
            const el = document.getElementById(id);
            if (el) el.checked = false;
        });
    } else {
        secaoProgramas.style.display = 'block';
    }
}

async function saveNewPatient() {
    // 1. Captura os valores
    const nomeInput = document.getElementById('newName').value.trim();
    const cpfInput = document.getElementById('newCpf').value.trim();
    const cnsInput = document.getElementById('newCns').value.trim();
    const birthDateInput = document.getElementById('newBirthDate').value;
    const isExternalInput = document.getElementById('newIsExternal').checked;

    if (!nomeInput || (!cpfInput && !cnsInput)) {
        alert("Por favor, preencha o Nome Completo e pelo menos um documento (CPF ou CNS).");
        return;
    }

    if (birthDateInput) {
        const dataNascimento = new Date(birthDateInput + 'T00:00:00');
        const hoje = new Date();
        hoje.setHours(0, 0, 0, 0);

        if (dataNascimento > hoje) {
            alert("Data inválida: A data de nascimento não pode estar no futuro!");
            return;
        }
    }

    const phonesRaw = document.getElementById('newPhones').value;
    const phonesList = phonesRaw
        ? phonesRaw.split(',')
            .map(n => n.replace(/\D/g, ''))
            .filter(n => n.length > 0)
        : [];

    const programsList = [];
    if (!isExternalInput) {
        if (document.getElementById('progDiabeticos').checked) programsList.push("DIABETICO");
        if (document.getElementById('progHipertensao').checked) programsList.push("HIPERTENSAO");
        if (document.getElementById('progSaudeMulher').checked) programsList.push("SAUDE_DA_MULHER");
        if (document.getElementById('progSaudeMental').checked) programsList.push("SAUDE_MENTAL");
    }

    const payload = {
        name: nomeInput,
        cpf: cpfInput.trim() === "" ? null : cpfInput.replace(/\D/g, ''),
        cns: cnsInput.trim() === "" ? null : cnsInput.replace(/\D/g, ''),
        birthDate: birthDateInput ? new Date(birthDateInput).toISOString() : null,
        external: isExternalInput,
        phones: phonesList,
        programs: programsList
    };

    try {
        const response = await fetch(`${API_URL}/patients`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.status === 201) {
            alert('Paciente cadastrado com sucesso!');
            clearNewPatientForm();

            const tabs = document.querySelectorAll('.tab');
            if (tabs.length > 0) {
                switchTab('tab-busca', tabs[0]);
            }
            searchPatients();
        } else {
            alert('Erro ao cadastrar. Verifique os dados.');
        }
    } catch (error) {
        alert('Erro de conexão com o servidor.');
    }
}

// --- INICIALIZAÇÃO DA PÁGINA ---
document.addEventListener("DOMContentLoaded", () => {
    let userName = localStorage.getItem('sgdm_userName');
    if (!userName || userName === 'undefined' || userName === 'null') {
        userName = 'Luiz Andrade';
    }

    const loggedUserEl = document.getElementById('loggedUser');
    if (loggedUserEl) {
        loggedUserEl.innerText = escapeHTML(userName);
    }

    renderInitialTable();

    // LER A URL E ABRIR A ABA CERTA
    if (window.location.hash === '#cadastro') {
        const tabs = document.querySelectorAll('.tab');
        if (tabs.length > 1) {
            switchTab('tab-cadastro', tabs[1]);
            history.replaceState(null, null, ' ');
        }
    }

    const newCpfEl = document.getElementById('newCpf');
    if (newCpfEl) newCpfEl.addEventListener('input', e => e.target.value = applyCpfMask(e.target.value));

    const newCnsEl = document.getElementById('newCns');
    if (newCnsEl) newCnsEl.addEventListener('input', e => e.target.value = applyCnsMask(e.target.value));

    const editCpfEl = document.getElementById('editCpf');
    if (editCpfEl) editCpfEl.addEventListener('input', e => e.target.value = applyCpfMask(e.target.value));

    const editCnsEl = document.getElementById('editCns');
    if (editCnsEl) editCnsEl.addEventListener('input', e => e.target.value = applyCnsMask(e.target.value));

    const newPhonesEl = document.getElementById('newPhones');
    if (newPhonesEl) newPhonesEl.addEventListener('input', e => e.target.value = applyMultiPhoneMask(e.target.value));

    const editPhonesEl = document.getElementById('editPhones');
    if (editPhonesEl) editPhonesEl.addEventListener('input', e => e.target.value = applyMultiPhoneMask(e.target.value));
});

async function searchPatients() {
    const searchInput = document.getElementById('searchInput');
    const statusFilter = document.getElementById('statusFilter');

    const query = searchInput ? searchInput.value : '';
    const statusFiltro = statusFilter ? statusFilter.value : '';

    const tbody = document.getElementById('patientsTableBody');
    if (!tbody) return;

    tbody.innerHTML = '<tr><td colspan="5" class="empty-msg">A pesquisar...</td></tr>';

    try {
        const response = await fetch(`${API_URL}/patients?query=${query}&status=${statusFiltro}`);

        if (response.ok) {
            currentPatients = await response.json();
            renderTable();
        } else {
            tbody.innerHTML = '<tr><td colspan="5" class="empty-msg" style="color: red;">Erro ao buscar pacientes.</td></tr>';
        }
    } catch (error) {
        console.error("API off, erro na requisição.", error);
        tbody.innerHTML = '<tr><td colspan="5" class="empty-msg" style="color: red;">Erro de conexão com o servidor.</td></tr>';
    }
}

function renderInitialTable() {
    const tbody = document.getElementById('patientsTableBody');
    if (tbody) {
        tbody.innerHTML = '<tr><td colspan="5" class="empty-msg">Utilize a barra de pesquisa ou os filtros acima para listar os pacientes.</td></tr>';
    }
}

function renderTable() {
    const tbody = document.getElementById('patientsTableBody');
    if (!tbody) return;

    tbody.innerHTML = '';

    if (!currentPatients || currentPatients.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="empty-msg">Nenhum paciente encontrado.</td></tr>';
        return;
    }

    currentPatients.forEach(p => {
        let statusHtml = p.status
            ? '<span class="status-badge" style="background-color: #d1fae5; color: #10b981;">Ativo</span>'
            : '<span class="status-badge status-danger" style="background-color: #fee2e2; color: #ef4444;">Inativo</span>';

        let vinculoHtml = p.external
            ? '<span style="color: #ea580c; font-weight: 600;"><i class="fa-solid fa-map-location-dot"></i> Externo</span>'
            : '<span style="color: #3b82f6; font-weight: 600;"><i class="fa-solid fa-house-medical"></i> UBS Local</span>';

        let cpfFormatado = p.cpf ? applyCpfMask(p.cpf) : '-';
        let cnsFormatado = p.cns ? applyCnsMask(p.cns) : '-';
        tbody.innerHTML += `
            <tr onclick="openPatientModal('${escapeHTML(p.id)}')">
                <td style="font-weight: 500;">${escapeHTML(p.name)}</td>
                <td>${escapeHTML(cpfFormatado)}</td>
                <td>${escapeHTML(cnsFormatado)}</td>
                <td>${statusHtml}</td>
                <td>${vinculoHtml}</td>
            </tr>
        `;
    });
}

function openPatientModal(id) {
    // Busca o paciente no array carregado na tabela
    const patient = currentPatients.find(p => String(p.id) === String(id));
    if (!patient) return;

    // Preenchimento dos dados básicos
    document.getElementById('viewName').innerText = patient.name;
    document.getElementById('viewCpf').innerText = patient.cpf ? applyCpfMask(patient.cpf) : '-';

    // CORREÇÃO 1: Atribuição do CNS que estava solta
    document.getElementById('viewCns').innerText = patient.cns ? applyCnsMask(patient.cns) : '-';

    let birthDateFormatted = 'Não informada';
    if (patient.birthDate) {
        const dateObj = new Date(patient.birthDate);
        // Usamos UTC para evitar que o fuso horário mude o dia do nascimento
        birthDateFormatted = dateObj.toLocaleDateString('pt-BR', { timeZone: 'UTC' });
    }
    document.getElementById('viewBirthDate').innerText = birthDateFormatted;
    document.getElementById('viewAge').innerText = formatPatientAge(patient.birthDate);

    document.getElementById('viewStatus').innerHTML = patient.status
        ? '<span style="color: #10b981; font-weight: 600;">Ativo</span>'
        : '<span style="color: #ef4444; font-weight: 600;">Inativo</span>';

    document.getElementById('viewExternal').innerHTML = patient.external
        ? '<span style="color: #ea580c; font-weight: 600;">Externo</span>'
        : '<span style="color: #3b82f6; font-weight: 600;">UBS Local</span>';

    // Telefones
    if (patient.phones && patient.phones.length > 0) {
        const formattedPhones = patient.phones.map(tel => formatPhone(tel));
        document.getElementById('viewPhones').innerText = formattedPhones.join(', ');
    } else {
        document.getElementById('viewPhones').innerText = 'Nenhum contato salvo';
    }

    // CORREÇÃO 2: Programas de Saúde (Lógica robusta)
    const viewProgramsEl = document.getElementById('viewPrograms');
    if (patient.programs && patient.programs.length > 0) {
        const programNames = patient.programs.map(p => {
            const labels = {
                "DIABETICO": "Diabetes",
                "HIPERTENSAO": "Hipertensão",
                "FARMACIA_BASICA": "Farmácia Básica",
                "SAUDE_DA_MULHER": "Saúde da Mulher",
                "SAUDE_MENTAL": "Saúde Mental"
            };
            // Verifica se 'p' é um objeto (p.name) ou uma string direta
            const progKey = p.name || p;
            return labels[progKey] || progKey;
        });
        viewProgramsEl.innerText = programNames.join(', ');
    } else {
        viewProgramsEl.innerText = 'Nenhum programa vinculado';
    }

    // CORREÇÃO 3: IMPORTANTE! Atribuir o ID ao input de edição para o botão funcionar
    document.getElementById('editId').value = patient.id;

    // Exibe o modal no modo visualização
    document.getElementById('viewMode').style.display = 'block';
    document.getElementById('editMode').style.display = 'none';
    document.getElementById('patientModal').classList.add('active');
}

function closeModal() {
    document.getElementById('patientModal').classList.remove('active');
}

function enableEditMode() {
    const id = document.getElementById('editId').value;
    const patient = currentPatients.find(p => String(p.id) === String(id));

    if (!patient) return;
    document.getElementById('editName').value = patient.name || '';
    document.getElementById('editCpf').value = patient.cpf ? applyCpfMask(patient.cpf) : '';
    document.getElementById('editCns').value = patient.cns ? applyCnsMask(patient.cns) : '';
    document.getElementById('editBirthDate').value = patient.birthDate ? patient.birthDate.split('T')[0] : '';

    const phonesToEdit = patient.phones ? patient.phones.join(', ') : '';
    document.getElementById('editPhones').value = applyMultiPhoneMask(phonesToEdit);

    document.getElementById('editExternal').checked = !!patient.external;
    document.getElementById('editStatus').checked = !!patient.status;

    const progCheckboxes = {
        'FARMACIA_BASICA': 'editProgFarmaciaBasica',
        'DIABETICO': 'editProgDiabeticos',
        'HIPERTENSAO': 'editProgHipertensao',
        'SAUDE_DA_MULHER': 'editProgSaudeMulher',
        'SAUDE_MENTAL': 'editProgSaudeMental'
    };

    Object.values(progCheckboxes).forEach(chkId => {
        const el = document.getElementById(chkId);
        if (el) el.checked = false;
    });

    if (patient.programs) {
        patient.programs.forEach(prog => {
            const checkboxId = progCheckboxes[prog.name];
            const el = document.getElementById(checkboxId);
            if (el) el.checked = true;
        });
    }

    document.getElementById('viewMode').style.display = 'none';
    document.getElementById('editMode').style.display = 'block';

    toggleProgramasEdit();
}

function cancelEditMode() {
    document.getElementById('viewMode').style.display = 'block';
    document.getElementById('editMode').style.display = 'none';
}

async function savePatientEdit() {
    const id = document.getElementById('editId').value;
    const nomeEdit = document.getElementById('editName').value.trim();
    const cpfEdit = document.getElementById('editCpf').value.trim();
    const cnsEdit = document.getElementById('editCns').value.trim();
    const birthDateEdit = document.getElementById('editBirthDate').value;

    if (!id) {
        alert("ID do paciente não encontrado.");
        return;
    }

    if (!nomeEdit || (!cpfEdit && !cnsEdit)) {
        alert("Erro: O Nome Completo e pelo menos um documento (CPF ou CNS) são obrigatórios.");
        return;
    }
    let birthDateISO = null;
    if (birthDateEdit) {
        const dataNascimento = new Date(birthDateEdit + 'T00:00:00');
        const hoje = new Date();
        hoje.setHours(0, 0, 0, 0);

        if (dataNascimento > hoje) {
            alert("Data inválida: A data de nascimento não pode estar no futuro!");
            return;
        }
        birthDateISO = new Date(birthDateEdit + 'T00:00:00').toISOString();
    }

    const isExternal = document.getElementById('editExternal').checked;
    const programsList = [];
    if (!isExternal) {
        const progMap = {
            'editProgFarmaciaBasica': 'FARMACIA_BASICA',
            'editProgDiabeticos': 'DIABETICO',
            'editProgHipertensao': 'HIPERTENSAO',
            'editProgSaudeMulher': 'SAUDE_DA_MULHER',
            'editProgSaudeMental': 'SAUDE_MENTAL'
        };
        Object.keys(progMap).forEach(key => {
            const el = document.getElementById(key);
            if (el && el.checked) programsList.push(progMap[key]);
        });
    }

    const phonesRaw = document.getElementById('editPhones').value;
    const phonesList = phonesRaw
        ? phonesRaw.split(',').map(n => n.replace(/\D/g, '')).filter(n => n.length > 0)
        : [];

    const payload = {
        name: nomeEdit,
        cpf: cpfEdit === "" ? null : cpfEdit.replace(/\D/g, ''),
        cns: cnsEdit === "" ? null : cnsEdit.replace(/\D/g, ''),
        birthDate: birthDateISO,
        external: isExternal,
        status: document.getElementById('editStatus').checked,
        phones: phonesList,
        programs: programsList
    };

    try {
        const response = await fetch(`${API_URL}/patients/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            alert('Prontuário atualizado com sucesso!');
            closeModal();
            searchPatients();
        } else {
            const errorData = await response.json();
            alert(errorData.message || 'Erro ao atualizar prontuário.');
        }
    } catch (error) {
        console.error(error);
        alert('Erro na comunicação com o servidor.');
    }
}

async function toggleStatus(id) {
    if (!confirm('Tem a certeza que deseja alterar o status deste paciente?')) return;

    try {
        const response = await fetch(`${API_URL}/patients/${id}/status`, { method: 'PATCH' });

        if (response.ok) {
            alert('Status atualizado com sucesso!');
            searchPatients();
        } else {
            alert('Erro ao alterar o status no servidor.');
        }
    } catch (e) {
        console.error(e);
    }
}

function formatPhone(phone) {
    if (!phone) return '';
    let numbers = phone.replace(/\D/g, '');

    if (numbers.length === 11) {
        return numbers.replace(/^(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
    } else if (numbers.length === 10) {
        return numbers.replace(/^(\d{2})(\d{4})(\d{4})/, '($1) $2-$3');
    } else {
        return phone;
    }
}

function applyCpfMask(value) {
    if (!value) return '';
    return value
        .replace(/\D/g, '')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{3})(\d{1,2})/, '$1-$2')
        .replace(/(-\d{2})\d+?$/, '$1');
}

function applyCnsMask(value) {
    if (!value) return '';
    return value
        .replace(/\D/g, '')
        .replace(/(\d{3})(\d)/, '$1.$2')
        .replace(/(\d{4})(\d)/, '$1.$2')
        .replace(/(\d{4})(\d{1,4})/, '$1.$2')
        .replace(/(\.\d{4})\d+?$/, '$1');
}

function applyMultiPhoneMask(value) {
    if (!value) return '';

    let parts = value.split(',');
    let maskedParts = parts.map(part => {
        let nums = part.replace(/\D/g, '');
        if (nums.length === 0) return '';

        if (nums.length <= 2) return `(${nums}`;
        if (nums.length <= 6) return `(${nums.substring(0, 2)}) ${nums.substring(2)}`;
        if (nums.length <= 10) return `(${nums.substring(0, 2)}) ${nums.substring(2, 6)}-${nums.substring(6)}`;

        return `(${nums.substring(0, 2)}) ${nums.substring(2, 7)}-${nums.substring(7, 11)}`;
    });

    let result = maskedParts.filter(p => p !== '').join(', ');

    if (value.trim().endsWith(',')) {
        result += ', ';
    }

    return result;
}

const searchInputEl = document.getElementById('searchInput');
const suggestionsList = document.getElementById('patientPageSuggestions');
let floatingSearchTimeout;

if (searchInputEl) {
    searchInputEl.addEventListener('input', function (e) {
        let val = e.target.value;

        if (/^\d/.test(val)) {
            e.target.value = applyCpfMask(val);
            val = e.target.value;
        }

        clearTimeout(floatingSearchTimeout);

        if (val.length >= 3) {
            floatingSearchTimeout = setTimeout(() => fetchFloatingSuggestions(val), 400);
        } else {
            if (suggestionsList) suggestionsList.style.display = 'none';
        }
    });
}

async function fetchFloatingSuggestions(query) {
    if (!suggestionsList) return;

    const statusFilter = document.getElementById('statusFilter');
    const statusFiltro = statusFilter ? statusFilter.value : 'ativos';

    try {
        const response = await fetch(`${API_URL}/patients?query=${encodeURIComponent(query)}&status=${statusFiltro}`);

        if (response.ok) {
            const patients = await response.json();
            suggestionsList.innerHTML = '';

            if (patients.length === 0) {
                suggestionsList.innerHTML = '<li style="padding: 12px 15px; color: #ef4444; font-size: 14px;">Nenhum paciente encontrado.</li>';
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
                        document.getElementById('searchInput').value = p.name;
                        suggestionsList.style.display = 'none';

                        currentPatients = [p];
                        renderTable();
                    };
                    suggestionsList.appendChild(li);
                });
            }
            suggestionsList.style.display = 'block';
        }
    } catch (error) {
        console.error("Erro na busca flutuante:", error);
    }
}
document.addEventListener('click', function (e) {
    if (e.target.id !== 'searchInput') {
        if (suggestionsList) suggestionsList.style.display = 'none';
    }
});

function formatPatientAge(birthDateString) {
    if (!birthDateString) return '-';

    const birth = new Date(birthDateString.split('T')[0] + 'T00:00:00');
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    let years = today.getFullYear() - birth.getFullYear();
    let months = today.getMonth() - birth.getMonth();

    if (today.getDate() < birth.getDate()) {
        months--;
    }

    if (months < 0) {
        years--;
        months += 12;
    }

    if (years >= 1) {
        return `${years} ano${years > 1 ? 's' : ''}`;
    } else if (months >= 1) {
        return `${months} ${months > 1 ? 'meses' : 'mês'}`;
    } else {
        const diffTime = Math.abs(today - birth);
        const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
        return `${diffDays} dia${diffDays !== 1 ? 's' : ''}`;
    }
}

window.toggleProgramasCadastro = toggleProgramasCadastro;
window.toggleProgramasEdit = toggleProgramasEdit;