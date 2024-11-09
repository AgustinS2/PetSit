DROP TABLE IF EXISTS usuarios;

-- Table structure for table usuarios
--
CREATE TABLE usuarios (
usr_id bigint NOT NULL AUTO_INCREMENT,
usr_nombre varchar(255) DEFAULT NULL,
usr_apellido varchar(255) DEFAULT NULL,
usr_correo varchar(255) DEFAULT NULL,
usr_tipo_usuario varchar(255) DEFAULT NULL,
PRIMARY KEY (usr_id)
);