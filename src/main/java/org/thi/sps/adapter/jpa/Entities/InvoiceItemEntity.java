package org.thi.sps.adapter.jpa.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.thi.sps.domain.model.InvoiceItem;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "invoice_items")
public class InvoiceItemEntity {
  @Id
  private String invoiceItemId;
  private String productId;
  private Integer quantity;
  private Double price;
  private Double total;

  public InvoiceItem toInvoiceItem() {
    return InvoiceItem.builder()
        .invoiceItemId(invoiceItemId)
        .productId(productId)
        .quantity(quantity)
        .price(price)
        .total(total)
        .build();
  }
}
