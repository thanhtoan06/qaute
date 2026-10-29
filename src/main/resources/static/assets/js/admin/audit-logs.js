document.addEventListener('DOMContentLoaded',
  // Chuẩn bị hộp thoại hiển thị chi tiết nhật ký kiểm toán.
  () => {
    const modal = document.getElementById('audit-detail-modal');
    if (!modal) return;
    const fields = {
      at: document.getElementById('audit-detail-at'),
      actor: document.getElementById('audit-detail-actor'),
      action: document.getElementById('audit-detail-action'),
      entity: document.getElementById('audit-detail-entity'),
      client: document.getElementById('audit-detail-client'),
      trace: document.getElementById('audit-detail-trace'),
      before: document.getElementById('audit-detail-before'),
      after: document.getElementById('audit-detail-after')
    };
    const error = document.getElementById('audit-detail-error');
    modal.addEventListener('show.bs.modal',
      // Tải và hiển thị chi tiết nhật ký khi mở hộp thoại.
      async (event) => {
      Object.values(fields).forEach((element) => { element.textContent = 'Đang tải…'; });
      error.classList.add('d-none');
      try {
        const response = await fetch(event.relatedTarget.dataset.url, { credentials: 'same-origin' });
        if (!response.ok) throw new Error('Không tải được chi tiết nhật ký.');
        const item = await response.json();
        fields.at.textContent = item.createdAt ? Qaute.formatDateTime(item.createdAt) : '—';
        fields.actor.textContent = [item.actorName, item.actorRole].filter(Boolean).join(' · ');
        fields.action.textContent = [item.actionLabel, item.action].filter(Boolean).join(' · ');
        fields.entity.textContent = [item.entityType, item.entityId].filter((part) => part !== null && part !== undefined).join(' #');
        fields.client.textContent = [item.ip, item.userAgent].filter(Boolean).join(' · ');
        fields.trace.textContent = item.traceId || '—';
        fields.before.textContent = item.before || '—';
        fields.after.textContent = item.after || '—';
      } catch (failure) {
        Object.values(fields).forEach((element) => { element.textContent = ''; });
        error.textContent = failure.message;
        error.classList.remove('d-none');
      }
    });
});
