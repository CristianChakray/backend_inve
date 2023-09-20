/* SEED DATA */

INSERT INTO tipo_acceso(id, nombre, estatus) VALUES(1, 'Sin acceso', 1);
INSERT INTO tipo_acceso(id, nombre, estatus) VALUES(2, 'Solo lectura', 1);
INSERT INTO tipo_acceso(id, nombre, estatus) VALUES(3, 'Edición', 1);

INSERT INTO nivel_acceso(id, nombre, estatus) VALUES(1, 'Asignado al usuario', 1);
INSERT INTO nivel_acceso(id, nombre, estatus) VALUES(2, 'Todas las solicitudes', 1);