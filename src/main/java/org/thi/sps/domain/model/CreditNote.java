package org.thi.sps.domain.model;

import java.time.LocalDate;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CreditNote {

  @Setter(AccessLevel.NONE)
  private Long id; //Gutschriftnummer
  private String invoiceId; //ID der Rechnung, auf die sich die Gutschrift bezieht
  private LocalDate createdDate; //Erstellungsdatum der Gutschrift
  private String reason; //Grund für die Gutschrift
  private double amount; //Betrag der Gutschrift (immer positiv)

}