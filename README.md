# Sistema de Inscripción Micro Servicios

## Descripción del Proyecto

Este proyecto es una evaluación de una herramienta de microservicios que gestiona usuarios, cursos e inscripciones, diseñada con una arquitectura moderna y escalable. El sistema está compuesto por múltiples servicios backend desarrollados en **Java** utilizando **Spring Boot** y conectados a bases de datos **PostgreSQL** y **MySQL**. El frontend está desarrollado en **Angular**, proporcionando una interfaz de usuario dinámica y responsiva.

### Componentes del Sistema

- **Microservicio de Usuarios**: Gestiona la creación, actualización, eliminación y consulta de usuarios. Este servicio se conecta a una base de datos **PostgreSQL** y utiliza un enfoque RESTful para exponer sus APIs.
- **Microservicio de Cursos**: Maneja la gestión de cursos, incluyendo la creación, modificación, consulta y eliminación de registros de cursos. Este servicio también utiliza **PostgreSQL** como su base de datos principal.

- **Microservicio de Inscripciones**: Gestiona las inscripciones de los usuarios en los cursos, permitiendo tanto la inscripción como la anulación. Este servicio está diseñado para interactuar con una base de datos **MySQL** para almacenar los datos de las inscripciones.

- **Frontend en Angular**: Proporciona una interfaz de usuario amigable y accesible, donde se pueden gestionar las funcionalidades de usuarios, cursos e inscripciones. El frontend se comunica con los microservicios a través de APIs REST, asegurando una experiencia de usuario fluida y eficiente.

### Tecnologías Utilizadas

- **Backend**: Java, Spring Boot, PostgreSQL, MySQL
- **Frontend**: Angular
- **Gestión de Microservicios**: Spring Cloud, Eureka (si es aplicable)
- **Base de Datos**: PostgreSQL, MySQL

### Funcionalidades Principales

- **Gestión de Usuarios**: Creación, edición, eliminación y visualización de perfiles de usuario.
- **Gestión de Cursos**: Administración de cursos, incluyendo la capacidad de agregar, modificar y eliminar cursos.
- **Inscripciones**: Módulo para que los usuarios se inscriban o desinscriban de los cursos disponibles.

### Objetivo del Proyecto

El objetivo principal de este proyecto es evaluar la eficiencia, escalabilidad y seguridad de una arquitectura basada en microservicios, así como la interoperabilidad entre diferentes tecnologías de bases de datos y la integración con un frontend moderno. La evaluación también incluye pruebas de rendimiento y seguridad para garantizar que el sistema pueda manejar grandes volúmenes de datos y usuarios de manera eficaz.
