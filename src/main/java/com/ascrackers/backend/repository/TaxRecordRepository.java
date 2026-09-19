package com.ascrackers.backend.repository;

import com.ascrackers.backend.model.TaxRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRecordRepository extends JpaRepository<TaxRecord, Long> {
}