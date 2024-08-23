package org.thi.sps.adapter.api.rest.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.thi.sps.domain.model.InvoiceItem;

@Getter
@Setter
public class InvoiceCreationRequest {
  private List<InvoiceItem> invoiceItems; //Liste der Rechnungspositionen
  private String clientId; //Kundennummer
  private LocalDate dateOfDelivery; //Lieferdatum bzw. Datum der erbrachten Dienstleistung
  private String invoiceDocumentId; //DokumentenId der Rechnung
  private String noticeOfTaxExemption; //Hinweis bei Steuerbefreiung
  private String noticeOfRetentionObligation; // In den Fällen des $14 b Abs. 1 Satz 5 UStF einen Hinweis auf die Aufbewahrungspflicht des Rechnungsempfängers

}
