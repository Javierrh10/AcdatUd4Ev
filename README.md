# 🎥 Gestión de Videoteca - MongoDB & Java

Este proyecto es una aplicación de consola desarrollada en Java que permite gestionar una base de datos documental utilizando **MongoDB**. El objetivo es aplicar los conceptos de persistencia NoSQL, flexibilidad de esquemas y acceso a datos desde aplicaciones modernas.

## 🚀 Características
- **Base de Datos NoSQL**: Uso de un modelo orientado a documentos (BSON).
- **Escalabilidad y Flexibilidad**: Los documentos no requieren un esquema fijo, permitiendo almacenar información heterogénea.
- **CRUD Completo**: Implementación de operaciones de creación, consulta, actualización y eliminación de documentos.
- **Relación Lógica**: Gestión de dos colecciones relacionadas (`peliculas` y `prestamos`) para mantener coherencia en el dominio.

## 🛠️ Tecnologías utilizadas
- **Java**: Lenguaje de programación principal.
- **MongoDB Atlas/Local**: Sistema de base de datos NoSQL.
- **Maven**: Gestión de dependencias (MongoDB Java Driver).

## 📋 Funcionalidades
1. **Ver Catálogo**: Consulta estructurada de documentos en la colección `peliculas`.
2. **Añadir Película**: Inserción de nuevos documentos con campos de texto y booleano.
3. **Registrar Préstamo**: Almacenamiento de información en una segunda colección relacionada.
4. **Actualización**: Modificación de campos específicos (disponibilidad).
5. **Eliminación**: Borrado de documentos según criterio.

## ⚙️ Configuración
Para ejecutar el proyecto, asegúrate de configurar tu URI de conexión en la clase `Main.java`:

```String uri = "tu_cadena_de_conexion_aqui";```

---
Desarrollado por Javierrh10 - 2026


