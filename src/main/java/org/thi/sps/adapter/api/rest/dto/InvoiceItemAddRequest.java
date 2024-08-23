package org.thi.sps.adapter.api.rest.dto;

import lombok.Getter;
import lombok.Setter;
import org.thi.sps.domain.model.ENUM.ProductCategory;
import org.thi.sps.domain.model.ENUM.TaxRate;
import org.thi.sps.domain.model.ENUM.Unit;

@Setter
@Getter
public class InvoiceItemAddRequest {
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

}
