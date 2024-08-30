package org.thi.sps.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.thi.sps.adapter.api.rest.dto.InvoiceItemsAdditionRequest;
import org.thi.sps.application.exeptions.InvoiceNotFoundException;
import org.thi.sps.domain.InvoiceService;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.InvoiceItem;
import org.thi.sps.domain.model.Product;
import org.thi.sps.domain.model.helper.InvoiceRequest;
import org.thi.sps.ports.outgoing.InvoiceRepository;

@ApplicationScoped
public class InvoiceServiceImpl implements InvoiceService {

  @Inject
  InvoiceRepository invoiceRepository;

  private final String noticeOfTaxExemption = "Notice of tax exemption";
  private final String noticeOfRetentionObligation = "Notice of retention obligation";
  private int invoiceNumber = 1;


  @Override
  @Transactional
  public Invoice createInvoice(InvoiceRequest invoiceRequest) {

    List<InvoiceItem> invoiceItems = invoiceRequest.getItems();

    calculateInvoiceItems(invoiceItems);

    Invoice invoice = Invoice.builder()
        .id(generateInvoiceId())
        .clientId(invoiceRequest.getClientId())
        .description(invoiceRequest.getDescription())
        .createdDate(LocalDate.now())
        .invoiceItems(invoiceItems)
        .dateOfDelivery(invoiceRequest.getDateOfDelivery())
        .noticeOfTaxExemption(noticeOfTaxExemption)
        .noticeOfRetentionObligation(noticeOfRetentionObligation)
        .netTotal(calculateNetTotalForInvoice(invoiceItems))
        .taxTotal(calculateTaxTotalForInvoice(invoiceItems))
        .total(calculateTotalForInvoice(invoiceItems))
        .build();
    return invoiceRepository.save(invoice);
  }

  private void calculateInvoiceItems(List<InvoiceItem> invoiceItems) {
    for (InvoiceItem invoiceItem : invoiceItems) {
      invoiceItem.setNetTotal(calculateNetTotalForInvoiceItem(invoiceItem));
      invoiceItem.setTaxTotal(calculateTaxTotalForInvoiceItem(invoiceItem));
      invoiceItem.setTotal(calculateTotalForInvoiceItem(invoiceItem));
    }
  }

  private double calculateTotalForInvoice(List<InvoiceItem> invoiceItems) {
    return invoiceItems.stream().mapToDouble(InvoiceItem::getTotal).sum();
  }

  private double calculateNetTotalForInvoice(List<InvoiceItem> invoiceItems) {
    return invoiceItems.stream().mapToDouble(InvoiceItem::getNetTotal).sum();
  }

  private double calculateTaxTotalForInvoice(List<InvoiceItem> invoiceItems) {
    return invoiceItems.stream().mapToDouble(InvoiceItem::getTaxTotal).sum();
  }

  private double calculateNetTotalForInvoiceItem(InvoiceItem invoiceItem) {
    return invoiceItem.getNetPrice() * invoiceItem.getQuantity() * (1 - invoiceItem.getDiscount());
  }

  private double calculateTaxTotalForInvoiceItem(InvoiceItem invoiceItem) {
    return invoiceItem.getNetTotal() * invoiceItem.getTaxRate();
  }

  private double calculateTotalForInvoiceItem(InvoiceItem invoiceItem) {
    return invoiceItem.getNetTotal() + invoiceItem.getTaxTotal();
  }
  @Override
  public Invoice updateInvoiceWithNewProducts(Invoice invoice, List<Product> items) {
    List<InvoiceItem> invoiceItems = new ArrayList<>(invoice.getInvoiceItems());
    if (items != null) {
      invoiceItems.addAll(transfromNewProductsToInvoiceItems(items));
    }
    invoice.setInvoiceItems(invoiceItems);
    return updateInvoice(invoice);
  }

  @Override
  @Transactional
  public Invoice updateInvoice(Invoice invoice) {
    Invoice invoiceFromDb = invoiceRepository.findById(invoice.getId());
    System.out.println("InvoiceFromDb: " + invoiceFromDb);
    if (invoiceFromDb == null) {
      throw new InvoiceNotFoundException("Invoice not found with id: " + invoice.getId());
    }

    calculateInvoiceItems(invoice.getInvoiceItems());

    invoiceFromDb.setDescription(invoice.getDescription());
    invoiceFromDb.setInvoiceItems(invoice.getInvoiceItems());
    invoiceFromDb.setNoticeOfTaxExemption(invoice.getNoticeOfTaxExemption());
    invoiceFromDb.setNoticeOfRetentionObligation(invoice.getNoticeOfRetentionObligation());
    invoiceFromDb.setNetTotal(calculateNetTotalForInvoice(invoice.getInvoiceItems()));
    invoiceFromDb.setTaxTotal(calculateTaxTotalForInvoice(invoice.getInvoiceItems()));
    invoiceFromDb.setTotal(calculateTotalForInvoice(invoice.getInvoiceItems()));
    return invoiceRepository.update(invoiceFromDb);
  }

  private List<InvoiceItem> transfromNewProductsToInvoiceItems(List<Product> products){
    List<InvoiceItem> invoiceItems = new ArrayList<>();
    for (Product item : products) {
      invoiceItems.add(InvoiceItem.builder()
          .name(item.getName())
          .description(item.getDescription())
          .category(item.getCategory())
          .quantity(item.getQuantity())
          .unit(item.getUnit())
          .discount(item.getDiscount())
          .netPrice(item.getNetPrice() - item.getNetPrice() * item.getDiscount())
          .taxRate(parseTaxRate(item.getTaxRate()))
          .netTotal(item.getNetPrice() * item.getQuantity())
          .taxTotal(item.getNetPrice() * item.getQuantity() * Double.parseDouble(item.getTaxRate()))
          .total(item.getNetPrice() * item.getQuantity() * Double.parseDouble(item.getTaxRate())
              + item.getNetPrice() * item.getQuantity())
          .build());
    }
    return invoiceItems;
  }

  private double parseTaxRate(String taxRateStr) {
    try {
      return taxRateStr != null ? Double.parseDouble(taxRateStr) : 0.0;
    } catch (NumberFormatException e) {
      return 0.19;  // Standardwert, wenn das Parsen fehlschlägt
    }
  }

  @Override
  public Invoice getInvoiceById(String invoiceId) {
    System.out.println("InvoiceId: " + invoiceId);
    return invoiceRepository.findById(invoiceId);
  }

  @Override
  public List<Invoice> getInvoicesByClientId(String clientId) {
    return invoiceRepository.findByClientId(clientId);
  }

  @Override
  public String generateInvoiceId() {
    LocalDate currentDate = LocalDate.now();
    String datePart = currentDate.format(DateTimeFormatter.BASIC_ISO_DATE);

    // Generiere eine eindeutige und fortlaufende Rechnungsnummer für das aktuelle Datum
    int invoiceNumber = getNextInvoiceNumberForDate();

    // Format der Rechnungs-ID: YYYYMMDD-InvoiceNumber
    return String.format("%s-%04d", datePart, invoiceNumber);
  }

  private int getNextInvoiceNumberForDate() {
    invoiceNumber++;
    // Ansonsten erhöhe die letzte gefundene Rechnungsnummer um 1
    return invoiceNumber;
  }


  @Override
  public List<Invoice> getAllInvoices() {
    List<Invoice> invoices = invoiceRepository.findAll();
    return invoices;
  }

  @Override
  public List<Product> getProducts(List<InvoiceItemsAdditionRequest> itemsToAdd,
      List<Product> productsFromService) {
    List<Product> newProducts = new ArrayList<>();
    if (itemsToAdd == null) {
      return newProducts;
    }
    for (InvoiceItemsAdditionRequest item : itemsToAdd) {
      for (Product product : productsFromService) {
        if (product.getId().equals(item.getId())) {
          product.setQuantity(item.getQuantity());
          product.setDiscount(item.getDiscount());
          newProducts.add(product);
        }
      }
    }
    return newProducts;
  }


}
