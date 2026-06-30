# Tienda-Electronica

Plataforma e-commerce de productos electrónicos con catálogo, carrito de compras, autenticación JWT y panel de administración.

## Stack

### Backend (`tienda-backend/`)
| Capa | Tecnología |
|------|------------|
| Lenguaje | Java 17 |
| Framework | Spring Boot 3.3.4 |
| Base de datos | MySQL (JPA / Hibernate) |
| Autenticación | JWT (jjwt 0.12.6) |
| Seguridad | Spring Security con roles ADMIN / CLIENTE |
| Documentación API | Swagger UI (springdoc-openapi) |
| Build | Maven + Docker multi-stage |
| Deploy | Render (contenedor Docker) |

### Frontend (`tienda-frontend/`)
| Capa | Tecnología |
|------|------------|
| Framework | React 18 |
| Build | Vite 5 |
| Estilos | Tailwind CSS 3 |
| Routing | React Router DOM 6 |
| Iconos | Lucide React |
| Deploy | Netlify (carpeta `dist/`) |

## Funcionalidades

### Usuarios
- Registro e inicio de sesión con JWT
- Roles: **CLIENTE** (compra) y **ADMIN** (gestión)

### Catálogo
- Navegación de productos por categorías
- Búsqueda y detalles de producto
- Imágenes reales vía Unsplash con fallback a iconos por categoría

### Carrito y Órdenes
- Agregar/remover productos al carrito
- Checkout con creación de orden
- Historial de órdenes del usuario

### Panel de Administración (`/admin`)
- Resumen con métricas (productos, órdenes, ingresos)
- CRUD de productos (crear, editar, eliminar, toggle activo)
- CRUD de categorías
- Gestión de órdenes (actualizar estado)
- Protegido solo para usuarios ADMIN

## Rutas del Frontend

| Ruta | Descripción | Acceso |
|------|-------------|--------|
| `/` | Catálogo de productos | Público |
| `/productos/:id` | Detalle de producto | Público |
| `/login` | Inicio de sesión | Público |
| `/registro` | Registro de usuario | Público |
| `/carrito` | Carrito de compras | Público |
| `/checkout` | Finalizar compra | CLIENTE / ADMIN |
| `/mis-ordenes` | Historial de órdenes | CLIENTE / ADMIN |
| `/admin` | Panel de administración | ADMIN |

## API REST (Backend)

Endpoints bajo `/api`:

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| POST | `/auth/register` | - | Registro |
| POST | `/auth/login` | - | Login (devuelve JWT) |
| GET | `/productos` | - | Listar productos |
| GET | `/productos/{id}` | - | Detalle producto |
| POST | `/productos` | ADMIN | Crear producto |
| PUT | `/productos/{id}` | ADMIN | Actualizar producto |
| PATCH | `/productos/{id}/activo` | ADMIN | Toggle activo |
| DELETE | `/productos/{id}` | ADMIN | Eliminar producto |
| GET | `/categorias` | - | Listar categorías |
| POST | `/categorias` | ADMIN | Crear categoría |
| PUT | `/categorias/{id}` | ADMIN | Actualizar categoría |
| DELETE | `/categorias/{id}` | ADMIN | Eliminar categoría |
| GET | `/ordenes/mis-ordenes` | CLIENTE/ADMIN | Órdenes del usuario |
| GET | `/ordenes` | ADMIN | Todas las órdenes |
| POST | `/ordenes` | CLIENTE/ADMIN | Crear orden |
| PATCH | `/ordenes/{id}/estado` | ADMIN | Actualizar estado |
| GET | `/usuarios` | ADMIN | Listar usuarios |

Documentación interactiva: `/swagger-ui.html`

## Desarrollo Local

### Requisitos
- Java 17+, Maven, MySQL, Node.js 18+

### 1. Base de datos
```sql
create database if not exists ecommerce_db;
```

### 2. Backend
```bash
cd tienda-backend/ecommerce
mvn spring-boot:run
```
Servidor en `http://localhost:8080`. El perfil por defecto usa `root` sin contraseña para MySQL local (configurable en `application.properties`).

### 3. Frontend
```bash
cd tienda-frontend
npm install
npm run dev
```
Servidor en `http://localhost:5173`. La conexión al backend se configura en `src/services/api.js`.

### Datos de prueba
Con `spring.sql.init.mode=always` (local), al iniciar se cargan 42 productos en pesos argentinos y dos usuarios de prueba:

| Email | Contraseña | Rol |
|-------|-----------|-----|
| `admin@tienda.com` | `password123` | ADMIN |
| `cliente@tienda.com` | `password123` | CLIENTE |

## Despliegue

### Backend (Render)
```bash
cd tienda-backend/ecommerce
git push  # Render detecta el Dockerfile y construye automáticamente
```
Variables de entorno requeridas en Render:
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `JWT_SECRET` (clave secreta para firmar tokens)

### Frontend (Netlify)
```bash
cd tienda-frontend
npm run build
# Subir carpeta dist/ a Netlify
```
La URL del backend se define en `src/services/api.js` (apuntar a la URL de Render).

## Estructura del Proyecto

```
Tienda-Electronica/
├── creacion-de-db/          # Scripts SQL de referencia
├── tienda-backend/
│   └── ecommerce/
│       ├── src/main/java/com/facundochacon/ecommerce/
│       │   ├── config/      # DataSeeder, CORS config
│       │   ├── controller/  # REST controllers
│       │   ├── dto/         # Data transfer objects
│       │   ├── exception/   # Manejo global de errores
│       │   ├── model/       # Entidades JPA
│       │   ├── repository/  # Repositorios Spring Data
│       │   ├── security/    # JWT filter, service, UserDetails
│       │   └── service/     # Lógica de negocio
│       ├── resources/       # application.properties, data.sql
│       └── Dockerfile
├── tienda-frontend/
│   ├── src/
│   │   ├── components/      # Navbar, ProductCard, Layout, etc.
│   │   ├── context/         # AuthContext, CarritoContext, TemaContext
│   │   ├── pages/           # Catálogo, Carrito, Checkout, Admin...
│   │   ├── services/        # api.js, authService, productoService...
│   │   └── utils/           # Funciones auxiliares
│   ├── dist/                # Build de producción
│   └── index.html
└── README.md
```
