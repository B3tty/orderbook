package com.nordnet.orderbook.services;

import com.nordnet.orderbook.models.Order;
import com.nordnet.orderbook.repositories.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
  @Autowired
  OrderRepository orderRepository;

  public List<Order> getAllOrders() {
    List<Order> orders = new ArrayList<Order>();
    orderRepository.findAll().forEach(order -> orders.add(order));
    return (orders);
  }

  public Order getOrderById(UUID orderId) {
    return (orderRepository.findById(orderId).get());
  }

  public Order saveOrder(Order order) {
    return orderRepository.save(order);
  }
}
