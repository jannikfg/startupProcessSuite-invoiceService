package org.thi.sps.domain.model;

import lombok.*;


@Builder
@Data
public class InvoiceItem {

  private String description;
  private double price;
  private int quantity;

  public InvoiceItem(String description, double price, int quantity) {
    this.description = description;
    this.price = price;
    this.quantity = quantity;
  }

  public double getTotal() {
    return price * quantity;
  }

}