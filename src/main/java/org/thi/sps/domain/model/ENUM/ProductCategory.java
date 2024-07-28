package org.thi.sps.domain.model.ENUM;

import lombok.Getter;

@Getter
public enum ProductCategory {
  COACHING ("Coaching"),
  TRAINING ("Training"),
  WORKSHOP ("Workshop"),
  SEMINAR ("Seminar"),
  PRODUCT ("Product"),
  PROTOTYPE ("Prototype"),
  SERVICE ("Service");

  private final String category;

  ProductCategory(String category) {
    this.category = category;
  }
}
