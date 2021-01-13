# Bokudos-server

This is the backend game server for the 2D game "Bokudos".

This project is containerized using Docker.
The framework is Java Spring Boot and the database is MySQL.

Swagger documentation is enabled for an easy UI over the API.
Open a browser to 'http://localhost:8082/api/swagger-ui.html' while the API is running with Docker.

## Running the API

You will need the Docker CLI and Docker-Compose CLI to run this API out of the box.

`NOTE:` 

With Docker-compose, you should be able to simply run

```bash
> docker-compose build
> docker-compose up
```

Here are the images to pull from Docker if the compose file is getting hung from long downloads
```bash
> docker pull <image>
```

## API Design


