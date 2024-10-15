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
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.thi.sps.adapter.api.rest.dto.InvoiceChangeDueDateRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceChangeRequestDTO;
import org.thi.sps.adapter.api.rest.dto.InvoiceChangeWithNewProductsRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceCreationRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceItemsAdditionRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceResponse;
import org.thi.sps.adapter.api.rest.dto.PaymentAdditionRequest;
import org.thi.sps.adapter.api.rest.dto.Product;
import org.thi.sps.adapter.api.rest.dto.ReminderAdditionRequest;
import org.thi.sps.domain.InvoiceService;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.InvoiceItem;
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
    List<Product> newProducts = getProducts(
        invoiceChangeWithNewProductsRequest.getItemsToAdd(),
        invoiceChangeWithNewProductsRequest.getProductsFromService());
    Invoice invoice = invoiceService.buildInvoiceFromInvoiceChangeRequest(
        invoiceChangeWithNewProductsRequest.getInvoiceChangeRequestDTO().toInvoiceChangeRequest());
    Invoice invoiceUpdated = updateInvoiceWithNewProducts(invoice, newProducts);
    return InvoiceResponse.fromInvoice(invoiceUpdated);
  }

  @POST
  @Path("/update")
  public InvoiceResponse updateInvoice(InvoiceChangeRequestDTO invoiceChangeRequestDTO) {
    Invoice invoice = invoiceService.buildInvoiceFromInvoiceChangeRequest(invoiceChangeRequestDTO.toInvoiceChangeRequest());
    Invoice invoiceUpdated = invoiceService.updateInvoice(invoice);
    return InvoiceResponse.fromInvoice(invoiceUpdated);
  }

  @POST
  @Path("/updateDueDate")
  public InvoiceResponse updateDueDate(InvoiceChangeDueDateRequest invoiceChangeDueDateRequest) {
    Invoice invoice = invoiceService.getInvoiceById(invoiceChangeDueDateRequest.getInvoiceId());
    invoice.setDueDate(invoiceChangeDueDateRequest.getNewDueDate());
    Invoice updatedInvoice = invoiceService.updateInvoice(invoice);
    return InvoiceResponse.fromInvoice(updatedInvoice);
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

  public List<Product> getProducts(List<InvoiceItemsAdditionRequest> itemsToAdd,
      List<Product> productsFromService) {
    List<Product> newProducts = new ArrayList<>();
    if (itemsToAdd == null) {
      return newProducts;
    }
    for (InvoiceItemsAdditionRequest item : itemsToAdd) {
      for (Product product : productsFromService) {
        if (product.getId().equals(item.getId())) {
          product.setQuantity(item.getQuantity());
          product.setDiscount(item.getDiscount());
          newProducts.add(product);
        }
      }
    }
    return newProducts;
  }

  public Invoice updateInvoiceWithNewProducts(Invoice invoice, List<Product> items) {
    List<InvoiceItem> invoiceItems = new ArrayList<>(invoice.getInvoiceItems());
    if (items != null) {
      invoiceItems.addAll(transfromNewProductsToInvoiceItems(items));
    }
    invoice.setInvoiceItems(invoiceItems);
    return invoiceService.updateInvoice(invoice);
  }

  private List<InvoiceItem> transfromNewProductsToInvoiceItems(List<Product> products) {
    List<InvoiceItem> invoiceItems = new ArrayList<>();
    for (Product item : products) {
      invoiceItems.add(InvoiceItem.builder()
          .name(item.getName())
          .description(item.getDescription())
          .category(item.getCategory())
          .quantity(item.getQuantity())
          .unit(item.getUnit())
          .discount(item.getDiscount())
          .netPrice(item.getNetPrice() - item.getNetPrice() * 100/item.getDiscount())
          .taxRate(parseTaxRate(item.getTaxRate()))
          .netTotal(item.getNetPrice() * item.getQuantity())
          .taxTotal(item.getNetPrice() * item.getQuantity() * Double.parseDouble(item.getTaxRate()))
          .total(item.getNetPrice() * item.getQuantity() * Double.parseDouble(item.getTaxRate())
              + item.getNetPrice() * item.getQuantity())
          .build());
    }
    return invoiceItems;
  }

  private double parseTaxRate(String taxRateStr) {
    try {
      return taxRateStr != null ? Double.parseDouble(taxRateStr) : 0.0;
    } catch (NumberFormatException e) {
      return 0.19;  // Standardwert, wenn das Parsen fehlschlägt
    }
  }
}
