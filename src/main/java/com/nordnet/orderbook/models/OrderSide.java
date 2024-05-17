package com.nordnet.orderbook.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@NotNull
@Getter
public enum OrderSide {
  BUY,
  SELL
}
