package com.nordnet.orderbook.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Summary {
  public double averagePrice;
  public double maxPrice;
  public double minPrice;
  public long totalNumber;
}
