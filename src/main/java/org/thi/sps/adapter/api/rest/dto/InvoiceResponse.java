package org.thi.sps.adapter.api.rest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.thi.sps.domain.model.Invoice;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InvoiceResponse {

  private String id; //Rechnungsnummer
  private String description; //Beschreibung
  private LocalDate createdDate; //Erstellungsdatum
  private List<InvoiceItemResponse> invoiceItems; //Liste der Rechnungspositionen
  private Long clientId; //Kundennummer
  private LocalDate dateOfDelivery; //Lieferdatum bzw. Datum der erbrachten Dienstleistung
  private String noticeOfTaxExemption; //Hinweis bei Steuerbefreiung
  private String noticeOfRetentionObligation; // In den Fällen des $14 b Abs. 1 Satz 5 UStF einen Hinweis auf die Aufbewahrungspflicht des Rechnungsempfängers
  private String netTotal; //Nettobetrag
  private String taxTotal; //Steuerbetrag
  private String total; //Gesamtbetrag
  private String totalOutstanding; //Offener Betrag
  private boolean isPaid; //Bezahlt
  private List<CreditNoteResponse> creditNotes; //Liste der Gutschriften
  private List<PaymentResponse> payments; //Liste der Zahlungen


  public static InvoiceResponse fromInvoice(Invoice invoice) {
    List<CreditNoteResponse> creditNotes = Optional.ofNullable(invoice.getCreditNotes())
        .map(list -> list.stream().map(CreditNoteResponse::fromCreditNote).toList())
        .orElseGet(List::of);
    List<PaymentResponse> payments = Optional.ofNullable(invoice.getPayments())
        .map(list -> list.stream().map(PaymentResponse::fromPayment).toList())
        .orElseGet(List::of);
    List<InvoiceItemResponse> invoiceItems = Optional.ofNullable(invoice.getInvoiceItems())
        .map(list -> list.stream().map(InvoiceItemResponse::fromInvoiceItem).toList())
        .orElseGet(List::of);
    return InvoiceResponse.builder()
        .id(invoice.getId())
        .description(invoice.getDescription())
        .createdDate(invoice.getCreatedDate())
        .invoiceItems(invoiceItems)
        .clientId(invoice.getClientId())
        .dateOfDelivery(invoice.getDateOfDelivery())
        .noticeOfTaxExemption(invoice.getNoticeOfTaxExemption())
        .noticeOfRetentionObligation(invoice.getNoticeOfRetentionObligation())
        .netTotal(String.format("%.2f", invoice.getNetTotal()))
        .taxTotal(String.format("%.2f", invoice.getTaxTotal()))
        .total(String.format("%.2f", invoice.getTotal()))
        .totalOutstanding(String.format("%.2f", invoice.getTotalOutstanding()))
        .isPaid(invoice.isPaid())
        .creditNotes(creditNotes)
        .payments(payments)
        .build();
  }
}
