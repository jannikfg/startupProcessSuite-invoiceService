package org.thi.sps.adapter.jpa;

import static io.quarkus.hibernate.orm.panache.Panache.getEntityManager;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import java.time.LocalDate;
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
    InvoiceEntity invoiceEntity = InvoiceEntity.fromInvoice(invoice);
    jpaInvoiceRepository.persistAndFlush(invoiceEntity);
    return invoiceEntity.toInvoice();
  }

  @Override
  public Invoice update(Invoice invoice) {
    InvoiceEntity invoiceEntity = InvoiceEntity.fromInvoice(invoice);
    InvoiceEntity mergedEntity = jpaInvoiceRepository.getEntityManager().merge(invoiceEntity);
    return mergedEntity.toInvoice();
  }

  @Override
  public Invoice findById(String id) {
    Optional<InvoiceEntity> invoiceEntity = jpaInvoiceRepository.findByIdOptional(id);
    System.out.println("InvoiceEntity: " + invoiceEntity.toString());
    return invoiceEntity.map(InvoiceEntity::toInvoice).orElse(null);
  }

  @Override
  public void delete(String invoiceId) {
    jpaInvoiceRepository.deleteById(invoiceId);
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
