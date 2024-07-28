package org.thi.sps.domain.model;

import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;


@Data
@Builder
public class Invoice {

  @Setter(AccessLevel.NONE)
  private String invoiceId; //Rechnungsnummer
  private LocalDate createdDate; //Erstellungsdatum
  private List<InvoiceItem> invoiceItems; //Liste der Rechnungspositionen
  private String clientId; //Kundennummer
  private LocalDate dateOfDelivery; //Lieferdatum bzw. Datum der erbrachten Dienstleistung
  private String invoiceDocumentId; //DokumentenId der Rechnung
  private String noticeOfTaxExemption; //Hinweis bei Steuerbefreiung
  private String noticeOfRetentionObligation; // In den Fällen des $14 b Abs. 1 Satz 5 UStF einen Hinweis auf die Aufbewahrungspflicht des Rechnungsempfängers

  public Invoice(String invoiceId, List<InvoiceItem> invoiceItems, String clientId, LocalDate dateOfDelivery, String invoiceDocumentId, String noticeOfTaxExemption, String noticeOfRetentionObligation) {
    this.invoiceId = invoiceId;
    this.createdDate = LocalDate.now();
    this.invoiceItems = invoiceItems;
    this.clientId = clientId;
    this.dateOfDelivery = dateOfDelivery;
    this.invoiceDocumentId = invoiceDocumentId;
    this.noticeOfTaxExemption = noticeOfTaxExemption;
    this.noticeOfRetentionObligation = noticeOfRetentionObligation;
  }

  public double getTotal() {
    return invoiceItems.stream().mapToDouble(InvoiceItem::getTotal).sum();
  }

  public double getNetTotal() {
    return invoiceItems.stream().mapToDouble(InvoiceItem::getNetTotal).sum();
  }

  public double getTaxTotal() {
    return invoiceItems.stream().mapToDouble(InvoiceItem::getTaxTotal).sum();
  }

}
