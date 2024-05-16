package com.nordnet.orderbook.models;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Price implements Serializable {
  @NotNull
  public double value;
  public Currency currency;

}
