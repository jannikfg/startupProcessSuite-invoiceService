package org.thi.sps.adapter.api.rest.dto;

import io.quarkus.arc.All;
import jakarta.ws.rs.GET;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAdditionRequest {

  private String invoiceId;
  private LocalDate paymentDate;
  private double amount;
  private String method;
  private String reference;

}
