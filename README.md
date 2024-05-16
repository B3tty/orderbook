# Orderbook

Backend assignment for the Nordnet recruiting process

## Technical choices

- Java 21: latest version with LTS

## Layered architecture


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

### GET all orders

This endpoint has no arguments and will return every order that has been created

- url `http://localhost:8080/orders`

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