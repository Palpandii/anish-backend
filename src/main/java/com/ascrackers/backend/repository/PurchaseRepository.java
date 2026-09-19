package com.ascrackers.backend.repository;

import com.ascrackers.backend.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}