package org.thi.sps.domain.model;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class InvoiceRequest {

  private Long id;
  private String description;
  private List<InvoiceRequestItem> items;
  private Long clientId;
  private LocalDate offerDate;
  private LocalDate validUntil;
  private LocalDate dateOfDelivery;

  private double netTotal;
  private double taxTotal;
  private double total;

}
