# Homsai Engine

The Homsai add-on for Home Assistant

## Features

- DDD architecture
- Central exception management
- File storage module
- Logging module (Log4j)
- Email module
- Basic unit test setup
- Spotless integration

## Setup


### Environment

- Go to /src/main/resources
- Create the application.yml file from the right environment (for example application.yml.local for local machine development)
- Create the application.properties file from the right environment (for example application.properties.local for local machine development)

### Dev application

To start the dev application just run 
```
gradlew bootRun
```

## Test

To run the unit test execute
```
gradlew test
```

## Deploy

- To create the .jar first of all create the right application.yml depending on the environment
- Run
```
gradlew build
```
- If you need the file storage module create the /home/media folder in the server
- The add the following owner and group
```
sudo chown -R tomcat8.tomcat8 /home/media
```
- The add the following permissions
```
sudo chmod -R 700 /home/media
```

## Lint

To perform the spotless checks run

```
./gradlew spotlessCheck
```
To fix the spotless errors run 

```
./gradlew spotlessApply
```

## Swagger Documentation

To see the documentation run the backend and go to http://localhost:8080/swagger-ui.html