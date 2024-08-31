package org.thi.sps.adapter.api.rest.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.thi.sps.domain.model.CreditNote;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CreditNoteResponse {

  private Long id; //Gutschriftnummer
  private String invoiceId; //ID der Rechnung, auf die sich die Gutschrift bezieht
  private LocalDate createdDate; //Erstellungsdatum der Gutschrift
  private String reason; //Grund für die Gutschrift
  private double amount; //Betrag der Gutschrift (immer positiv)

  public static CreditNoteResponse fromCreditNote(CreditNote creditNote) {
    return CreditNoteResponse.builder()
        .id(creditNote.getId())
        .invoiceId(creditNote.getInvoiceId())
        .createdDate(creditNote.getCreatedDate())
        .reason(creditNote.getReason())
        .amount(creditNote.getAmount())
        .build();
  }
}
