package org.thi.sps.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.thi.sps.domain.model.ENUM.ProductCategory;
import org.thi.sps.domain.model.ENUM.TaxRate;
import org.thi.sps.domain.model.ENUM.Unit;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.domain.model.InvoiceItem;

public interface InvoiceService {

  Invoice createInvoice(List<InvoiceItem> invoiceItems, String clientId,
      LocalDate dateOfDelivery, String invoiceDocumentId, String noticeOfTaxExemption,
      String noticeOfRetentionObligation);

  void addItem(String invoiceId, ProductCategory category, String description, double price,
      int quantity, Unit unit, TaxRate tax, double discount);

  void removeItem(String invoiceId, String description);

  Invoice getInvoiceById(String invoiceId);

  List<Invoice> getInvoicesByClientId(String clientId);

  String generateInvoiceId();

}