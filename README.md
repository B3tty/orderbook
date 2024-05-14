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

Call with Postman

### POST endpoint - create order

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