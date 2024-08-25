package org.thi.sps.adapter.jpa;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import org.thi.sps.adapter.jpa.Entities.InvoiceEntity;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.ports.outgoing.InvoiceRepository;

@ApplicationScoped
public class JpaInvoiceRepositoryImpl implements InvoiceRepository {

  @Inject
  JpaInvoiceRepository jpaInvoiceRepository;

  @Override
  public Invoice save(Invoice invoice) {
    System.out.println("Invoice in JpaInvoiceRepositoryImpl: " + invoice);

    InvoiceEntity invoiceEntity = InvoiceEntity.fromInvoice(invoice);


    jpaInvoiceRepository.persistAndFlush(invoiceEntity);
    return invoiceEntity.toInvoice();
  }

  @Override
  public Invoice update(Invoice invoice) {
    return null;
  }

  @Override
  public Invoice findById(String id) {
    return null;
  }

  @Override
  public void delete(String invoiceId) {

  }

  @Override
  public List<Invoice> findByClientId(String clientId) {
    return null;
  }

  @Override
  public List<Invoice> findAll() {
    List<InvoiceEntity> invoiceEntities = jpaInvoiceRepository.listAll();
    return invoiceEntities.stream().map(InvoiceEntity::toInvoice).toList();
  }
}
