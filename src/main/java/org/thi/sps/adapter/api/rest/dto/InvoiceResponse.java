package org.thi.sps.adapter.api.rest.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.InvoiceItem;

@Setter
@Getter
public class InvoiceResponse {
  private String invoiceId; //Rechnungsnummer
  private LocalDate createdDate; //Erstellungsdatum
  private List<InvoiceItem> invoiceItems; //Liste der Rechnungspositionen
  private String clientId; //Kundennummer
  private LocalDate dateOfDelivery; //Lieferdatum bzw. Datum der erbrachten Dienstleistung
  private String invoiceDocumentId; //DokumentenId der Rechnung
  private String noticeOfTaxExemption; //Hinweis bei Steuerbefreiung
  private String noticeOfRetentionObligation; // In den Fällen des $14 b Abs. 1 Satz 5 UStF einen Hinweis auf die Aufbewahrungspflicht des Rechnungsempfängers


  public static InvoiceResponse fromInvoice(Invoice invoice) {
    InvoiceResponse invoiceResponse = new InvoiceResponse();
    invoiceResponse.setInvoiceId(invoice.getInvoiceId());
    invoiceResponse.setCreatedDate(invoice.getCreatedDate());
    invoiceResponse.setInvoiceItems(invoice.getInvoiceItems());
    invoiceResponse.setClientId(invoice.getClientId());
    invoiceResponse.setDateOfDelivery(invoice.getDateOfDelivery());
    invoiceResponse.setInvoiceDocumentId(invoice.getInvoiceDocumentId());
    invoiceResponse.setNoticeOfTaxExemption(invoice.getNoticeOfTaxExemption());
    invoiceResponse.setNoticeOfRetentionObligation(invoice.getNoticeOfRetentionObligation());
    return invoiceResponse;
  }
}
