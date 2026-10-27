(function () {
    'use strict';

    document.addEventListener('click', function (event) {
        var target = event.target instanceof Element
            ? event.target.closest('[data-confirm-message]')
            : null;
        if (!target || target instanceof HTMLFormElement) return;
        if (!window.confirm(target.getAttribute('data-confirm-message'))) {
            event.preventDefault();
        }
    });

    document.addEventListener('submit', function (event) {
        var form = event.target;
        if (!(form instanceof HTMLFormElement) || !form.hasAttribute('data-confirm-message')) return;
        if (!window.confirm(form.getAttribute('data-confirm-message'))) {
            event.preventDefault();
        }
    });

    document.addEventListener('error', function (event) {
        var image = event.target;
        if (!(image instanceof HTMLImageElement)) return;

        var fallbackSource = image.getAttribute('data-fallback-src');
        if (fallbackSource) {
            image.removeAttribute('data-fallback-src');
            image.src = fallbackSource;
            return;
        }

        var errorAction = image.getAttribute('data-image-error');
        if (errorAction === 'hide') {
            image.hidden = true;
        } else if (errorAction === 'show-sibling') {
            image.hidden = true;
            if (image.nextElementSibling) image.nextElementSibling.style.display = 'flex';
        }
    }, true);
}());
