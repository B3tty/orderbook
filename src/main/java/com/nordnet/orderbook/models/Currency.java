package com.nordnet.orderbook.models;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public class Currency implements Serializable {
  @NotBlank
  public String name;
  @NotBlank
  public String symbol;
}
