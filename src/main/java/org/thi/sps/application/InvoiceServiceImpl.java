package org.thi.sps.application;

import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.List;
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
  public Invoice createInvoice(List<InvoiceItem> invoiceItems, String clientId,
      LocalDate dateOfDelivery, String invoiceDocumentId, String noticeOfTaxExemption,
      String noticeOfRetentionObligation) {
    Invoice newInvoice = new Invoice(generateInvoiceId(), invoiceItems, clientId, dateOfDelivery,
        noticeOfTaxExemption, noticeOfRetentionObligation);
    invoiceRepository.saveInvoice(newInvoice);
    return newInvoice;
  }

  @Override
  public void addItem(String invoiceId, ProductCategory category, String description, double price,
      int quantity, Unit unit, TaxRate tax, double discount) {
    Invoice invoice = invoiceRepository.getInvoiceById(invoiceId);
    if (invoice != null) {
      InvoiceItem newItem = new InvoiceItem(category, description, price, quantity, unit, tax,
          discount);
      invoice.getInvoiceItems().add(newItem);
      invoiceRepository.saveInvoice(invoice);
    }
  }

  @Override
  public void removeItem(String invoiceId, String description) {
    invoiceRepository.deleteInvoice(invoiceId);
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

  /*public double getTaxTotal() {
    return netPrice * quantity * tax.getRate();
  }

  public double getNetTotal() {
    return netPrice * quantity * (1 - discount);
  }

  public double getTotal() {
    return invoiceItems.stream().mapToDouble(InvoiceItem::getTotal).sum();
  }

  public double getNetTotal() {
    return invoiceItems.stream().mapToDouble(InvoiceItem::getNetTotal).sum();
  }

  public double getTaxTotal() {
    return invoiceItems.stream().mapToDouble(InvoiceItem::getTaxTotal).sum();
  }
  */

}
