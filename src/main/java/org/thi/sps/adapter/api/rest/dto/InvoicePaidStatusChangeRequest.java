package org.thi.sps.adapter.api.rest.dto;

import io.quarkus.arc.All;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InvoicePaidStatusChangeRequest {
  private String invoiceId;
  private boolean paid;

}
