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
import lombok.NoArgsConstructor;
import org.thi.sps.adapter.api.rest.dto.InvoiceCreationRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceItemAddRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceResponse;
import org.thi.sps.domain.InvoiceService;
import org.thi.sps.domain.model.Invoice;

@ApplicationScoped
@NoArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/v1/invoices")
public class InvoiceController {

  @Inject
  InvoiceService invoiceService;

  @GET
  @Path("/{invoiceId}")
  public InvoiceResponse getInvoiceById(@PathParam("invoiceId") String id) {
    Invoice invoice = invoiceService.getInvoiceById(id);
    return InvoiceResponse.fromInvoice(invoice);
  }

  @POST
  @Path("/")
  public InvoiceResponse createInvoice(InvoiceCreationRequest invoiceCreationRequest) {
    Invoice invoice = invoiceService.createInvoice(
        invoiceCreationRequest.getInvoiceItems(), invoiceCreationRequest.getClientId(),
        invoiceCreationRequest.getDateOfDelivery(), invoiceCreationRequest.getInvoiceDocumentId(),
        invoiceCreationRequest.getNoticeOfTaxExemption(),
        invoiceCreationRequest.getNoticeOfRetentionObligation());

    return InvoiceResponse.fromInvoice(invoice);
  }

  @POST
  @Path("/{invoiceId}/items")
  public void addItem(@PathParam("invoiceId") String invoiceId,
      InvoiceItemAddRequest invoiceItemAddRequest) {
    invoiceService.addItem(invoiceId, invoiceItemAddRequest.getCategory(),
        invoiceItemAddRequest.getDescription(), invoiceItemAddRequest.getPrice(),
        invoiceItemAddRequest.getQuantity(), invoiceItemAddRequest.getUnit(),
        invoiceItemAddRequest.getTax(), invoiceItemAddRequest.getDiscount());
  }

}
