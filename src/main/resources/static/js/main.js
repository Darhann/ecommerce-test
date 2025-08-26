document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('jwt_token');
    const navLinks = document.getElementById('nav-links');

    if (token) {
        navLinks.innerHTML = `
            <a href="/cart" class="btn btn-outline-light me-2">Корзина</a>
            <button onclick="logout()" class="btn btn-light">Выйти</button>
        `;
    } else {
        navLinks.innerHTML = `
            <a href="/login" class="btn btn-outline-light me-2">Вход</a>
            <a href="/register" class="btn btn-light">Регистрация</a>
        `;
    }
});

function logout() {
    localStorage.removeItem('jwt_token');
    window.location.href = '/login';
}