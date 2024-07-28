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
  private String invoiceId;
  private LocalDate createdDate;
  private List<InvoiceItem> items;
  private String clientId;
  private String InvoiceDocumentId;

  public static InvoiceResponse fromInvoice(Invoice invoice) {
    InvoiceResponse response = new InvoiceResponse();
    response.setInvoiceId(invoice.getInvoiceId().toString());
    response.setCreatedDate(invoice.getCreatedDate());
    response.setItems(invoice.getItems());
    response.setClientId(invoice.getClientId());
    response.setInvoiceDocumentId(invoice.getInvoiceDocumentId());
    return response;
  }
}
