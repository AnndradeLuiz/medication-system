const loginInput = document.getElementById('loginDocument');
const passwordInput = document.getElementById('password');
const loginForm = document.getElementById('loginForm');
const errorMessage = document.getElementById('errorMessage');

// Lógica de Máscara Inteligente (CPF ou Matrícula)
loginInput.addEventListener('input', function(e) {
    let value = e.target.value;

    // Se o usuário digitou apenas números, assumimos que é CPF e aplicamos a máscara
    if (/^\d+$/.test(value.replace(/\D/g, ''))) {
        value = value.replace(/\D/g, ''); // Remove tudo que não é número

        if (value.length > 11) {
            value = value.slice(0, 11); // Limita a 11 dígitos
        }

        // Aplica a máscara 000.000.000-00
        if (value.length > 9) {
            value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
        } else if (value.length > 6) {
            value = value.replace(/(\d{3})(\d{3})(\d{1,3})/, "$1.$2.$3");
        } else if (value.length > 3) {
            value = value.replace(/(\d{3})(\d{1,3})/, "$1.$2");
        }
    }
    // Se tiver letras, deixamos como está (assumindo que é Matrícula, ex: MAT-2026)

    e.target.value = value;
});

loginForm.addEventListener('submit', function(e) {
    e.preventDefault(); // Evita que a página recarregue

    // Esconde a mensagem de erro
    errorMessage.style.display = 'none';

    console.log("Simulando login para a apresentação...");

    // Salva o nome do usuário no navegador para usarmos na próxima tela
    localStorage.setItem('sgdmi_userName', 'Luiz Andrade');

    // Pula direto para a tela principal!
    window.location.href = '/dashboard.html';
});