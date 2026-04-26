(function () {
    'use strict';

    if (typeof Chart === 'undefined' || !window.SCS_CHART_DATA) return;
    var D = window.SCS_CHART_DATA;

    var PALETTE = [
        '#2563eb', '#0ea5e9', '#16a34a', '#d97706', '#dc2626',
        '#7c3aed', '#db2777', '#0891b2', '#65a30d', '#f59e0b'
    ];

    var PRIORITY_COLORS = {
        LOW:      '#94a3b8',
        MEDIUM:   '#3b82f6',
        HIGH:     '#f97316',
        CRITICAL: '#dc2626'
    };
    var STATUS_COLORS = {
        PENDING:     '#94a3b8',
        ASSIGNED:    '#3b82f6',
        IN_PROGRESS: '#d97706',
        RESOLVED:    '#16a34a',
        CLOSED:      '#64748b'
    };

    function applyCanvasHeight(el, fallbackHeight) {
        if (!el) return;
        var raw = parseInt(el.getAttribute('height'), 10);
        var height = Number.isFinite(raw) && raw > 0 ? raw : fallbackHeight;
        if (height > 0) {
            // Keep responsive width while pinning a predictable drawing height.
            el.style.height = height + 'px';
        }
    }

    function renderLine(id, labels, series, label) {
        var el = document.getElementById(id);
        if (!el) return;
        applyCanvasHeight(el, 120);
        new Chart(el, {
            type: 'line',
            data: {
                labels: labels || [],
                datasets: [{
                    label: label || 'Complaints',
                    data: series || [],
                    borderColor: '#2563eb',
                    backgroundColor: 'rgba(37, 99, 235, 0.12)',
                    fill: true,
                    tension: 0.3,
                    pointRadius: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: {
                    y: { beginAtZero: true, ticks: { precision: 0 } }
                }
            }
        });
    }

    function renderDoughnut(id, map, colorLookup) {
        var el = document.getElementById(id);
        if (!el || !map) return;
        applyCanvasHeight(el, 160);
        var labels = Object.keys(map);
        var values = labels.map(function (k) { return map[k]; });
        var colors = labels.map(function (k, i) {
            return (colorLookup && colorLookup[k]) || PALETTE[i % PALETTE.length];
        });
        new Chart(el, {
            type: 'doughnut',
            data: { labels: labels, datasets: [{ data: values, backgroundColor: colors }] },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { position: 'bottom' } }
            }
        });
    }

    function renderBar(id, map, label, horizontal) {
        var el = document.getElementById(id);
        if (!el || !map) return;
        applyCanvasHeight(el, 160);
        var labels = Object.keys(map);
        var values = labels.map(function (k) { return map[k]; });
        var colors = labels.map(function (_, i) { return PALETTE[i % PALETTE.length]; });
        new Chart(el, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: label || 'Count',
                    data: values,
                    backgroundColor: colors
                }]
            },
            options: {
                indexAxis: horizontal ? 'y' : 'x',
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: {
                    x: { beginAtZero: true, ticks: { precision: 0 } },
                    y: { beginAtZero: true, ticks: { precision: 0 } }
                }
            }
        });
    }

    renderLine('trendChart', D.trendLabels, D.trendSeries, 'Complaints');
    renderDoughnut('priorityChart', D.priorities, PRIORITY_COLORS);
    renderDoughnut('statusChart',   D.statuses,   STATUS_COLORS);
    renderBar('departmentChart',    D.departments, 'By Department', false);
    renderBar('locationChart',      D.locations,   'By Location', true);
})();
