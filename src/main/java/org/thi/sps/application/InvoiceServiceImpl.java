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
      System.out.println("InvoiceItem in InvoiceServiceImpl in create Invoice before Calculation: " + invoiceItem);
      invoiceItem.setNetTotal(calculateNetTotalForInvoiceItem(invoiceItem));
      invoiceItem.setTaxTotal(calculateTaxTotalForInvoiceItem(invoiceItem));
      invoiceItem.setTotal(calculateTotalForInvoiceItem(invoiceItem));
      System.out.println("InvoiceItem in InvoiceServiceImpl in create Invoice after Calculation: " + invoiceItem);
    }
  }

  private double calculateTotalForInvoice(List<InvoiceItem> invoiceItems) {
    double value = invoiceItems.stream().mapToDouble(InvoiceItem::getTotal).sum();
    System.out.println("Total in calculateTotalForInvoice in InvoiceServiceImpl: " + value);
    return value;
  }

  private double calculateNetTotalForInvoice(List<InvoiceItem> invoiceItems) {
    double value = invoiceItems.stream().mapToDouble(InvoiceItem::getNetTotal).sum();
    System.out.println("NetTotal in calculateNetTotalForInvoice in InvoiceServiceImpl: " + value);
    return value;
  }

  private double calculateTaxTotalForInvoice(List<InvoiceItem> invoiceItems) {
    double value = invoiceItems.stream().mapToDouble(InvoiceItem::getTaxTotal).sum();
    System.out.println("TaxTotal in calculateTaxTotalForInvoice in InvoiceServiceImpl: " + value);
    return value;
  }

  private double calculateNetTotalForInvoiceItem(InvoiceItem invoiceItem) {
    double value = invoiceItem.getNetPrice() * invoiceItem.getQuantity() * (1 - invoiceItem.getDiscount());
    System.out.println("NetTotal in calculateNetTotalForInvoiceItem in InvoiceServiceImpl: " + value);
    return value;
  }

  private double calculateTaxTotalForInvoiceItem(InvoiceItem invoiceItem) {
    double value = invoiceItem.getNetTotal() * invoiceItem.getTaxRate();
    System.out.println("TaxTotal in calculateTaxTotalForInvoiceItem in InvoiceServiceImpl: " + value);
    return value;
  }

  private double calculateTotalForInvoiceItem(InvoiceItem invoiceItem) {
    double value = invoiceItem.getNetTotal() + invoiceItem.getTaxTotal();
    System.out.println("Total in calculateTotalForInvoiceItem in InvoiceServiceImpl: " + value);
    return value;
  }
  @Override
  public Invoice updateInvoiceWithNewProducts(Invoice invoice, List<Product> items) {
    List<InvoiceItem> invoiceItems = new ArrayList<>(invoice.getInvoiceItems());
    System.out.println("InvoiceItems in updateInvoiceWithNewProducts in InvoiceServiceImpl: " + invoiceItems);
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
    System.out.println("Products in transfromNewProductsToInvoiceItems in InvoiceServiceImpl: " + products);
    List<InvoiceItem> invoiceItems = new ArrayList<>();
    for (Product item : products) {
      invoiceItems.add(InvoiceItem.builder()
          .id(item.getId())
          .name(item.getName())
          .description(item.getDescription())
          .category(item.getCategory())
          .quantity(item.getQuantity())
          .unit(item.getUnit())
          .netPrice(item.getNetPrice() - item.getNetPrice() * item.getDiscount())
          .taxRate(item.getTaxRate() != null ? Double.parseDouble(item.getTaxRate()) : 0.0)
          .netTotal(item.getNetPrice() * item.getQuantity())
          .taxTotal(item.getNetPrice() * item.getQuantity() * Double.parseDouble(item.getTaxRate()))
          .total(item.getNetPrice() * item.getQuantity() * Double.parseDouble(item.getTaxRate())
              + item.getNetPrice() * item.getQuantity())
          .build());
    }
    System.out.println("InvoiceItems in transfromNewProductsToInvoiceItems in InvoiceServiceImpl: " + invoiceItems);
    return invoiceItems;
  }

  @Override
  public Invoice getInvoiceById(String invoiceId) {
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
    System.out.println("Invoices in InvoiceServiceImpl: " + invoices);
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
          newProducts.add(product);
        }
      }
    }
    System.out.println("New Products in getProducts in InvoiceServiceImpl: " + newProducts);
    return newProducts;
  }


}
