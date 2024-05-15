package com.nordnet.orderbook.controllers;

import com.nordnet.orderbook.models.Order;
import com.nordnet.orderbook.models.OrderSide;
import com.nordnet.orderbook.models.Price;
import com.nordnet.orderbook.models.Summary;
import com.nordnet.orderbook.services.OrderService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.extern.java.Log;
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
    Order newOrder = new Order(createRequest.ticker, createRequest.side, createRequest.volume,
        createRequest.price);
    orderService.saveOrder(newOrder);
    return new ResponseEntity<>(newOrder.getId(), HttpStatus.CREATED);
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<Order> getOrder(@PathVariable UUID orderId) {
    // TODO: Logic to fetch the order by ID
    Order order = new Order();
    if (order != null) {
      return new ResponseEntity<>(order, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/summary")
  public ResponseEntity<Summary> getOrderSummary(@RequestParam SummaryRequest summaryRequest) {
    // TODO: Logic to calculate order summary
    Summary summary = new Summary();
    return new ResponseEntity<>(summary, HttpStatus.OK);
  }

  @Data
  public static class CreateOrderRequest {
    public String ticker;
    public OrderSide side;
    public double volume;
    public Price price;
  }

  @Data
  public static class SummaryRequest {
    public String ticker;
    public Date date;
    public OrderSide orderSide;
  }
}
