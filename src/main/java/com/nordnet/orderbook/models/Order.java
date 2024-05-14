package com.nordnet.orderbook.models;

import java.util.UUID;

public class Order {

  public UUID id;
  public String ticker;
  public OrderSide side;

  // This might make more sense as "long" if we only buy/sell integer numbers of stocks
  public double volume;
  public Price price;
}
