package org.thi.sps.domain;

import java.time.LocalDate;
import java.util.List;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.InvoiceItem;
import org.thi.sps.domain.model.InvoiceRequest;

public interface InvoiceService {

  Invoice createInvoice(InvoiceRequest invoiceRequest);

  Invoice getInvoiceById(String invoiceId);

  List<Invoice> getInvoicesByClientId(String clientId);

  String generateInvoiceId();

  List<Invoice> getAllInvoices();
}