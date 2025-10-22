// ======= Datos de prueba (se pisan si existe localStorage) =======
const seedPets = [
  {
    id: crypto.randomUUID(),
    name: "Luna",
    breed: "Siames",
    sex: "F",
    birth: "2015-05-01",
    docId: "41.873.282",
    contact: "Dueña: Ana · +54 11 5555-5555",
    photo: "assets/img/luna.jpg",
    publicUrl: "https://tu-dominio/pets/luna"
  },
  {
    id: crypto.randomUUID(),
    name: "Milo",
    breed: "Mestizo",
    sex: "M",
    birth: "2019-09-10",
    docId: "A-12345",
    contact: "Dueño: Leo · +54 11 4444-4444",
    photo: "assets/img/milo.jpg",
    publicUrl: "https://tu-dominio/pets/milo"
  },
  {
    id: crypto.randomUUID(),
    name: "Olivia",
    breed: "Border Collie",
    sex: "F",
    birth: "2021-02-02",
    docId: "BC-2021",
    contact: "Dueña: Sofi · +54 11 3333-3333",
    photo: "assets/img/olivia.jpg",
    publicUrl: "https://tu-dominio/pets/olivia"
  }
];

const STORAGE_KEY = "petsit_pets";
let pets = loadPets();

// ======= Helpers =======
function loadPets(){
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(seedPets));
    return structuredClone(seedPets);
  }
  try { return JSON.parse(raw); }
  catch { return structuredClone(seedPets); }
}
function savePets(){ localStorage.setItem(STORAGE_KEY, JSON.stringify(pets)); }
function fmtBirth(iso){ if(!iso) return "—"; const d = new Date(iso+"T00:00:00"); return d.toLocaleDateString(); }

// ======= Render de tarjetas =======
const grid = document.getElementById("petsGrid");
render();

function render(){
  grid.innerHTML = "";
  pets.forEach(p => {
    const col = document.createElement("div");
    col.className = "col-12 col-md-6 col-lg-4";

    col.innerHTML = `
      <article class="pet-card h-100">
        <img class="pet-photo" src="${p.photo || 'assets/img/no-photo.jpg'}" alt="${p.name}">
        <div class="pet-body">
          <h3 class="pet-title">${p.name}</h3>
          <div class="pet-meta">${p.breed || '—'} · ${p.sex || '—'} · ${fmtBirth(p.birth)}</div>
        </div>
        <div class="pet-footer">
          <div class="pet-actions">
            <button class="btn btn-action" data-action="documento" data-id="${p.id}">
              <i class="bi bi-file-earmark-text me-1"></i> Documento
            </button>
            <button class="btn btn-outline-secondary rounded-pill fw-bold" data-action="editar" data-id="${p.id}">
              <i class="bi bi-pencil-square me-1"></i> Editar
            </button>
          </div>
          <div class="pet-qrwrap d-none d-sm-block">
            <div class="qr-box"><!-- solo decorativo en la card --></div>
            <small class="qr-legend">Escaneá para ver el documento</small>
          </div>
        </div>
      </article>
    `;
    grid.appendChild(col);
  });
}

// ======= Abrir Documento (con QR) =======
const docModalEl = document.getElementById("docModal");
const docModal = new bootstrap.Modal(docModalEl);
let currentQR = null;

grid.addEventListener("click", (e)=>{
  const btn = e.target.closest("button[data-action]");
  if(!btn) return;
  const id = btn.getAttribute("data-id");
  const pet = pets.find(x => x.id === id);
  if(!pet) return;

  if(btn.dataset.action === "documento"){
    fillDocModal(pet);
    docModal.show();
  } else if(btn.dataset.action === "editar"){
    openFormForEdit(pet);
  }
});

function fillDocModal(p){
  // Campos
  document.getElementById("docPhoto").src = p.photo || "assets/img/no-photo.jpg";
  document.getElementById("docName").textContent = p.name || "—";
  document.getElementById("docBreed").textContent = p.breed || "—";
  document.getElementById("docSex").textContent = p.sex || "—";
  document.getElementById("docBirth").textContent = fmtBirth(p.birth);
  document.getElementById("docId").textContent = p.docId || "—";
  document.getElementById("docContact").textContent = p.contact || "—";

  // Link público (puede ser tu futura URL de detalle)
  const link = p.publicUrl || (`https://tu-dominio/pets/${encodeURIComponent(p.name)}`);
  const a = document.getElementById("docPublicLink");
  a.href = link;

  // QR
  const qrWrap = document.getElementById("qrBox");
  qrWrap.innerHTML = "";
  currentQR = new QRCode(qrWrap, {
    text: link,
    width: 120,
    height: 120,
    correctLevel: QRCode.CorrectLevel.M
  });
}

// Descargar QR como PNG
document.getElementById("btnDownloadQR").addEventListener("click", ()=>{
  if(!currentQR) return;
  // La lib genera <img> o <canvas>. Buscamos cualquiera.
  const img = document.querySelector("#qrBox img");
  const canvas = document.querySelector("#qrBox canvas");

  let dataUrl = null;
  if(canvas) dataUrl = canvas.toDataURL("image/png");
  else if(img) dataUrl = img.src;

  if(!dataUrl) return;
  const a = document.createElement("a");
  a.href = dataUrl;
  a.download = "qr-mascota.png";
  a.click();
});

// ======= Agregar / Editar =======
const petFormModalEl = document.getElementById("petFormModal");
const petFormModal = new bootstrap.Modal(petFormModalEl);
const petForm = document.getElementById("petForm");
const petFormTitle = document.getElementById("petFormTitle");
let editingId = null;

// Botón “Agregar”
document.getElementById("btnAddPet")?.addEventListener("click", ()=>{
  editingId = null;
  petFormTitle.textContent = "Registrar mascota";
  petForm.reset();
});

// Abrir para editar
function openFormForEdit(p){
  editingId = p.id;
  petFormTitle.textContent = "Editar mascota";
  petForm.name.value = p.name || "";
  petForm.breed.value = p.breed || "";
  petForm.sex.value = p.sex || "";
  petForm.birth.value = p.birth || "";
  petForm.docId.value = p.docId || "";
  petForm.contact.value = p.contact || "";
  petForm.photo.value = p.photo || "";
  petFormModal.show();
}

// Guardar (crear o editar)
petForm.addEventListener("submit", (e)=>{
  e.preventDefault();
  const data = {
    name: petForm.name.value.trim(),
    breed: petForm.breed.value.trim(),
    sex: petForm.sex.value,
    birth: petForm.birth.value,
    docId: petForm.docId.value.trim(),
    contact: petForm.contact.value.trim(),
    photo: petForm.photo.value.trim() || "assets/img/no-photo.jpg",
  };

  if(editingId){
    const idx = pets.findIndex(p => p.id === editingId);
    pets[idx] = { ...pets[idx], ...data };
  } else {
    pets.unshift({
      id: crypto.randomUUID(),
      ...data,
      publicUrl: `https://tu-dominio/pets/${encodeURIComponent(data.name || "mascota")}`
    });
  }
  savePets();
  render();
  petFormModal.hide();
});
