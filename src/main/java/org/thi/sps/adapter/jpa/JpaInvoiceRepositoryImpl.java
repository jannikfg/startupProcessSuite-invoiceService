package org.thi.sps.adapter.jpa;

import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import org.thi.sps.adapter.jpa.Entities.InvoiceEntity;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.ports.outgoing.InvoiceRepository;

public class JpaInvoiceRepositoryImpl implements InvoiceRepository {

  @Inject
  JpaInvoiceRepository jpaInvoiceRepository;

  @Override
  public void saveInvoice(Invoice invoice) {
    InvoiceEntity invoiceEntity = new InvoiceEntity();
    invoiceEntity.setClientId(invoice.getClientId());
    invoiceEntity.setInvoiceDocumentId(invoice.getInvoiceDocumentId());
    jpaInvoiceRepository.persist(invoiceEntity);
  }

  @Override
  public Invoice getInvoiceById(String invoiceId) {
    Optional<InvoiceEntity> invoiceEntity = jpaInvoiceRepository.findByIdOptional(invoiceId);
    return invoiceEntity.map(InvoiceEntity::toInvoice).orElse(null);
  }

  @Override
  public void deleteInvoice(String invoiceId) {

  }

  @Override
  public List<Invoice> getInvoicesByClient(String clientId) {
    return null;
  }
}
