# Desarrollo Web 

Este proyecto es una aplicación de backend en **Spring Boot** que permite gestionar **Processes**, **Activities** y **Edges**.  
Proporciona operaciones **CRUD** y una API **RESTful** para interactuar con la base de datos **MySQL**.

---

## Tecnologías

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- REST API
- Maven

---

## Estructura del proyecto

- **model**: Entidades JPA (`Process`, `Activity`, `Edge`)  
- **repository**: Interfaces de acceso a datos  
- **service**: Servicios con la lógica de negocio y métodos CRUD  
- **controller**: Controladores REST para exponer las APIs

---

## Endpoints principales

### Processes
- `GET /api/processes` → Listar todos los processes  
- `GET /api/processes/{id}` → Obtener process por ID  
- `POST /api/processes` → Crear un process  
- `PUT /api/processes/{id}` → Actualizar un process  
- `DELETE /api/processes/{id}` → Eliminar un process

### Activities
- `GET /api/activities` → Listar todas las actividades  
- `GET /api/activities/{id}` → Obtener actividad por ID  
- `POST /api/activities` → Crear una actividad  
- `PUT /api/activities/{id}` → Actualizar actividad  
- `DELETE /api/activities/{id}` → Eliminar actividad

### Edges
- `GET /api/edges` → Listar todos los edges  
- `GET /api/edges/{id}` → Obtener edge por ID  
- `POST /api/edges` → Crear un edge  
- `PUT /api/edges/{id}` → Actualizar edge  
- `DELETE /api/edges/{id}` → Eliminar edge

---

## Configuración de la base de datos

En `src/main/resources/application.properties`:

`spring.application.name=DesarrolloWeb
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/tu_base_de_datos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver`

## Cómo ejecutar

1. Clonar el repositorio
2. Configurar la base de datos en application.properties
3. Ejecutar con Maven:

`mvn clean install
mvn spring-boot:run`

4. Acceder a los endpoints vía Postman, navegador o frontend.

### Notas

- La aplicación maneja relaciones entre Process, Activity y Edge.

- Cada entidad tiene sus operaciones CRUD listas.
