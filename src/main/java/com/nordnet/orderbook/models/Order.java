package com.nordnet.orderbook.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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
  @NotBlank
  public String ticker;
  public OrderSide side;

  // This might make more sense as "long" if we only buy/sell integer numbers of stocks
  @NotNull
  public double volume;
  public Price price;
  public LocalDate dateCreated;

  public Order(String ticker, OrderSide side, double volume, Price price) {
    this.ticker = ticker;
    this.side = side;
    this.volume = volume;
    this.price = price;
    this.dateCreated = LocalDate.now();
  }
}
