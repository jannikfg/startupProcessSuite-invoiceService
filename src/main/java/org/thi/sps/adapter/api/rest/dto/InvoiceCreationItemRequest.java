package org.thi.sps.adapter.api.rest.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.thi.sps.domain.model.InvoiceRequestItem;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InvoiceCreationItemRequest {

  private Long id;
  private String name;
  private String description;
  private String category;
  private double quantity;
  private String unit;
  private LocalDate dateOfDelivery;

  private double netPrice;
  private double discount;
  private double netTotal;

  private double taxRate;
  private double taxTotal;

  private double total;

  public InvoiceRequestItem toInvoiceRequestItem() {
    return InvoiceRequestItem.builder()
        .id(this.id)
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
