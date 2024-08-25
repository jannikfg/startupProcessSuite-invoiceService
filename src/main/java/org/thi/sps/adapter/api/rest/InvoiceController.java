package org.thi.sps.adapter.api.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import lombok.NoArgsConstructor;
import org.thi.sps.adapter.api.rest.dto.InvoiceCreationRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceResponse;
import org.thi.sps.domain.InvoiceService;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.InvoiceRequest;

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
    System.out.println(invoiceCreationRequest);

    InvoiceRequest invoiceRequest = invoiceCreationRequest.toInvoiceRequest();
    System.out.println(invoiceRequest);

    Invoice invoice = invoiceService.createInvoice(invoiceRequest);
    System.out.println(invoice);
    return InvoiceResponse.fromInvoice(invoice);
  }

  @GET
  @Path("/all")
  public List<InvoiceResponse> getAllInvoices() {
    List<Invoice> invoices = invoiceService.getAllInvoices();
    return invoices.stream().map(InvoiceResponse::fromInvoice).toList();
  }


}
