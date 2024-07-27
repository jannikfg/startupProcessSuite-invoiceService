package org.thi.sps.domain;

import java.util.List;
import java.util.UUID;
import org.thi.sps.domain.model.Invoice;

public interface InvoiceService {

  Invoice createInvoice(String clientId);

  void addItem(UUID invoiceId, String description, double price, int quantity);

  void removeItem(UUID invoiceId, String description);

  Invoice getInvoice(UUID invoiceId);

  List<Invoice> getInvoicesByClient(String clientId);


}