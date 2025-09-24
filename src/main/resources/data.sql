-- Dumping data for table prendas
--
INSERT INTO usuarios (usr_nombre, usr_apellido, usr_correo, usr_tipo_usuario, usr_contrasena, usr_telefono) VALUES
('Agustin', 'Segovia', 'agus@mgail.com', 'DUENO', 'agus123', '1122334455'),
('Maia', 'Cohen', 'maia@gmail.com', 'ADMINISTRADOR', 'maia123', '1122334456'),
('Carlos', 'Ferreira', 'carlos@gmail.com', 'DUENO', 'admin123', '1122334457'),
('Refugio', 'San Martín', 'contacto@refugiosm.org', 'REFUGIO', 'refugio2024', '1122334458');

INSERT INTO mascotas (msc_nombre, msc_raza, msc_color, msc_tamano, msc_edad, msc_descripcion, msc_foto, msc_dueno_id) VALUES
('Firulais', 'Labrador', 'Negro', 'Grande', 5, 'Muy juguetón y sociable', 'firulais.jpg', 1),
('Mimi', 'Caniche', 'Blanco', 'Chico', 3, 'Le teme a los ruidos fuertes', 'mimi.jpg', 1),
('Toby', 'Mestizo', 'Marrón', 'Mediano', 4, 'Rescatado de la calle', 'toby.jpg', 3);


INSERT INTO refugios (ref_nombre, ref_direccion, ref_telefono, ref_correo, ref_foto, ref_ubicacion) VALUES
('Refugio Esperanza', 'Av. Siempre Viva 123', '1122334455', 'esperanza@refugio.org', 'esperanza.jpg', 'Buenos Aires'),
('Refugio Patitas', 'Calle Falsa 456', '1133445566', 'contacto@patitas.org', 'patitas.jpg', 'La Plata'),
('Hogar Animal', 'Ruta 8 km 50', '1144556677', 'hogaranimal@refugio.org', 'hogaranimal.jpg', 'Pilar');

INSERT INTO adopciones (ado_mascota_id, ado_usuario_id, ado_estado, ado_descripcion) VALUES
(3, 3, 'ACTIVA', 'Toby busca una familia responsable'),
(2, 1, 'ACTIVA', 'Mimi necesita un hogar lleno de amor'),
(1, 3, 'CERRADA', 'Firulais fue adoptado exitosamente');

INSERT INTO veterinarias (vet_nombre, vet_direccion, vet_telefono, vet_horario_atencion, vet_ubicacion, vet_activa) VALUES
('VetPlus', 'Av. Rivadavia 1234', '1122334455', 'Lunes a viernes de 9 a 18hs', 'CABA', TRUE),
('PetCare', 'Calle Mitre 432', '1133445566', 'Todos los días de 8 a 20hs', 'Lanús', TRUE),
('Salud Animal', 'Diagonal Norte 100', '1144556677', 'Lunes a sábado de 10 a 17hs', 'Quilmes', FALSE);
--Alertas de prueba para el mapa
INSERT INTO alertas (alt_usuario_id, alt_descripcion, alt_lat, alt_lng)
VALUES
    (1, 'Visto en Plaza Italia', -34.5873, -58.4100),
    (1, 'Se perdió en Microcentro', -34.6037, -58.3816);





