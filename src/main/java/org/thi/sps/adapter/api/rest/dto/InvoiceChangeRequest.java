package org.thi.sps.adapter.api.rest.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class InvoiceChangeRequest {
  private String id;
  private String description;
  private List<InvoiceItemRequest> invoiceItems;
  private String noticeOfTaxExemption;
  private String noticeOfRetentionObligation;
}
