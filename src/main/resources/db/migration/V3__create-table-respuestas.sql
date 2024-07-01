CREATE TABLE respuestas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contenido TEXT NOT NULL,
    foro_id BIGINT NOT NULL,
    FOREIGN KEY (foro_id) REFERENCES foros(id) ON UPDATE CASCADE ON DELETE CASCADE
);