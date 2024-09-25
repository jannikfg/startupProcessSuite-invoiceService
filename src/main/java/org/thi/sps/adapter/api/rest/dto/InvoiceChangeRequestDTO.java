package org.thi.sps.adapter.api.rest.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.thi.sps.domain.model.helper.InvoiceChangeRequest;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class InvoiceChangeRequestDTO {
  private String id;
  private String description;
  private LocalDate dueDate;
  private List<InvoiceItemRequestDTO> invoiceItems;
  private String noticeOfTaxExemption;
  private String noticeOfRetentionObligation;

  public InvoiceChangeRequest toInvoiceChangeRequest() {
    return InvoiceChangeRequest.builder()
        .id(this.id)
        .description(this.description)
        .dueDate(this.dueDate)
        .invoiceItems(this.invoiceItems.stream().map(InvoiceItemRequestDTO::toInvoiceItemRequest).toList())
        .noticeOfTaxExemption(this.noticeOfTaxExemption)
        .noticeOfRetentionObligation(this.noticeOfRetentionObligation)
        .build();
  }
}
