package org.thi.sps.ports.outgoing;

import java.util.List;
import org.thi.sps.domain.model.Invoice;

public interface InvoiceRepository {

  void saveInvoice(Invoice invoice);

  Invoice getInvoice(String invoiceId);

  void deleteInvoice(String invoiceId);

  List<Invoice> getInvoicesByClient(String clientId);

}
