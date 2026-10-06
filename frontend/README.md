* { box-sizing: border-box; }

body {
  margin: 0;
  font-family: Arial, Helvetica, sans-serif;
  background: #f4f7fb;
  color: #1f2937;
}

button, input, select {
  font: inherit;
}

button { cursor: pointer; }

.app-shell {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #0b3d91;
  color: white;
  border-radius: 12px;
  padding: 16px 20px;
  margin-bottom: 24px;
}

.brand {
  font-weight: 700;
  font-size: 1.25rem;
  cursor: pointer;
}

nav {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

nav button {
  background: transparent;
  border: 1px solid rgba(255,255,255,0.25);
  color: white;
  border-radius: 8px;
  padding: 8px 12px;
}

.page {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 18px rgba(0,0,0,0.06);
}

.hero {
  padding-top: 12px;
}

.hero h1 {
  font-size: 2.3rem;
  margin: 0 0 16px;
}

.eyebrow {
  color: #2563eb;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  font-size: 0.75rem;
  font-weight: 700;
  margin-bottom: 12px;
}

.hero-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  margin-top: 18px;
}

.primary, .secondary {
  border: none;
  border-radius: 10px;
  padding: 10px 16px;
  font-weight: 600;
}

.primary {
  background: #2563eb;
  color: white;
}

.secondary {
  background: #e2e8f0;
  color: #0f172a;
}

.card-section {
  margin-top: 28px;
}

.steps, .service-grid, .branch-grid, .appointment-list {
  display: grid;
  gap: 18px;
}

.steps {
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
}

.step-card, .mini-card, .branch-card, .service-item, .appointment-card {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 18px;
}

.step-card span {
  display: inline-flex;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  align-items: center;
  justify-content: center;
  background: #dbeafe;
  color: #1d4ed8;
  font-weight: 700;
}

.service-grid, .branch-grid {
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
}

.service-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;
}

.booking-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.booking-step {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.two-col {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 18px;
}

input, select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
}

.slot-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  gap: 12px;
}

.slot {
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  background: white;
  padding: 10px;
}

.slot.selected {
  background: #dcfce7;
  border-color: #22c55e;
}

.slot:disabled {
  background: #f1f5f9;
  color: #94a3b8;
  cursor: not-allowed;
}

.summary-box {
  background: #f0f9ff;
  border: 1px solid #bae6fd;
  border-radius: 12px;
  padding: 18px;
}

.alert {
  padding: 12px 14px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.alert.error {
  background: #fee2e2;
  color: #991b1b;
}

.search-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 24px;
}

.confirmation-page {
  text-align: center;
}

@media (max-width: 768px) {
  .topbar {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }

  .service-item {
    flex-direction: column;
    align-items: flex-start;
  }
}
