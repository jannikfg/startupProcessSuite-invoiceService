package org.thi.sps.adapter.api.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import lombok.NoArgsConstructor;
import jakarta.ws.rs.core.MediaType;
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
  @Path("/{id}")
  public InvoiceResponse getInvoiceById(@PathParam("id") String id) {
    Invoice invoice = invoiceService.getInvoiceById(id);

    return InvoiceResponse.fromInvoice(invoice);
  }
}
