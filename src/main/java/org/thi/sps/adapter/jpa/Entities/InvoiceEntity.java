package org.thi.sps.adapter.jpa.Entities;

import static java.util.stream.Collectors.toList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.thi.sps.domain.model.Invoice;

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
  private String noticeOfTaxExemption;
  private String noticeOfRetentionObligation;
  private double netTotal;
  private double taxTotal;
  private double total;

  public Invoice toInvoice() {
    return Invoice.builder()
        .id(this.id)
        .description(this.description)
        .createdDate(this.createdDate)
        .clientId(this.clientId)
        .invoiceItems(this.items.stream().map(InvoiceItemEntity::toInvoiceItem).collect(toList()))
        .dateOfDelivery(this.dateOfDelivery)
        .noticeOfTaxExemption(this.noticeOfTaxExemption)
        .noticeOfRetentionObligation(this.noticeOfRetentionObligation)
        .netTotal(this.netTotal)
        .taxTotal(this.taxTotal)
        .total(this.total)
        .build();
  }

  public static InvoiceEntity fromInvoice(Invoice invoice) {
    return new InvoiceEntity(invoice.getId(), invoice.getDescription(), invoice.getCreatedDate(),
        invoice.getClientId(),
        invoice.getInvoiceItems().stream().map(InvoiceItemEntity::fromInvoiceItem)
            .collect(toList()), invoice.getDateOfDelivery(), invoice.getNoticeOfTaxExemption(),
        invoice.getNoticeOfRetentionObligation(), invoice.getNetTotal(), invoice.getTaxTotal(),
        invoice.getTotal());
  }
}
