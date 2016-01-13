## Service to Service Communication example

This application demonstrates one service making requests of another service.

The scenario is that the second service is responsible for sending back the email addresses for each of the 
available Accounts in the Application for authorized admins.

## Setup Stormpath

In your Stormpath Admin Console, do the following:

1. Create an Application and make note of the href
2. Create a Directory and map it to the application
3. Create a Group in the Directory and make note of the href
4. Create a number of Accounts and add at least one to the Group from the previous step

## Build the application

```
mvn clean package
```

## Running the application

Start two instances of the application. The first will communicate with the second.


1. Start first app on the default port, 8080:

    ```
    STORMPATH_API_KEY_FILE=<path to your api key file> \
    STORMPATH_APPLICATION_HREF=<href from step #1 above> \
    java -jar target/*.jar
    ```

2. Start the second app on port 8081:

    ```
    STORMPATH_API_KEY_FILE=<path to your api key file> \
    STORMPATH_APPLICATION_HREF=<href from step #1 above> \
    STORMPATH_ADMIN_GROUP_HREF=<href from step #3 above> \
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
    http://localhost:8080/accounts
    ```
    
As a convenience, you can run the `./get_accounts.sh` shell script. 

It will ask you to input your email address and input your password.

It will then authenticate to the first application using the oauth `grant_type=password`.

Next, it will extract the returned access token and use it to hit the `/accounts` endpoint on the first service.

The first service will connect to the second service and return the result.

