# Bokudos-server

This is the backend game server for the 2D game "Bokudos".

This project is containerized using Docker.
The framework is Java Spring Boot and the database is MySQL.

Swagger documentation is enabled for an easy UI over the API.
Open a browser to http://localhost:8082/api/swagger-ui.html while the API is running with Docker.

## Building project

The project is built with maven. The below command can be used to build/package the project.

```mvn package```

This will download all the needed maven dependencies and package the files into a jar file that is located in the ```target``` folder.

## Running the API without Docker

With most IDEs, you should be able to just run the main class ```com.bokudos.bokudosserver.BokudosServerApplication``` to start the server.
This works nicely in IntelliJ IDEA with a Spring Boot run configuration.

## Running the API with Docker

You may also run the API using Docker. For this, you will need the Docker CLI and Docker-Compose CLI.

With Docker-compose, you should be able to simply run

```bash
> docker-compose build
> docker-compose up
```

## API Design

### Games
- GET api/v1/games/:gameId => get game
- POST api/v1/games => add new game

### Players

- GET api/v1/games/:gameId/players => get players in game
