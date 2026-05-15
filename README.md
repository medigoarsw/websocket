# MediGo WebSocket Service

Este es el microservicio de WebSockets para la plataforma MediGo. Se encarga de la comunicación en tiempo real para subastas y seguimiento de logística (GPS).

## Tecnologías

- **Java 21**
- **Spring Boot 3.1.5**
- **Spring WebSocket (STOMP)**
- **Redis Pub/Sub** (para comunicación entre microservicios)
- **JUnit 5 & Mockito** (Pruebas unitarias)
- **JaCoCo** (Cobertura de código)

## Funcionalidades Principales

1.  **Subastas en Tiempo Real**: Recibe ofertas vía WebSocket y las reenvía al backend. Escucha eventos de Redis para notificar cambios de precio y nuevas ofertas a los clientes suscritos.
2.  **Seguimiento Logístico**: Retransmite coordenadas GPS de repartidores a afiliados y viceversa en tiempo real.
3.  **Seguridad**: Integración con JWT para asegurar las conexiones WebSocket.

## Ejecución

### Prerrequisitos
- JDK 21
- Maven 3.x
- Redis (corriendo localmente o configurado en `application.properties`)

### Correr localmente
```bash
mvn spring-boot:run
```

### Ejecutar pruebas
```bash
mvn test
```

## Cobertura de Código

El proyecto tiene configurado JaCoCo para el análisis de cobertura. Para generar el reporte:
```bash
mvn test
```
El reporte se encontrará en `target/site/jacoco/index.html`.

## CI/CD

El proyecto cuenta con un workflow de GitHub Actions que ejecuta las pruebas automáticamente en cada push o pull request a la rama `main`.
