package org.thi.sps.application;

import jakarta.inject.Inject;
import java.util.List;
import java.util.UUID;
import org.thi.sps.domain.InvoiceService;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.ports.outgoing.InvoiceRepository;


public class InvoiceServiceImpl implements InvoiceService {

  @Inject
  InvoiceRepository invoiceRepository;

  @Override
  public Invoice createInvoice(String clientId) {
    return null;
  }

  @Override
  public void addItem(UUID invoiceId, String description, double price, int quantity) {

  }

  @Override
  public void removeItem(UUID invoiceId, String description) {

  }

  @Override
  public Invoice getInvoice(UUID invoiceId) {
    return null;
  }

  @Override
  public List<Invoice> getInvoicesByClient(String clientId) {
    return null;
  }
}
