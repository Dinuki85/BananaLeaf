package com.branchsales.repository;

import com.branchsales.entity.MainItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface MainItemRepository extends JpaRepository<MainItem, Long> {
    @Query("SELECT m FROM MainItem m LEFT JOIN FETCH m.mainCategory")
    List<MainItem> findAllWithCategory();
}
