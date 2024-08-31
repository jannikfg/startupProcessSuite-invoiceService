package org.thi.sps.domain.model;

import java.time.LocalDate;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

  private Long id;              // Eindeutige ID der Zahlung
  private String invoiceId;       // Referenz zur zugehörigen Rechnung
  private LocalDate paymentDate;  // Datum der Zahlung
  private double amount;          // Betrag der Zahlung
  private String method;          // Zahlungsart (z.B. Überweisung, Kreditkarte)
  private String reference;       // Referenz oder Verwendungszweck der Zahlung
}
