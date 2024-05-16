package com.nordnet.orderbook.services;

import com.nordnet.orderbook.models.Order;
import com.nordnet.orderbook.models.OrderSide;
import com.nordnet.orderbook.models.Price;
import com.nordnet.orderbook.models.Summary;
import com.nordnet.orderbook.repositories.OrderRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
  @Autowired
  OrderRepository orderRepository;

  public List<Order> getAllOrders() {
    List<Order> orders = new ArrayList<>();
    orderRepository.findAll().forEach(order -> orders.add(order));
    return (orders);
  }

  public Order getOrderById(UUID orderId) {
    var orderMatch =  orderRepository.findById(orderId);
    return (orderMatch.isEmpty() ? null : orderMatch.get());
  }

  public Order saveOrder(Order order) {
    return orderRepository.save(order);
  }

  public Summary getSummary(String ticker, LocalDate date, OrderSide orderSide) {
    List<Order> matchingOrders = getMatchingOrders(ticker, date, orderSide);
    if (matchingOrders.isEmpty()) {
      return new Summary(0, 0, 0, 0);
    }

    DoubleSummaryStatistics summaryStats =
        matchingOrders.stream().map(Order::getPrice).collect(Collectors.summarizingDouble(Price::getValue));

    return new Summary(summaryStats.getAverage(), summaryStats.getMax(), summaryStats.getMin(),
        matchingOrders.size());
  }

  private List<Order> getMatchingOrders(String ticker, LocalDate date, OrderSide orderSide) {
    List<Order> orders = new ArrayList<>();
    orderRepository.findAll().forEach(order -> {
      if (order.getTicker().equals(ticker) && order.getSide().equals(orderSide) && order.getDateCreated().equals(date)) {
        orders.add(order);
      }
    });
    return (orders);
  }
}
