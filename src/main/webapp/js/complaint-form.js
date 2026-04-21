(function () {
    'use strict';

    var form          = document.getElementById('complaintForm');
    if (!form) return;

    var title         = document.getElementById('title');
    var description   = document.getElementById('description');
    var image         = document.getElementById('image');
    var preview       = document.getElementById('imagePreview');
    var smartPreview  = document.getElementById('smartPreview');
    var emergencyBox  = document.getElementById('emergencyWarning');

    var sentimentEl       = document.getElementById('previewSentiment');
    var priorityEl        = document.getElementById('previewPriority');
    var emergencyEl       = document.getElementById('previewEmergency');
    var tagsEl            = document.getElementById('previewTags');
    var recommendationsEl = document.getElementById('previewRecommendations');

    var ctx = (document.querySelector('base[href]') && document.querySelector('base[href]').href) || '';
    if (!ctx) {
        // derive from form action
        var action = form.getAttribute('action') || '';
        var idx = action.indexOf('/user/submit-complaint');
        ctx = idx >= 0 ? action.substring(0, idx) : '';
    }

    if (image && preview) {
        image.addEventListener('change', function () {
            var file = image.files && image.files[0];
            if (!file) { preview.classList.add('d-none'); preview.src = ''; return; }
            var reader = new FileReader();
            reader.onload = function (e) {
                preview.src = e.target.result;
                preview.classList.remove('d-none');
            };
            reader.readAsDataURL(file);
        });
    }

    var suggestDebounce;
    function scheduleSuggest() {
        clearTimeout(suggestDebounce);
        suggestDebounce = setTimeout(fetchSuggest, 450);
    }

    function fetchSuggest() {
        var t = (title.value || '').trim();
        var d = (description.value || '').trim();
        if ((t + ' ' + d).trim().length < 8) {
            if (smartPreview) smartPreview.classList.add('d-none');
            if (emergencyBox) emergencyBox.classList.add('d-none');
            return;
        }
        var qs = 'title=' + encodeURIComponent(t) +
                 '&description=' + encodeURIComponent(d);
        fetch(ctx + '/api/suggest?' + qs, { credentials: 'same-origin' })
            .then(function (r) { return r.ok ? r.json() : null; })
            .then(function (data) {
                if (!data) return;
                renderPreview(data);
            })
            .catch(function () { /* silent */ });
    }

    function renderPreview(data) {
        if (!smartPreview) return;
        smartPreview.classList.remove('d-none');
        sentimentEl.textContent = data.sentiment || '—';
        priorityEl.textContent  = data.priority || '—';
        emergencyEl.textContent = data.isEmergency ? 'YES' : 'no';
        emergencyEl.className = data.isEmergency ? 'fw-semibold text-danger' : 'fw-semibold text-success';

        tagsEl.innerHTML = '';
        (data.tags || []).forEach(function (t) {
            var span = document.createElement('span');
            span.className = 'badge bg-light text-dark border me-1';
            span.textContent = '#' + t;
            tagsEl.appendChild(span);
        });
        if ((data.tags || []).length === 0) tagsEl.textContent = '—';

        recommendationsEl.innerHTML = '';
        (data.recommendations || []).forEach(function (r) {
            var li = document.createElement('li');
            li.textContent = r;
            recommendationsEl.appendChild(li);
        });

        if (emergencyBox) emergencyBox.classList.toggle('d-none', !data.isEmergency);
    }

    if (title)       title.addEventListener('input', scheduleSuggest);
    if (description) description.addEventListener('input', scheduleSuggest);

    form.addEventListener('submit', function (e) {
        var t = (title.value || '').trim();
        var d = (description.value || '').trim();
        if (!t || !d) {
            e.preventDefault();
            alert('Title and description are required.');
            return;
        }
        var btn = form.querySelector('button[type="submit"]');
        if (btn) {
            btn.disabled = true;
            btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Submitting...';
        }
    });
})();
