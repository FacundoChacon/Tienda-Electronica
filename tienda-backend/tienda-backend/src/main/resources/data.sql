-- =====================================================================
-- Datos de prueba para desarrollo local.
-- Se ejecuta automaticamente al levantar la app (ver application.properties:
-- spring.sql.init.mode=always y spring.jpa.defer-datasource-initialization=true,
-- necesarios para que Hibernate cree las tablas ANTES de que se inserten estos datos).
--
-- Contraseña de los dos usuarios de prueba: "password123"
-- (el hash de abajo corresponde a esa contrasena, generado con BCrypt).
-- =====================================================================

-- Limpiar datos previos (util para reiniciar el entorno de prueba sin borrar la base a mano).
DELETE FROM detalle_ordenes;
DELETE FROM ordenes;
DELETE FROM productos;
DELETE FROM categorias;
DELETE FROM usuarios;

-- --------------------------------------------------------------------
-- Usuarios de prueba
-- --------------------------------------------------------------------
INSERT INTO usuarios (id, nombre, email, password_hash, rol, fecha_registro) VALUES
(1, 'Facundo Admin', 'admin@tienda.com', '$2a$10$EblZqNptyYvcLm/VInsPiu.fHTV.7nMqkHFVlOLanLOZpfBJlYfqW', 'ADMIN', NOW()),
(2, 'Cliente de Prueba', 'cliente@tienda.com', '$2a$10$EblZqNptyYvcLm/VInsPiu.fHTV.7nMqkHFVlOLanLOZpfBJlYfqW', 'CLIENTE', NOW());

-- --------------------------------------------------------------------
-- Categorias
-- --------------------------------------------------------------------
INSERT INTO categorias (id, nombre, descripcion) VALUES
(1, 'Notebooks', 'Laptops para uso personal, gaming y oficina'),
(2, 'Celulares', 'Smartphones de distintas marcas y rangos de precio'),
(3, 'Audio', 'Auriculares, parlantes y equipos de sonido'),
(4, 'Componentes PC', 'Placas de video, procesadores, memorias y mas'),
(5, 'Accesorios', 'Mouses, teclados, fundas y otros accesorios');

-- --------------------------------------------------------------------
-- Productos
-- --------------------------------------------------------------------
INSERT INTO productos (id, categoria_id, nombre, marca, descripcion, precio, stock, imagen_url, activo) VALUES
-- Notebooks
(1, 1, 'Notebook Gamer 15.6" RTX 4060', 'Lenovo', 'Procesador Intel Core i7, 16GB RAM, SSD 512GB, placa RTX 4060', 1450000.00, 8, 'https://placehold.co/400x400?text=Lenovo+Gamer', true),
(2, 1, 'Notebook Ultrabook 14"', 'ASUS', 'Procesador Intel Core i5, 8GB RAM, SSD 256GB, ideal oficina', 780000.00, 15, 'https://placehold.co/400x400?text=ASUS+Ultrabook', true),
(3, 1, 'MacBook Air M2', 'Apple', 'Chip M2, 8GB RAM unificada, SSD 256GB', 1690000.00, 5, 'https://placehold.co/400x400?text=MacBook+Air', true),

-- Celulares
(4, 2, 'Galaxy S24', 'Samsung', '256GB, camara triple 50MP, pantalla AMOLED 6.2"', 980000.00, 20, 'https://placehold.co/400x400?text=Galaxy+S24', true),
(5, 2, 'iPhone 15', 'Apple', '128GB, chip A16, camara dual 48MP', 1150000.00, 12, 'https://placehold.co/400x400?text=iPhone+15', true),
(6, 2, 'Redmi Note 13', 'Xiaomi', '128GB, camara 108MP, bateria 5000mAh', 320000.00, 30, 'https://placehold.co/400x400?text=Redmi+Note+13', true),

-- Audio
(7, 3, 'Auriculares Inalambricos WH-1000XM5', 'Sony', 'Cancelacion de ruido activa, 30hs de bateria', 450000.00, 18, 'https://placehold.co/400x400?text=Sony+WH1000XM5', true),
(8, 3, 'Parlante Bluetooth Portatil', 'JBL', 'Resistente al agua IP67, 12hs de bateria', 95000.00, 25, 'https://placehold.co/400x400?text=JBL+Parlante', true),

-- Componentes PC
(9, 4, 'Placa de Video RTX 4070', 'NVIDIA', '12GB GDDR6X, ideal gaming en 1440p', 890000.00, 6, 'https://placehold.co/400x400?text=RTX+4070', true),
(10, 4, 'Memoria RAM 16GB DDR4', 'Kingston', '3200MHz, kit de 2x8GB', 65000.00, 40, 'https://placehold.co/400x400?text=RAM+16GB', true),

-- Accesorios
(11, 5, 'Mouse Gamer Inalambrico', 'Logitech', 'Sensor 25600 DPI, bateria de larga duracion', 78000.00, 35, 'https://placehold.co/400x400?text=Mouse+Logitech', true),
(12, 5, 'Teclado Mecanico RGB', 'Redragon', 'Switches azules, retroiluminacion RGB personalizable', 110000.00, 22, 'https://placehold.co/400x400?text=Teclado+Redragon', true);

-- --------------------------------------------------------------------
-- Reiniciar los contadores AUTO_INCREMENT para que las proximas
-- inserciones (desde la app, vía POST) continuen despues del ultimo ID usado.
-- --------------------------------------------------------------------
ALTER TABLE usuarios AUTO_INCREMENT = 3;
ALTER TABLE categorias AUTO_INCREMENT = 6;
ALTER TABLE productos AUTO_INCREMENT = 13;
