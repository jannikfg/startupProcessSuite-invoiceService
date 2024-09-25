package org.thi.sps.domain;

import java.time.LocalDate;
import java.util.List;
import org.thi.sps.adapter.api.rest.dto.InvoiceChangeRequestDTO;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.helper.InvoiceChangeRequest;
import org.thi.sps.domain.model.helper.InvoiceRequest;

public interface InvoiceService {

  Invoice createInvoice(InvoiceRequest invoiceRequest);

  Invoice updateInvoice(Invoice invoice);

  Invoice getInvoiceById(String invoiceId);

  List<Invoice> getInvoicesByClientId(String clientId);

  String generateInvoiceId();


  List<Invoice> getAllInvoices();

  Invoice addPaymentToInvoice(String invoiceId, LocalDate paymentDay, double amount, String method, String reference);

  Invoice buildInvoiceFromInvoiceChangeRequest(InvoiceChangeRequest invoiceChangeRequest);

  Invoice addReminderToInvoice(int dueInDays, String reminderLevel, String invoiceId);
}