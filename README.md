# spacecrafts-mariano.

Este microservicio permite administrar los nombres de naves espaciales que aparecen en series y películas a través de una API REST.
## Marco del proyecto

- Lenguaje: Java 21 LTS sobre un marco SpringBoot 3.4.2
- Maven: maven versión 3.9.6
- OpenApi: OpenApi versión 3.0.1
- RabbitMQ: Broker de mensajería
- Springboot caching: Sistema de caché
- Spring AOP: Paradigma de programación orientado a aspectos
- Docker: Herramienta para crear y administrar contenedores

## Tecnologías necesarias

- Java 21
- Docker V27.3.1 (incluye docker compose)
- Lombok 1.18.34
- Maven 3.9.6
- puertos libres:
    - 1986 : Utilizado por la aplicación para recibir peticiones.|
    - 5672: Usado por RabbitMQ para la conexión con la aplicación.
    - 15672: Usado por RabbitMQ para la interfaz web de administración.

## Modo de ejecucion.

- Importe el proyecto a un IDE.
- Ejecute `docker compose up`, Esto iniciará RabbitMQ. Para acceder a la consola de administración,
ingrese a http://localhost:15672/
    - User: guest
    - Password: guest
- Inicie la aplicación.

## Modo de uso

1- Ejecutandolo desde Docker.

- Descomente en docker-compose.yml la referencia a la imagen de Docker.
- Ejecutar por consola con `docker compose up`

Link a la imagen: https://hub.docker.com/r/marianoigarzabal/spacecrafts-mariano

## URL's

- Swagger-ui: http://localhost:1986/swagger-ui/index.html
- RabbitMQ managment: http://localhost:15672/
  - User: guest
  - Password: guest

