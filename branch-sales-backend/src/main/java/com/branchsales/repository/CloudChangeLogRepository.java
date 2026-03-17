package com.branchsales.repository;

import com.branchsales.entity.CloudChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudChangeLogRepository extends JpaRepository<CloudChangeLog, Long> {
}
