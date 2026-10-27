/* ==========================================================
   EcoTrack Full-Stack Frontend Logic
   Microservices & Docker Architecture (Exam Task ID: 2400032862)
   ========================================================== */

// Base API configuration: Automatically detects Docker / Localhost environment
const BACKEND_PORT = 8080;
const API_BASE = window.location.port === `${BACKEND_PORT}` 
    ? '' 
    : (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1' 
        ? `http://${window.location.hostname}:${BACKEND_PORT}` 
        : '');

let allRecords = [];
let pendingDeleteId = null;

// Initialize on DOM Ready
document.addEventListener('DOMContentLoaded', () => {
    initFormValidation();
    initSearch();
    initStatusToggle();
    checkBackendHealth();
    loadRecords();

    // Auto-update Swagger link
    const swaggerEl = document.getElementById('swaggerLink');
    if (swaggerEl) {
        swaggerEl.href = `${API_BASE || 'http://localhost:8080'}/swagger-ui.html`;
    }
});

// ─── Health Check ──────────────────────────────────────────
async function checkBackendHealth() {
    const indicator = document.getElementById('apiStatusIndicator');
    const text = document.getElementById('apiStatusText');
    try {
        const res = await fetch(`${API_BASE}/`, { method: 'GET' });
        if (res.ok) {
            indicator.className = 'status-indicator bg-success rounded-circle';
            text.textContent = 'Backend Online';
        } else {
            throw new Error();
        }
    } catch {
        indicator.className = 'status-indicator bg-danger rounded-circle';
        text.textContent = 'Backend Offline';
    }
}

// ─── Form Validation & Submission (No ID Field) ───────────
function initFormValidation() {
    const form = document.getElementById('addZoneForm');

    form.addEventListener('submit', async (event) => {
        event.preventDefault();
        event.stopPropagation();

        const zoneNameInput = document.getElementById('zoneName');
        const collectionDayInput = document.getElementById('collectionDay');
        const truckCountInput = document.getElementById('truckCount');
        const activeStatusInput = document.getElementById('activeStatus');

        // Frontend validation
        let isValid = true;

        if (!zoneNameInput.value.trim() || zoneNameInput.value.trim().length < 2) {
            zoneNameInput.classList.add('is-invalid');
            isValid = false;
        } else {
            zoneNameInput.classList.remove('is-invalid');
            zoneNameInput.classList.add('is-valid');
        }

        if (!collectionDayInput.value) {
            collectionDayInput.classList.add('is-invalid');
            isValid = false;
        } else {
            collectionDayInput.classList.remove('is-invalid');
            collectionDayInput.classList.add('is-valid');
        }

        const truckCountVal = parseInt(truckCountInput.value, 10);
        if (isNaN(truckCountVal) || truckCountVal < 1 || truckCountVal > 50) {
            truckCountInput.classList.add('is-invalid');
            isValid = false;
        } else {
            truckCountInput.classList.remove('is-invalid');
            truckCountInput.classList.add('is-valid');
        }

        form.classList.add('was-validated');

        if (!isValid) {
            showToast('Please fix the validation errors in the form.', 'danger');
            return;
        }

        // Prepare Payload - strictly NO ID SENT, backend generates ID
        const payload = {
            zoneName: zoneNameInput.value.trim(),
            collectionDay: collectionDayInput.value,
            truckCount: truckCountVal,
            active: activeStatusInput.checked
        };

        const submitBtn = document.getElementById('submitBtn');
        const spinner = document.getElementById('submitSpinner');
        const icon = document.getElementById('submitIcon');

        submitBtn.disabled = true;
        spinner.classList.remove('d-none');
        icon.classList.add('d-none');

        try {
            const response = await fetch(`${API_BASE}/api/waste-collections`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => null);
                let errorMsg = 'Failed to create record.';
                if (errorData && errorData.validationErrors) {
                    errorMsg = Object.values(errorData.validationErrors).join(', ');
                } else if (errorData && errorData.message) {
                    errorMsg = errorData.message;
                }
                throw new Error(errorMsg);
            }

            const created = await response.json();
            showToast(`Zone "${created.zoneName}" added successfully with auto ID #${created.id}!`, 'success');
            resetForm();
            loadRecords();

            // Smooth scroll to records table
            document.getElementById('records-section').scrollIntoView({ behavior: 'smooth' });
        } catch (err) {
            showToast(err.message, 'danger');
        } finally {
            submitBtn.disabled = false;
            spinner.classList.add('d-none');
            icon.classList.remove('d-none');
        }
    });
}

function resetForm() {
    const form = document.getElementById('addZoneForm');
    form.reset();
    form.classList.remove('was-validated');
    form.querySelectorAll('.is-valid, .is-invalid').forEach(el => {
        el.classList.remove('is-valid', 'is-invalid');
    });
    document.getElementById('truckCount').value = '1';
    document.getElementById('activeStatus').checked = true;
    updateStatusText(true);
}

function initStatusToggle() {
    const toggle = document.getElementById('activeStatus');
    toggle.addEventListener('change', () => {
        updateStatusText(toggle.checked);
    });
}

function updateStatusText(isActive) {
    const label = document.getElementById('statusLabelText');
    label.textContent = isActive 
        ? 'Currently active in collection route' 
        : 'Suspended / Inactive route';
}

// ─── Fetch & Display Records ───────────────────────────────
async function loadRecords() {
    const loadingState = document.getElementById('loadingState');
    const emptyState = document.getElementById('emptyState');
    const tableContainer = document.getElementById('tableContainer');
    const tableBody = document.getElementById('recordsTableBody');

    loadingState.classList.remove('d-none');
    emptyState.classList.add('d-none');
    tableContainer.classList.add('d-none');

    try {
        const response = await fetch(`${API_BASE}/api/waste-collections`);
        if (!response.ok) throw new Error(`HTTP Error: ${response.status}`);

        const data = await response.json();
        allRecords = Array.isArray(data) ? data : [];

        updateStatistics(allRecords);
        renderTable(allRecords);
    } catch (err) {
        showToast('Unable to connect to backend service. Check if server is running.', 'danger');
        emptyState.classList.remove('d-none');
        updateStatistics([]);
    } finally {
        loadingState.classList.add('d-none');
    }
}

// ─── Render Table ──────────────────────────────────────────
function renderTable(records) {
    const emptyState = document.getElementById('emptyState');
    const tableContainer = document.getElementById('tableContainer');
    const tableBody = document.getElementById('recordsTableBody');

    if (records.length === 0) {
        emptyState.classList.remove('d-none');
        tableContainer.classList.add('d-none');
        tableBody.innerHTML = '';
        return;
    }

    emptyState.classList.add('d-none');
    tableContainer.classList.remove('d-none');

    tableBody.innerHTML = records.map(record => {
        const statusBadge = record.active
            ? '<span class="badge bg-success-subtle text-success border border-success-subtle rounded-pill px-2 py-1"><i class="bi bi-check-circle-fill me-1"></i>Active</span>'
            : '<span class="badge bg-secondary-subtle text-secondary border border-secondary-subtle rounded-pill px-2 py-1"><i class="bi bi-dash-circle me-1"></i>Inactive</span>';

        return `
            <tr>
                <td><span class="badge badge-zone-id px-2 py-1 rounded">#${record.id}</span></td>
                <td>
                    <div class="fw-bold text-dark">${escapeHtml(record.zoneName)}</div>
                    <small class="text-muted">ID: ${record.id}</small>
                </td>
                <td>
                    <span class="badge bg-light text-dark border px-2 py-1">
                        <i class="bi bi-calendar3 me-1 text-primary"></i>${escapeHtml(record.collectionDay)}
                    </span>
                </td>
                <td>
                    <span class="fw-semibold text-dark">${record.truckCount}</span>
                    <small class="text-muted">truck${record.truckCount > 1 ? 's' : ''}</small>
                </td>
                <td>${statusBadge}</td>
                <td class="text-end">
                    <button class="btn btn-outline-danger btn-sm rounded-pill px-3" onclick="openDeleteModal(${record.id}, '${escapeHtml(record.zoneName).replace(/'/g, "\\'")}')">
                        <i class="bi bi-trash3 me-1"></i> Delete
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}

// ─── Real-Time Search Filter ───────────────────────────────
function initSearch() {
    const searchInput = document.getElementById('searchInput');
    searchInput.addEventListener('input', (e) => {
        const query = e.target.value.toLowerCase().trim();
        if (!query) {
            renderTable(allRecords);
            return;
        }
        const filtered = allRecords.filter(r => 
            (r.zoneName && r.zoneName.toLowerCase().includes(query)) ||
            (r.collectionDay && r.collectionDay.toLowerCase().includes(query)) ||
            (r.id && r.id.toString().includes(query))
        );
        renderTable(filtered);
    });
}

// ─── Statistics Calculation ────────────────────────────────
function updateStatistics(records) {
    const totalZones = records.length;
    const totalTrucks = records.reduce((sum, r) => sum + (r.truckCount || 0), 0);
    const activeZones = records.filter(r => r.active).length;
    const inactiveZones = totalZones - activeZones;

    document.getElementById('statTotalZones').textContent = totalZones;
    document.getElementById('statTotalTrucks').textContent = totalTrucks;
    document.getElementById('statActiveZones').textContent = activeZones;
    document.getElementById('statInactiveZones').textContent = inactiveZones;
}

// ─── Delete Record Operations ──────────────────────────────
function openDeleteModal(id, zoneName) {
    pendingDeleteId = id;
    document.getElementById('deleteRecordId').textContent = `#${id}`;
    document.getElementById('deleteRecordZone').textContent = zoneName;

    const modal = new bootstrap.Modal(document.getElementById('deleteModal'));
    modal.show();
}

async function executeDelete() {
    if (!pendingDeleteId) return;

    const btn = document.getElementById('confirmDeleteBtn');
    const spinner = document.getElementById('deleteSpinner');
    const icon = document.getElementById('deleteIcon');

    btn.disabled = true;
    spinner.classList.remove('d-none');
    icon.classList.add('d-none');

    try {
        const res = await fetch(`${API_BASE}/api/waste-collections/${pendingDeleteId}`, {
            method: 'DELETE'
        });

        if (!res.ok) {
            throw new Error(`Failed to delete record (Status: ${res.status})`);
        }

        showToast(`Record #${pendingDeleteId} removed successfully.`, 'success');
        
        // Hide modal
        const modalEl = document.getElementById('deleteModal');
        const modal = bootstrap.Modal.getInstance(modalEl);
        if (modal) modal.hide();

        pendingDeleteId = null;
        loadRecords();
    } catch (err) {
        showToast(err.message, 'danger');
    } finally {
        btn.disabled = false;
        spinner.classList.add('d-none');
        icon.classList.remove('d-none');
    }
}

// ─── Toast Feedback Notification ───────────────────────────
function showToast(message, type = 'success') {
    const toastEl = document.getElementById('liveToast');
    const toastMsg = document.getElementById('toastMessage');
    const toastIcon = document.getElementById('toastIcon');

    toastEl.className = `toast align-items-center text-white border-0 rounded-3 shadow bg-${type}`;
    toastMsg.textContent = message;

    if (type === 'success') {
        toastIcon.className = 'bi bi-check-circle-fill fs-5 text-white';
    } else if (type === 'danger') {
        toastIcon.className = 'bi bi-exclamation-octagon-fill fs-5 text-white';
    } else {
        toastIcon.className = 'bi bi-info-circle-fill fs-5 text-white';
    }

    const toast = new bootstrap.Toast(toastEl, { delay: 4000 });
    toast.show();
}

// ─── Utility: HTML Escaping ────────────────────────────────
function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}
