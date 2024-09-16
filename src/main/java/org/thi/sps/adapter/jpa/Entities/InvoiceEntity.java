package org.thi.sps.adapter.jpa.Entities;

import static java.util.stream.Collectors.toList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.thi.sps.domain.model.CreditNote;
import org.thi.sps.domain.model.Document;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.InvoiceItem;
import org.thi.sps.domain.model.Payment;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "invoices")
public class InvoiceEntity {

  @Id
  private String id;
  private String description;
  private LocalDate createdDate;
  private Long clientId;

  @OneToMany(cascade = CascadeType.ALL)
  private List<InvoiceItemEntity> items;

  private LocalDate dateOfDelivery;
  private LocalDate dueDate;
  private String noticeOfTaxExemption;
  private String noticeOfRetentionObligation;
  private double netTotal;
  private double taxTotal;
  private double total;
  private double totalOutstanding;
  private boolean paid;

  @OneToMany(cascade = CascadeType.ALL)
  private List<CreditNoteEntity> creditNotes;

  @OneToMany(cascade = CascadeType.ALL)
  private List<PaymentEntity> payments;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "invoice_documents", joinColumns = @JoinColumn(name = "invoice_id"))
  private List<Document> documents;

  public Invoice toInvoice() {
    List<InvoiceItem> items = Optional.ofNullable(this.items)
        .map(list -> list.stream().map(InvoiceItemEntity::toInvoiceItem).collect(toList()))
        .orElseGet(Collections::emptyList);

    List<CreditNote> creditNotes = Optional.ofNullable(this.creditNotes)
        .map(list -> list.stream().map(CreditNoteEntity::toCreditNote).collect(toList()))
        .orElseGet(Collections::emptyList);

    List<Payment> payments = Optional.ofNullable(this.payments)
        .map(list -> list.stream().map(PaymentEntity::toPayment).collect(toList()))
        .orElseGet(Collections::emptyList);

    return Invoice.builder()
        .id(this.id)
        .description(this.description)
        .createdDate(this.createdDate)
        .clientId(this.clientId)
        .invoiceItems(items)
        .dateOfDelivery(this.dateOfDelivery)
        .dueDate(this.dateOfDelivery)
        .noticeOfTaxExemption(this.noticeOfTaxExemption)
        .noticeOfRetentionObligation(this.noticeOfRetentionObligation)
        .netTotal(this.netTotal)
        .taxTotal(this.taxTotal)
        .total(this.total)
        .totalOutstanding(this.totalOutstanding)
        .paid(this.paid)
        .creditNotes(creditNotes)
        .payments(payments)
        .documents(this.documents)
        .build();
  }

  public static InvoiceEntity fromInvoice(Invoice invoice) {
    List<InvoiceItemEntity> items = Optional.ofNullable(invoice.getInvoiceItems())
        .map(list -> list.stream().map(InvoiceItemEntity::fromInvoiceItem).collect(toList()))
        .orElseGet(Collections::emptyList);

    List<CreditNoteEntity> creditNotes = Optional.ofNullable(invoice.getCreditNotes())
        .map(list -> list.stream().map(CreditNoteEntity::fromCreditNote).collect(toList()))
        .orElseGet(Collections::emptyList);

    List<PaymentEntity> payments = Optional.ofNullable(invoice.getPayments())
        .map(list -> list.stream().map(PaymentEntity::fromPayment).collect(toList()))
        .orElseGet(Collections::emptyList);

    return new InvoiceEntity(
        invoice.getId(),
        invoice.getDescription(),
        invoice.getCreatedDate(),
        invoice.getClientId(),
        items,
        invoice.getDateOfDelivery(),
        invoice.getDueDate(),
        invoice.getNoticeOfTaxExemption(),
        invoice.getNoticeOfRetentionObligation(),
        invoice.getNetTotal(),
        invoice.getTaxTotal(),
        invoice.getTotal(),
        invoice.getTotalOutstanding(),
        invoice.isPaid(),
        creditNotes,
        payments,
        invoice.getDocuments()
    );
  }
}
