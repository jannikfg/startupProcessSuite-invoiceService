package org.thi.sps.domain.model.ENUM;

import lombok.Getter;

@Getter
public enum TaxRate {
  NO_TAX (0.0),
  STANDARD_TAX (0.19),
  REDUCED_TAX (0.07);

  private final double rate;

  TaxRate(double rate) {
    this.rate = rate;
  }
}
