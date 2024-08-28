package org.thi.sps.adapter.api.rest.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.thi.sps.domain.model.helper.InvoiceRequest;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class InvoiceCreationRequest {

  private String id;
  private String description;
  private List<InvoiceItemRequest> items;
  private Long clientId;
  private LocalDate offerDate;
  private LocalDate validUntil;
  private LocalDate dateOfDelivery;

  private double netTotal;
  private double taxTotal;
  private double total;

  public InvoiceRequest toInvoiceRequest() {
    return InvoiceRequest.builder()
        .description(this.description)
        .items(this.items.stream().map(InvoiceItemRequest::toInvoiceItem).toList())
        .clientId(this.clientId)
        .offerDate(this.offerDate)
        .validUntil(this.validUntil)
        .netTotal(this.netTotal)
        .taxTotal(this.taxTotal)
        .total(this.total)
        .build();
  }
}
