# Orderbook

The Order Book Microservice is a simplified implementation of an order book system built using Java and Spring Boot.
This microservice provides endpoints for creating orders, fetching individual orders, and retrieving order summaries based on ticker and date.

## Technical choices

- Java and Spring Boot: Chosen for their robustness, ease of development, and extensive 
  ecosystem of libraries and tools. Java 21 was chosen as the latest version with Long Term Support.

- Maven: Used for dependency management and project build automation.

- RESTful API: Implemented using Spring MVC to provide a lightweight, scalable, and 
platform-independent interface for interacting with the microservice.

- MockMvc for Integration Testing: Used for integration testing of controller endpoints, 
providing a simulated environment for testing HTTP requests and responses.

## Architecture

The service follows a layered architecture pattern, with the following layers:

- Controller layer: This layer receives incoming HTTP requests and sends back HTTP responses. It
  handles the input validation, and calls the appropriate service layer methods to process the requests.

- Service layer: This layer contains the business logic of the application. It handles the
  database interactions through a repository interface, and performs any necessary data processing and transformations.

- Repository layer: This layer is responsible for the data access and persistence. It uses a
  CrudRepository interface provided by Spring Data JPA to perform the basic CRUD operations on a database.

- Model layer: This layer contains the data model and entities used by the application. It
  defines the data structures that are stored and manipulated by the service, such as orders.


## How to Run

To run the order book API, you can follow these steps:

- Clone the repository to your local machine.

- Open the project in your IDE of choice.

- Build the project using Maven: `mvn clean install`

- Start the application using Maven: `mvn spring-boot:run`

- Optional: Run the tests: `mvn clean verify`. You can also run the tests directly in your IDE.

The API will be available at http://localhost:8080/orders

## How to make calls

To call this API, you can use an agent like Postman.

### POST an order

This endpoint creates (and save) an order as defined in the body of the request. The id and date 
of the order will be defined automatically (date as current date at the moment of the call, id 
as a randomly generated UUID).

- url: `http://localhost:8080/orders` 

- input: details of the order you wish to create, as the body of the request
```json
{
    "ticker": "SAVE",
    "orderside": "buy",
    "volume": "43",
    "price": {
        "value": 32.1,
        "currency": {
            "name": "swedish krona",
            "symbol": "SEK"
        }
    }

}
```

- output: uuid of the created order

### GET an order by id

This endpoint returns an order from id, passed in the path

- url `http://localhost:8080/orders/{id}`
  (for example `http://localhost:8080/orders/3427e677-829d-4f38-bd74-d05327266cdf)
- input: id of the order, as a path variable
- output: the relevant order, or 404 if no relevant order was found

### GET a summary of orders

This endpoint returns a summary (average price, minimum and maximum price, number of orders) for 
a given ticker, side, and date, passed as request parameters

- url `http://localhost:8080/orders/summary?ticker={yourTicker}&date={yourDate}&side={SELL|BUY}`
- input: the search parameters for the order, passed as parameters of the request
  - `ticker`
  - `date` in a "yyyy-MM-dd" format
  - `side`, as SELL or BUY
- output: a summary of the matching orders, including average, min and max prices, and the total 
  number of relevant orders

## Possible improvements

- Database integration

  Implement persistence using a relational database like PostgreSQL or MySQL for storing orders and order data.
  As of now, the order repository will be emptied each time the service is stopped and restarted.

- Authentication and Authorization

  We could add security measures such as JWT-based authentication and role-based access control to 
  secure endpoints and restrict access.

- Swagger Documentation

  Adding Swagger for automatic generation of API documentation would make it easier for 
  consumers to understand and interact with the API.

- Logging and Monitoring

  For a production service, we should add logging to capture important events and errors, and 
  integrate with monitoring tools like Prometheus and Grafana for real-time monitoring of 
  application health and performance metrics.

- Summary function

  In the current state, it doesn't take the currency into consideration. For a real case, we 
  would need to convert all the prices in the same currency in order to have meaningful results.