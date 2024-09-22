package org.thi.sps.adapter.jpa;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.thi.sps.adapter.jpa.Entities.InvoiceEntity;
import org.thi.sps.domain.model.Invoice;
import org.thi.sps.ports.outgoing.InvoiceRepository;

@ApplicationScoped
public class JpaInvoiceRepositoryImpl implements InvoiceRepository {

  @Inject
  JpaInvoiceRepository jpaInvoiceRepository;

  @Inject
  EntityManager entityManager;

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


  @Transactional
  public Integer getMaxInvoiceNumberForMonth(String yearMonth) {
    String query = "SELECT MAX(CAST(SUBSTRING(i.id, 8, 5) AS INTEGER)) " +
        "FROM InvoiceEntity i " +
        "WHERE FUNCTION('TO_CHAR', i.createdDate, 'yyyyMM') = :yearMonth";

    Integer maxInvoiceNumber = entityManager.createQuery(query, Integer.class)
        .setParameter("yearMonth", yearMonth)
        .getSingleResult();

    String lockQuery = "SELECT i FROM InvoiceEntity i " +
        "WHERE FUNCTION('TO_CHAR', i.createdDate, 'yyyyMM') = :yearMonth";

    entityManager.createQuery(lockQuery, InvoiceEntity.class)
        .setParameter("yearMonth", yearMonth)
        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
        .getResultList();

    return maxInvoiceNumber;
  }
}


