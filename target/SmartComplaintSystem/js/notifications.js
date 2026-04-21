(function () {
    'use strict';

    var POLL_INTERVAL_MS = 30000;
    var ctx = deriveContextPath();

    function deriveContextPath() {
        var path = window.location.pathname;
        var match = path.match(/^(\/[^/]+)?\//);
        return ''; // default: root context (Jetty in dev uses "/")
    }

    function fetchNotifications() {
        fetch(ctx + '/api/notifications?unread=true', { credentials: 'same-origin' })
            .then(function (r) { return r.ok ? r.json() : null; })
            .then(function (data) {
                if (!data) return;
                updateBadge(data.unreadCount || 0);
            })
            .catch(function () { /* silent */ });
    }

    function updateBadge(count) {
        var badge = document.getElementById('notificationBadge');
        if (!badge) return;
        if (count > 0) {
            badge.textContent = count > 99 ? '99+' : String(count);
            badge.classList.remove('d-none');
        } else {
            badge.classList.add('d-none');
        }
    }

    document.addEventListener('click', function (e) {
        var trigger = e.target.closest('[data-mark-read]');
        if (!trigger) return;
        var id = trigger.getAttribute('data-mark-read');
        if (!id) return;
        var body = new URLSearchParams();
        body.append('id', id);
        fetch(ctx + '/api/notifications', {
            method: 'POST',
            credentials: 'same-origin',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: body.toString()
        }).then(fetchNotifications);
    });

    fetchNotifications();
    setInterval(fetchNotifications, POLL_INTERVAL_MS);
})();
