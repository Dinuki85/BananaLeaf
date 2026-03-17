package com.branchsales.repository;

import com.branchsales.entity.IncomingInvoiceEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IncomingInvoiceEventRepository extends JpaRepository<IncomingInvoiceEvent, Long> {
    Optional<IncomingInvoiceEvent> findBySaleUuid(String saleUuid);
}
