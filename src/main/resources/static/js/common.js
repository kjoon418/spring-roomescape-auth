document.addEventListener('DOMContentLoaded', function () {
    var btn = document.getElementById('_errModalBtn');
    if (btn) {
        btn.addEventListener('click', function () {
            document.getElementById('_errModal').style.display = 'none';
        });
    }
});

function showErrorModal(message) {
    var modal = document.getElementById('_errModal');
    document.getElementById('_errModalMsg').textContent = message;
    modal.style.display = 'flex';
}

/* ── Auth Helpers ──────────────────────────────────────── */

function getCurrentUser() {
    try {
        const s = sessionStorage.getItem('currentUser');
        return s ? JSON.parse(s) : null;
    } catch (_) {
        return null;
    }
}

function getSelectedShop() {
    try {
        const s = sessionStorage.getItem('selectedShop');
        return s ? JSON.parse(s) : null;
    } catch (_) {
        return null;
    }
}

function setSelectedShop(id, name) {
    sessionStorage.setItem('selectedShop', JSON.stringify({ id, name }));
}

/**
 * Redirect to /view if the current user does not have one of the specified roles.
 * @param {...string} roles - allowed role strings e.g. 'ADMIN', 'MANAGER', 'MEMBER'
 * @returns {boolean} true if allowed, false if redirected
 */
function requireRole(...roles) {
    const user = getCurrentUser();
    if (!user || !roles.includes(user.role)) {
        location.href = '/view';
        return false;
    }
    return true;
}

/**
 * Redirect to /view/user if no shop has been selected yet.
 * @returns {object|false} selectedShop or false if redirected
 */
function requireShop() {
    const shop = getSelectedShop();
    if (!shop) {
        location.href = '/view/user';
        return false;
    }
    return shop;
}

/**
 * For admin sub-pages: redirect to /view/admin if no shop selected.
 * @returns {object|false} selectedShop or false if redirected
 */
function requireAdminShop() {
    if (!requireRole('ADMIN')) return false;
    const shop = getSelectedShop();
    if (!shop) {
        location.href = '/view/admin';
        return false;
    }
    return shop;
}

/* ── Nav Rendering ─────────────────────────────────────── */

document.addEventListener('DOMContentLoaded', function () {
    const nav = document.querySelector('.site-header nav');
    if (!nav) return;

    const user = getCurrentUser();
    const role = user ? user.role : null;

    // Build links based on role
    const links = [{ href: '/view', label: '웰컴' }];

    if (role === 'ADMIN') {
        links.push({ href: '/view/admin', label: '관리자' });
        links.push({ href: '/view/user', label: '사용자' });
    } else if (role === 'MANAGER') {
        links.push({ href: '/view/manager', label: '매니저' });
        links.push({ href: '/view/user', label: '사용자' });
    } else if (role === 'MEMBER') {
        links.push({ href: '/view/user', label: '사용자' });
    }

    nav.innerHTML = links
        .map(l => `<a href="${l.href}">${l.label}</a>`)
        .join('');
});
