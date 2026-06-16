-- ============================================================
-- Script de inicialización - Oracle Database 21c
-- TEMA: Conexión Java a Base de Datos
--
-- Ejecutar como DBA (sysdba) primero para crear el usuario:
--   sqlplus sys/oracle@//localhost:1521/XEPDB1 AS SYSDBA
--   CREATE USER examen IDENTIFIED BY examen123;
--   GRANT CONNECT, RESOURCE, CREATE TABLE, CREATE SEQUENCE TO examen;
--   ALTER USER examen QUOTA UNLIMITED ON USERS;
--   EXIT;
--
-- Luego conectar con el nuevo usuario y ejecutar este script:
--   sqlplus examen/examen123@//localhost:1521/XEPDB1
--   @init.sql
-- ============================================================

-- Tabla de usuarios para autenticación
CREATE TABLE usuarios (
    id              NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username        VARCHAR2(50)  NOT NULL,
    password        VARCHAR2(100) NOT NULL,
    nombre_completo VARCHAR2(100) NOT NULL,
    rol             VARCHAR2(20)  DEFAULT 'USER',
    CONSTRAINT uq_username UNIQUE (username)
);

-- Tabla de productos (CRUD principal)
-- Oracle no tiene tipo BOOLEAN: se usa NUMBER(1) CHECK (0 o 1)
CREATE TABLE productos (
    id                   NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre               VARCHAR2(100) NOT NULL,
    descripcion          VARCHAR2(500),
    precio               NUMBER(10, 2) NOT NULL,
    stock                NUMBER(6)     DEFAULT 0,
    categoria            VARCHAR2(50),
    activo               NUMBER(1)     DEFAULT 1 CHECK (activo IN (0, 1)),
    fecha_creacion       TIMESTAMP     DEFAULT SYSTIMESTAMP,
    fecha_actualizacion  TIMESTAMP     DEFAULT SYSTIMESTAMP
);

-- ============================================================
-- Datos iniciales de prueba
-- ============================================================

-- Usuarios (admin/admin123 y user/user123)
INSERT INTO usuarios (username, password, nombre_completo, rol)
VALUES ('admin', 'admin123', 'Administrador del Sistema', 'ADMIN');

INSERT INTO usuarios (username, password, nombre_completo, rol)
VALUES ('user', 'user123', 'Usuario de Prueba', 'USER');

-- Productos de ejemplo
INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo)
VALUES ('Laptop HP Pavilion 15', 'Laptop con procesador Intel i7, 16GB RAM, 512GB SSD', 899.99, 25, 'Electrónica', 1);

INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo)
VALUES ('Mouse Logitech MX Master 3', 'Mouse inalámbrico ergonómico con sensor de alta precisión', 79.99, 150, 'Periféricos', 1);

INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo)
VALUES ('Teclado Mecánico Corsair K70', 'Teclado mecánico RGB con switches Cherry MX Red', 129.99, 80, 'Periféricos', 1);

INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo)
VALUES ('Monitor Samsung 27" 4K', 'Monitor IPS 4K UHD con HDR10 y FreeSync', 449.99, 30, 'Electrónica', 1);

INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo)
VALUES ('Auriculares Sony WH-1000XM5', 'Auriculares inalámbricos con cancelación de ruido', 349.99, 60, 'Audio', 1);

INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo)
VALUES ('Webcam Logitech C920', 'Webcam Full HD 1080p con micrófono estéreo', 69.99, 100, 'Periféricos', 1);

INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo)
VALUES ('SSD Samsung 970 EVO 1TB', 'Disco de estado sólido NVMe M.2 de alto rendimiento', 109.99, 200, 'Almacenamiento', 1);

INSERT INTO productos (nombre, descripcion, precio, stock, categoria, activo)
VALUES ('Silla Gamer Secretlab Titan', 'Silla ergonómica para gaming con soporte lumbar', 499.99, 15, 'Mobiliario', 0);

COMMIT;
