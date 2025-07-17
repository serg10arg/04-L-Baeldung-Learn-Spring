-- Definición de la tabla project
CREATE TABLE project (
                         id INTEGER NOT NULL AUTO_INCREMENT,
                         nombre VARCHAR(128) NOT NULL,
                         fecha_creacion DATE NOT NULL,
                         PRIMARY KEY (id)
);

-- Definición de la tabla task
CREATE TABLE task (
                      id INTEGER NOT NULL AUTO_INCREMENT,
                      nombre VARCHAR(255),
                      descripcion VARCHAR(255),
                      fecha_creacion DATE,
                      fecha_vencimiento DATE,
                      estado VARCHAR(50),
                      project_id INTEGER,
                      PRIMARY KEY (id)
);

-- Agregar clave foránea para la relación entre task y project
ALTER TABLE task ADD CONSTRAINT FK_TASK_PROJECT
    FOREIGN KEY (project_id) REFERENCES project(id);
