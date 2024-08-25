package org.thi.sps.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Product {
  private Long id;
  private String category;
  private String name;
  private String description;
  private double netPrice;
  private String unit;
  private String tax;
  private double discount;
  private double quantity;
}
