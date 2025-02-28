package org.thi.sps.application;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thi.sps.application.exeptions.InvoiceNotFoundException;
import org.thi.sps.domain.InvoiceService;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.InvoiceItem;
import org.thi.sps.domain.model.Payment;
import org.thi.sps.domain.model.Reminder;
import org.thi.sps.domain.model.helper.InvoiceChangeRequest;
import org.thi.sps.domain.model.helper.InvoiceItemRequest;
import org.thi.sps.domain.model.helper.InvoiceRequest;
import org.thi.sps.ports.outgoing.InvoiceRepository;

@ApplicationScoped
public class InvoiceServiceImpl implements InvoiceService {

  @Inject
  InvoiceRepository invoiceRepository;

  private static final Logger LOG = LoggerFactory.getLogger(InvoiceServiceImpl.class);

  private final String noticeOfTaxExemption = "Dieser Text kann als Hinweis auf eine Steuerbefreiung genutzt werden.";
  private final String noticeOfRetentionObligation = "Dieser Text kann als Hinweis auf eine Aufbewahrungspflicht genutzt werden.";


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

  public Invoice buildInvoiceFromInvoiceChangeRequest(
      InvoiceChangeRequest invoiceChangeRequest) {
    return Invoice.builder()
        .id(invoiceChangeRequest.getId())
        .description(invoiceChangeRequest.getDescription())
        .invoiceItems(
            invoiceChangeRequest.getInvoiceItems().stream().map(InvoiceItemRequest::toInvoiceItem)
                .toList())
        .noticeOfRetentionObligation(invoiceChangeRequest.getNoticeOfRetentionObligation())
        .noticeOfTaxExemption(invoiceChangeRequest.getNoticeOfTaxExemption())
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
    double paymentsTotal = 0;
    if (invoice.getPayments() != null) {
      paymentsTotal = invoice.getPayments().stream().mapToDouble(Payment::getAmount).sum();
    }
    double totalOutstanding = invoice.getTotal() - paymentsTotal;
    return totalOutstanding;
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
    invoiceFromDb.setDueDate(invoice.getDueDate());
    invoiceFromDb.setPayments(invoice.getPayments());
    invoiceFromDb.setTotalOutstanding(calculateTotalOutstanding(invoiceFromDb));
    invoiceFromDb.setPaid(invoice.isPaid());
    if (invoice.getPayments() != null) {
      invoiceFromDb.setPayments(invoice.getPayments());
    }
    if (invoice.getReminders() != null) {
      invoiceFromDb.setReminders(invoice.getReminders());
    }
    return invoiceRepository.update(invoiceFromDb);
  }


  @Override
  public Invoice getInvoiceById(String invoiceId) {
    return invoiceRepository.findById(invoiceId);
  }

  @Override
  public List<Invoice> getInvoicesByClientId(String clientId) {
    return invoiceRepository.findByClientId(clientId);
  }

  // Erzeugt eine Rechnungs-ID im Format YYYYMM-XXXXX
  public String generateInvoiceId() {
    LocalDate currentDate = LocalDate.now();
    String yearMonthPart = currentDate.format(DateTimeFormatter.ofPattern("yyyyMM"));

    int invoiceNumber = getNextInvoiceNumberForMonth(yearMonthPart);

    return String.format("%s-%05d", yearMonthPart, invoiceNumber);
  }

  private int getNextInvoiceNumberForMonth(String yearMonthPart) {
    try {
      Integer currentMaxInvoiceNumber = invoiceRepository.getMaxInvoiceNumberForMonth(yearMonthPart);
      if (currentMaxInvoiceNumber == null) {
        currentMaxInvoiceNumber = 0;
      }

      return currentMaxInvoiceNumber + 1;
    } catch (Exception e) {
      LOG.error("Fehler beim Abrufen der nächsten Rechnungsnummer für Monat {}: ", yearMonthPart, e);
      return generateFallbackInvoiceNumber();
    }
  }

  private int generateFallbackInvoiceNumber() {
    return (int) (Math.random() * 100000);
  }

  @Override
  public List<Invoice> getAllInvoices() {
    return invoiceRepository.findAll();
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

  @Override
  public Invoice addReminderToInvoice(int dueInDays, String reminderLevel, String invoiceId) {
    Invoice invoiceFromDb = invoiceRepository.findById(invoiceId);
    if (invoiceFromDb == null) {
      throw new InvoiceNotFoundException("Invoice not found with id: " + invoiceId);
    }
    List<Reminder> reminders = invoiceFromDb.getReminders();
    if (reminders == null) {
      reminders = new ArrayList<>();
    }

    String reminderId = invoiceId + "-" + reminderLevel;

    reminders.add(Reminder.builder()
        .id(reminderId)
        .dueInDays(dueInDays)
        .reminderLevel(reminderLevel)
        .paymentIds(new ArrayList<String>(invoiceFromDb.getPayments()
            .stream()
            .map(payment -> String.valueOf(payment.getId()))
            .toList()))
        .reminderDate(LocalDate.now())
        .totalOutstanding(invoiceFromDb.getTotalOutstanding())
        .build());
    invoiceFromDb.setReminders(reminders);
    return updateInvoice(invoiceFromDb);
  }

}
