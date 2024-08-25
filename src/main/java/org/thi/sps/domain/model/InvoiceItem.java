package org.thi.sps.domain.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class InvoiceItem {

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

}