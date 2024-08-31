package org.thi.sps.adapter.jpa.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.thi.sps.domain.model.Payment;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "invoice_payments")
public class PaymentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;              // Eindeutige ID der Zahlung
  private String invoiceId;       // Referenz zur zugehörigen Rechnung
  private LocalDate paymentDate;  // Datum der Zahlung
  private double amount;          // Betrag der Zahlung
  private String method;          // Zahlungsart (z.B. Überweisung, Kreditkarte)
  private String reference;

  public Payment toPayment() {
    return Payment.builder()
        .id(this.id)
        .invoiceId(this.invoiceId)
        .paymentDate(this.paymentDate)
        .amount(this.amount)
        .method(this.method)
        .reference(this.reference)
        .build();
  }

  public static PaymentEntity fromPayment(Payment payment) {
    PaymentEntity paymentEntity = new PaymentEntity();
    paymentEntity.setId(payment.getId());
    paymentEntity.setInvoiceId(payment.getInvoiceId());
    paymentEntity.setPaymentDate(payment.getPaymentDate());
    paymentEntity.setAmount(payment.getAmount());
    paymentEntity.setMethod(payment.getMethod());
    paymentEntity.setReference(payment.getReference());
    return paymentEntity;
  }
}
