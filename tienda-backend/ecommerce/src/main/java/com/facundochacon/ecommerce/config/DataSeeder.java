package com.facundochacon.ecommerce.config;

import com.facundochacon.ecommerce.model.Categoria;
import com.facundochacon.ecommerce.model.Producto;
import com.facundochacon.ecommerce.model.Usuario;
import com.facundochacon.ecommerce.repository.CategoriaRepository;
import com.facundochacon.ecommerce.repository.ProductoRepository;
import com.facundochacon.ecommerce.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.findByEmail("admin@tienda.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setEmail("admin@tienda.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRol(Usuario.Rol.ADMIN);
            usuarioRepository.save(admin);
            log.info("Usuario admin creado: admin@tienda.com / admin123");
        }

        if (categoriaRepository.count() > 0) {
            log.info("Las categorias ya existen, se omite la creacion");
            return;
        }

        Categoria notebooks = crearCategoria("Notebooks", "Laptops y ultrabooks de alto rendimiento");
        Categoria smartphones = crearCategoria("Smartphones", "Telefonos inteligentes de ultima generacion");
        Categoria tablets = crearCategoria("Tablets", "Tablets y iPads para trabajo y entretenimiento");
        Categoria audifonos = crearCategoria("Audifonos", "Audifonos inalambricos y cascos con cancelacion de ruido");
        Categoria teclados = crearCategoria("Teclados", "Teclados mecanicos e inalambricos");
        Categoria mouse = crearCategoria("Mouse", "Ratones ergonomicos y gaming");
        Categoria monitores = crearCategoria("Monitores", "Monitores 4K y gaming de alta tasa de refresco");
        Categoria almacenamiento = crearCategoria("Almacenamiento", "Discos SSD, HDD y memorias USB");
        Categoria accesorios = crearCategoria("Accesorios", "Hubs, soportes, cargadores y mas");
        Categoria impresion = crearCategoria("Impresion", "Impresoras laser y multifuncion");

        crearProducto(notebooks, "Notebook Dell XPS 15", "Dell", "Potente ultrabook con pantalla OLED 3.5K, Intel Core i7-13700H, 16GB RAM, 512GB SSD", new BigDecimal("1899.99"), 10, "https://picsum.photos/seed/dell-xps/400/400");
        crearProducto(notebooks, "MacBook Air M3", "Apple", "Chip Apple M3, pantalla Liquid Retina 13.6\", 8GB RAM, 256GB SSD", new BigDecimal("1399.99"), 15, "https://picsum.photos/seed/macbook-air/400/400");
        crearProducto(notebooks, "Notebook Lenovo ThinkPad X1 Carbon", "Lenovo", "Intel Core i7-1355U, 16GB RAM, 512GB SSD, pantalla 14\" WQUXGA", new BigDecimal("1599.99"), 8, "https://picsum.photos/seed/thinkpad/400/400");
        crearProducto(notebooks, "Notebook HP Spectre x360", "HP", "Convertible 2-en-1, Intel Core i7, 16GB RAM, 1TB SSD, pantalla tactil 13.5\"", new BigDecimal("1499.99"), 12, "https://picsum.photos/seed/hp-spectre/400/400");

        crearProducto(smartphones, "iPhone 16 Pro Max", "Apple", "Chip A18 Pro, pantalla Super Retina XDR 6.9\", 256GB, camara de 48MP", new BigDecimal("1199.99"), 20, "https://picsum.photos/seed/iphone-16/400/400");
        crearProducto(smartphones, "Samsung Galaxy S25 Ultra", "Samsung", "Snapdragon 8 Gen 4, pantalla Dynamic AMOLED 6.8\", 256GB, S Pen incluido", new BigDecimal("1299.99"), 18, "https://picsum.photos/seed/galaxy-s25/400/400");
        crearProducto(smartphones, "Google Pixel 9 Pro", "Google", "Tensor G4, pantalla LTPO OLED 6.7\", 128GB, camara de 50MP con IA", new BigDecimal("999.99"), 14, "https://picsum.photos/seed/pixel-9/400/400");
        crearProducto(smartphones, "Xiaomi 14 Pro", "Xiaomi", "Snapdragon 8 Gen 3, pantalla AMOLED 6.73\", 256GB, camara Leica de 50MP", new BigDecimal("899.99"), 16, "https://picsum.photos/seed/xiaomi-14/400/400");

        crearProducto(tablets, "iPad Pro M4 11\"", "Apple", "Chip M4, pantalla Ultra Retina XDR, 256GB, compatible con Apple Pencil Pro", new BigDecimal("1099.99"), 10, "https://picsum.photos/seed/ipad-pro/400/400");
        crearProducto(tablets, "Samsung Galaxy Tab S10 Ultra", "Samsung", "Pantalla Dynamic AMOLED 14.6\", 256GB, S Pen incluido, 5G", new BigDecimal("899.99"), 8, "https://picsum.photos/seed/galaxy-tab/400/400");
        crearProducto(tablets, "Microsoft Surface Pro 10", "Microsoft", "Intel Core Ultra 7, 16GB RAM, 512GB SSD, pantalla 13\" PixelSense Flow", new BigDecimal("1299.99"), 6, "https://picsum.photos/seed/surface-pro/400/400");
        crearProducto(tablets, "Lenovo Tab P12", "Lenovo", "Pantalla 12.7\" 3K, MediaTek Dimensity 7050, 8GB RAM, 256GB, incluye lápiz", new BigDecimal("499.99"), 20, "https://picsum.photos/seed/lenovo-tab/400/400");

        crearProducto(audifonos, "Sony WH-1000XM6", "Sony", "Cancelacion de ruido activa lider, 30h de bateria, audio LDAC de alta resolucion", new BigDecimal("349.99"), 25, "https://picsum.photos/seed/sony-xm6/400/400");
        crearProducto(audifonos, "AirPods Pro 3", "Apple", "Chip H3, cancelacion de ruido adaptativa, audio espacial personalizado, USB-C", new BigDecimal("249.99"), 30, "https://picsum.photos/seed/airpods-pro/400/400");
        crearProducto(audifonos, "Bose QuietComfort Ultra", "Bose", "Cancelacion de ruido legendaria, audio inmersivo con seguimiento de cabeza, 24h", new BigDecimal("429.99"), 15, "https://picsum.photos/seed/bose-qc/400/400");
        crearProducto(audifonos, "Sennheiser Momentum 4", "Sennheiser", "Audio premium con transductores de 42mm, cancelacion de ruido adaptativa, 60h", new BigDecimal("379.99"), 12, "https://picsum.photos/seed/sennheiser-m4/400/400");

        crearProducto(teclados, "Logitech MX Mechanical", "Logitech", "Teclado mecanico inalambrico, switches tactiles, retroiluminado, Bluetooth multipunto", new BigDecimal("199.99"), 20, "https://picsum.photos/seed/mx-mechanical/400/400");
        crearProducto(teclados, "Keychron Q1 Pro", "Keychron", "Teclado mecanico 75% inalambrico, carcasa de aluminio, switches Gateron Jupiter", new BigDecimal("219.99"), 14, "https://picsum.photos/seed/keychron-q1/400/400");
        crearProducto(teclados, "Corsair K70 RGB Pro", "Corsair", "Teclado mecanico gaming, switches Cherry MX Red, frame de aluminio, cable USB-C", new BigDecimal("179.99"), 18, "https://picsum.photos/seed/corsair-k70/400/400");
        crearProducto(teclados, "Razer BlackWidow V4 75%", "Razer", "Teclado gaming compacto, switches Razer Orange, hot-swap, iluminacion Chroma", new BigDecimal("159.99"), 22, "https://picsum.photos/seed/razer-bw/400/400");

        crearProducto(mouse, "Logitech MX Master 3S", "Logitech", "Mouse ergonomico inalambrico, sensor 8000 DPI, scroll MagSpeed, USB-C, 70 dias de bateria", new BigDecimal("99.99"), 30, "https://picsum.photos/seed/mx-master/400/400");
        crearProducto(mouse, "Razer DeathAdder V3 Pro", "Razer", "Mouse gaming inalambrico, 63g, sensor Focus Pro 30K, 90h de bateria", new BigDecimal("149.99"), 20, "https://picsum.photos/seed/deathadder/400/400");
        crearProducto(mouse, "Apple Magic Mouse", "Apple", "Mouse inalambrico, superficie Multi-Touch, recargable via USB-C, diseno minimalista", new BigDecimal("79.99"), 25, "https://picsum.photos/seed/magic-mouse/400/400");
        crearProducto(mouse, "SteelSeries Aerox 9", "SteelSeries", "Mouse gaming ultraliviano 69g, 18 botones programables, sensor TrueMove Air", new BigDecimal("129.99"), 16, "https://picsum.photos/seed/aerox-9/400/400");

        crearProducto(monitores, "Monitor LG UltraGear 27\" 4K OLED", "LG", "Panel OLED 27\", 4K UHD, 240Hz, 0.03ms GtG, HDMI 2.1, compatible G-Sync", new BigDecimal("499.99"), 10, "https://picsum.photos/seed/lg-ultragear/400/400");
        crearProducto(monitores, "Monitor Dell S2722QC 27\" 4K", "Dell", "Panel IPS 27\", 4K UHD, USB-C 65W, parlantes integrados, ideal para oficina", new BigDecimal("399.99"), 15, "https://picsum.photos/seed/dell-s2722/400/400");
        crearProducto(monitores, "Monitor Samsung Odyssey G7 32\"", "Samsung", "Panel VA Curvo 1000R, 32\", QHD, 240Hz, 1ms, HDR600, G-Sync", new BigDecimal("699.99"), 8, "https://picsum.photos/seed/odyssey-g7/400/400");
        crearProducto(monitores, "Apple Studio Display 27\"", "Apple", "Pantalla 5K Retina 27\", 600 nits, True Tone, camara 12MP, 6 parlantes", new BigDecimal("1599.99"), 6, "https://picsum.photos/seed/studio-display/400/400");

        crearProducto(almacenamiento, "Samsung SSD 990 Pro 2TB", "Samsung", "SSD NVMe M.2, lecturas 7450 MB/s, escrituras 6900 MB/s, PCIe 4.0", new BigDecimal("249.99"), 25, "https://picsum.photos/seed/samsung-990/400/400");
        crearProducto(almacenamiento, "WD Black SN850X 1TB", "WD", "SSD NVMe M.2, lecturas 7300 MB/s, escrituras 6300 MB/s, Game Mode 2.0", new BigDecimal("179.99"), 30, "https://picsum.photos/seed/wd-sn850/400/400");
        crearProducto(almacenamiento, "Seagate Expansion Desktop 5TB", "Seagate", "Disco duro externo 5TB, USB 3.0, plug-and-play, ideal para backups", new BigDecimal("129.99"), 20, "https://picsum.photos/seed/seagate-5tb/400/400");
        crearProducto(almacenamiento, "SanDisk Extreme Pro 1TB", "SanDisk", "SSD portatil 1TB, USB-C 20 Gbps, resistencia IP55, 2000 MB/s", new BigDecimal("149.99"), 18, "https://picsum.photos/seed/sandisk-extreme/400/400");

        crearProducto(accesorios, "Anker Hub USB-C 7-en-1", "Anker", "Hub USB-C con HDMI 4K, 2x USB-A, USB-C PD 100W, lector SD, Ethernet", new BigDecimal("49.99"), 40, "https://picsum.photos/seed/anker-hub/400/400");
        crearProducto(accesorios, "Soporte Ajustable Notebook Ergotron", "Ergotron", "Soporte de escritorio ajustable, compatible con notebooks hasta 17\", alivio cervical", new BigDecimal("89.99"), 20, "https://picsum.photos/seed/ergotron/400/400");
        crearProducto(accesorios, "Apple AirTag 4 Pack", "Apple", "Localizador Bluetooth, red de busqueda Find My, resistente al agua, bateria reemplazable", new BigDecimal("99.99"), 35, "https://picsum.photos/seed/airtag/400/400");
        crearProducto(accesorios, "Belkin Cargador Inalambrico 3-en-1", "Belkin", "Carga simultanea para iPhone, Apple Watch y AirPods, 15W, certificado Qi2", new BigDecimal("69.99"), 25, "https://picsum.photos/seed/belkin-charger/400/400");

        crearProducto(impresion, "HP LaserJet Pro M404dn", "HP", "Impresora laser blanco y negro, 38 ppm, duplex automatico, red Ethernet", new BigDecimal("299.99"), 10, "https://picsum.photos/seed/hp-laserjet/400/400");
        crearProducto(impresion, "Epson EcoTank ET-5850", "Epson", "Impresora multifuncion tanque de tinta, Wifi, duplex, hasta 20000 paginas incluidas", new BigDecimal("499.99"), 8, "https://picsum.photos/seed/epson-ecotank/400/400");
        crearProducto(impresion, "Brother HL-L2370DW", "Brother", "Impresora laser inalambrica, 32 ppm, duplex automatico, 250 hojas de entrada", new BigDecimal("179.99"), 15, "https://picsum.photos/seed/brother-hl/400/400");
        crearProducto(impresion, "Canon PIXMA G6020", "Canon", "Impresora multifuncion tanque de tinta, color, Wifi, hasta 11000 paginas incluidas", new BigDecimal("249.99"), 12, "https://picsum.photos/seed/canon-pixma/400/400");

        log.info("Seed completado: 10 categorias y 40 productos creados");
    }

    private Categoria crearCategoria(String nombre, String descripcion) {
        Categoria c = new Categoria();
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        return categoriaRepository.save(c);
    }

    private void crearProducto(Categoria categoria, String nombre, String marca,
                               String descripcion, BigDecimal precio, int stock, String imagenUrl) {
        Producto p = new Producto();
        p.setCategoria(categoria);
        p.setNombre(nombre);
        p.setMarca(marca);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setImagenUrl(imagenUrl);
        p.setActivo(true);
        productoRepository.save(p);
    }
}
