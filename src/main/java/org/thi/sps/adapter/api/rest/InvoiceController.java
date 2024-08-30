package org.thi.sps.adapter.api.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.thi.sps.adapter.api.rest.dto.InvoiceChangeRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceChangeWithNewProductsRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceCreationRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceItemRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceResponse;
import org.thi.sps.domain.InvoiceService;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.Product;
import org.thi.sps.domain.model.helper.InvoiceRequest;

@ApplicationScoped
@NoArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/v1/invoices")
public class InvoiceController {

  @Inject
  InvoiceService invoiceService;


  @POST
  @Path("/create")
  public InvoiceResponse createInvoice(InvoiceCreationRequest invoiceCreationRequest) {
    InvoiceRequest invoiceRequest = invoiceCreationRequest.toInvoiceRequest();
    Invoice invoice = invoiceService.createInvoice(invoiceRequest);

    return InvoiceResponse.fromInvoice(invoice);
  }

  @GET
  @Path("/all")
  public List<InvoiceResponse> getAllInvoices() {
    List<Invoice> invoices = invoiceService.getAllInvoices();
    return invoices.stream().map(InvoiceResponse::fromInvoice).toList();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public InvoiceResponse getInvoice(@PathParam("id")String id) {
    Invoice invoice = invoiceService.getInvoice(id);
    return InvoiceResponse.fromInvoice(invoice);
  }

  @POST
  @Path("/updateWithNewProducts")
  public InvoiceResponse updateInvoice(
      @RequestBody InvoiceChangeWithNewProductsRequest invoiceChangeWithNewProductsRequest) {
    List<Product> newProducts = invoiceService.getProducts(
        invoiceChangeWithNewProductsRequest.getItemsToAdd(),
        invoiceChangeWithNewProductsRequest.getProductsFromService());
    Invoice invoice = buildInvoiceFromInvoiceChangeRequest(
        invoiceChangeWithNewProductsRequest.getInvoiceChangeRequest());
    Invoice invoiceUpdated = invoiceService.updateInvoiceWithNewProducts(invoice, newProducts);
    return InvoiceResponse.fromInvoice(invoiceUpdated);
  }

  @POST
  @Path("/update")
  public InvoiceResponse updateInvoice(InvoiceChangeRequest invoiceChangeRequest) {
    Invoice invoice = buildInvoiceFromInvoiceChangeRequest(invoiceChangeRequest);
    Invoice invoiceUpdated = invoiceService.updateInvoice(invoice);
    return InvoiceResponse.fromInvoice(invoiceUpdated);
  }

  private Invoice buildInvoiceFromInvoiceChangeRequest(InvoiceChangeRequest invoiceChangeRequest) {
    return Invoice.builder()
        .id(invoiceChangeRequest.getId())
        .description(invoiceChangeRequest.getDescription())
        .invoiceItems(
            invoiceChangeRequest.getInvoiceItems().stream().map(InvoiceItemRequest::toInvoiceItem)
                .toList())
        .noticeOfRetentionObligation(invoiceChangeRequest.getNoticeOfRetentionObligation())
        .noticeOfTaxExemption(invoiceChangeRequest.getNoticeOfTaxExemption())
        .build();
  }


}
