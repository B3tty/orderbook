package com.nordnet.orderbook.models;

import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Order {
  public UUID id;
  public String ticker;
  public OrderSide side;

  // This might make more sense as "long" if we only buy/sell integer numbers of stocks
  public double volume;
  public Price price;
  public Date dateCreated;

  public Order(String ticker, OrderSide side, double volume, Price price) {
    this.ticker = ticker;
    this.side = side;
    this.volume = volume;
    this.price = price;
    this.id = UUID.randomUUID();
    this.dateCreated = new Date();
  }
}
