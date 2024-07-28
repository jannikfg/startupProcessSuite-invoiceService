package org.thi.sps.domain.model;

import lombok.*;
import org.thi.sps.domain.model.ENUM.ProductCategory;
import org.thi.sps.domain.model.ENUM.TaxRate;
import org.thi.sps.domain.model.ENUM.Unit;


@Builder
@Data
public class InvoiceItem {

  private ProductCategory category;
  private String description;
  private double price;
  private int quantity;
  private Unit unit;
  private TaxRate tax;
  private double discount; // x % Discount
  private double netTotal;
  private double taxTotal;
  private double total;

  public InvoiceItem(ProductCategory category, String description, double price, int quantity, Unit unit, TaxRate tax, double discount) {
    this.category = category;
    this.description = description;
    this.price = price;
    this.quantity = quantity;
    this.unit = unit;
    this.tax = tax;
    this.discount = discount;
    this.netTotal = getNetTotal();
    this.taxTotal = getTaxTotal();
    this.total = getTotal();
  }

  public double getTotal() {
    return getNetTotal() + getTaxTotal();
  }

  public double getTaxTotal() {
    return price * quantity * tax.getRate();
  }

  public double getNetTotal() {
    return price * quantity * (1 - discount);
  }

}