package org.thi.sps.domain.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.*;

@AllArgsConstructor
@Data
@Builder
public class Invoice {

  @Setter(AccessLevel.NONE)
  private UUID invoiceId;
  private LocalDate createdDate;
  private List<InvoiceItem> items;
  private String clientId;

  public Invoice() {
    this.invoiceId = UUID.randomUUID();
    this.createdDate = LocalDate.now();
  }

  public double getTotal() {
    return items.stream().mapToDouble(InvoiceItem::getTotal).sum();
  }

}
