package org.thi.sps.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.thi.sps.domain.InvoiceService;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.InvoiceItem;
import org.thi.sps.domain.model.InvoiceRequest;
import org.thi.sps.domain.model.InvoiceRequestItem;
import org.thi.sps.ports.outgoing.InvoiceRepository;

@ApplicationScoped
public class InvoiceServiceImpl implements InvoiceService {

  @Inject
  InvoiceRepository invoiceRepository;

  private final String noticeOfTaxExemption = "Notice of tax exemption";
  private final String noticeOfRetentionObligation = "Notice of retention obligation";


  @Override
  @Transactional
  public Invoice createInvoice(InvoiceRequest invoiceRequest) {

    List<InvoiceItem> invoiceItems = invoiceRequest.getItems().stream()
        .map(InvoiceRequestItem::toInvoiceItem).toList();

    Invoice invoice = Invoice.builder()
        .id(String.valueOf(Math.random()*100))
        .clientId(invoiceRequest.getClientId())
        .description(invoiceRequest.getDescription())
        .createdDate(LocalDate.now())
        .invoiceItems(invoiceItems)
        .dateOfDelivery(invoiceRequest.getDateOfDelivery())
        .noticeOfTaxExemption(noticeOfTaxExemption)
        .noticeOfRetentionObligation(noticeOfRetentionObligation)
        .build();
    return invoiceRepository.save(invoice);
  }

  @Override
  public Invoice getInvoiceById(String invoiceId) {
    return null;
  }

  @Override
  public List<Invoice> getInvoicesByClientId(String clientId) {
    return null;
  }

  @Override
  public String generateInvoiceId() {
    return null;
  }

  @Override
  public List<Invoice> getAllInvoices() {
    List<Invoice> invoices = invoiceRepository.findAll();
    System.out.println("Invoices in InvoiceServiceImpl: " + invoices);
    return invoices;
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
