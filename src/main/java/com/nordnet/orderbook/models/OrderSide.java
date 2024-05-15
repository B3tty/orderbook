package com.nordnet.orderbook.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum OrderSide {
  BUY,
  SELL
}
