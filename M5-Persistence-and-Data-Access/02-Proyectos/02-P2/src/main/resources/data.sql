-- Datos iniciales para la tabla project
INSERT INTO project (nombre, fecha_creacion) VALUES ('Sistema de Gestión de Proyectos', '2023-01-15');
INSERT INTO project (nombre, fecha_creacion) VALUES ('Desarrollo de Aplicación Móvil', '2023-03-01');

-- Datos iniciales para la tabla task
INSERT INTO task (nombre, descripcion, fecha_creacion, fecha_vencimiento, estado, project_id)
VALUES ('Diseño de Base de Datos', 'Diseñar el esquema de la base de datos para el sistema', '2023-01-15', '2023-01-20', 'CREADO', 1);
INSERT INTO task (nombre, descripcion, fecha_creacion, fecha_vencimiento, estado, project_id)
VALUES ('Implementación de API REST', 'Desarrollar los endpoints de la API', '2023-01-21', '2023-02-10', 'EN_PROGRESO', 1);
INSERT INTO task (nombre, descripcion, fecha_creacion, fecha_vencimiento, estado, project_id)
VALUES ('Desarrollo de Interfaz de Usuario', 'Implementar la interfaz de usuario para la aplicación móvil', '2023-03-05', '2023-03-25', 'CREADO', 2);
