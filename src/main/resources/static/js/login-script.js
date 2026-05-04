const API_URL = 'http://localhost:8080';
const loginInput = document.getElementById('loginDocument');
const passwordInput = document.getElementById('password');
const loginForm = document.getElementById('loginForm');
const errorMessage = document.getElementById('errorMessage');

loginInput.addEventListener('input', function (e) {
    let value = e.target.value;

    const hasLetters = /[a-zA-Z]/.test(value);

    if (!hasLetters) {
        value = value.replace(/\D/g, '');

        if (value.length > 11) {
            value = value.slice(0, 11);
        }

        if (value.length > 9) {
            value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
        } else if (value.length > 6) {
            value = value.replace(/(\d{3})(\d{3})(\d{1,3})/, "$1.$2.$3");
        } else if (value.length > 3) {
            value = value.replace(/(\d{3})(\d{1,3})/, "$1.$2");
        }
    } else {
        value = value.toUpperCase();
    }
    e.target.value = value;
});

loginForm.addEventListener('submit', async function (e) {
    e.preventDefault();
    errorMessage.style.display = 'none';

    let rawLogin = loginInput.value;
    const password = passwordInput.value;

    const hasLetters = /[a-zA-Z]/.test(rawLogin);
    if (!hasLetters) {
        rawLogin = rawLogin.replace(/\D/g, '');
    }

    const payload = {
        login: rawLogin,
        password: password
    };

    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const data = await response.json();

            // Agora o data.id existe porque mudamos o Record no Java!
            localStorage.setItem('sgdm_userName', data.name);
            localStorage.setItem('sgdm_employeeId', data.id);

            window.location.href = '/home-screen.html';
        }
    } catch (error) {
        console.error("Erro de conexão:", error);
        errorMessage.innerText = 'Erro de conexão. Tente novamente.';
        errorMessage.style.display = 'block';
    }
});

