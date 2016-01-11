## Service to Service Communication example

This application demonstrates one service making requests of another service.

## Build the application

```
mvn clean package
```

## Running the application

Start two instances of the application. The first will communicate with the second.

1. Start first app 

    ```
    STORMPATH_API_KEY_FILE=<path to your api key file> \
    STORMPATH_APPLICATION_HREF=<href for your Stormpath application> \
    java -jar target/*.jar
    ```

2. Start the second app

    ```
    STORMPATH_API_KEY_FILE=<path to your api key file> \
    STORMPATH_APPLICATION_HREF=<href for your Stormpath application> \
    java -Dserver.port=8081 -jar target/*.jar
    ```

## Exercising the application

1. Authenticate

    ```
    curl -X POST \
      -H "Origin: http://localhost:8080" \
      -H "Content-Type: application/x-www-form-urlencoded" \
      -d "grant_type=password&username=<your username>&password=<your password>" \
    http://localhost:8080/oauth/token
    ```

2. Use the token from the previous command

    ```
    curl \
      -H "Authorization: Bearer <bearer token from previous step>" \
    http://localhost:8080/protectedEndpoint
    ```