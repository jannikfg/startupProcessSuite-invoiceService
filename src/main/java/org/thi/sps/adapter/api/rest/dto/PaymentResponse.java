package org.thi.sps.adapter.api.rest.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.thi.sps.domain.model.Payment;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class PaymentResponse {

  private Long id;              // Eindeutige ID der Zahlung
  private String invoiceId;       // Referenz zur zugehörigen Rechnung
  private LocalDate paymentDate;  // Datum der Zahlung
  private double amount;          // Betrag der Zahlung
  private String method;          // Zahlungsart (z.B. Überweisung, Kreditkarte)
  private String reference;       // Referenz oder Verwendungszweck der Zahlung

  public static PaymentResponse fromPayment(Payment payment) {

    return PaymentResponse.builder()
        .id(payment.getId())
        .invoiceId(payment.getInvoiceId())
        .paymentDate(payment.getPaymentDate())
        .amount(payment.getAmount())
        .method(payment.getMethod())
        .reference(payment.getReference())
        .build();

  }
}
