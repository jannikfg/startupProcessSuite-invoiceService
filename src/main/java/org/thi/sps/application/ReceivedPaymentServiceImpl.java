package org.thi.sps.application;

import jakarta.inject.Inject;
import org.thi.sps.domain.InvoiceService;
import org.thi.sps.domain.model.Invoice;

public class ReceivedPaymentServiceImpl implements ReceivedPaymentService {

  @Inject
  private InvoiceService invoiceService;

  @Override
  public double checkReceivedPaymentForInvoice(String invoiceId) {
    Invoice invoice = invoiceService.getInvoiceById(invoiceId);
    double requestedAmount = invoice.getTotal();
    return requestedAmount;
  }

}
