package org.thi.sps.adapter.jpa;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.thi.sps.adapter.jpa.Entities.InvoiceEntity;

@ApplicationScoped
public class JpaInvoiceRepository implements PanacheRepositoryBase<InvoiceEntity, String> {

}
