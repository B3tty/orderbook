package com.nordnet.orderbook.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="orders")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
  @Id
  @GeneratedValue
  public UUID id;
  public String ticker;
  public OrderSide side;

  // This might make more sense as "long" if we only buy/sell integer numbers of stocks
  public double volume;
  public Price price;
  public Instant instantCreated;

  public Order(String ticker, OrderSide side, double volume, Price price) {
    this.ticker = ticker;
    this.side = side;
    this.volume = volume;
    this.price = price;
    this.instantCreated = Instant.now(); // This will use UTC time zone
  }
}
