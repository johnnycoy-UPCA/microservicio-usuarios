INSERT INTO usuarios (
    nombre,
    apellido,
    documento_identidad,
    celular,
    fecha_nacimiento,
    correo,
    clave,
    rol_id,
    restaurante_id,
    activo,
    fecha_creacion,
    fecha_actualizacion
)
SELECT
    'Administrador',
    'Plazoleta',
    '0000000001',
    '+570000000001',
    DATE '1990-01-01',
    'admin@plazoleta.com',
    '$2a$10$lbuk8rDFWFInE5NfDnfFM.INlCrAk.s7iYCOBfa6ZJXQyRX3YDdR2',
    r.id,
    NULL,
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM roles r
WHERE r.nombre = 'ADMINISTRADOR'
  AND r.activo = TRUE
  AND NOT EXISTS (
      SELECT 1
      FROM usuarios u
      WHERE LOWER(u.correo) = 'admin@plazoleta.com'
         OR u.documento_identidad = '0000000001'
  );