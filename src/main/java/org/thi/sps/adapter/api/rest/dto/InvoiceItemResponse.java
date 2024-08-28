package org.thi.sps.adapter.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.thi.sps.domain.model.InvoiceItem;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItemResponse {

  private Long id;
  private String name;
  private String description;
  private String category;


  private double quantity;
  private String unit;

  private double netPrice;
  private double taxRate;
  private double discount;

  private String netTotal;
  private String taxTotal;
  private String total;

  public static InvoiceItemResponse fromInvoiceItem(InvoiceItem invoiceItem) {
    return InvoiceItemResponse.builder()
        .id(invoiceItem.getId())
        .name(invoiceItem.getName())
        .description(invoiceItem.getDescription())
        .category(invoiceItem.getCategory())
        .quantity(invoiceItem.getQuantity())
        .unit(invoiceItem.getUnit())
        .netPrice(invoiceItem.getNetPrice())
        .taxRate(invoiceItem.getTaxRate())
        .discount(invoiceItem.getDiscount())
        .netTotal(String.format("%.2f", invoiceItem.getNetTotal()))
        .taxTotal(String.format("%.2f", invoiceItem.getTaxTotal()))
        .total(String.format("%.2f", invoiceItem.getTotal()))
        .build();
  }

}
