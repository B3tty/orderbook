package com.nordnet.orderbook.controllers;

import com.nordnet.orderbook.models.Order;
import com.nordnet.orderbook.models.OrderSide;
import com.nordnet.orderbook.models.Price;
import com.nordnet.orderbook.models.Summary;
import com.nordnet.orderbook.services.OrderService;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
  @Autowired
  OrderService orderService;
  Logger logger = LoggerFactory.getLogger(OrderController.class);
  static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  // Added for ease of testing
  @GetMapping
  public ResponseEntity<List<Order>> getAllOrders() {
    List<Order> orders = orderService.getAllOrders();
    if (orders.isEmpty()) {
      return (ResponseEntity.notFound().build());
    } else {
      return (ResponseEntity.ok(orders));
    }
  }

  @PostMapping
  public ResponseEntity<UUID> createOrder(@RequestBody CreateOrderRequest createRequest) {
    Order newOrder = orderService.saveOrder(new Order(createRequest.ticker, createRequest.side, createRequest.volume,
        createRequest.price));
    return new ResponseEntity<>(newOrder.getId(), HttpStatus.CREATED);
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<Order> getOrder(@PathVariable UUID orderId) {
    Order order = orderService.getOrderById(orderId);
    if (order != null) {
      return new ResponseEntity<>(order, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/summary")
  public ResponseEntity<Summary> getOrderSummary(@RequestParam @NotBlank String ticker,
      @RequestParam @NotBlank String date, @RequestParam @NotBlank String side) {
    try {
      logger.info("first log");
      LocalDate parsedDate = LocalDate.parse(date, formatter);
      logger.info("date parsed");
      Summary summary = orderService.getSummary(ticker, parsedDate, OrderSide.valueOf(side));
      logger.info("summary gotten");
      return new ResponseEntity<>(summary, HttpStatus.OK);
    } catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @Data
  public static class CreateOrderRequest {
    public String ticker;
    public OrderSide side;
    public double volume;
    public Price price;
  }
}
