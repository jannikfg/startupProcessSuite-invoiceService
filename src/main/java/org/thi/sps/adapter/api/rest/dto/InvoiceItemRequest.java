package org.thi.sps.adapter.api.rest.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.thi.sps.domain.model.InvoiceItem;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class InvoiceItemRequest {

  private Long id;
  private String name;
  private String description;
  private String category;
  private double quantity;
  private String unit;
  private LocalDate dateOfDelivery;

  private double netPrice;
  private double discount;

  private double taxRate;

  public InvoiceItem toInvoiceItem() {
    return InvoiceItem.builder()
        .id(this.id)
        .name(this.name)
        .description(this.description)
        .category(this.category)
        .quantity(this.quantity)
        .unit(this.unit)
        .netPrice(this.netPrice)
        .discount(this.discount)
        .taxRate(this.taxRate)
        .build();
  }
}
