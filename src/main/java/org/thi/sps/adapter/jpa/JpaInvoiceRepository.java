package org.thi.sps.adapter.jpa;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.thi.sps.adapter.jpa.Entities.InvoiceEntity;

@ApplicationScoped
public class JpaInvoiceRepository implements PanacheRepository<InvoiceEntity> {

}
