🗺️ MetaMapa – Plataforma de Mapeo Colaborativo

MetaMapa es una plataforma de mapeo colaborativo y visualización de eventos geolocalizados, desarrollada como parte del Trabajo Práctico Anual de la materia Diseño de Sistemas de Información

El sistema permite visualizar, crear y gestionar eventos geolocalizados a partir de datasets, aportes de usuarios e integración con APIs externas, exponiendo su funcionalidad mediante una API REST.


📌 Contexto del Proyecto

El proyecto fue desarrollado siguiendo un esquema de 7 entregas progresivas, abarcando distintas etapas del diseño de sistemas:

- Modelado orientado a objetos  
- Diseño del dominio y reglas de negocio  
- Arquitectura en capas  
- Persistencia de datos  
- Diseño e implementación de APIs REST  
- Validaciones y manejo de errores  

El objetivo principal fue construir un backend escalable, mantenible y correctamente estructurado, aplicando buenas prácticas de ingeniería de software.


🎯 Objetivos del Sistema

- Gestionar eventos geolocalizados de forma colaborativa  
- Integrar información proveniente de fuentes externas  
- Aplicar reglas de negocio y validaciones del dominio  
- Exponer la funcionalidad a través de una API REST clara y consistente  
- Mantener una adecuada separación de responsabilidades  


🏗️ Arquitectura

El backend está implementado utilizando una arquitectura en capas, inspirada en principios de microservicios, con una clara separación entre:

- Capa de servicios: entidades, reglas de negocio y lógica central  
- Capa de repositorios: persistencia 
- Capa de presentación: controladores REST  

Este enfoque permite mejorar:

- La mantenibilidad del código  
- La escalabilidad del sistema  
- La claridad y organización del proyecto  

🧩 Funcionalidades Principales

- 📍 Creación y visualización de eventos geolocalizados  
- 👥 Aportes y participación de usuarios  
- 🗂️ Ingesta de eventos a partir de datasets  
- 🔌 Integración con APIs externas  
- ✅ Validación de reglas de negocio  
- 📊 Modelo de dominio estructurado  


🛠️ Tecnologías Utilizadas

Backend
- Java  
- Spring Boot  
- Spring Web (API REST)  
- Hibernate / JPA  
- Maven  

Base de Datos
- MySQL   
- Persistencia mediante ORM  

Herramientas
- Git y GitHub  
- Postman (testing de endpoints)  
