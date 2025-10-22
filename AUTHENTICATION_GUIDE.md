# ✅ Sistema de Autenticación Implementado

## 🎉 Implementación Completada

Se ha implementado exitosamente el sistema de autenticación JWT en el backend. Los siguientes componentes fueron creados:

### 📦 Dependencias Agregadas
- ✅ JJWT (JSON Web Token) v0.11.5
- ✅ Spring Security Crypto (BCrypt)

### 📝 Archivos Creados

1. **DTOs** (`src/main/java/com/wikigroup/desarrolloweb/dtos/`)
   - ✅ `LoginRequest.java` - Request para login
   - ✅ `RegisterRequest.java` - Request para registro
   - ✅ `AuthResponse.java` - Response con token y usuario

2. **Utilidades** (`src/main/java/com/wikigroup/desarrolloweb/util/`)
   - ✅ `JwtUtil.java` - Generación y validación de tokens JWT

3. **Servicios** (`src/main/java/com/wikigroup/desarrolloweb/service/`)
   - ✅ `AuthService.java` - Lógica de negocio para autenticación

4. **Controladores** (`src/main/java/com/wikigroup/desarrolloweb/controller/`)
   - ✅ `AuthController.java` - Endpoints REST

5. **Configuración**
   - ✅ `application.properties` - Configuración JWT
   - ✅ `CorsConfig.java` - Configuración CORS (ya existía)

### 📊 Modelo Actualizado
- ✅ `Usuario.java` - Agregado campo `rol`
- ✅ `UsuarioDto.java` - Agregados campos `password` y `rol`
- ✅ `UsuarioRepository.java` - Agregados métodos `findByEmail()` y `existsByEmail()`

---

## 🚀 Endpoints Disponibles

### 1. **Registro de Usuario** (POST)
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json
```

**Request Body:**
```json
{
  "empresa": {
    "nombre": "Mi Empresa S.A.S.",
    "descripcion": "Empresa de prueba"
  },
  "usuario": {
    "nombre": "Juan Pérez",
    "email": "juan@miempresa.com",
    "password": "123456"
  }
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuQG1p...",
  "usuario": {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan@miempresa.com",
    "password": null,
    "rol": "ADMINISTRADOR",
    "empresaId": 1
  }
}
```

---

### 2. **Login de Usuario** (POST)
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json
```

**Request Body:**
```json
{
  "correo": "juan@miempresa.com",
  "password": "123456"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuQG1p...",
  "usuario": {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan@miempresa.com",
    "password": null,
    "rol": "ADMINISTRADOR",
    "empresaId": 1
  }
}
```

---

## 🧪 Pruebas con curl

### Registro:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "empresa": {
      "nombre": "Test Company",
      "descripcion": "Una empresa de prueba"
    },
    "usuario": {
      "nombre": "Admin User",
      "email": "admin@test.com",
      "password": "123456"
    }
  }'
```

### Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "correo": "admin@test.com",
    "password": "123456"
  }'
```

---

## 🔐 Seguridad Implementada

1. **BCrypt Password Encoding**
   - ✅ Las contraseñas se hashean con BCrypt
   - ✅ Nunca se almacenan en texto plano
   - ✅ No se devuelven en las respuestas (se setean a `null`)

2. **JWT Tokens**
   - ✅ Generados con HS256
   - ✅ Duración: 24 horas (86400000 ms)
   - ✅ Contienen: email, empresaId, rol
   - ✅ Firmados con clave secreta

3. **Validaciones**
   - ✅ Verificación de email duplicado en registro
   - ✅ Validación de credenciales en login
   - ✅ Manejo de errores con mensajes apropiados

---

## ⚙️ Configuración JWT

**application.properties:**
```properties
jwt.secret=mi-clave-secreta-super-segura-cambiar-en-produccion-debe-tener-al-menos-256-bits
jwt.expiration=86400000
```

⚠️ **IMPORTANTE:** 
- Cambiar `jwt.secret` en producción
- Usar una clave de al menos 256 bits
- NO commitear la clave secreta en Git

---

## 🔄 Flujo de Autenticación

### Registro:
```
Frontend → POST /api/auth/register
    ↓
Backend crea Empresa
    ↓
Backend crea Usuario (rol: ADMINISTRADOR)
    ↓
Backend hashea password con BCrypt
    ↓
Backend genera JWT token
    ↓
Backend retorna {token, usuario}
    ↓
Frontend guarda token en localStorage
```

### Login:
```
Frontend → POST /api/auth/login
    ↓
Backend busca usuario por email
    ↓
Backend verifica password con BCrypt
    ↓
Backend genera JWT token
    ↓
Backend retorna {token, usuario}
    ↓
Frontend guarda token en localStorage
```

---

## 🗄️ Base de Datos

**Tabla `usuarios` actualizada:**
```sql
CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),  -- Hash BCrypt
    rol VARCHAR(255),       -- ADMINISTRADOR, EDITOR, SOLO_LECTURA
    empresa_id BIGINT,
    FOREIGN KEY (empresa_id) REFERENCES empresas(id)
);
```

---

## ✅ Estado del Backend

- ✅ Servidor corriendo en: `http://localhost:8080`
- ✅ Base de datos: H2 en memoria
- ✅ Consola H2: `http://localhost:8080/h2-console`
- ✅ CORS habilitado para `http://localhost:4200`
- ✅ Endpoints de autenticación funcionando

---

## 🎯 Próximos Pasos

1. **Probar desde el frontend Angular**
   - El frontend ya debería poder registrarse y hacer login
   - Los errores de CORS están resueltos
   - Los endpoints están disponibles

2. **Implementar Guards en Angular** (opcional)
   - Proteger rutas con AuthGuard
   - Interceptor HTTP para agregar el token a las peticiones

3. **Validaciones adicionales** (opcional)
   - Validar formato de email
   - Validar longitud mínima de password
   - Validar campos requeridos

4. **Para producción:**
   - Cambiar `jwt.secret` a una clave más segura
   - Configurar base de datos MySQL
   - Implementar refresh tokens
   - Agregar rate limiting

---

## 📝 Notas

- Las contraseñas se hashean automáticamente con BCrypt
- El primer usuario de cada empresa se crea como ADMINISTRADOR
- Los tokens JWT expiran en 24 horas
- La configuración CORS permite peticiones desde Angular (localhost:4200)

---

✅ **El backend está listo para recibir peticiones de autenticación desde el frontend Angular.**
