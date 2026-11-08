CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT uk_roles_nombre UNIQUE (nombre)
);

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    documento_identidad VARCHAR(20) NOT NULL,
    celular VARCHAR(13) NOT NULL,
    fecha_nacimiento DATE NULL,
    correo VARCHAR(100) NOT NULL,
    clave VARCHAR(100) NOT NULL,
    rol_id BIGINT NOT NULL,
    restaurante_id BIGINT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_usuarios_documento UNIQUE (documento_identidad),
    CONSTRAINT uk_usuarios_correo UNIQUE (correo),
    CONSTRAINT fk_usuarios_roles
        FOREIGN KEY (rol_id)
        REFERENCES roles (id)
);

CREATE INDEX idx_usuarios_rol
    ON usuarios (rol_id);

CREATE INDEX idx_usuarios_restaurante
    ON usuarios (restaurante_id);

INSERT INTO roles (nombre, descripcion, activo)
VALUES
    ('ADMINISTRADOR', 'Usuario responsable de la administracion general', TRUE),
    ('PROPIETARIO', 'Usuario propietario de uno o varios restaurantes', TRUE),
    ('EMPLEADO', 'Usuario encargado de gestionar pedidos de un restaurante', TRUE),
    ('CLIENTE', 'Usuario autorizado para consultar restaurantes y realizar pedidos', TRUE);
    