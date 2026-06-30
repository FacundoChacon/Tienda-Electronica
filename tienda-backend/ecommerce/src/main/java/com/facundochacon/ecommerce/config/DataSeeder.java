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

        crearProducto(notebooks, "Notebook Dell XPS 15", "Dell", "Potente ultrabook con pantalla OLED 3.5K, Intel Core i7-13700H, 16GB RAM, 512GB SSD", new BigDecimal("1899.99"), 10, "https://images.unsplash.com/photo-1773418517576-f6e481c4feea?w=400&h=400&fit=crop");
        crearProducto(notebooks, "MacBook Air M3", "Apple", "Chip Apple M3, pantalla Liquid Retina 13.6\", 8GB RAM, 256GB SSD", new BigDecimal("1399.99"), 15, "https://images.unsplash.com/photo-1767097587570-206837933fe0?w=400&h=400&fit=crop");
        crearProducto(notebooks, "Notebook Lenovo ThinkPad X1 Carbon", "Lenovo", "Intel Core i7-1355U, 16GB RAM, 512GB SSD, pantalla 14\" WQUXGA", new BigDecimal("1599.99"), 8, "https://images.unsplash.com/photo-1675868374373-3a01b8da5ed6?w=400&h=400&fit=crop");
        crearProducto(notebooks, "Notebook HP Spectre x360", "HP", "Convertible 2-en-1, Intel Core i7, 16GB RAM, 1TB SSD, pantalla tactil 13.5\"", new BigDecimal("1499.99"), 12, "https://images.unsplash.com/photo-1776243365820-f64005e4a204?w=400&h=400&fit=crop");

        crearProducto(smartphones, "iPhone 16 Pro Max", "Apple", "Chip A18 Pro, pantalla Super Retina XDR 6.9\", 256GB, camara de 48MP", new BigDecimal("1199.99"), 20, "https://images.unsplash.com/photo-1753192698843-0ba22e4d927e?w=400&h=400&fit=crop");
        crearProducto(smartphones, "Samsung Galaxy S25 Ultra", "Samsung", "Snapdragon 8 Gen 4, pantalla Dynamic AMOLED 6.8\", 256GB, S Pen incluido", new BigDecimal("1299.99"), 18, "https://images.unsplash.com/photo-1723054072995-af2b91c5cbb6?w=400&h=400&fit=crop");
        crearProducto(smartphones, "Google Pixel 9 Pro", "Google", "Tensor G4, pantalla LTPO OLED 6.7\", 128GB, camara de 50MP con IA", new BigDecimal("999.99"), 14, "https://images.unsplash.com/photo-1754650840542-c2048b77c75b?w=400&h=400&fit=crop");
        crearProducto(smartphones, "Xiaomi 14 Pro", "Xiaomi", "Snapdragon 8 Gen 3, pantalla AMOLED 6.73\", 256GB, camara Leica de 50MP", new BigDecimal("899.99"), 16, "https://images.unsplash.com/photo-1713990122367-1159d2f64eff?w=400&h=400&fit=crop");

        crearProducto(tablets, "iPad Pro M4 11\"", "Apple", "Chip M4, pantalla Ultra Retina XDR, 256GB, compatible con Apple Pencil Pro", new BigDecimal("1099.99"), 10, "https://images.unsplash.com/photo-1753192698843-0ba22e4d927e?w=400&h=400&fit=crop");
        crearProducto(tablets, "Samsung Galaxy Tab S10 Ultra", "Samsung", "Pantalla Dynamic AMOLED 14.6\", 256GB, S Pen incluido, 5G", new BigDecimal("899.99"), 8, "https://images.unsplash.com/photo-1723054072995-af2b91c5cbb6?w=400&h=400&fit=crop");
        crearProducto(tablets, "Microsoft Surface Pro 10", "Microsoft", "Intel Core Ultra 7, 16GB RAM, 512GB SSD, pantalla 13\" PixelSense Flow", new BigDecimal("1299.99"), 6, "https://images.unsplash.com/photo-1776243365820-f64005e4a204?w=400&h=400&fit=crop");
        crearProducto(tablets, "Lenovo Tab P12", "Lenovo", "Pantalla 12.7\" 3K, MediaTek Dimensity 7050, 8GB RAM, 256GB, incluye lápiz", new BigDecimal("499.99"), 20, "https://images.unsplash.com/photo-1713990122367-1159d2f64eff?w=400&h=400&fit=crop");

        crearProducto(audifonos, "Sony WH-1000XM6", "Sony", "Cancelacion de ruido activa lider, 30h de bateria, audio LDAC de alta resolucion", new BigDecimal("349.99"), 25, "https://images.unsplash.com/photo-1614860243518-c12eb2fdf66c?w=400&h=400&fit=crop");
        crearProducto(audifonos, "AirPods Pro 3", "Apple", "Chip H3, cancelacion de ruido adaptativa, audio espacial personalizado, USB-C", new BigDecimal("249.99"), 30, "https://images.unsplash.com/photo-1520170350707-b2da59970118?w=400&h=400&fit=crop");
        crearProducto(audifonos, "Bose QuietComfort Ultra", "Bose", "Cancelacion de ruido legendaria, audio inmersivo con seguimiento de cabeza, 24h", new BigDecimal("429.99"), 15, "https://images.unsplash.com/photo-1754142654796-cafbcb9f633b?w=400&h=400&fit=crop");
        crearProducto(audifonos, "Sennheiser Momentum 4", "Sennheiser", "Audio premium con transductores de 42mm, cancelacion de ruido adaptativa, 60h", new BigDecimal("379.99"), 12, "https://images.unsplash.com/photo-1614860243518-c12eb2fdf66c?w=400&h=400&fit=crop");

        crearProducto(teclados, "Logitech MX Mechanical", "Logitech", "Teclado mecanico inalambrico, switches tactiles, retroiluminado, Bluetooth multipunto", new BigDecimal("199.99"), 20, "https://images.unsplash.com/photo-1768407682629-f208cfe90627?w=400&h=400&fit=crop");
        crearProducto(teclados, "Keychron Q1 Pro", "Keychron", "Teclado mecanico 75% inalambrico, carcasa de aluminio, switches Gateron Jupiter", new BigDecimal("219.99"), 14, "https://images.unsplash.com/photo-1733151535078-e2c8cf1ae18f?w=400&h=400&fit=crop");
        crearProducto(teclados, "Corsair K70 RGB Pro", "Corsair", "Teclado mecanico gaming, switches Cherry MX Red, frame de aluminio, cable USB-C", new BigDecimal("179.99"), 18, "https://images.unsplash.com/photo-1725272413223-9eb0f5d7a6a4?w=400&h=400&fit=crop");
        crearProducto(teclados, "Razer BlackWidow V4 75%", "Razer", "Teclado gaming compacto, switches Razer Orange, hot-swap, iluminacion Chroma", new BigDecimal("159.99"), 22, "https://images.unsplash.com/photo-1768407682629-f208cfe90627?w=400&h=400&fit=crop");

        crearProducto(mouse, "Logitech MX Master 3S", "Logitech", "Mouse ergonomico inalambrico, sensor 8000 DPI, scroll MagSpeed, USB-C, 70 dias de bateria", new BigDecimal("99.99"), 30, "https://images.unsplash.com/photo-1616071358846-9f34f471815d?w=400&h=400&fit=crop");
        crearProducto(mouse, "Razer DeathAdder V3 Pro", "Razer", "Mouse gaming inalambrico, 63g, sensor Focus Pro 30K, 90h de bateria", new BigDecimal("149.99"), 20, "https://images.unsplash.com/photo-1554876194-024e06bbc3cf?w=400&h=400&fit=crop");
        crearProducto(mouse, "Apple Magic Mouse", "Apple", "Mouse inalambrico, superficie Multi-Touch, recargable via USB-C, diseno minimalista", new BigDecimal("79.99"), 25, "https://images.unsplash.com/photo-1733151535078-e2c8cf1ae18f?w=400&h=400&fit=crop");
        crearProducto(mouse, "SteelSeries Aerox 9", "SteelSeries", "Mouse gaming ultraliviano 69g, 18 botones programables, sensor TrueMove Air", new BigDecimal("129.99"), 16, "https://images.unsplash.com/photo-1544966326-6d858fb3f74d?w=400&h=400&fit=crop");

        crearProducto(monitores, "Monitor LG UltraGear 27\" 4K OLED", "LG", "Panel OLED 27\", 4K UHD, 240Hz, 0.03ms GtG, HDMI 2.1, compatible G-Sync", new BigDecimal("499.99"), 10, "https://images.unsplash.com/photo-1772114037582-633fe892763a?w=400&h=400&fit=crop");
        crearProducto(monitores, "Monitor Dell S2722QC 27\" 4K", "Dell", "Panel IPS 27\", 4K UHD, USB-C 65W, parlantes integrados, ideal para oficina", new BigDecimal("399.99"), 15, "https://images.unsplash.com/photo-1632603093711-0d93a0bcc6cc?w=400&h=400&fit=crop");
        crearProducto(monitores, "Monitor Samsung Odyssey G7 32\"", "Samsung", "Panel VA Curvo 1000R, 32\", QHD, 240Hz, 1ms, HDR600, G-Sync", new BigDecimal("699.99"), 8, "https://images.unsplash.com/photo-1762219214808-154d74e0d761?w=400&h=400&fit=crop");
        crearProducto(monitores, "Apple Studio Display 27\"", "Apple", "Pantalla 5K Retina 27\", 600 nits, True Tone, camara 12MP, 6 parlantes", new BigDecimal("1599.99"), 6, "https://images.unsplash.com/photo-1775410631936-7de96322df0b?w=400&h=400&fit=crop");

        crearProducto(almacenamiento, "Samsung SSD 990 Pro 2TB", "Samsung", "SSD NVMe M.2, lecturas 7450 MB/s, escrituras 6900 MB/s, PCIe 4.0", new BigDecimal("249.99"), 25, "https://images.unsplash.com/photo-1757083840018-cd665233a112?w=400&h=400&fit=crop");
        crearProducto(almacenamiento, "WD Black SN850X 1TB", "WD", "SSD NVMe M.2, lecturas 7300 MB/s, escrituras 6300 MB/s, Game Mode 2.0", new BigDecimal("179.99"), 30, "https://images.unsplash.com/photo-1760623227551-2eae8f9cb675?w=400&h=400&fit=crop");
        crearProducto(almacenamiento, "Seagate Expansion Desktop 5TB", "Seagate", "Disco duro externo 5TB, USB 3.0, plug-and-play, ideal para backups", new BigDecimal("129.99"), 20, "https://images.unsplash.com/photo-1757083840018-cd665233a112?w=400&h=400&fit=crop");
        crearProducto(almacenamiento, "SanDisk Extreme Pro 1TB", "SanDisk", "SSD portatil 1TB, USB-C 20 Gbps, resistencia IP55, 2000 MB/s", new BigDecimal("149.99"), 18, "https://images.unsplash.com/photo-1760623227551-2eae8f9cb675?w=400&h=400&fit=crop");

        crearProducto(accesorios, "Anker Hub USB-C 7-en-1", "Anker", "Hub USB-C con HDMI 4K, 2x USB-A, USB-C PD 100W, lector SD, Ethernet", new BigDecimal("49.99"), 40, "https://images.unsplash.com/photo-1618499893452-942141785a2a?w=400&h=400&fit=crop");
        crearProducto(accesorios, "Soporte Ajustable Notebook Ergotron", "Ergotron", "Soporte de escritorio ajustable, compatible con notebooks hasta 17\", alivio cervical", new BigDecimal("89.99"), 20, "https://images.unsplash.com/photo-1772114037582-633fe892763a?w=400&h=400&fit=crop");
        crearProducto(accesorios, "Apple AirTag 4 Pack", "Apple", "Localizador Bluetooth, red de busqueda Find My, resistente al agua, bateria reemplazable", new BigDecimal("99.99"), 35, "https://images.unsplash.com/photo-1652819804299-eea887780ca7?w=400&h=400&fit=crop");
        crearProducto(accesorios, "Belkin Cargador Inalambrico 3-en-1", "Belkin", "Carga simultanea para iPhone, Apple Watch y AirPods, 15W, certificado Qi2", new BigDecimal("69.99"), 25, "https://images.unsplash.com/photo-1616071358846-9f34f471815d?w=400&h=400&fit=crop");

        crearProducto(impresion, "HP LaserJet Pro M404dn", "HP", "Impresora laser blanco y negro, 38 ppm, duplex automatico, red Ethernet", new BigDecimal("299.99"), 10, "https://images.unsplash.com/photo-1770017863955-56a1501a3c93?w=400&h=400&fit=crop");
        crearProducto(impresion, "Epson EcoTank ET-5850", "Epson", "Impresora multifuncion tanque de tinta, Wifi, duplex, hasta 20000 paginas incluidas", new BigDecimal("499.99"), 8, "https://images.unsplash.com/photo-1642243747081-eedd813fe7c9?w=400&h=400&fit=crop");
        crearProducto(impresion, "Brother HL-L2370DW", "Brother", "Impresora laser inalambrica, 32 ppm, duplex automatico, 250 hojas de entrada", new BigDecimal("179.99"), 15, "https://images.unsplash.com/photo-1770017863955-56a1501a3c93?w=400&h=400&fit=crop");
        crearProducto(impresion, "Canon PIXMA G6020", "Canon", "Impresora multifuncion tanque de tinta, color, Wifi, hasta 11000 paginas incluidas", new BigDecimal("249.99"), 12, "https://images.unsplash.com/photo-1642243747081-eedd813fe7c9?w=400&h=400&fit=crop");

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
