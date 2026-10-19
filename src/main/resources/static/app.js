const state = {
  trucks: [],
  selectedTruckId: null,
  cart: new Map(),
  sellerTruckId: null,
  latitude: 30.2672,
  longitude: -97.7431,
  toastTimer: null
};

const truckList = document.querySelector("#truck-list");
const cartContent = document.querySelector("#cart-content");
const cartFooter = document.querySelector("#cart-footer");
const toast = document.querySelector("#toast");

function escapeHtml(value) {
  return String(value ?? "").replace(/[&<>"']/g, character => ({
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': "&quot;",
    "'": "&#39;"
  })[character]);
}

function money(value) {
  return new Intl.NumberFormat("en-US", { style: "currency", currency: "USD" }).format(value);
}

async function api(path, options = {}) {
  const response = await fetch(path, {
    ...options,
    headers: { "Content-Type": "application/json", ...options.headers }
  });
  if (!response.ok) {
    let message = `Request failed (${response.status}).`;
    try {
      const body = await response.json();
      message = body.message || body.detail || message;
    } catch {
      // Keep the useful HTTP status if the response is not JSON.
    }
    throw new Error(message);
  }
  return response.status === 204 ? null : response.json();
}

function showToast(message, isError = false) {
  window.clearTimeout(state.toastTimer);
  toast.textContent = message;
  toast.classList.toggle("error", isError);
  toast.classList.add("show");
  state.toastTimer = window.setTimeout(() => toast.classList.remove("show"), 3600);
}

function distanceLabel(distanceKm) {
  if (distanceKm == null) return "";
  const miles = distanceKm * 0.621371;
  return miles < 0.1 ? "Less than 0.1 mi" : `${miles.toFixed(1)} mi away`;
}

async function loadTrucks() {
  const params = new URLSearchParams({
    latitude: String(state.latitude),
    longitude: String(state.longitude)
  });
  truckList.innerHTML = '<div class="loading-card">Finding your neighborhood\'s good stuff…</div>';
  try {
    state.trucks = await api(`/api/trucks?${params}`);
    if (!state.trucks.some(truck => truck.id === state.selectedTruckId)) {
      state.selectedTruckId = state.trucks[0]?.id ?? null;
      state.cart.clear();
    }
    renderTrucks();
    renderCart();
    renderSellerTruckOptions();
    renderSellerMenu();
    await loadOrders();
  } catch (error) {
    truckList.innerHTML = `<div class="loading-card">${escapeHtml(error.message)}</div>`;
    showToast(error.message, true);
  }
}

function renderTrucks() {
  if (!state.trucks.length) {
    truckList.innerHTML = '<div class="loading-card">No trucks found nearby just yet.</div>';
    return;
  }
  truckList.innerHTML = state.trucks.map((truck, index) => {
    const selected = truck.id === state.selectedTruckId;
    const menu = selected
      ? `<div class="menu-items">${truck.menu.length
        ? truck.menu.map(item => `
          <div class="menu-item">
            <span class="menu-item-emoji">${escapeHtml(item.emoji)}</span>
            <div class="menu-item-copy">
              <span class="menu-item-name">${escapeHtml(item.name)}</span>
              <span class="menu-item-description">${escapeHtml(item.description || "Made fresh, just for you.")}</span>
              <span class="menu-price">${money(item.price)}</span>
            </div>
            <button class="add-item" type="button" data-add-item="${escapeHtml(item.id)}" aria-label="Add ${escapeHtml(item.name)} to your order">+</button>
          </div>`).join("")
        : '<p class="truck-description">This truck is updating its menu. Check back soon.</p>'}</div>`
      : "";
    return `
      <article class="truck-card ${selected ? "selected" : ""}" style="--truck-accent:${escapeHtml(truck.accent)}">
        <button class="truck-visual" type="button" data-select-truck="${escapeHtml(truck.id)}" aria-label="View ${escapeHtml(truck.name)} menu">
          <span class="truck-emoji">${["🥪", "🥙", "🍱", "🌮"][index % 4]}</span>
          <span class="truck-visual-label">NEIGHBORHOOD FAVORITE</span>
        </button>
        <div class="truck-info">
          <div class="truck-info-top">
            <div><p class="truck-cuisine">${escapeHtml(truck.cuisine)}</p><h3>${escapeHtml(truck.name)}</h3></div>
            <span class="distance">${distanceLabel(truck.distanceKm)}</span>
          </div>
          <p class="truck-description">${escapeHtml(truck.description)}</p>
          <p class="truck-address"><span aria-hidden="true">⌖</span>${escapeHtml(truck.address)}</p>
          ${menu}
        </div>
      </article>`;
  }).join("");
}

function renderCart() {
  const lines = [...state.cart.entries()];
  const count = lines.reduce((sum, [, line]) => sum + line.quantity, 0);
  document.querySelector("#cart-count").textContent = String(count);
  cartFooter.hidden = count === 0;
  if (!lines.length) {
    cartContent.innerHTML = `<div class="empty-cart"><span class="empty-cart-icon" aria-hidden="true">✳</span>
      <strong>Good things start here.</strong><p>Choose a truck, then add a bite or two.</p></div>`;
    document.querySelector("#cart-total").textContent = money(0);
    return;
  }
  cartContent.innerHTML = lines.map(([itemId, line]) => `
    <div class="cart-line">
      <div class="cart-line-copy"><p class="cart-line-name">${escapeHtml(line.item.name)}</p>
        <p class="cart-line-price">${money(line.item.price * line.quantity)}</p></div>
      <div class="quantity-control">
        <button type="button" data-quantity="${escapeHtml(itemId)}" data-change="-1" aria-label="Remove one ${escapeHtml(line.item.name)}">−</button>
        <span>${line.quantity}</span>
        <button type="button" data-quantity="${escapeHtml(itemId)}" data-change="1" aria-label="Add one ${escapeHtml(line.item.name)}">+</button>
      </div>
    </div>`).join("");
  const total = lines.reduce((sum, [, line]) => sum + line.item.price * line.quantity, 0);
  document.querySelector("#cart-total").textContent = money(total);
}

function selectTruck(truckId) {
  if (truckId === state.selectedTruckId) return;
  if (state.cart.size) {
    state.cart.clear();
    showToast("Your basket was cleared so it stays with one truck.");
  }
  state.selectedTruckId = truckId;
  renderTrucks();
  renderCart();
}

function renderSellerTruckOptions() {
  const select = document.querySelector("#seller-truck");
  if (!select) return;
  if (!state.sellerTruckId || !state.trucks.some(truck => truck.id === state.sellerTruckId)) {
    state.sellerTruckId = state.trucks[0]?.id ?? null;
  }
  select.innerHTML = state.trucks.map(truck =>
    `<option value="${escapeHtml(truck.id)}">${escapeHtml(truck.name)}</option>`
  ).join("");
  if (state.sellerTruckId) select.value = state.sellerTruckId;
  updateSellerFields();
}

function getSellerTruck() {
  return state.trucks.find(truck => truck.id === state.sellerTruckId) ?? null;
}

function updateSellerFields() {
  const truck = getSellerTruck();
  if (!truck) return;
  document.querySelector("#seller-address").value = truck.address;
  document.querySelector("#seller-latitude").value = truck.latitude;
  document.querySelector("#seller-longitude").value = truck.longitude;
}

function renderSellerMenu() {
  const container = document.querySelector("#seller-menu");
  const truck = getSellerTruck();
  if (!container || !truck) return;
  container.innerHTML = truck.menu.map(item => `
    <div class="seller-menu-item">
      <span class="menu-item-emoji">${escapeHtml(item.emoji)}</span>
      <div class="menu-item-copy"><span class="menu-item-name">${escapeHtml(item.name)}</span>
        <span class="menu-item-description">${escapeHtml(item.description || "No description")}</span></div>
      <span class="menu-price">${money(item.price)}</span>
      <button class="remove-item" data-remove-item="${escapeHtml(item.id)}" type="button" aria-label="Remove ${escapeHtml(item.name)} from menu">×</button>
    </div>`).join("") || '<div class="empty-orders">Your menu is empty. Add your first item above.</div>';
}

async function loadOrders() {
  const container = document.querySelector("#order-list");
  const truck = getSellerTruck();
  if (!container || !truck) return;
  container.innerHTML = '<div class="loading-card">Checking for new orders…</div>';
  try {
    const orders = await api(`/api/orders?truckId=${encodeURIComponent(truck.id)}`);
    renderOrders(orders);
  } catch (error) {
    container.innerHTML = `<div class="empty-orders">${escapeHtml(error.message)}</div>`;
  }
}

function renderOrders(orders) {
  const container = document.querySelector("#order-list");
  if (!orders.length) {
    container.innerHTML = '<div class="empty-orders">Your next order could be just around the corner.</div>';
    return;
  }
  container.innerHTML = orders.map(order => {
    const nextStatus = ({ RECEIVED: "PREPARING", PREPARING: "READY", READY: "COMPLETED" })[order.status];
    return `<article class="incoming-order">
      <div class="incoming-order-top">
        <div><h4>${escapeHtml(order.customerName)} <span style="color:#9b9d93;font-weight:500">· ${escapeHtml(order.id)}</span></h4>
          <p class="order-meta">${new Date(order.createdAt).toLocaleTimeString([], { hour: "numeric", minute: "2-digit" })} · ${order.items.reduce((sum, item) => sum + item.quantity, 0)} items</p></div>
        <span class="order-status">${escapeHtml(order.status)}</span>
      </div>
      <ul class="incoming-items">${order.items.map(item => `<li>${item.quantity} × ${escapeHtml(item.name)}</li>`).join("")}</ul>
      <div class="incoming-total"><span>Total · pay at pickup</span><strong>${money(order.total)}</strong></div>
      ${nextStatus ? `<button class="button button-outline order-status-action" type="button" data-order-status="${escapeHtml(order.id)}" data-next-status="${nextStatus}" style="min-height:31px;margin-top:10px;font-size:9px">Mark ${nextStatus.toLowerCase()} <span aria-hidden="true">→</span></button>` : ""}
    </article>`;
  }).join("");
}

function setView(view) {
  const isSeller = view === "seller";
  document.querySelector("#discover-view").hidden = isSeller;
  document.querySelector(".hero").hidden = isSeller;
  document.querySelector("#seller-view").hidden = !isSeller;
  document.querySelectorAll(".nav-link").forEach(button => {
    const active = button.dataset.view === view;
    button.classList.toggle("active", active);
    button.setAttribute("aria-current", active ? "page" : "false");
  });
  if (isSeller) {
    renderSellerTruckOptions();
    renderSellerMenu();
    loadOrders();
  }
}

document.querySelectorAll(".nav-link").forEach(button => {
  button.addEventListener("click", () => setView(button.dataset.view));
});

document.querySelector("#find-trucks").addEventListener("click", async () => {
  const latitude = Number(document.querySelector("#latitude").value);
  const longitude = Number(document.querySelector("#longitude").value);
  if (!Number.isFinite(latitude) || !Number.isFinite(longitude)
      || latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
    showToast("Enter a valid latitude and longitude.", true);
    return;
  }
  state.latitude = latitude;
  state.longitude = longitude;
  document.querySelector("#location-label").textContent = `${latitude.toFixed(2)}, ${longitude.toFixed(2)}`;
  await loadTrucks();
});

document.querySelector("#use-location").addEventListener("click", () => {
  if (!navigator.geolocation) {
    showToast("Location is not available in this browser. Enter coordinates instead.", true);
    return;
  }
  navigator.geolocation.getCurrentPosition(async position => {
    state.latitude = position.coords.latitude;
    state.longitude = position.coords.longitude;
    document.querySelector("#latitude").value = state.latitude;
    document.querySelector("#longitude").value = state.longitude;
    document.querySelector("#location-label").textContent = "Current location";
    await loadTrucks();
    showToast("Showing food trucks near you.");
  }, error => {
    const reason = error.code === error.PERMISSION_DENIED
      ? "Location permission was denied. Enter coordinates instead."
      : "Couldn't get your location. Enter coordinates instead.";
    showToast(reason, true);
  }, { enableHighAccuracy: false, timeout: 10000, maximumAge: 60000 });
});

truckList.addEventListener("click", event => {
  const selectButton = event.target.closest("[data-select-truck]");
  if (selectButton) {
    selectTruck(selectButton.dataset.selectTruck);
    return;
  }
  const addButton = event.target.closest("[data-add-item]");
  if (!addButton) return;
  const truck = state.trucks.find(candidate => candidate.id === state.selectedTruckId);
  const item = truck?.menu.find(candidate => candidate.id === addButton.dataset.addItem);
  if (!item) return;
  const line = state.cart.get(item.id);
  state.cart.set(item.id, { item, quantity: Math.min(99, (line?.quantity ?? 0) + 1) });
  renderCart();
});

cartContent.addEventListener("click", event => {
  const button = event.target.closest("[data-quantity]");
  if (!button) return;
  const itemId = button.dataset.quantity;
  const line = state.cart.get(itemId);
  if (!line) return;
  const quantity = line.quantity + Number(button.dataset.change);
  if (quantity < 1) state.cart.delete(itemId);
  else if (quantity <= 99) state.cart.set(itemId, { ...line, quantity });
  renderCart();
});

document.querySelector("#place-order").addEventListener("click", async () => {
  const customerName = document.querySelector("#customer-name").value.trim();
  if (!customerName) {
    showToast("Add a name so the truck knows who to call.", true);
    document.querySelector("#customer-name").focus();
    return;
  }
  if (!state.selectedTruckId || !state.cart.size) return;
  const button = document.querySelector("#place-order");
  button.disabled = true;
  try {
    const order = await api("/api/orders", {
      method: "POST",
      body: JSON.stringify({
        truckId: state.selectedTruckId,
        customerName,
        items: [...state.cart.entries()].map(([menuItemId, line]) => ({ menuItemId, quantity: line.quantity }))
      })
    });
    state.cart.clear();
    document.querySelector("#customer-name").value = "";
    renderCart();
    showToast(`Order ${order.id} is in! ${order.truckName} will get it started.`);
    await loadOrders();
  } catch (error) {
    showToast(error.message, true);
  } finally {
    button.disabled = false;
  }
});

document.querySelector("#seller-truck").addEventListener("change", event => {
  state.sellerTruckId = event.target.value;
  updateSellerFields();
  renderSellerMenu();
  loadOrders();
});

document.querySelector("#location-form").addEventListener("submit", async event => {
  event.preventDefault();
  const truck = getSellerTruck();
  if (!truck) return;
  try {
    const updated = await api(`/api/trucks/${encodeURIComponent(truck.id)}/location`, {
      method: "PUT",
      body: JSON.stringify({
        address: document.querySelector("#seller-address").value.trim(),
        latitude: Number(document.querySelector("#seller-latitude").value),
        longitude: Number(document.querySelector("#seller-longitude").value)
      })
    });
    state.trucks = state.trucks.map(candidate => candidate.id === updated.id ? updated : candidate);
    renderTrucks();
    renderSellerTruckOptions();
    showToast(`${updated.name}'s location is updated.`);
  } catch (error) {
    showToast(error.message, true);
  }
});

document.querySelector("#menu-form").addEventListener("submit", async event => {
  event.preventDefault();
  const truck = getSellerTruck();
  if (!truck) return;
  try {
    const item = await api(`/api/trucks/${encodeURIComponent(truck.id)}/menu`, {
      method: "POST",
      body: JSON.stringify({
        name: document.querySelector("#item-name").value.trim(),
        description: document.querySelector("#item-description").value.trim(),
        price: Number(document.querySelector("#item-price").value),
        emoji: document.querySelector("#item-emoji").value.trim()
      })
    });
    state.trucks = state.trucks.map(candidate =>
      candidate.id === truck.id ? { ...candidate, menu: [...candidate.menu, item] } : candidate
    );
    event.target.reset();
    renderSellerMenu();
    renderTrucks();
    showToast(`${item.name} is now on the menu.`);
  } catch (error) {
    showToast(error.message, true);
  }
});

document.querySelector("#seller-menu").addEventListener("click", async event => {
  const button = event.target.closest("[data-remove-item]");
  const truck = getSellerTruck();
  if (!button || !truck) return;
  try {
    await api(`/api/trucks/${encodeURIComponent(truck.id)}/menu/${encodeURIComponent(button.dataset.removeItem)}`, {
      method: "DELETE"
    });
    state.trucks = state.trucks.map(candidate =>
      candidate.id === truck.id
        ? { ...candidate, menu: candidate.menu.filter(item => item.id !== button.dataset.removeItem) }
        : candidate
    );
    state.cart.delete(button.dataset.removeItem);
    renderSellerMenu();
    renderTrucks();
    renderCart();
    showToast("Food item removed from the menu.");
  } catch (error) {
    showToast(error.message, true);
  }
});

document.querySelector("#order-list").addEventListener("click", async event => {
  const button = event.target.closest("[data-order-status]");
  if (!button) return;
  button.disabled = true;
  try {
    await api(`/api/orders/${encodeURIComponent(button.dataset.orderStatus)}/status`, {
      method: "PATCH",
      body: JSON.stringify({ status: button.dataset.nextStatus })
    });
    await loadOrders();
    showToast(`Order marked ${button.dataset.nextStatus.toLowerCase()}.`);
  } catch (error) {
    button.disabled = false;
    showToast(error.message, true);
  }
});

document.querySelector("#refresh-orders").addEventListener("click", loadOrders);

loadTrucks();
