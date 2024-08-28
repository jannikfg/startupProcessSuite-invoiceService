package org.thi.sps.adapter.api.rest.dto;

import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.thi.sps.domain.model.Product;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@Builder
public class InvoiceChangeWithNewProductsRequest {
  private InvoiceChangeRequest invoiceChangeRequest;
  private List<InvoiceItemsAdditionRequest> itemsToAdd;
  private List<Product> productsFromService;
}
