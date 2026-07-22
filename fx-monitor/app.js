/* fx-monitor/app.js
 * Polls /health, /api/rates, and /api/admin/accepting every 5 s.
 * All requests go to the same origin (served by nginx) which proxies
 * them upstream — the browser never talks directly to the API.
 */

let pollCount = 0;

function setHealth(up) {
  const dot  = document.getElementById('health-dot');
  const text = document.getElementById('health-text');
  dot.className = 'dot ' + (up ? 'green' : 'red');
  text.textContent = up ? 'healthy' : 'unhealthy';
}

function renderRates(rates) {
  const tbody = document.getElementById('rates-body');
  if (!rates || rates.length === 0) {
    tbody.innerHTML = '<tr><td colspan="3" id="empty-msg">No rates found</td></tr>';
    return;
  }
  tbody.innerHTML = rates.map(r => `
    <tr>
      <td>${r.baseCode}/${r.quoteCode}</td>
      <td class="rate">${r.rate.toFixed(4)}</td>
      <td class="date">${r.rateDate}</td>
    </tr>
  `).join('');
}

function pollHealth() {
  fetch('/health')
    .then(r => setHealth(r.ok))
    .catch(() => setHealth(false));
}

function pollRates() {
  fetch('/api/rates')
    .then(r => r.ok ? r.json() : Promise.reject(r.status))
    .then(data => renderRates(data))
    .catch(() => {});
}

function pollAccepting() {
  const btn    = document.getElementById('toggle-btn');
  const banner = document.getElementById('accepting-banner');

  fetch('/api/admin/accepting')
    .then(r => {
      if (r.status === 404) {
        banner.textContent = 'ACCEPTING endpoint not yet available (404) — build it in Task 5';
        btn.disabled = true;
        return null;
      }
      return r.ok ? r.json() : Promise.reject(r.status);
    })
    .then(data => {
      if (data === null) return;
      banner.textContent = '';
      btn.disabled = false;
      btn.textContent = data.accepting ? 'ACCEPTING ✓' : 'NOT ACCEPTING ✗';
      btn.style.background = data.accepting ? '#166534' : '#7f1d1d';
      btn.style.color = '#f8fafc';
    })
    .catch(() => {
      banner.textContent = 'Could not reach accepting endpoint';
      btn.disabled = true;
    });
}

function poll() {
  pollCount++;
  document.getElementById('poll-count').textContent = `polls: ${pollCount}`;
  pollHealth();
  pollRates();
  pollAccepting();
}

poll();
setInterval(poll, 5000);
