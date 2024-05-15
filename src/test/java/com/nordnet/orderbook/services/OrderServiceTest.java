package com.nordnet.orderbook.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nordnet.orderbook.models.Order;
import com.nordnet.orderbook.models.OrderSide;
import com.nordnet.orderbook.models.Price;
import com.nordnet.orderbook.repositories.OrderRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
  @Mock
  private OrderRepository orderRepository;

  @InjectMocks
  private OrderService orderService;

  private static UUID uuid = UUID.fromString("c5a2f509-20dd-43bf-8e2d-61fb7dabae40");

  private Order referenceOrder = new Order(uuid, "TIC", OrderSide.BUY, 53.0, new Price(),
      new Date());

  // saveOrder

  @Test
  void saveOrder() {
    when(orderRepository.save(referenceOrder)).thenReturn(referenceOrder);

    Order result = orderService.saveOrder(referenceOrder);

    assertNotNull(result);
    assertEquals(referenceOrder.id, result.id);
    assertEquals(referenceOrder.ticker, result.ticker);
    assertEquals(referenceOrder.side, result.side);
    assertEquals(referenceOrder.volume, result.volume);
    assertEquals(referenceOrder.price.value, result.price.value);
    assertEquals(referenceOrder.price.currency, result.price.currency);
    assertEquals(referenceOrder.dateCreated, result.dateCreated);
    verify(orderRepository, times(1)).save(referenceOrder);
  }


  // getAllOrders

  @Test
  void getAllOrders() {
    when(orderRepository.findAll()).thenReturn(new ArrayList<>(List.of(referenceOrder)));
    List<Order> result = orderService.getAllOrders();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(referenceOrder, result.get(0));
    verify(orderRepository, times(1)).findAll();
  }

  // getOrderById

  @Test
  void getOrderById() {
    when(orderRepository.findById(referenceOrder.getId())).thenReturn(Optional.of(
        referenceOrder));

    Order result = orderService.getOrderById(referenceOrder.getId());

    assertNotNull(result);
    assertEquals(referenceOrder.getId(), result.getId());
    verify(orderRepository, times(1)).findById(referenceOrder.getId());
  }

  @Test
  void getOrderById_WhenNotFound() {
    when(orderRepository.findById(referenceOrder.getId())).thenReturn(Optional.empty());

    Order result = orderService.getOrderById(referenceOrder.getId());

    assertNull(result);
    verify(orderRepository, times(1)).findById(referenceOrder.getId());
  }



}
