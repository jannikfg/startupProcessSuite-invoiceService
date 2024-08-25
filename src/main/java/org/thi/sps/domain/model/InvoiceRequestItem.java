package org.thi.sps.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InvoiceRequestItem {

  private Long id;
  private String name;
  private String description;
  private String category;
  private double quantity;
  private String unit;

  private double netPrice;
  private double discount;
  private double netTotal;

  private double taxRate;
  private double taxTotal;

  private double total;

  public InvoiceItem toInvoiceItem() {
    return InvoiceItem.builder()
        .name(this.name)
        .description(this.description)
        .category(this.category)
        .quantity(this.quantity)
        .unit(this.unit)
        .netPrice(this.netPrice)
        .discount(this.discount)
        .netTotal(this.netTotal)
        .taxRate(this.taxRate)
        .taxTotal(this.taxTotal)
        .total(this.total)
        .build();
  }

}
