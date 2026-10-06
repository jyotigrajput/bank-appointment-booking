const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {})
    },
    ...options
  });

  const contentType = response.headers.get('content-type') || '';
  const payload = contentType.includes('application/json') ? await response.json() : null;

  if (!response.ok) {
    throw new Error(payload?.message || 'Request failed');
  }

  return payload;
}

export const api = {
  getServices: () => request('/services'),
  getBranches: () => request('/branches'),
  getAvailability: ({ branchId, serviceId, date }) => request(`/availability?branchId=${branchId}&serviceId=${serviceId}&date=${date}`),
  createAppointment: (appointment) => request('/appointments', {
    method: 'POST',
    body: JSON.stringify(appointment)
  }),
  searchAppointments: ({ email, phone }) => {
    const params = new URLSearchParams();
    if (email) params.append('email', email);
    if (phone) params.append('phone', phone);
    return request(`/appointments/search?${params.toString()}`);
  },
  cancelAppointment: (id) => request(`/appointments/${id}/cancel`, { method: 'PATCH' })
};
