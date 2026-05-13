-- PetSit data.sql actualizado
-- USUARIOS
INSERT INTO usuarios (usr_nombre, usr_apellido, usr_correo, usr_tipo_usuario, usr_contrasena, usr_telefono) VALUES
                                                                                                                ('Admin','PetSit','admin@petsit.com','ADMINISTRADOR','$2a$10$placeholder.admin.hash.here.replace','1100000001'),
                                                                                                                ('Agustin','Segovia','agus@gmail.com','DUENO','$2a$10$placeholder.agus.hash.here.replace','1122334455'),
                                                                                                                ('Maia','Cohen','maia@gmail.com','DUENO','$2a$10$placeholder.maia.hash.here.replace','1122334456'),
                                                                                                                ('Carlos','Ferreira','carlos@gmail.com','DUENO','$2a$10$placeholder.carlos.hash.here.replace','1122334457');

-- VETERINARIAS (vet_activa=TRUE para aparecer en la vista)
INSERT INTO veterinarias (vet_nombre, vet_direccion, vet_telefono, vet_horario_atencion, vet_ubicacion, vet_activa) VALUES
                                                                                                                        ('Clinica Veterinaria Belgrano','Moldes 2345, Belgrano','+54 11 4555-1111','Lun a Dom 24hs','Belgrano, CABA',TRUE),
                                                                                                                        ('Patitas Caballito','Hidalgo 987, Caballito','+54 11 4321-3333','Lun a Sab 9-20hs','Caballito, CABA',TRUE),
                                                                                                                        ('Love Pets Palermo','Av. Santa Fe 123, Palermo','+54 11 4000-7777','Lun a Dom 8-22hs','Palermo, CABA',TRUE),
                                                                                                                        ('BlackDog Almagro','Corrientes 3560, Almagro','+54 11 4777-1212','Lun a Sab 9-21hs','Almagro, CABA',TRUE),
                                                                                                                        ('Galia Vet Recoleta','Azcuenaga 1450, Recoleta','+54 11 4511-9090','Lun a Dom 24hs','Recoleta, CABA',TRUE),
                                                                                                                        ('Hub City Palermo','Honduras 4901, Palermo','+54 11 4899-2200','Lun a Vie 10-20hs','Palermo, CABA',TRUE),
                                                                                                                        ('Pet World Saavedra','Balbin 4700, Saavedra','+54 11 4701-3344','Lun a Dom 8-23hs','Saavedra, CABA',TRUE),
                                                                                                                        ('Master-Animal Villa Urquiza','Av. Monroe 5200, Villa Urquiza','+54 11 4521-7788','Lun a Sab 9-20hs','Villa Urquiza, CABA',TRUE),
                                                                                                                        ('PetCare Lanus','Calle Mitre 432, Lanus','1133445566','Todos los dias 8-20hs','Lanus, GBA',TRUE),
                                                                                                                        ('Salud Animal Quilmes','Diagonal Norte 100, Quilmes','1144556677','Lun a Sab 10-17hs','Quilmes, GBA',FALSE);

-- REFUGIOS
INSERT INTO refugios (ref_nombre, ref_direccion, ref_telefono, ref_correo, ref_foto, ref_ubicacion) VALUES
                                                                                                        ('Lost & Found','Av. Francisco Beiro 4567, Villa Devoto','+54 11 4567-8901','info@lostandfound.org.ar','arca-peludos.png','Villa Devoto, CABA'),
                                                                                                        ('Animal Resource Center','Neuquen 1234, Caballito','+54 11 4321-2222','contacto@arc.org.ar','cielo-mascotas.png','Caballito, CABA'),
                                                                                                        ('America Dog & Cat Hotel','Zapiola 2230, Colegiales','+54 11 4782-3010','hola@americapets.com.ar','america.jpg','Colegiales, CABA'),
                                                                                                        ('Animal Care Belgrano','Juramento 2450, Belgrano','+54 11 4786-1122','atencion@animalcare.com.ar','animal-care.jpg','Belgrano, CABA'),
                                                                                                        ('Friends Recoleta','Av. Callao 980, Recoleta','+54 11 4371-8899','friends@refugio.org.ar','friends.jpg','Recoleta, CABA'),
                                                                                                        ('Greenville Caballito','Av. Pedro Goyena 850, Caballito','+54 11 4901-3377','info@greenville.org.ar','Greenville.jpg','Caballito, CABA'),
                                                                                                        ('Petland Palermo','Av. Santa Fe 4100, Palermo','+54 11 4832-7700','contacto@petland.com.ar','petland.jpg','Palermo, CABA'),
                                                                                                        ('The Pets Hotel','Av. Monroe 5200, Villa Urquiza','+54 11 4543-2290','hotel@thepetshotel.com.ar','pets-hotel.jpg','Villa Urquiza, CABA'),
                                                                                                        ('Vida Animal Belgrano','Virrey del Pino 2750, Belgrano','+54 11 4781-5566','vidaanimal@refugio.org.ar','vida-animal.jpg','Belgrano, CABA'),
                                                                                                        ('Refugio Esperanza','Av. Siempre Viva 123','1122334455','esperanza@refugio.org','esperanza.jpg','Buenos Aires');

-- MASCOTAS
INSERT INTO mascotas (msc_nombre, msc_raza, msc_color, msc_tamano, msc_edad, msc_descripcion, msc_foto, msc_sexo, msc_dueno_id) VALUES
                                                                                                                                    ('Firulais','Labrador','Negro','grande',5,'Muy jugeton y sociable','firulais.jpg','Macho',2),
                                                                                                                                    ('Mimi','Caniche','Blanco','chico',3,'Le teme a los ruidos fuertes','mimi.jpg','Hembra',2),
                                                                                                                                    ('Toby','Mestizo','Marron','mediano',4,'Rescatado de la calle','toby.jpg','Macho',3),
                                                                                                                                    ('Luna','Siames','Gris','chico',2,'Tranquila y carinosa','luna.jpg','Hembra',3);

-- ADOPCIONES (estado ACTIVA para aparecer en "Adoptar")
INSERT INTO adopciones (ado_mascota_id, ado_usuario_id, ado_estado, ado_descripcion) VALUES
                                                                                         (3,3,'ACTIVA','Toby busca una familia responsable con espacio para paseos'),
                                                                                         (2,2,'ACTIVA','Mimi necesita un hogar tranquilo sin ninos pequenos'),
                                                                                         (4,3,'ACTIVA','Luna es ideal para departamento, muy independiente');

-- ALERTAS de prueba
INSERT INTO alertas (alt_usuario_id, alt_descripcion, alt_lat, alt_lng) VALUES
                                                                            (2,'Visto en Plaza Italia',-34.5873,-58.4100),
                                                                            (2,'Se perdio en Microcentro',-34.6037,-58.3816);
