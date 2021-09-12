# Pre-processing a web request before sending to controller.
Spring Boot project to demonstrate pre-processing a web request before sending to controller.

Follow the below steps for setting up and run the application.

1. Open the application using your favourite IDE (Intellij IDEA preferred)
2. Create a Postgres database named testdb (refer application.properties)
3. Debug the application
4. Execute the file src\main\resources\initialize.sql to populate dummy db data

Testing

Let's use an HTTP client (Postman preferred) to login to the application and to call some secured APIs.

1. Login: Send an HTTP POST request putting user credentials in the body.
    url: http://localhost:8090/api/auth/login
    body: {"username": "malindu", "password": "password1"}
    response: {"jwt": "example-jwt....", ...}
    
2. Calling secured APIs: Send an HTTP GET request .
    url: http://localhost:8090/api/test/moderator
    response: "Moderator Data." 
    
3. Debugging: Put debug pointers inside AuthenticationTokenFilter and check the flow. 