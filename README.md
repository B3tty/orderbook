# Orderbook

Backend assignment for the Nordnet recruiting process

## Technical choices

- Java 21: latest version with LTS

## Architecture

This RESTful API service is built using the Spring Boot framework, which provides a lightweight
and easy-to-use platform for building web applications in Java.

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

The API will be available at http://localhost:8080/orders

## How to call

To call this API, you can use an agent like Postman.

### POST an order

This endpoint creates (and save) an order as defined in the body of the request. The id and date 
of the order will be defined automatically (date as current date at the moment of the call, id 
as a randomly generated UUID).

- url: `http://localhost:8080/orders` 

- Body:
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

### GET an order by id

This endpoint returns an order from id, passed in the path

- url `http://localhost:8080/orders/{id}`
  (for example `http://localhost:8080/orders/3427e677-829d-4f38-bd74-d05327266cdf)

### GET a summary of orders

This endpoint returns a summary (average price, minimum and maximum price, number of orders) for 
a given ticker, side, and date, passed as request parameters

- url `http://localhost:8080/orders/summary?ticker={yourTicker}&date={yourDate}&side={SELL|BUY}`
- parameters
  - ticker
  - date in a "yyyy-MM-dd" format
  - order side, as SELL or BUY