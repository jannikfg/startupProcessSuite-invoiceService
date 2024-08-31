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
import org.thi.sps.domain.model.CreditNote;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "invoice_creditNotes")
public class CreditNoteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id; //Gutschriftnummer
  private String invoiceId; //ID der Rechnung, auf die sich die Gutschrift bezieht
  private LocalDate createdDate; //Erstellungsdatum der Gutschrift
  private String reason; //Grund für die Gutschrift
  private double amount; //Betrag der Gutschrift (immer positiv)

  public CreditNote toCreditNote() {
    return CreditNote.builder()
        .id(this.id)
        .invoiceId(this.invoiceId)
        .createdDate(this.createdDate)
        .reason(this.reason)
        .amount(this.amount)
        .build();
  }

  public static CreditNoteEntity fromCreditNote(CreditNote creditNote) {
    CreditNoteEntity creditNoteEntity = new CreditNoteEntity();
    creditNoteEntity.setId(creditNote.getId());
    creditNoteEntity.setInvoiceId(creditNote.getInvoiceId());
    creditNoteEntity.setCreatedDate(creditNote.getCreatedDate());
    creditNoteEntity.setReason(creditNote.getReason());
    creditNoteEntity.setAmount(creditNote.getAmount());
    return creditNoteEntity;
  }
}
