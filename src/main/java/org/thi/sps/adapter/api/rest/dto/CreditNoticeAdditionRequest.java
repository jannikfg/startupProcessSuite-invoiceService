package org.thi.sps.adapter.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CreditNoticeAdditionRequest {

  private String invoiceId;
  private String reason;
  private double amount;

}
