package org.thi.sps.domain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import org.thi.sps.adapter.api.rest.dto.InvoiceChangeRequest;
import org.thi.sps.adapter.api.rest.dto.InvoiceItemsAdditionRequest;
import org.thi.sps.adapter.api.rest.dto.NewDocumentCreationRequest;
import org.thi.sps.domain.model.CreditNote;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.Product;
import org.thi.sps.domain.model.helper.InvoiceRequest;

public interface InvoiceService {

  Invoice createInvoice(InvoiceRequest invoiceRequest);

  //Funktion in aktueller Implementierung, um den Prozess optimal abzubilden.
  // Neue Rechnungspoistionen werden zunächst in Form von Produkten übergeben und dann hinzuegfügt
  Invoice updateInvoiceWithNewProducts(Invoice invoice, List<Product> items);

  String getLatestDocumentOfInvoice(String invoiceId);

  String addNewDocumentToInvoice(NewDocumentCreationRequest newDocumentCreationRequest);

  //Funktion, falls Vorbefüllung bereits im Frontend stattfindet
  Invoice updateInvoice(Invoice invoice);

  Invoice getInvoiceById(String invoiceId);

  List<Invoice> getInvoicesByClientId(String clientId);

  String generateInvoiceId();


  List<Invoice> getAllInvoices();

  List<Product> getProducts(List<InvoiceItemsAdditionRequest> itemsToAdd,
      List<Product> productsFromService);

  Invoice addCreditNoteToInvoice(String invoiceId, String reason, double amount);

  Invoice addPaymentToInvoice(String invoiceId, LocalDate paymentDay, double amount, String method, String reference);

  Invoice buildInvoiceFromInvoiceChangeRequest(InvoiceChangeRequest invoiceChangeRequest);
}