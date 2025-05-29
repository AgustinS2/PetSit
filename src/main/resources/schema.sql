DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS mascotas;
DROP TABLE IF EXISTS refugios;
DROP TABLE IF EXISTS alertas;
DROP TABLE IF EXISTS reportes;
DROP TABLE IF EXISTS adopciones;
DROP TABLE IF EXISTS postulaciones;
DROP TABLE IF EXISTS veterinarias;


CREATE TABLE usuarios (
usr_id INT NOT NULL AUTO_INCREMENT,
usr_nombre varchar(255) DEFAULT NULL,
usr_apellido varchar(255) DEFAULT NULL,
usr_correo varchar(255) DEFAULT NULL,
usr_tipo_usuario varchar(255) DEFAULT NULL,
usr_contrasena VARCHAR(255) DEFAULT NULL,
usr_telefono varchar(255) DEFAULT NULL,
PRIMARY KEY (usr_id)
);

CREATE TABLE mascotas (
  msc_id INT AUTO_INCREMENT PRIMARY KEY,
  msc_nombre VARCHAR(100),
  msc_raza VARCHAR(100),
  msc_color VARCHAR(100),
  msc_tamano VARCHAR(50), -- chico, mediano, grande
  msc_edad INT,
  msc_descripcion varchar(255),
  --msc_estado ENUM('PERDIDO', 'ENCONTRADO', 'EN_REFUGIO', 'EN_CASA') DEFAULT 'PERDIDO',
  msc_foto VARCHAR(255),
  msc_dueno_id INT,
  FOREIGN KEY (msc_dueno_id) REFERENCES usuarios(usr_id)
);

CREATE TABLE refugios (
  ref_id INT AUTO_INCREMENT PRIMARY KEY,
  ref_nombre VARCHAR(100),
  ref_direccion VARCHAR(255),
  ref_telefono VARCHAR(20),
  ref_correo VARCHAR(100),
  ref_foto VARCHAR(255),
  ref_ubicacion VARCHAR(255)
);

CREATE TABLE alertas (
  alt_id INT AUTO_INCREMENT PRIMARY KEY,
  alt_mascota_id INT, -- Puede ser NULL si la mascota no es de nadie
  alt_usuario_id INT NOT NULL,
  alt_fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  alt_descripcion VARCHAR(255),
  alt_ubicacion VARCHAR(255),
  alt_foto VARCHAR(255),
  FOREIGN KEY (alt_usuario_id) REFERENCES usuarios(usr_id),
  FOREIGN KEY (alt_mascota_id) REFERENCES mascotas(msc_id)
);

CREATE TABLE reportes (
  rep_id INT AUTO_INCREMENT PRIMARY KEY,
  rep_usuario_id INT NOT NULL,
  rep_fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  rep_ubicacion VARCHAR(255),
  rep_descripcion VARCHAR(255),
  rep_foto VARCHAR(255),
  rep_estado VARCHAR(255) DEFAULT 'PENDIENTE',
  rep_refugio_id INT,
  rep_alerta_id INT, -- Relación con alerta si se asocia
  FOREIGN KEY (rep_usuario_id) REFERENCES usuarios(usr_id),
  FOREIGN KEY (rep_refugio_id) REFERENCES refugios(ref_id),
  FOREIGN KEY (rep_alerta_id) REFERENCES alertas(alt_id)
);

CREATE TABLE adopciones (
  ado_id INT AUTO_INCREMENT PRIMARY KEY,
  ado_mascota_id INT NOT NULL,
  ado_usuario_id INT NOT NULL,
  ado_fecha_publicacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ado_estado varchar(255) DEFAULT 'ACTIVA',
  ado_descripcion varchar(255),
  FOREIGN KEY (ado_mascota_id) REFERENCES mascotas(msc_id),
  FOREIGN KEY (ado_usuario_id) REFERENCES usuarios(usr_id)
);

CREATE TABLE postulaciones (
  pos_id INT AUTO_INCREMENT PRIMARY KEY,
  pos_adopcion_id INT NOT NULL,
  pos_usuario_id INT NOT NULL,
  pos_fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  pos_mensaje varchar(255),
  FOREIGN KEY (pos_adopcion_id) REFERENCES adopciones(ado_id),
  FOREIGN KEY (pos_usuario_id) REFERENCES usuarios(usr_id)
);

CREATE TABLE veterinarias (
  vet_id INT AUTO_INCREMENT PRIMARY KEY,
  vet_nombre VARCHAR(255),
  vet_direccion VARCHAR(255),
  vet_telefono VARCHAR(50),
  vet_horario_atencion VARCHAR(100),
  vet_ubicacion VARCHAR(255),
  vet_activa BOOLEAN DEFAULT TRUE
);