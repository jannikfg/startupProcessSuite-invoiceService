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
  private String id; //Rechnungsnummer
  private String description; //Beschreibung
  private LocalDate createdDate; //Erstellungsdatum
  private List<InvoiceItem> invoiceItems; //Liste der Rechnungspositionen
  private String clientId; //Kundennummer
  private LocalDate dateOfDelivery; //Lieferdatum bzw. Datum der erbrachten Dienstleistung
  private String noticeOfTaxExemption; //Hinweis bei Steuerbefreiung
  private String noticeOfRetentionObligation; // In den Fällen des $14 b Abs. 1 Satz 5 UStF einen Hinweis auf die Aufbewahrungspflicht des Rechnungsempfängers
  private double netTotal; //Nettobetrag
  private double taxTotal; //Steuerbetrag
  private double total; //Gesamtbetrag

  public Invoice(String id, List<InvoiceItem> invoiceItems, String clientId,
      LocalDate dateOfDelivery, String noticeOfTaxExemption, String noticeOfRetentionObligation) {
    this.id = id;
    this.createdDate = LocalDate.now();
    this.invoiceItems = invoiceItems;
    this.clientId = clientId;
    this.dateOfDelivery = dateOfDelivery;
    this.noticeOfTaxExemption = noticeOfTaxExemption;
    this.noticeOfRetentionObligation = noticeOfRetentionObligation;
  }

}
