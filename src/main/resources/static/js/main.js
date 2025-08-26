/**
 * Функция для декодирования JWT токена, чтобы получить из него данные (например, роли).
 * @param {string} token - JWT токен.
 * @returns {object|null} - Объект с данными из токена или null в случае ошибки.
 */
function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    } catch (e) {
        return null;
    }
}

/**
 * Эта функция выполняется после полной загрузки страницы.
 * Она проверяет наличие токена и обновляет шапку сайта.
 */
document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('jwt_token');
    const navLinks = document.getElementById('nav-links');

    if (token) {
        // Если токен есть, пользователь авторизован
        let navHtml = `
            <a href="/cart" class="btn btn-outline-light me-2">Корзина</a>
        `;

        // Декодируем токен, чтобы проверить роль пользователя
        const decodedToken = parseJwt(token);
        if (decodedToken && decodedToken.roles && decodedToken.roles.includes('ROLE_ADMIN')) {
            // Если роль - ADMIN, добавляем кнопку для создания товара
            navHtml += `<a href="/add-product" class="btn btn-outline-warning me-2">Добавить товар</a>`;
        }

        // В конце добавляем кнопку "Выйти"
        navHtml += `<button onclick="logout()" class="btn btn-light">Выйти</button>`;

        navLinks.innerHTML = navHtml;

    } else {
        // Если токена нет, показываем кнопки для входа и регистрации
        navLinks.innerHTML = `
            <a href="/login" class="btn btn-outline-light me-2">Вход</a>
            <a href="/register" class="btn btn-light">Регистрация</a>
        `;
    }
});

/**
 * Функция для выхода из системы.
 * Удаляет токен и перенаправляет на страницу входа.
 */
function logout() {
    localStorage.removeItem('jwt_token');
    window.location.href = '/login';
}