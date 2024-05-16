package com.nordnet.orderbook.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@NotBlank
@Getter
public enum OrderSide {
  BUY,
  SELL
}
