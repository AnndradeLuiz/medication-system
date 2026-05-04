const API_URL = 'http://localhost:8080';

// Limites para o cálculo manual no Front-end
const LIMITE_CRITICO_MEDICAMENTO = 50;
const LIMITE_CRITICO_INSUMO = 100;

document.addEventListener("DOMContentLoaded", () => {
    // 1. Configura o Usuário
    const userName = localStorage.getItem('sgdm_userName') || 'Luiz Andrade';
    const loggedUserEl = document.getElementById('loggedUser');
    if (loggedUserEl) loggedUserEl.innerText = userName;

    loadDashboardMetrics();
    loadCriticalStock();
});

async function loadDashboardMetrics() {
    const elements = {
        topItem: document.getElementById('metricTopItem'),
        dispensations: document.getElementById('metricDispensations'),
        lowStock: document.getElementById('metricLowStock'),
        expiring: document.getElementById('metricExpiring')
    };

    try {
        const response = await fetch(`${API_URL}/dashboard/metrics`);
        if (response.ok) {
            const data = await response.json();
            elements.topItem.innerText = data.mostDispensedItem || 'Nenhum';
            elements.dispensations.innerText = data.dispensationsToday || 0;
            elements.lowStock.innerText = data.lowStockCount || 0;
            elements.expiring.innerText = data.expiringBatchesCount || 0;
        }
    } catch (error) {
        console.error("Erro ao buscar métricas:", error);
    }
}

// --- TABELA DE ESTOQUE CRÍTICO (CORRIGIDA) ---
async function loadCriticalStock() {
    const tbody = document.getElementById('criticalStockBody');
    if (!tbody) return;

    try {
        const [medRes, supRes] = await Promise.all([
            fetch(`${API_URL}/medications`)
        ]);

        let medications = medRes.ok ? await medRes.json() : [];

        let criticalItems = [];

        medications.forEach(med => {
            let totalStock = 0;
            if (med.lots) {
                totalStock = med.lots.reduce((acc, lot) => acc + (lot.currentQuantity || 0), 0);
            }

            if (totalStock <= LIMITE_CRITICO_MEDICAMENTO) {
                criticalItems.push({
                    type: 'Medicamento',
                    icon: '<i class="fa-solid fa-pills" style="color: #3b82f6;"></i>',
                    // USANDO O CAMPO CORRETO: activeIngredient
                    name: `${med.activeIngredient} (${med.concentration})`,
                    stock: totalStock
                });
            }
        });

        criticalItems.sort((a, b) => a.stock - b.stock);
        renderCriticalTable(criticalItems);

    } catch (error) {
        console.error("Erro ao carregar estoque crítico:", error);
        tbody.innerHTML = `<tr><td colspan="4" style="text-align: center; color: #ef4444;">Erro de conexão.</td></tr>`;
    }
}

function renderCriticalTable(items) {
    const tbody = document.getElementById('criticalStockBody');
    tbody.innerHTML = '';

    if (items.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4" style="text-align: center; padding: 20px; color: #10b981;">Estoque saudável!</td></tr>`;
        return;
    }

    items.forEach(item => {
        const isZero = item.stock === 0;
        const statusBadge = isZero
            ? `<span class="status-badge status-danger">Esgotado</span>`
            : `<span class="status-badge status-warning">Baixo</span>`;

        tbody.innerHTML += `
            <tr>
                <td><span style="display: flex; align-items: center; gap: 8px;">${item.icon} ${item.type}</span></td>
                <td style="font-weight: 600;">${item.name}</td>
                <td style="text-align: center; font-weight: 700; color: ${isZero ? '#ef4444' : '#d97706'};">${item.stock} un</td>
                <td style="text-align: center;">${statusBadge}</td>
            </tr>
        `;
    });
}