package org.thi.sps.application;

import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.thi.sps.domain.InvoiceService;
import org.thi.sps.domain.model.ENUM.ProductCategory;
import org.thi.sps.domain.model.ENUM.TaxRate;
import org.thi.sps.domain.model.ENUM.Unit;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.InvoiceItem;
import org.thi.sps.ports.outgoing.InvoiceRepository;


public class InvoiceServiceImpl implements InvoiceService {

  @Inject
  InvoiceRepository invoiceRepository;


  @Override
  public Invoice createInvoice(String invoiceId, List<InvoiceItem> invoiceItems, String clientId,
      LocalDate dateOfDelivery, String invoiceDocumentId, String noticeOfTaxExemption,
      String noticeOfRetentionObligation) {
    Invoice newInvoice = new Invoice(generateInvoiceId(), invoiceItems, clientId, dateOfDelivery, invoiceDocumentId,
        noticeOfTaxExemption, noticeOfRetentionObligation);
    invoiceRepository.saveInvoice(newInvoice);
    return newInvoice;
  }

  @Override
  public void addItem(UUID invoiceId, ProductCategory category, String description, double price,
      int quantity, Unit unit, TaxRate tax, double discount) {
    Invoice invoice = invoiceRepository.getInvoiceById(invoiceId.toString());
    if (invoice != null) {
      InvoiceItem newItem = new InvoiceItem(category, description, price, quantity, unit, tax,
          discount);
      invoice.getInvoiceItems().add(newItem);
      invoiceRepository.saveInvoice(invoice);
    }
  }


  @Override
  public void removeItem(UUID invoiceId, String description) {
    invoiceRepository.deleteInvoice(invoiceId.toString());
  }

  @Override
  public Invoice getInvoiceById(String invoiceId) {
    return invoiceRepository.getInvoiceById(invoiceId);
  }

  @Override
  public List<Invoice> getInvoicesByClientId(String clientId) {
    return invoiceRepository.getInvoicesByClient(clientId);
  }

  @Override
  public String generateInvoiceId() {
    return null;
  }
}
