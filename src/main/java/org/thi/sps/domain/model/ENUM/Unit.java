package org.thi.sps.domain.model.ENUM;

import lombok.Getter;

@Getter
public enum Unit {
  STUNDEN ("Stunde"),
  TAGE ("Tag"),
  STÜCK ("Stück"),
  KILO ("Kilo"),
  LITER ("Liter"),
  METER ("Meter"),
  KUBIKMETER ("Kubikmeter");

  private final String unit;

  Unit(String unit) {
    this.unit = unit;
  }

}
