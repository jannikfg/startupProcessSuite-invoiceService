package org.thi.sps.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.thi.sps.adapter.api.rest.dto.InvoiceChangeRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceItemRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceItemsAdditionRequest;
import org.thi.sps.application.exeptions.InvoiceNotFoundException;
import org.thi.sps.domain.InvoiceService;
import org.thi.sps.domain.model.CreditNote;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.InvoiceItem;
import org.thi.sps.domain.model.Payment;
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
        .dueDate(calculateDueDate(invoiceRequest.getDateOfDelivery()))
        .noticeOfTaxExemption(noticeOfTaxExemption)
        .noticeOfRetentionObligation(noticeOfRetentionObligation)
        .netTotal(calculateNetTotalForInvoice(invoiceItems))
        .taxTotal(calculateTaxTotalForInvoice(invoiceItems))
        .total(calculateTotalForInvoice(invoiceItems))
        .totalOutstanding(calculateTotalForInvoice(invoiceItems))
        .paid(false)
        .build();
    return invoiceRepository.save(invoice);
  }

  private LocalDate calculateDueDate(LocalDate dateOfDelivery) {
    return dateOfDelivery.plusDays(14);
  }

  public Invoice buildInvoiceFromInvoiceChangeRequest(InvoiceChangeRequest invoiceChangeRequest) {
    return Invoice.builder()
        .id(invoiceChangeRequest.getId())
        .description(invoiceChangeRequest.getDescription())
        .invoiceItems(
            invoiceChangeRequest.getInvoiceItems().stream().map(InvoiceItemRequest::toInvoiceItem)
                .toList())
        .noticeOfRetentionObligation(invoiceChangeRequest.getNoticeOfRetentionObligation())
        .noticeOfTaxExemption(invoiceChangeRequest.getNoticeOfTaxExemption())
        .documents(invoiceChangeRequest.getDocuments())
        .build();
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
    double netPrice = invoiceItem.getNetPrice() >= 0 ? invoiceItem.getNetPrice() : 0;
    double quantity = invoiceItem.getQuantity() >= 0 ? invoiceItem.getQuantity() : 0;
    double discount = invoiceItem.getDiscount() >= 0 && invoiceItem.getDiscount() <= 100 ? invoiceItem.getDiscount() : 0;

    return netPrice * quantity * (1 - discount / 100);
  }

  private double calculateTaxTotalForInvoiceItem(InvoiceItem invoiceItem) {
    return invoiceItem.getNetTotal() * invoiceItem.getTaxRate();
  }

  private double calculateTotalForInvoiceItem(InvoiceItem invoiceItem) {
    return invoiceItem.getNetTotal() + invoiceItem.getTaxTotal();
  }

  private double calculateTotalOutstanding(Invoice invoice) {

    if (invoice.getCreditNotes() == null && invoice.getPayments() == null) {
      return invoice.getTotal();
    }

    double creditNotesTotal = 0;
    if (invoice.getCreditNotes() != null) {
      creditNotesTotal = invoice.getCreditNotes().stream().mapToDouble(CreditNote::getAmount)
          .sum();
    }

    double paymentsTotal = 0;
    if (invoice.getPayments() != null) {
      paymentsTotal = invoice.getPayments().stream().mapToDouble(Payment::getAmount).sum();
    }
    double totalOutstanding = invoice.getTotal() - creditNotesTotal - paymentsTotal;
    if (totalOutstanding < 0) {
      return 0;
    }
    return totalOutstanding;
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

  /*
  @Override
  public String addNewDocumentToInvoice(NewDocumentCreationRequest newDocumentCreationRequest) {
    Invoice invoice = getInvoiceById(newDocumentCreationRequest.getInvoiceId());
    List<Document> documents = new ArrayList<>(invoice.getDocuments());
    String documentId = getNextDocumentId(invoice);
    documents.add(Document.builder()
        .id(documentId)
        .reason(newDocumentCreationRequest.getReason())
        .build());
    invoice.setDocuments(documents);
    updateInvoice(invoice);
    return documentId;
  }


  public String getNextDocumentId(Invoice invoice) {
    return invoice.getId() + "_" + (invoice.getDocuments().size() + 1);
  }
   */

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
    invoiceFromDb.setDueDate(invoice.getDueDate());
    invoiceFromDb.setCreditNotes(invoice.getCreditNotes());
    invoiceFromDb.setPayments(invoice.getPayments());
    invoiceFromDb.setTotalOutstanding(calculateTotalOutstanding(invoice));
    invoiceFromDb.setPaid(invoice.isPaid());
    if (invoice.getCreditNotes() != null) {
      invoiceFromDb.setCreditNotes(invoice.getCreditNotes());
    }
    if (invoice.getPayments() != null) {
      invoiceFromDb.setPayments(invoice.getPayments());
    }
    if (invoice.getDocuments() != null) {
      invoiceFromDb.setDocuments(invoice.getDocuments());
    }
    return invoiceRepository.update(invoiceFromDb);
  }

  private List<InvoiceItem> transfromNewProductsToInvoiceItems(List<Product> products) {
    List<InvoiceItem> invoiceItems = new ArrayList<>();
    for (Product item : products) {
      invoiceItems.add(InvoiceItem.builder()
          .name(item.getName())
          .description(item.getDescription())
          .category(item.getCategory())
          .quantity(item.getQuantity())
          .unit(item.getUnit())
          .discount(item.getDiscount())
          .netPrice(item.getNetPrice() - item.getNetPrice() * 100/item.getDiscount())
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
    //TODO: Hier muss noch eine ausfallsichere Lösung her
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

  @Override
  public Invoice addCreditNoteToInvoice(String invoiceId, String reason, double amount) {
    Invoice invoice = getInvoiceById(invoiceId);
    CreditNote creditNote = CreditNote.builder()
        .invoiceId(invoiceId)
        .createdDate(LocalDate.now())
        .reason(reason)
        .amount(amount)
        .build();
    List<CreditNote> creditNotes = new ArrayList<>(invoice.getCreditNotes());
    creditNotes.add(creditNote);
    invoice.setCreditNotes(creditNotes);

    return updateInvoice(invoice);
  }

  @Override
  public Invoice addPaymentToInvoice(String invoiceId, LocalDate paymentDay, double amount,
      String method, String reference) {
    Invoice invoice = getInvoiceById(invoiceId);
    Payment payment = Payment.builder()
        .invoiceId(invoiceId)
        .paymentDate(paymentDay)
        .amount(amount)
        .method(method)
        .reference(reference)
        .build();
    List<Payment> payments = new ArrayList<>(invoice.getPayments());
    payments.add(payment);
    invoice.setPayments(payments);

    return updateInvoice(invoice);
  }

}
