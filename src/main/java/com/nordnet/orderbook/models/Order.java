package com.nordnet.orderbook.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;
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
  @NotBlank
  public OrderSide side;

  // This might make more sense as "long" if we only buy/sell integer numbers of stocks
  @NotBlank
  public double volume;
  @NotBlank
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
