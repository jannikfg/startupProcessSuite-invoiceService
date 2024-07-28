package org.thi.sps.adapter.jpa.Entities;

import static java.util.stream.Collectors.toList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
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
  private String invoiceId;
  private LocalDate createdDate;
  private String clientId;
  private String invoiceDocumentId;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "invoice")
  private List<InvoiceItemEntity> items;

  public Invoice toInvoice() {
    return Invoice.builder()
        .invoiceId(UUID.fromString(invoiceId))
        .createdDate(createdDate)
        .clientId(clientId)
        .invoiceDocumentId(invoiceDocumentId)
        .items(items.stream().map(InvoiceItemEntity::toInvoiceItem).collect(toList()))
        .build();
  }
}
