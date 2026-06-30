-- =====================================================================
-- Datos de prueba para desarrollo local.
-- Se ejecuta automaticamente al levantar la app (ver application.properties:
-- spring.sql.init.mode=always y spring.jpa.defer-datasource-initialization=true,
-- necesarios para que Hibernate cree las tablas ANTES de que se inserten estos datos).
--
-- Contrasena de los dos usuarios de prueba: "password123"
-- Hash BCrypt verificado (no solo generado "a ojo"): se confirmo programaticamente
-- que decodifica exactamente a "password123" antes de incluirlo aqui.
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
(1, 'Facundo Admin', 'admin@tienda.com', '$2b$12$ylDNXZ1oSQn4/fRLLxMEb.dn3WjFHLRQ0wACJz239wR/i9ncFjjQK', 'ADMIN', NOW()),
(2, 'Cliente de Prueba', 'cliente@tienda.com', '$2b$12$ylDNXZ1oSQn4/fRLLxMEb.dn3WjFHLRQ0wACJz239wR/i9ncFjjQK', 'CLIENTE', NOW());

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
(1, 1, 'Notebook Gamer 15.6" RTX 4060', 'Lenovo', 'Procesador Intel Core i7, 16GB RAM, SSD 512GB, placa RTX 4060', 1450000.00, 8, 'https://images.unsplash.com/photo-1773418517576-f6e481c4feea?w=600&h=600&fit=crop&auto=format', true),
(2, 1, 'Notebook Ultrabook 14"', 'ASUS', 'Procesador Intel Core i5, 8GB RAM, SSD 256GB, ideal oficina', 780000.00, 15, 'https://images.unsplash.com/photo-1675868374373-3a01b8da5ed6?w=600&h=600&fit=crop&auto=format', true),
(3, 1, 'MacBook Air M2', 'Apple', 'Chip M2, 8GB RAM unificada, SSD 256GB', 1690000.00, 5, 'https://images.unsplash.com/photo-1767097587570-206837933fe0?w=600&h=600&fit=crop&auto=format', true),

-- Celulares
(4, 2, 'Galaxy S24', 'Samsung', '256GB, camara triple 50MP, pantalla AMOLED 6.2"', 980000.00, 20, 'https://images.unsplash.com/photo-1723054072995-af2b91c5cbb6?w=600&h=600&fit=crop&auto=format', true),
(5, 2, 'iPhone 15', 'Apple', '128GB, chip A16, camara dual 48MP', 1150000.00, 12, 'https://images.unsplash.com/photo-1753192698843-0ba22e4d927e?w=600&h=600&fit=crop&auto=format', true),
(6, 2, 'Redmi Note 13', 'Xiaomi', '128GB, camara 108MP, bateria 5000mAh', 320000.00, 30, 'https://images.unsplash.com/photo-1713990122367-1159d2f64eff?w=600&h=600&fit=crop&auto=format', true),

-- Audio
(7, 3, 'Auriculares Inalambricos WH-1000XM5', 'Sony', 'Cancelacion de ruido activa, 30hs de bateria', 450000.00, 18, 'https://images.unsplash.com/photo-1614860243518-c12eb2fdf66c?w=600&h=600&fit=crop&auto=format', true),
(8, 3, 'Parlante Bluetooth Portatil', 'JBL', 'Resistente al agua IP67, 12hs de bateria', 95000.00, 25, 'https://images.unsplash.com/photo-1563203425-a1c5aeeb2e94?w=600&h=600&fit=crop&auto=format', true),

-- Componentes PC
(9, 4, 'Placa de Video RTX 4070', 'NVIDIA', '12GB GDDR6X, ideal gaming en 1440p', 890000.00, 6, 'https://images.unsplash.com/photo-1751830631604-c25655ea9ffd?w=600&h=600&fit=crop&auto=format', true),
(10, 4, 'Memoria RAM 16GB DDR4', 'Kingston', '3200MHz, kit de 2x8GB', 65000.00, 40, 'https://images.unsplash.com/photo-1760623227551-2eae8f9cb675?w=600&h=600&fit=crop&auto=format', true),

-- Accesorios
(11, 5, 'Mouse Gamer Inalambrico', 'Logitech', 'Sensor 25600 DPI, bateria de larga duracion', 78000.00, 35, 'https://images.unsplash.com/photo-1554876194-024e06bbc3cf?w=600&h=600&fit=crop&auto=format', true),
(12, 5, 'Teclado Mecanico RGB', 'Redragon', 'Switches azules, retroiluminacion RGB personalizable', 110000.00, 22, 'https://images.unsplash.com/photo-1768407682629-f208cfe90627?w=600&h=600&fit=crop&auto=format', true),

-- Notebooks (mas opciones)
(13, 1, 'Notebook Gamer 17" RTX 4080', 'ASUS ROG', 'Procesador Intel Core i9, 32GB RAM, SSD 1TB, placa RTX 4080', 2350000.00, 4, 'https://images.unsplash.com/photo-1776243365820-f64005e4a204?w=600&h=600&fit=crop&auto=format', true),
(14, 1, 'Notebook Empresarial 14"', 'Dell', 'Procesador Intel Core i5, 16GB RAM, SSD 512GB, certificacion militar', 950000.00, 18, 'https://images.unsplash.com/photo-1637607698829-de4171988e79?w=600&h=600&fit=crop&auto=format', true),
(15, 1, 'MacBook Pro 14" M3', 'Apple', 'Chip M3 Pro, 18GB RAM unificada, SSD 512GB, pantalla Liquid Retina XDR', 2890000.00, 3, 'https://images.unsplash.com/photo-1525547719571-a2d4ac8945e2?w=600&h=600&fit=crop&auto=format', true),
(16, 1, 'Notebook Convertible 13"', 'HP', 'Pantalla tactil 360°, Intel Core i5, 8GB RAM, SSD 256GB', 720000.00, 14, 'https://images.unsplash.com/photo-1748878946701-5db0744fbbd8?w=600&h=600&fit=crop&auto=format', true),
(17, 1, 'Notebook Gamer 15.6" RTX 4050', 'Acer Predator', 'Procesador AMD Ryzen 7, 16GB RAM, SSD 512GB, pantalla 144Hz', 1180000.00, 9, 'https://images.unsplash.com/photo-1773418517576-f6e481c4feea?w=600&h=600&fit=crop&auto=format', true),
(18, 1, 'Chromebook 11"', 'Lenovo', 'Procesador Intel Celeron, 4GB RAM, 64GB almacenamiento, ChromeOS', 280000.00, 25, 'https://images.unsplash.com/photo-1675868374373-3a01b8da5ed6?w=600&h=600&fit=crop&auto=format', true),

-- Celulares (mas opciones)
(19, 2, 'Galaxy S24 Ultra', 'Samsung', '512GB, camara cuadruple 200MP, S Pen incluido', 1680000.00, 10, 'https://images.unsplash.com/photo-1746276584005-77376a4af877?w=600&h=600&fit=crop&auto=format', true),
(20, 2, 'iPhone 15 Pro Max', 'Apple', '256GB, chip A17 Pro, camara triple 48MP, titanio', 1890000.00, 7, 'https://images.unsplash.com/photo-1753192698843-0ba22e4d927e?w=600&h=600&fit=crop&auto=format', true),
(21, 2, 'Galaxy A55', 'Samsung', '128GB, camara triple 50MP, pantalla AMOLED 6.6"', 480000.00, 28, 'https://images.unsplash.com/photo-1723054072995-af2b91c5cbb6?w=600&h=600&fit=crop&auto=format', true),
(22, 2, 'Redmi Note 13 Pro', 'Xiaomi', '256GB, camara 200MP, carga rapida 67W', 420000.00, 22, 'https://images.unsplash.com/photo-1713990122367-1159d2f64eff?w=600&h=600&fit=crop&auto=format', true),
(23, 2, 'Moto Edge 50', 'Motorola', '256GB, pantalla curva AMOLED, resistente al agua', 510000.00, 16, 'https://images.unsplash.com/photo-1746276584005-77376a4af877?w=600&h=600&fit=crop&auto=format', true),
(24, 2, 'Pixel 8', 'Google', '128GB, chip Tensor G3, camara con IA avanzada', 890000.00, 11, 'https://images.unsplash.com/photo-1754650840542-c2048b77c75b?w=600&h=600&fit=crop&auto=format', true),
(25, 2, 'iPhone SE', 'Apple', '64GB, chip A15, diseño compacto, Touch ID', 540000.00, 19, 'https://images.unsplash.com/photo-1753192698843-0ba22e4d927e?w=600&h=600&fit=crop&auto=format', true),

-- Audio (mas opciones)
(26, 3, 'Auriculares In-Ear Pro', 'Apple', 'AirPods Pro 2, cancelacion de ruido activa, estuche con MagSafe', 380000.00, 24, 'https://images.unsplash.com/photo-1652819804299-eea887780ca7?w=600&h=600&fit=crop&auto=format', true),
(27, 3, 'Auriculares Gaming RGB', 'HyperX', 'Sonido envolvente 7.1, microfono desmontable', 135000.00, 20, 'https://images.unsplash.com/photo-1642243747081-eedd813fe7c9?w=600&h=600&fit=crop&auto=format', true),
(28, 3, 'Barra de Sonido 2.1', 'Samsung', 'Subwoofer inalambrico, Bluetooth, 300W', 290000.00, 12, 'https://images.unsplash.com/photo-1520390244437-6f1c5eae66ff?w=600&h=600&fit=crop&auto=format', true),
(29, 3, 'Parlante Bluetooth XL', 'Sony', 'Sonido extra bass, 24hs de bateria, resistente al agua', 175000.00, 17, 'https://images.unsplash.com/photo-1707055365112-b1fc71ba915d?w=600&h=600&fit=crop&auto=format', true),
(30, 3, 'Auriculares Inalambricos Deportivos', 'JBL', 'Resistentes al sudor, sujecion deportiva, 8hs de bateria', 85000.00, 30, 'https://images.unsplash.com/photo-1754142654796-cafbcb9f633b?w=600&h=600&fit=crop&auto=format', true),
(31, 3, 'Microfono de Streaming USB', 'HyperX', 'Condensador, patron cardioide, soporte antivibracion', 120000.00, 15, 'https://images.unsplash.com/photo-1642243747081-eedd813fe7c9?w=600&h=600&fit=crop&auto=format', true),

-- Componentes PC (mas opciones)
(32, 4, 'Placa de Video RTX 4090', 'NVIDIA', '24GB GDDR6X, ideal gaming 4K y produccion', 2100000.00, 2, 'https://images.unsplash.com/photo-1752179634046-5159d1b13f6f?w=600&h=600&fit=crop&auto=format', true),
(33, 4, 'Procesador AMD Ryzen 7', 'AMD', '8 nucleos, 16 hilos, hasta 5.4GHz', 380000.00, 13, 'https://images.unsplash.com/photo-1741392078112-f3a5fe979bcf?w=600&h=600&fit=crop&auto=format', true),
(34, 4, 'Procesador Intel Core i7', 'Intel', '14 nucleos, hasta 5.6GHz, socket LGA1700', 420000.00, 10, 'https://images.unsplash.com/photo-1769773724549-38e234f3018a?w=600&h=600&fit=crop&auto=format', true),
(35, 4, 'SSD NVMe 1TB', 'Samsung', 'Velocidad de lectura 7000MB/s, factor M.2', 95000.00, 35, 'https://images.unsplash.com/photo-1757083840018-cd665233a112?w=600&h=600&fit=crop&auto=format', true),
(36, 4, 'Fuente de Poder 750W', 'Corsair', '80 Plus Gold, modular, ventilador silencioso', 110000.00, 18, 'https://images.unsplash.com/photo-1761132046162-01e1b6d29dd7?w=600&h=600&fit=crop&auto=format', true),
(37, 4, 'Memoria RAM 32GB DDR5', 'Corsair', '6000MHz, kit de 2x16GB, disipador incluido', 180000.00, 16, 'https://images.unsplash.com/photo-1760623227551-2eae8f9cb675?w=600&h=600&fit=crop&auto=format', true),

-- Accesorios (mas opciones)
(38, 5, 'Monitor Gamer 27" 144Hz', 'LG', 'Panel IPS, resolucion 1440p, tiempo de respuesta 1ms', 480000.00, 11, 'https://images.unsplash.com/photo-1772114037582-633fe892763a?w=600&h=600&fit=crop&auto=format', true),
(39, 5, 'Webcam Full HD', 'Logitech', '1080p a 30fps, microfono integrado, enfoque automatico', 65000.00, 27, 'https://images.unsplash.com/photo-1725272413223-9eb0f5d7a6a4?w=600&h=600&fit=crop&auto=format', true),
(40, 5, 'Silla Gamer Ergonomica', 'Cougar', 'Reclinable 180°, soporte lumbar, apoyabrazos ajustables', 320000.00, 8, 'https://images.unsplash.com/photo-1770195483917-b3bb444b7a29?w=600&h=600&fit=crop&auto=format', true),
(41, 5, 'Hub USB-C 7 en 1', 'Anker', 'HDMI 4K, lector SD, 3 puertos USB 3.0', 55000.00, 33, 'https://images.unsplash.com/photo-1618499893452-942141785a2a?w=600&h=600&fit=crop&auto=format', true),
(42, 5, 'Cargador Inalambrico 15W', 'Belkin', 'Carga rapida Qi, compatible con iPhone y Android', 38000.00, 40, 'https://images.unsplash.com/photo-1616071358846-9f34f471815d?w=600&h=600&fit=crop&auto=format', true);

-- --------------------------------------------------------------------
-- Reiniciar los contadores AUTO_INCREMENT para que las proximas
-- inserciones (desde la app, vía POST) continuen despues del ultimo ID usado.
-- --------------------------------------------------------------------
ALTER TABLE usuarios AUTO_INCREMENT = 3;
ALTER TABLE categorias AUTO_INCREMENT = 6;
ALTER TABLE productos AUTO_INCREMENT = 43;
