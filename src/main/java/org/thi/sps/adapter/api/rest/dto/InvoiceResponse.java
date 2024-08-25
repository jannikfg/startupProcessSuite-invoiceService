package org.thi.sps.adapter.api.rest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.InvoiceItem;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InvoiceResponse {
  private String id; //Rechnungsnummer
  private String description; //Beschreibung
  private LocalDate createdDate; //Erstellungsdatum
  private List<InvoiceItem> invoiceItems; //Liste der Rechnungspositionen
  private Long clientId; //Kundennummer
  private LocalDate dateOfDelivery; //Lieferdatum bzw. Datum der erbrachten Dienstleistung
  private String noticeOfTaxExemption; //Hinweis bei Steuerbefreiung
  private String noticeOfRetentionObligation; // In den Fällen des $14 b Abs. 1 Satz 5 UStF einen Hinweis auf die Aufbewahrungspflicht des Rechnungsempfängers
  private double netTotal; //Nettobetrag
  private double taxTotal; //Steuerbetrag
  private double total; //Gesamtbetrag

  public static InvoiceResponse fromInvoice(Invoice invoice) {
    return InvoiceResponse.builder()
        .id(invoice.getId())
        .description(invoice.getDescription())
        .createdDate(invoice.getCreatedDate())
        .invoiceItems(invoice.getInvoiceItems())
        .clientId(invoice.getClientId())
        .dateOfDelivery(invoice.getDateOfDelivery())
        .noticeOfTaxExemption(invoice.getNoticeOfTaxExemption())
        .noticeOfRetentionObligation(invoice.getNoticeOfRetentionObligation())
        .netTotal(invoice.getNetTotal())
        .taxTotal(invoice.getTaxTotal())
        .total(invoice.getTotal())
        .build();
  }
}
