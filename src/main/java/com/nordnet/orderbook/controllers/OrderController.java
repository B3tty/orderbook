package com.nordnet.orderbook.controllers;

import com.nordnet.orderbook.models.Order;
import com.nordnet.orderbook.models.OrderSide;
import com.nordnet.orderbook.models.Price;
import com.nordnet.orderbook.models.Summary;
import com.nordnet.orderbook.services.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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

  @PostMapping
  public ResponseEntity<UUID> createOrder(@RequestBody @Valid CreateOrderRequest createRequest) {
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
      @RequestParam @NotBlank @Pattern(regexp="^[0-9]{4}-[0-9]{2}-[0-9]{2}$",
          message="date should be in format yyyy-MM-dd") String date,
      @RequestParam @NotBlank String side) {
    try {
      LocalDate parsedDate = LocalDate.parse(date, formatter);
      Summary summary = orderService.getSummary(ticker, parsedDate, OrderSide.valueOf(side));
      return new ResponseEntity<>(summary, HttpStatus.OK);
    } catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @Data
  public static class CreateOrderRequest {
    @NotBlank
    public String ticker;
    @Valid
    @NotNull
    public OrderSide side;
    @Positive
    public double volume;
    @Valid
    @NotNull
    public Price price;
  }
}
