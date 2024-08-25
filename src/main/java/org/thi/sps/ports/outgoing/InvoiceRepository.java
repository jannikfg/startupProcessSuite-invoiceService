package org.thi.sps.ports.outgoing;

import java.util.List;
import org.thi.sps.domain.model.Invoice;

public interface InvoiceRepository {

  Invoice save(Invoice invoice);

  Invoice update(Invoice invoice);

  Invoice findById(String id);

  void delete(String invoiceId);

  List<Invoice> findByClientId(String clientId);

  List<Invoice> findAll();

}
