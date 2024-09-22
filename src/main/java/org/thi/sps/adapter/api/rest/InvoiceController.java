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
import org.thi.sps.adapter.api.rest.dto.InvoiceChangeDueDateRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceChangeRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceChangeWithNewProductsRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceCreationRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceResponse;
import org.thi.sps.adapter.api.rest.dto.PaymentAdditionRequest;
import org.thi.sps.adapter.api.rest.dto.ReminderAdditionRequest;
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
  public InvoiceResponse getInvoice(@PathParam("id") String id) {
    System.out.println("Get invoice with id: " + id);
    Invoice invoice = invoiceService.getInvoiceById(id);
    return InvoiceResponse.fromInvoice(invoice);
  }


  @POST
  @Path("/updateWithNewProducts")
  public InvoiceResponse updateInvoice(
      @RequestBody InvoiceChangeWithNewProductsRequest invoiceChangeWithNewProductsRequest) {
    List<Product> newProducts = invoiceService.getProducts(
        invoiceChangeWithNewProductsRequest.getItemsToAdd(),
        invoiceChangeWithNewProductsRequest.getProductsFromService());
    Invoice invoice = invoiceService.buildInvoiceFromInvoiceChangeRequest(
        invoiceChangeWithNewProductsRequest.getInvoiceChangeRequest());
    Invoice invoiceUpdated = invoiceService.updateInvoiceWithNewProducts(invoice, newProducts);
    return InvoiceResponse.fromInvoice(invoiceUpdated);
  }

  @POST
  @Path("/update")
  public InvoiceResponse updateInvoice(InvoiceChangeRequest invoiceChangeRequest) {
    Invoice invoice = invoiceService.buildInvoiceFromInvoiceChangeRequest(invoiceChangeRequest);
    Invoice invoiceUpdated = invoiceService.updateInvoice(invoice);
    return InvoiceResponse.fromInvoice(invoiceUpdated);
  }

  @GET
  @Path("/{id}/setPaid")
  public InvoiceResponse setPaid(@PathParam("id") String id) {
    Invoice invoice = invoiceService.getInvoiceById(id);
    invoice.setPaid(true);
    Invoice updatedInvoice = invoiceService.updateInvoice(invoice);
    return InvoiceResponse.fromInvoice(updatedInvoice);
  }

  @POST
  @Path("/addPayment")
  public InvoiceResponse addPayment(@RequestBody PaymentAdditionRequest paymentAdditionRequest) {
    System.out.println("Add payment to invoice: " + paymentAdditionRequest);
    Invoice invoice = invoiceService.addPaymentToInvoice(paymentAdditionRequest.getInvoiceId(),
        paymentAdditionRequest.getPaymentDate(), paymentAdditionRequest.getAmount(),
        paymentAdditionRequest.getMethod(), paymentAdditionRequest.getReference());
    Invoice updatedInvoice = invoiceService.updateInvoice(invoice);
    return InvoiceResponse.fromInvoice(updatedInvoice);
  }

  @POST
  @Path("/changeDueDate")
  public InvoiceResponse changeDueDate(
      @RequestBody InvoiceChangeDueDateRequest invoiceChangeDueDateRequest) {
    Invoice invoice = invoiceService.getInvoiceById(invoiceChangeDueDateRequest.getInvoiceId());
    invoice.setDueDate(invoiceChangeDueDateRequest.getNewDueDate());
    Invoice updatedInvoice = invoiceService.updateInvoice(invoice);
    return InvoiceResponse.fromInvoice(updatedInvoice);
  }

  @POST
  @Path("/addReminder")
  public InvoiceResponse addReminder(@RequestBody ReminderAdditionRequest reminderAdditionRequest) {
    Invoice invoice = invoiceService.addReminderToInvoice(reminderAdditionRequest.getDueInDays(),
        reminderAdditionRequest.getReminderLevel(), reminderAdditionRequest.getInvoiceId());
    return InvoiceResponse.fromInvoice(invoice);
  }
}
