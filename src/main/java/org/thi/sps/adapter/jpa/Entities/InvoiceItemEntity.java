package org.thi.sps.adapter.jpa.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@Table(name = "invoice_items")
public class InvoiceItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;
  private String name;
  private String description;
  private String category;


  private double quantity;
  private String unit;

  private double netPrice;
  private double taxRate;
  private double discount;

  private double netTotal;
  private double taxTotal;
  private double total;

  public InvoiceItem toInvoiceItem() {
    return InvoiceItem.builder()
        .id(this.id)
        .name(this.name)
        .description(this.description)
        .category(this.category)
        .quantity(this.quantity)
        .unit(this.unit)
        .netPrice(this.netPrice)
        .taxRate(this.taxRate)
        .discount(this.discount)
        .netTotal(this.netTotal)
        .taxTotal(this.taxTotal)
        .total(this.total)
        .build();
  }

  public static InvoiceItemEntity fromInvoiceItem(InvoiceItem invoiceItem) {
    return InvoiceItemEntity.builder()
        .id(invoiceItem.getId())
        .name(invoiceItem.getName())
        .description(invoiceItem.getDescription())
        .category(invoiceItem.getCategory())
        .quantity(invoiceItem.getQuantity())
        .unit(invoiceItem.getUnit())
        .netPrice(invoiceItem.getNetPrice())
        .taxRate(invoiceItem.getTaxRate())
        .discount(invoiceItem.getDiscount())
        .netTotal(invoiceItem.getNetTotal())
        .taxTotal(invoiceItem.getTaxTotal())
        .total(invoiceItem.getTotal())
        .build();
  }
}
