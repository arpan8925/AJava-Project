(function () {
    'use strict';

    var btn       = document.getElementById('voiceBtn');
    var btnLabel  = document.getElementById('voiceBtnLabel');
    var statusEl  = document.getElementById('voiceStatus');
    var textarea  = document.getElementById('description');

    if (!btn || !textarea) return;

    var SR = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (!SR) {
        btn.disabled = true;
        if (statusEl) {
            statusEl.innerHTML = '<span class="text-muted">Voice input is not supported in this browser. ' +
                                 'Use Chrome or Edge, or type your complaint manually.</span>';
        }
        return;
    }

    var recognition = new SR();
    recognition.lang           = 'en-US';
    recognition.continuous     = true;
    recognition.interimResults = true;

    var listening = false;
    var baseline  = '';

    recognition.onresult = function (event) {
        var finalText  = '';
        var interimText = '';
        for (var i = event.resultIndex; i < event.results.length; i++) {
            var transcript = event.results[i][0].transcript;
            if (event.results[i].isFinal) finalText += transcript + ' ';
            else interimText += transcript;
        }
        if (finalText) {
            baseline = (baseline + ' ' + finalText).replace(/\s+/g, ' ').trim();
        }
        textarea.value = (baseline + ' ' + interimText).trim();
        textarea.dispatchEvent(new Event('input', { bubbles: true }));
    };

    recognition.onerror = function (e) {
        if (statusEl) statusEl.textContent = 'Voice error: ' + e.error;
        setListening(false);
    };

    recognition.onend = function () {
        if (listening) {
            // browser stopped us; restart to keep session alive
            try { recognition.start(); } catch (err) { setListening(false); }
        } else {
            setListening(false);
        }
    };

    btn.addEventListener('click', function () {
        if (listening) {
            listening = false;
            recognition.stop();
        } else {
            baseline = textarea.value;
            try {
                recognition.start();
                setListening(true);
                if (statusEl) statusEl.textContent = 'Listening… click again to stop.';
            } catch (err) {
                if (statusEl) statusEl.textContent = 'Could not start voice input: ' + err.message;
            }
        }
    });

    function setListening(v) {
        listening = v;
        btn.classList.toggle('btn-outline-primary', !v);
        btn.classList.toggle('btn-danger', v);
        if (btnLabel) btnLabel.textContent = v ? 'Stop' : 'Use Voice';
        if (!v && statusEl) statusEl.textContent = '';
    }
})();
